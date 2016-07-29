#数据持久化——Android文件系统#

##简介##
Android的文件系统是Android数据持久化的一种方式，包括内部存储和外部存储，文件系统的具体内容分别如下：
  ![](http://img.blog.csdn.net/20151211224232123?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

##内部存储##
 内部存储，其中的data文件夹就是内部存储，其中有两个文件夹比较常用，分别是app文件夹和data文件夹，app文件夹里面存放的是所有安装的apk文件，data文件夹里的子目录分别是shared_prefs，databases，files，cache。

  - data/data/包名/shared_prefs，使用SharedPreferences时，其生成的XML文件就在shared_prefs中，
  - data/data/包名/databases，使用数据库时，数据库文件在databases中，
  - data/data/包名/files，普通数据在files中，
  - data/data/包名/cache，缓存文件在cache文件夹中。
 
 

 
##外部存储##
外部存储一般是storage文件夹或者mnt文件夹，外部存储中的sdcard文件夹分为两个部分，公有目录和私有目录：
  
  - 公有目录，其中有九个文件夹；
  - 私有目录，就是Android这个文件夹，包含files和cache子目录。
  

##内部存储的操作##

  - 获取内部存储总空间
  	
	 

		public  static long getTotalSize(){
	        File path = Environment.getDataDirectory() ;
	        StatFs statFs = new StatFs(path.getPath()) ;
	        long blockSize = statFs.getBlockSize() ;
	        long totalSize = statFs.getBlockCount() ;
	        return totalSize*blockSize ;
	    }


  - 获取内部存储可用空间

		public static long getAvailableSize(){
	        File file = Environment.getDataDirectory() ;
	        StatFs statFs = new StatFs(file.getPath()) ;
	        long blockSize = statFs.getBlockSize() ;
	        long availiableSize = statFs.getAvailableBlocks() ;
	        return availiableSize*blockSize ;
	    }

  - 写文件到内部存储
	 
		 public void save(String fileName , String content)throws IOException{
		        File file = new File(context.getFilesDir(),fileName) ;
		        FileOutputStream fos = new FileOutputStream(file) ;
		        fos.write(content.getBytes());
		        fos.close();
		    }
   

  - 从内部存储读文件
  

		public String getContent(String fileName )throws IOException{
	        FileInputStream fileInputStream = context.openFileInput(fileName);
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        byte[] data = new byte[1024] ;
	        int len =-1 ;
	        while((len = fileInputStream.read(data))!= -1){
	            byteArrayOutputStream.write(data , 0 , len);
	        }
	        return new String(byteArrayOutputStream.toByteArray());
	    }


##外部存储的操作##
  - 获取SDCard的状态
	  
		public static boolean isSDExist(){
	        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ;
	    }


  - 获取SDCard的剩余空间
		
		public static long getSDAvailiableSize(){
		        if (isSDExist()){
		            StatFs statFs = new StatFs(getSDBasrDir()) ;
		            long blockSize = statFs.getBlockSize() ;
		            long availableSize = statFs.getAvailableBlocksLong();
		            return  blockSize*availableSize/1024/1024 ;
		        }
		        return 0 ;
		    }
	    
  - 获取SDCard的总空间

		public static long getSDSize(){
	        if (isSDExist()){
	            StatFs statFs = new StatFs(getSDBasrDir()) ;
	            long blockSize = statFs.getBlockSize() ;
	            long totalSize = statFs.getBlockCountLong() ;
	            return blockSize*totalSize/1024/1024 ;
	
	        }
	        return 0 ;
	    }


  - 保存文件到SDCard


		public boolean saveToDir(String fileName , String content)throws IOException{
	        String dir = Environment.getExternalStorageDirectory().getAbsolutePath()  ;
	        File file = new File(dir) ;
	
	        if (isSDExist()  ){
	            if (!file.exists()){
	                file.mkdirs();
	            }
	            FileOutputStream fileOutputStream = new FileOutputStream(new File(file, fileName) );
	            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
	            byte[] bytes = content.getBytes() ;
	            bufferedOutputStream.write(bytes);
	            bufferedOutputStream.flush();
	            return true;
	        }
	        return false ;
	    }

  - 从SDCard读取文件


		public String getContent(String fileName) throws IOException{
	        String dir = DIR+fileName  ;
	        File file = new File(dir) ;
	        if (file.exists()) {
	            FileInputStream fileInputStream = new FileInputStream(file);
	            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	            byte[] data = new byte[1024] ;
	            int len =-1 ;
	            while((len = fileInputStream.read(data))!= -1){
	                byteArrayOutputStream.write(data , 0 , len);
	            }
	            return new String(byteArrayOutputStream.toByteArray());
	
	        }
	        return null ;
	    }


