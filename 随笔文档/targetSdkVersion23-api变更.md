# android targetsdk 21--23

## 本文目的

向开发者展示sdkVersion 21 到 23 的变化，以及 targetSdkVersion 由 21 更新到 23 需要注意的问题

## 版本变化 API 22

#### 已弃用的 HTTP 类

Android 5.1 中已弃用 <mark>org.apache.http</mark> 类和 <mark>android.net.http.AndroidHttpClient</mark> 类,这些类将不再保留

#### 多 SIM 卡支持

Android 5.1 添加了对同时使用多个蜂窝运营商 SIM 卡的支持。有了此功能，用户可以在具有两个或多个 SIM 卡插槽的设备上激活和使用额外的 SIM.

可以通过 [SubscriptionManager](https://developer.android.com/reference/android/telephony/SubscriptionManager.html) 类获取有关当前激活的 SIM 的信息，包括设备是否被认为在当前网络上漫游。对于希望为对数据访问费用敏感的设备用户减少或关闭应用数据访问的开发者而言，这些信息非常有用。可以通过请求 [READ_PHONE_STATE](https://developer.android.com/reference/android/Manifest.permission.html#READ_PHONE_STATE) 权限和对 [SubscriptionManager](https://developer.android.com/reference/android/telephony/SubscriptionManager.html) 对象设置 [SubscriptionManager.OnSubscriptionsChangedListener](https://developer.android.com/reference/android/telephony/SubscriptionManager.OnSubscriptionsChangedListener.html)，提醒应用注意设备当前网络连接的状态变化

## 版本变化 API 23

#### 运行时请求权限

对于以 Android 6.0（API 级别 23）或更高版本为目标平台的应用，需要在运行时检查和请求权限

具体方法如下：

```java
	// Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTCODE) {
            if (grantResults.length > 0 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                String  deviceId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                Log.w(TAG, "onRequestPermissionsResult:  deviceId " + deviceId);
            } else {
                Log.w(TAG, "onRequestPermissionsResult: permission denied");
            }
        }
    }

	// 请求权限
    public void getPermission() {
		// 检查 是否已经拥有该权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        	// 未拥有 --  请求该权限
            // 是否对用户解释使用该权限的一些理由
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                Log.w(TAG, "getPermission: 下面显示对用户关于使用该权限的一些解释");
                // 解释 为什么使用该权限 然后再请求
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUESTCODE);
            } else {
                // 不解释  直接请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUESTCODE);
            }
        } else {
        	// 已经拥有该权限  直接使用权限
            String deviceId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            Log.w(TAG, "getPermission:  has granted , so deviceId is " + deviceId);
        }

    }
```

权限分为两类：正常权限和危险权限

 - 正常权限不会直接给用户隐私权带来风险。如果您的应用在其清单中列出了正常权限，系统将自动授予该权限

 - 危险权限会授予应用访问用户机密数据的权限。如果您的应用在其清单中列出了正常权限，系统将自动授予该权限。如果您列出了危险权限，则用户必须明确批准您的应用使用这些权限

   ![](http://upload-images.jianshu.io/upload_images/650671-3297bcf7a0e7f34b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

系统版本对权限的影响：

 - 如果设备运行的是 Android 5.1 或更低版本，或者应用的目标 SDK 为 22 或更低：如果您在清单中列出了危险权限，则用户必须在安装应用时授予此权限；如果他们不授予此权限，系统根本不会安装应用

 - 如果设备运行的是 Android 6.0 或更高版本，或者应用的目标 SDK 为 23 或更高：应用必须在清单中列出权限，并且它必须在运行时请求其需要的每项危险权限。用户可以授予或拒绝每项权限，且即使用户拒绝权限请求，应用仍可以继续运行有限的功能

亦可参考官方 [详细](https://developer.android.com/training/permissions/requesting.html)

#### 取消支持 Apache HTTP 客户端

Android 6.0 版移除了对 Apache HTTP 客户端的支持。如果您的应用使用该客户端，并以 Android 2.3（API 级别 9）或更高版本为目标平台，请改用 [HttpURLConnection](https://developer.android.com/reference/java/net/HttpURLConnection.html) 类，若要继续使用 Apache HTTP API，请在 build.gradle 中声明编译依赖项
```gradle
android {
    useLibrary 'org.apache.http.legacy'
}
```

#### 硬件标识符访问权

为给用户提供更严格的数据保护，从此版本开始，对于使用 WLAN API 和 Bluetooth API 的应用，Android 移除了对设备本地硬件标识符的编程访问权。[WifiInfo.getMacAddress()](https://developer.android.com/reference/android/net/wifi/WifiInfo.html#getMacAddress()) 方法和 [BluetoothAdapter.getAddress()](https://developer.android.com/reference/android/bluetooth/BluetoothAdapter.html#getAddress()) 方法现在会返回常量值 02:00:00:00:00:00。

现在，要通过蓝牙和 WLAN 扫描访问附近外部设备的硬件标识符，您的应用必须拥有 [ACCESS_FINE_LOCATION](https://developer.android.com/reference/android/Manifest.permission.html#ACCESS_FINE_LOCATION) 或 [ACCESS_COARSE_LOCATION](https://developer.android.com/reference/android/Manifest.permission.html#ACCESS_COARSE_LOCATION) 权限。

 - [WifiManager.getScanResults()](https://developer.android.com/reference/android/net/wifi/WifiManager.html#getScanResults())

 - [BluetoothDevice.ACTION_FOUND](https://developer.android.com/reference/android/bluetooth/BluetoothDevice.html#ACTION_FOUND)

 - [BluetoothLeScanner.startScan()](https://developer.android.com/reference/android/bluetooth/le/BluetoothLeScanner.html#startScan(android.bluetooth.le.ScanCallback))

#### 通知

此版本移除了 <mark>Notification.setLatestEventInfo()</mark> 方法。请改用 [Notification.Builder](https://developer.android.com/reference/android/app/Notification.Builder.html) 类来构建通知。要重复更新通知，请重复使用 Notification.Builder 实例。调用 [build()](https://developer.android.com/reference/android/app/Notification.Builder.html#build() 方法可获取更新后的 [Notification](https://developer.android.com/reference/android/app/Notification.html) 实例。

另外，命令行也发生了变化，adb shell dumpsys notification 命令不再打印输出您的通知文本。请改用 adb shell dumpsys notification --noredact 命令打印输出 notification 对象中的文本。


#### 音频管理器变更

不再支持通过 [AudioManager](https://developer.android.com/reference/android/media/AudioManager.html) 类直接设置音量或将特定音频流静音。[setStreamSolo()](https://developer.android.com/reference/android/media/AudioManager.html#setStreamSolo(int,%20boolean)) 方法已弃用，您应该改为调用 [requestAudioFocus()](https://developer.android.com/reference/android/media/AudioManager.html#requestAudioFocus(android.media.AudioManager.OnAudioFocusChangeListener,%20int,%20int)) 方法。类似地，[setStreamMute()](https://developer.android.com/reference/android/media/AudioManager.html#setStreamMute(int,%20boolean)) 方法也已弃用，请改为调用 [adjustStreamVolume()](https://developer.android.com/reference/android/media/AudioManager.html#adjustStreamVolume(int,%20int,%20int)) 方法并传入方向值 [ADJUST_MUTE](https://developer.android.com/reference/android/media/AudioManager.html#ADJUST_MUTE) 或 [ADJUST_UNMUTE](https://developer.android.com/reference/android/media/AudioManager.html#ADJUST_UNMUTE)


#### 文本选择

当用户在应用中选择文本时，你可以在一个浮动工具栏中显示“剪切”、“复制”和“粘贴”等文本选择操作。其在用户交互实现上与为单个视图启用上下文操作模式中所述的上下文操作栏类似

#### 浏览器书签变更

此版本移除了对全局书签的支持。<mark>android.provider.Browser.getAllBookmarks()</mark> 和 <mark>android.provider.Browser.saveBookmark()</mark> 方法现已移除。同样，<mark>READ_HISTORY_BOOKMARKS</mark> 权限和<mark> WRITE_HISTORY_BOOKMARKS</mark> 权限也已移除。如果您的应用以 Android 6.0（API 级别 23）或更高版本为目标平台，请勿从全局提供程序访问书签或使用书签权限。您的应用应改为在内部存储书签数据

#### WLAN 和网络连接变更

此版本对 WLAN API 和 Networking API 引入了以下行为变更。

 - 现在，你的应用只能更改由你创建的 [WifiConfiguration](https://developer.android.com/reference/android/net/wifi/WifiConfiguration.html) 对象的状态。系统不允许你修改或删除由用户或其他应用创建的 [WifiConfiguration](https://developer.android.com/reference/android/net/wifi/WifiConfiguration.html) 对象

 - 在之前的版本中，如果应用利用带有 disableAllOthers=true 设置的 [enableNetwork()](https://developer.android.com/reference/android/net/wifi/WifiManager.html#enableNetwork(int,%20boolean)) 强制设备连接特定 WLAN 网络，设备将会断开与移动数据网络等其他网络的连接。在此版本中，设备不再断开与上述其他网络的连接。如果您的应用的 targetSdkVersion 为 “20” 或更低，则会固定连接所选 WLAN 网络。如果您的应用的 targetSdkVersion 为 “21” 或更高，请使用多网络 API（如 [openConnection()](https://developer.android.com/reference/android/net/Network.html#openConnection(java.net.URL)、[bindSocket()](https://developer.android.com/reference/android/net/Network.html#bindSocket(java.net.Socket)) 和新增的 [bindProcessToNetwork()](https://developer.android.com/reference/android/net/ConnectivityManager.html#bindProcessToNetwork(android.net.Network)) 方法）来确保通过所选网络传送网络流量

#### 相机服务变更

在此版本中，相机服务中共享资源的访问模式已从之前的“先到先得”访问模式更改为高优先级进程优先的访问模式
详情 [查看](https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-camera)





