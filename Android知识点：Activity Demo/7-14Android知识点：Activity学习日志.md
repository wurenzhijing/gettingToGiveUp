#Activity#
##Activity的生命周期##
 - Activity是Android的四大组件之一，是Android程序的呈现层，显示可视化的用户界面，并接受与用户交互所产生的界面事件，Activity有四种状态，分别是Running，Paused，Stopped，Killed。Activity的生命周期包含onCreate()，onStart()，onRestart()，onResume()，onPause()，onStop()，onDestroy()。
 - 在onCreate()中一般进行一些初始化数据，设置用户界面等；在onStart()在Activity从Stop状态转换为Active状态时被调用；onRestart()在Activity由Stop状态转化为Active状态时调用;onResume()在Activity由Pause状态转化为Active状态时被调用；onPause()在Activity从Active状态转化为Pause状态时被调用；onStop()在Activity从Active状态转换为Stop状态时调用；onDestroy()在Activity结束时被调用，处理一些资源释放，内存清理。
 
##Activity的启动模式##
 
 - Standard模式，默认模式，允许有多个相同的Activity实例叠加。在Demo中，通过点击按钮来让MainActivity多次启动自己，看以发现TextView中显示的activity实例名称发生变化。
 - singleTop模式，允许有多个实例，但是不允许多个相同的Activity叠加。在Demo中使用两个Activity，SingleTopActivity为singleTop模式，TopActivity为标准模式，观察TextView中Activity的名称变化。
 - singleTask模式，只允许有一个实例，若activity不存在，则在当前task中重新创建新的实例，若存在，则把task中在其之上的其他Activity实例destroy，然后调用它的onNewIntent方法。在Demo中有3个Activity分别为SingleTaskActivity，TaskActivity1，TaskActivity2，其中SingleTaskActivity是SingleTask模式，其他则是标准模式，观察TextView中的Activity名称变化。
 - singleInstance模式，只允许有一个实例，并且这个实例中独立运行在一个task中，不允许有别的activity存在，在Demo中，有3个Activity，分别是SingleInstanceActivity，InstanceActivity1，InstanceActivity2，其中SingleInstanceActivity为SingleInstance模式，观察TextView中Activity名称变化。
##Activity Stack与Task#
 
 - Activity Stack是用来管理Activity的栈，遵循先进先出的原则，系统总显示位于栈顶的Activity。
 - Task是指将相关的Activity组合在一起，以Activity Stack的方式管理。
 