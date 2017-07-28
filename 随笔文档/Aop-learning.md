# AOP 学习

## AspectJ 尝试

#### 环境搭建

 - project 中的 build.gradle 添加classpath
```gradle
 classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:1.0.8'
```

 - app 中的 build.gradle 添加
```gradle
apply plugin: 'android-aspectjx'
compile 'org.aspectj:aspectjrt:1.8.9'
```

#### 测试 Aspect -- @Before

```java
@Aspect
public class AspectTest {
    String TAG = AspectTest.class.getSimpleName();

	//注意这里的 android.app.Activity.on 路径
    @Before("execution(* android.app.Activity.on**(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.i(TAG, "onActivityMethodBefore: " + key);
    }
}
```

**结果**

```java
package com.example.administrator.callbacknotifymany;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        Bundle localBundle = savedInstanceState;
        JoinPoint localJoinPoint = Factory.makeJP(ajc$tjp_0, this, this, localBundle);
        AspectTest.aspectOf().onActivityMethodBefore(localJoinPoint);
        super.onCreate(savedInstanceState);
        setContentView(2130968603);
        testAop();
    }

    protected void onStart() {
        JoinPoint localJoinPoint = Factory.makeJP(ajc$tjp_1, this, this);
        AspectTest.aspectOf().onActivityMethodBefore(localJoinPoint);
        super.onStart();
    }

    protected void onResume() {
        JoinPoint localJoinPoint = Factory.makeJP(ajc$tjp_2, this, this);
        AspectTest.aspectOf().onActivityMethodBefore(localJoinPoint);
        super.onResume();
    }

    protected void onDestroy() {
        JoinPoint localJoinPoint = Factory.makeJP(ajc$tjp_3, this, this);
        AspectTest.aspectOf().onActivityMethodBefore(localJoinPoint);
        super.onDestroy();
    }
}
```

 - 如果是 <code>android.app.Activity.on**(..)</code> , <code>android.app.Activity </code>的所有以 on 开头的方法都会被插入， 那么我们应用中所有的 Activity 都会被插入，因为都继承于 <code>android.app.Activity </code>

 - 如果将路径改为一个确定的方法，比如<code>com.example.administrator.callbacknotifymany.MainActivity.testAop() </code> ，那么 MainActivity 中的 testAop() 方法会被插入， 如果 MainActivity 中有对 testAop() 方法的重载，如下 ：
```java
public class MainActivity extends AppCompatActivity {

    private static  final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testAop();
        testAop("ddddddaaaaaaaaaaa");
    }

    public void testAop() {
        Log.i(TAG, "testAop ");
    }
	// 重载 testAop() 方法
    public void testAop(String s) {
        Log.i(TAG, "testAop " + s);
    }
}
```

  - <code>com.example.administrator.callbacknotifymany.MainActivity.testAop(..) </code> 会插入 testAop() 以及它的重载方法

   - <code>com.example.administrator.callbacknotifymany.MainActivity.testAop() </code> 只会插入 testAop() 方法

  - <code>com.example.administrator.callbacknotifymany.MainActivity.testAop(java.lang.String) </code> 只会插入 testAop(String s) 方法

  - <code>@Before("execution(\** com.example.administrator.callbacknotifymany.MainActivity.testAop(..))")</code> 这个是完整的注解，不要删除 execution 后面的 <code>\**</code> ， 否则会报错

 - 语法

  - @Before ： Advice， 插入点
  - excecution ： 处理 Join Point 的类型， 例如 call， execution
  - <code>\** \**(..)</code> :
    - 第一个 <code>\**</code> 是固定命名
    - 第二个 <code>\**</code> 是方法名（带类名，带包名）
    - 括号中的 <code>..</code> 是方法的参数（带包名的类）
    - <code> * com.example.administrator.callbacknotifymany.MainActivity.testAop(java.lang.String)</code> 能唯一确定一个方法
  - onActivityMethodBefore(JoinPoint joinPoint) : 插入的代码

#### Aspect -- @After

```java
@Aspect
public class AspectTest {
    String TAG = AspectTest.class.getSimpleName();

    @Before("execution(* com.example.administrator.callbacknotifymany.MainActivity.testAop(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.i(TAG, "onActivityMethodBefore: " + key);
    }

    @After("execution(* com.example.administrator.callbacknotifymany.MainActivity.testAop(..))")
    public void onActivityMethodAfter(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.i(TAG, "onActivityMethodAfter: " + key);
    }
}
```

**结果**
```java
public class MainActivity extends AppCompatActivity {
    private static final String TAG;
    private static final JoinPoint.StaticPart ajc$tjp_0;
    private static final JoinPoint.StaticPart ajc$tjp_1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130968603);
        testAop();
        testAop("ddddddaaaaaaaaaaa");
    }

    @WorkerThread
    public String getStr() {
        return "ffffffffffffffffffffffff";
    }

    public void testAop() {
        JoinPoint localJoinPoint = Factory.makeJP(ajc$tjp_0, this, this);
        try {
            AspectTest.aspectOf().onActivityMethodBefore(localJoinPoint);
            Log.i(TAG, "testAop ");
        } catch (Throwable localThrowable) {
            AspectTest.aspectOf().onActivityMethodAfter(localJoinPoint);
            throw localThrowable;
        }
        AspectTest.aspectOf().onActivityMethodAfter(localJoinPoint);
    }

    public void testAop(String s) {
        String str = s;
        JoinPoint localJoinPoint = Factory.makeJP(ajc$tjp_1, this, this, str);
        try {
            AspectTest.aspectOf().onActivityMethodBefore(localJoinPoint);
            Log.i(TAG, "testAop " + s);
        } catch (Throwable localThrowable) {
            AspectTest.aspectOf().onActivityMethodAfter(localJoinPoint);
            throw localThrowable;
        }
        AspectTest.aspectOf().onActivityMethodAfter(localJoinPoint);
    }
```

#### Aspect -- @Around

包含 Before 和 After 的所有功能

```java
    @Around("execution(* com.example.administrator.callbacknotifymany.MainActivity.testAop(..))")
    public void onActivityMethodAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.i(TAG, "onActivityMethodAround:  first " + key);
        joinPoint.proceed();
        Log.i(TAG, "onActivityMethodAround:  second " + key);
    }
```

 - joinPoint.proceed() 代表执行原始方法

 - Around 插入的方法** 不能有返回值 ？？**

 - Around 不能与 After Before 公用， 否则编译不通过，但是可以和 Before 或者 After 使用

  - Around  与 After  ， After 在 Around 之前执行

  - Around  与   Before ， Before 在 Around 之前执行

#### 自定义 Pointcut切入点

 - 定义注解类
```java
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface MyAnnotion {
}
```

 - 定义 Pointcut , 将注解类注册到切入点 ，对注解的使用者进行切入
```java
    @Pointcut("execution(@com.example.administrator.callbacknotifymany.MyAnnotion * *(..))")
    public void selfPointCutMethod(){
    }
```
 这里的路径<code>execution(@com.example.administrator.callbacknotifymany.MyAnnotion * *(..))</code> ，其中 <code>@com.example.administrator.callbacknotifymany.MyAnnotion</code> 为注解类名 ， 后面的 <code>\** \**(..)</code> 是要插入的范围 ， 可以指定到具体的类，方法

 - 添加切入代码 , 这里包括 Before ， After ， Around
```java
    @Before("selfPointCutMethod()")
    public void onSelfPointCutBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.i(TAG, "onSelfPointCutBefore: "+ key);
    }
```

 - 使用注解
```java
    @MyAnnotion
    public void testSelfAop(String s) {
        Log.i(TAG, "testSelfAop: " + s);
    }

    @MyAnnotion
    public String testSelfAop( ) {
        Log.i(TAG, "testSelfAop: "  );
        return "sssssssssssssssssssssssssssssssss";
    }
```

**现在切入的目标是注解的使用者**

```java
    @MyAnnotion
    public void testSelfAop(String s) {
        String str = s;
        JoinPoint localJoinPoint = Factory.makeJP(ajc$tjp_1, this, this, str);
        AspectTest.aspectOf().onSelfPointCutBefore(localJoinPoint);
        Log.i(TAG, "testSelfAop: " + s);
    }

    @MyAnnotion
    public String testSelfAop() {
        JoinPoint localJoinPoint = Factory.makeJP(ajc$tjp_2, this, this);
        AspectTest.aspectOf().onSelfPointCutBefore(localJoinPoint);
        Log.i(TAG, "testSelfAop: ");
        return "sssssssssssssssssssssssssssssssss";
    }
```

#### Call 与 Execution

看一下 Call 的例子

```java
    @Before("call(* com.example.administrator.callbacknotifymany.MainActivity.testAop(java.lang.String))")
    public void onActivityMethodBeforeCall(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.i(TAG, "onActivityMethodBeforeCall: " + key);
    }
```

编译之后

```java
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130968603);

        String str = "ddddddaaaaaaaaaaa";
        MainActivity localMainActivity = this;
        JoinPoint localJoinPoint = Factory.makeJP(ajc$tjp_0, this, localMainActivity, str);
        AspectTest.aspectOf().onActivityMethodBeforeCall(localJoinPoint);  // 在切入方法的调用之前
        localMainActivity.testAop(str);
    }
```

由此可以看出 call 与 execution 的差别了

```java
@Before(call(* *(..)))
method()
@After(call(* *(..)))

public void method(){
	@Before(execution(* *(..)))
    ....
    ....
    @After(execution(* *(..)))
}
```

#### 切入点过滤与 withincode

有一种情况， 有多个方法A， B， C 调用 method ， 我们只想在 方法B 调用 method 是切入 ， 那么应该这么做

```java
    // 在 onResume 方法B内
    @Pointcut("withincode(* com.example.administrator.callbacknotifymany.MainActivity.onResume())")
    public void onResumeAop(){
    }

    // 在调用 testAop(String s)  method方法时 切入
    @Pointcut("call(* com.example.administrator.callbacknotifymany.MainActivity.testAop(java.lang.String))")
    public void onActivityMethodTestAop() {
    }

    // 同时满足上面两个条件 方法B中 + method
    @Pointcut("onResumeAop() && onActivityMethodTestAop()")
    public void withincodeCut(){
    }

    @After("withincodeCut()")
    public void onActivityMethodAfterCall(JoinPoint joinpoint) throws Throwable {
        String key = joinpoint.getSignature().toString();
        Log.i(TAG, "onActivityMethodAfterCall: "+key);
    }
```

编译后的
```java
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130968603);
        String str = "ddddddaaaaaaaaaaa";
        testAop(str);   // 没有被切入
    }

    protected void onResume() {   // 方法B
        super.onResume();
        Log.i(TAG, "onResume: ");
        String str = "dddddddddddd";
        MainActivity localMainActivity = this;
        JoinPoint localJoinPoint = Factory.makeJP(ajc$tjp_1, this, localMainActivity, str);
        try {
            AspectTest.aspectOf().onActivityMethodBeforeCall(localJoinPoint);
            localMainActivity.testAop(str);  // method
        } catch (Throwable localThrowable) {
            AspectTest.aspectOf().onActivityMethodAfterCall(localJoinPoint); // 切入
            throw localThrowable;
        }
        AspectTest.aspectOf().onActivityMethodAfterCall(localJoinPoint);
    }
```
