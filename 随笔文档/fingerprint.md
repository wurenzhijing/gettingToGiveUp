# Fingerprint

## API
这个 API 是Google 在 Android6.0 之后才推出来

 - FingerprintManager，api level 23，指纹权限检查

	```java
    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M){
        FingerprintManager manager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.authenticate(, , , , );

	```
    使用在6.0以下会崩溃，try catch 都没用

 - FingerprintManagerCompat，v4 包，不需要权限检查，google 推荐

	```java
FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(this);
```

 - FingerprintManagerCompatApi23，v4 包，不需要权限检查，封装性较好，使用方便

	```java
FingerprintManagerCompatApi23.authenticate( , , , , );
```

无论是通过何种方式来获取何种 Manager ，指纹识别的最终实现都是通过 <code>FingerprintManager</code>
```java
    @RequiresPermission(USE_FINGERPRINT)
    public void authenticate(@Nullable CryptoObject crypto, @Nullable CancellationSignal cancel,
            int flags, @NonNull AuthenticationCallback callback, Handler handler, int userId) {
        if (callback == null) {
            throw new IllegalArgumentException("Must supply an authentication callback");
        }

        if (cancel != null) {
            if (cancel.isCanceled()) {
                Log.w(TAG, "authentication already canceled");
                return;
            } else {
                cancel.setOnCancelListener(new OnAuthenticationCancelListener(crypto));
            }
        }

        if (mService != null) try {
            useHandler(handler);
            mAuthenticationCallback = callback;
            mCryptoObject = crypto;
            long sessionId = crypto != null ? crypto.getOpId() : 0;
            mService.authenticate(mToken, sessionId, userId, mServiceReceiver, flags,
                    mContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.w(TAG, "Remote exception while authenticating: ", e);
            if (callback != null) {
                // Though this may not be a hardware issue, it will cause apps to give up or try
                // again later.
                callback.onAuthenticationError(FINGERPRINT_ERROR_HW_UNAVAILABLE,
                        getErrorString(FINGERPRINT_ERROR_HW_UNAVAILABLE));
            }
        }
    }
```

## 使用条件

 - API level 23

	指纹识别 API 是在 api level 23 也就是 Android 6.0 中加入的，因此我们的 app 必须运行在这个系统版本之上。 google 推荐使用 android Support Library v4 包来获得 FingerprintManagerCompat 对象，因为在获得的时候这个包会检查当前系统平台的版本。

 - 硬件

	有指纹识别的硬件
    ```java
    fingerprintManager.isHardwareDetected()
    ```

 - 设备必须处于安全保护中

	有指纹识别的手机，在使用指纹识别的时候，还需要强制设置密码或者图案解锁，如果未设置的话是不许使用指纹识别的

	```java
KeyguardManager keyguardManager =(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
if (keyguardManager.isKeyguardSecure()) {
    // this device is secure.
}
```
 - 是否有注册的指纹
```java
fingerprintManager.hasEnrolledFingerprints()
```

## 使用场景

 - 纯本地使用，如指纹解锁

 - 与后台交互，如指纹支付，指纹登录

## 使用介绍

** 指纹识别 api **

```java
FingerprintManagerCompat.from(this).authenticate(CryptoObject crypto, int flags, CancellationSignal cancel, AuthenticationCallback callback,Handler handler)
```

 - CryptoObject ，<code>@Nullable</code> , FingerprintManager 的内部类， 加密对象，扫描器通过这个判断认证结果的合法性 <code>new FingerprintManager.CryptoObject(cipher)</code>

	```java
    Cipher cipher;
    try {
       cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
       	+ KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        throw new RuntimeException("创建Cipher对象失败", e);
    }
    
    try {
        keyStore.load(null);             // 如何获取 keyStore
        SecretKey key = (SecretKey) keyStore.getKey(keyName, null);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return true;
    } catch (IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyStoreException | InvalidKeyException e) {
        throw new RuntimeException("初始化 cipher 失败", e);
    }
```

 - flags , 标志，默认为 0

 - CancellationSignal ，<code>@Nullable</code>, 取消扫描操作， 如果不取消，扫描器会扫描到超时，一般是30S左右 ，<code>new CancellationSignal()</code> 就可以创建该对象

 - AuthenticationCallback，<code>@NonNull</code>, 是 FingerprintManager 类里面的一个抽象类，指纹识别的回调方法，包括成功、失败、错误等

	```java
    class MyCallback extends FingerprintManagerCompat.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            //指纹多次验证错误进入这个方法。并且暂时不能调用指纹验证
            Log.e(TAG, "onAuthenticationError: " + errString);
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            // 在指纹识别中 当碰到一些可以修复的错误时 helpString 来给与用户一些提示 如 请保证传感器无异物遮挡。。。
            super.onAuthenticationHelp(helpMsgId, helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            // 成功
            Log.i(TAG, "onAuthenticationSucceeded: 识别成功");
        }

        @Override
        public void onAuthenticationFailed() {
            Log.e(TAG, "onAuthenticationFailed: 识别失败 " );
        }
    }
    ```

 - Handler，<code>@Nullable</code>, 用于处理回调事件，一般传 <code>null</code>，因为 FingerprintManagerCompat 默认使用 Main Looper 来处理

## 指纹识别设备间的差异

Android 6.0 之后Google官方推出了指纹识别，但是很多厂商在6.0之前就有了指纹识别这一功能，这就涉及到了适配问题，不可能抛弃6.0之前的用户

#### [Sumsung Pass](http://developer.samsung.com/galaxy/pass)

Android 4.2（API 17）及以上，设备有指纹识别传感器

三星针对 Galaxy 提供的指纹识别SDK，并且有提供指纹识别的 dialog，而 Android 官方提供的是没有界面的

#### [魅族SDK](http://open-wiki.flyme.cn/index.php?title=%E6%8C%87%E7%BA%B9%E8%AF%86%E5%88%ABAPI)

6.0以下的魅族手机用魅族提供的 api，6.0 以上的使用官方提供的 api


## 指纹识别解决方案

 #### [Reprint](https://github.com/ajalt/reprint)

 简单、统一的指纹识别库，适用于 Android 与 RxJava 扩展
  - 不需处理不同的指纹识别API，包括 ImPrint 和 SamSung Pass
  - 修复了底层API中无证书错误
  - 从支持库中支持比FingerprintManagerCompat更多的Imprint设备
  - 提供可选的RxJava接口


用法：
 1、初始化，<code>Application.onCreate()</code> 中初始化 <code>Reprint.initialize(this)</code>
 2、使用Rx接口
 ```java
 RxReprint.authenticate()
    .subscribe(result -> {
        switch (result.status) {
            case SUCCESS:
                showSuccess();
                break;
            case NONFATAL_FAILURE:
                showHelp(result.failureReason, result.errorMessage);
                break;
            case FATAL_FAILURE:
                showError(result.failureReason, result.errorMessage);
                break;
        }
    });
```
3、也可以使用传统接口
```java
Reprint.authenticate(new AuthenticationListener() {
    public void onSuccess(int moduleTag) {
        showSuccess();
    }

    public void onFailure(AuthenticationFailureReason failureReason, boolean fatal,
                          CharSequence errorMessage, int moduleTag, int errorCode) {
        showError(failureReason, fatal, errorMessage, errorCode);
    }
});
```
