#进程与线程——并发编程

##遇到问题

一般的异步程序只执行一遍就不会需要了，直接new Thead().start这种方式开启的线程由于是匿名的，无法管理，而且如果要多次执行的话，就会new多个匿名的线程，占用资源。

##线程池的解决

使用线程池可以重复利用存在的线程，这样可以减小开销，利用线程池可以执行定时、并发数的控制。

##线程池的使用
Executors共有四种形式，分别为：
 
 - newCacheThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。

  			
		// newCachedThreadPool创建一个可缓存线程池，
        // 如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        ExecutorService cacheExecutor = Executors.newCachedThreadPool();
            final ArrayList<String>  activeCache = new ArrayList<>() ;
            for (int i= 0 ;i < 30 ; i++){
                final int index = i ;
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                cacheExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("newCachedThreadPool" ," activeCache count = "+ Thread.activeCount() + "  index = "+index);

                        activeCache.add(" activeCache count = "+ Thread.activeCount() + "  index = "+index );
                        try {
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            cacheExecutor.shutdown();
            if (arrayAdapter != null){
                arrayAdapter.clear();
            }
            arrayAdapter = new ArrayAdapter(MainActivity.this , android.R.layout.simple_expandable_list_item_1 , activeCache ) ;
            listView.setAdapter(arrayAdapter);

        //  这是Log的打印结果            
		08-18 10:28:50.735 30040-30119/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 8  index = 0
		08-18 10:28:50.836 30040-30120/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 9  index = 1
		08-18 10:28:50.936 30040-30121/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 10  index = 2
		08-18 10:28:51.036 30040-30124/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 11  index = 3
		08-18 10:28:51.140 30040-30125/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 12  index = 4
		08-18 10:28:51.237 30040-30126/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 13  index = 5
		08-18 10:28:51.338 30040-30127/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 14  index = 6
		08-18 10:28:51.441 30040-30128/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 15  index = 7
		08-18 10:28:51.542 30040-30129/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 16  index = 8
		08-18 10:28:51.643 30040-30130/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 17  index = 9
		08-18 10:28:51.741 30040-30119/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 17  index = 10
		08-18 10:28:51.842 30040-30120/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 17  index = 11
		08-18 10:28:51.942 30040-30121/com.example.administrator.executelearning D/newCachedThreadPool:  activeCache count = 17  index = 12

 - newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。


		// newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
		final ArrayList<String>  activeFixed = new ArrayList<>() ;
            ExecutorService fixedExecutor = Executors.newFixedThreadPool(3) ;
            for ( int i = 0 ; i < 20 ; i++){
                final int index = i ;
                fixedExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("newFixedTheadPool" , " activce count = "+Thread.activeCount()+"  index = "+index) ;
                        activeFixed.add(" activeFixed count = "+ Thread.activeCount() + "  index = "+index );
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
            fixedExecutor.shutdown();
            if (arrayAdapter != null){
                arrayAdapter.clear();
            }
            arrayAdapter = new ArrayAdapter(MainActivity.this , android.R.layout.simple_expandable_list_item_1 , activeFixed ) ;
            listView.setAdapter(arrayAdapter);

		// 这是Log中的结果
		08-18 10:34:32.570 5356-5474/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 9  index = 0
		08-18 10:34:32.571 5356-5475/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 9  index = 1
		08-18 10:34:32.572 5356-5476/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 2
		08-18 10:34:33.571 5356-5474/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 3
		08-18 10:34:33.572 5356-5475/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 4
		08-18 10:34:33.573 5356-5476/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 5
		08-18 10:34:34.572 5356-5474/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 6
		08-18 10:34:34.573 5356-5475/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 7
		08-18 10:34:34.577 5356-5476/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 8
		08-18 10:34:35.573 5356-5474/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 9
		08-18 10:34:35.574 5356-5475/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 10
		08-18 10:34:35.578 5356-5476/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 11
		08-18 10:34:36.574 5356-5474/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 12
		08-18 10:34:36.575 5356-5475/com.example.administrator.executelearning D/newFixedTheadPool:  activce count = 10  index = 13
				
 - newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。

		//newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
            // 延时
            final ArrayList<String>  activeSchedule = new ArrayList<>() ;
            ScheduledExecutorService scheduleExecutor =   Executors.newScheduledThreadPool(5) ;
            scheduleExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    Log.d("newScheduleThreadPool" ,"delay 3s schedule activce count = "+Thread.activeCount() ) ;
                    activeSchedule.add("delay 3s schedule activce count = "+Thread.activeCount());
                }
            } , 3 , TimeUnit.SECONDS);
            // 定期   定时器

            ScheduledExecutorService scheduledExecutorFixed = Executors.newScheduledThreadPool(5) ;
            scheduledExecutorFixed.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Log.d("newScheduleThreadPool " ,"fixed delay 1s and execute every 3s  schedule activce count = "+Thread.activeCount() ) ;
                }
            } , 1 , 3 , TimeUnit.SECONDS);
            scheduleExecutor.shutdown();
            if (arrayAdapter != null){
                arrayAdapter.clear();
            }
            arrayAdapter = new ArrayAdapter(MainActivity.this , android.R.layout.simple_expandable_list_item_1 , activeSchedule ) ;
            listView.setAdapter(arrayAdapter);
		
		//这是Log打印出的结果
		08-18 10:35:56.431 5356-7142/com.example.administrator.executelearning D/newScheduleThreadPool: fixed delay 1s and execute every 3s  schedule activce count = 9
		08-18 10:35:57.910 5356-7171/com.example.administrator.executelearning D/newScheduleThreadPool: fixed delay 1s and execute every 3s  schedule activce count = 12
		08-18 10:35:58.429 5356-7141/com.example.administrator.executelearning D/newScheduleThreadPool: delay 3s schedule activce count = 13
		08-18 10:35:59.431 5356-7142/com.example.administrator.executelearning D/newScheduleThreadPool: fixed delay 1s and execute every 3s  schedule activce count = 12
		08-18 10:35:59.909 5356-7170/com.example.administrator.executelearning D/newScheduleThreadPool: delay 3s schedule activce count = 13
		08-18 10:36:00.914 5356-7171/com.example.administrator.executelearning D/newScheduleThreadPool: fixed delay 1s and execute every 3s  schedule activce count = 12
		08-18 10:36:02.433 5356-7161/com.example.administrator.executelearning D/newScheduleThreadPool: fixed delay 1s and execute every 3s  schedule activce count = 13
		08-18 10:36:03.910 5356-7171/com.example.administrator.executelearning D/newScheduleThreadPool: fixed delay 1s and execute every 3s  schedule activce count = 14
		08-18 10:36:05.431 5356-7142/com.example.administrator.executelearning D/newScheduleThreadPool: fixed delay 1s and execute every 3s  schedule activce count = 15

 - newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。

		final ExecutorService singleExecutor = Executors.newSingleThreadExecutor() ;
            final ArrayList<String>  activeSingle = new ArrayList<>() ;
            for ( int i = 0 ; i < 15 ; i++){
                final int index = i ;
                singleExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("newSingleThreadPool" , " active count = "+Thread.activeCount()+"  index = "+index) ;
                       // activeSingle.add(" active count = "+Thread.activeCount()+"  index = "+index);
                        Message msg = new Message() ;
                        msg.obj = " active count = "+Thread.activeCount()+"  index = "+index ;
                        handler.sendMessage(msg) ;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            singleExecutor.shutdown();

            if (arrayAdapter != null){
                arrayAdapter.clear();
            }

		// 这是Log打印的结果 
		08-18 10:38:11.024 9681-9803/com.example.administrator.executelearning D/newSingleThreadPool:  active count = 8  index = 0
		08-18 10:38:12.025 9681-9803/com.example.administrator.executelearning D/newSingleThreadPool:  active count = 8  index = 1
		08-18 10:38:13.025 9681-9803/com.example.administrator.executelearning D/newSingleThreadPool:  active count = 8  index = 2
		08-18 10:38:14.026 9681-9803/com.example.administrator.executelearning D/newSingleThreadPool:  active count = 8  index = 3
		08-18 10:38:15.027 9681-9803/com.example.administrator.executelearning D/newSingleThreadPool:  active count = 8  index = 4
		08-18 10:38:16.027 9681-9803/com.example.administrator.executelearning D/newSingleThreadPool:  active count = 8  index = 5
		08-18 10:38:17.027 9681-9803/com.example.administrator.executelearning D/newSingleThreadPool:  active count = 8  index = 6
		08-18 10:38:18.028 9681-9803/com.example.administrator.executelearning D/newSingleThreadPool:  active count = 8  index = 7
		08-18 10:38:19.028 9681-9803/com.example.administrator.executelearning D/newSingleThreadPool:  active count = 8  index = 8