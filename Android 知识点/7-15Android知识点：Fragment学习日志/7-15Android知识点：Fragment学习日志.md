#Fragment#
##一、Fragment的生命周期##

如下图是Fragment的生命周期
- onAttach()，表示fragment已关联到activity。
- onCreate()，系统创建fragment时使用。
- onCreateVie()，系统在当前fragment上绘制UI布局。
- onActivityCreated()，当onCreate方法执行完成后调用。
- onStart()，fragment启动时调用。
- onResume()，fragment获取焦点时调用。
- onPause()，用户离开fragment时调用。
- onStop()，fragment不可见。
- onDestroyView()，fragment销毁布局，清除视图资源。
- onDestroy()，销毁fragment对象。
- onDetach()，脱离activity。

##二、Fragment的管理和事务处理##
###Fragment的管理###
Activity管理Fragment主要依靠FragmentManager。FragmentManager的功能主要为一下几点：
- 使用findFragmentById()或findFragmentByTag()方法来获取指定Fragment。
- 调用popBackStack()方法将Fragment从后台栈中弹出(模拟用户按下BACK按键)。
- 调用addOnBackStackChangeListener()注册一个监听器，用于监听后台栈的变化。

###Fragment事务处理###
Activity对Fragment执行的多个改变操作主要由FragmentTransaction来执行，它代表Activity对Fragment执行多个改变，可通过FragmentManager来获得。对Fragment的添加，删除，替换则需要借助于FragmentTransaction对象，包括add()，remove()，和replace()等操作，使用commit来提交事务。

##三、Retain Fragment##
Retain Fragment是用来跨越Activity保留活动对象，调用Fragment.setRetaininstance(true)允许我们跳过销毁和重新创建的周期，指示系统保留当前的fragment实例。

##四、Fragments的消息通信##
  Fragments的消息通信，Fragment间不直接通信，都是借助Activity来实现通信的，而最常用的方法就是就借助接口来实现。具体步骤如下：
- 定义接口 在Fragment中定义接口。
- 实现接口 在Activity中实现Fragment中定义的接口。
- 在Activity中将消息传递给另一个Fragment。
  


 