#网络通信——网络操作#

##简介##
Android网络连接的基本实现有两种方式，一种是通过http来实现，另一种是通过socket来实现。

  - Http通信最显著的特点是客户端发送的每次请求都需要服务端回送响应，在请求结束后，会主动释放连接。
  - Socket通信，建立Socket连接至少需要一对套接字，一个运行于客户端ClientSocket，另一个运行于服务端ServerSocket。Socket一旦建立连接，双方就可以开始互相发送数据内容，直至双方连接断开。

##Http通信##

Android中实现Http通信是使用HttpURLConnection接口，Http通信有两种方式即POST和GET。

    //获取HttpURLConnection对象
	URL urlPath = new URL(url) ;
    HttpURLConnection urlConnection = (HttpURLConnection)urlPath.openConnection();	



  - GET方法，可以获得静态页面，也可以把参数放在URL字符串后面，传递给服务器；


		public  void javaHttpGet(String url){
	        URL urlPath ;
	        try{
	            urlPath = new URL(url) ;
				// 打开连接
	            HttpURLConnection urlConnection = (HttpURLConnection)urlPath.openConnection() ;
	            urlConnection.setConnectTimeout(5000);
				//连接成功
	            urlConnection.connect();
	            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String inputLine = null ;
	            String resultData = null ;
	            while((inputLine = bufferedReader.readLine()) != null){
	                resultData+= inputLine ;
	            }
	            Log.i("_____GET_____",resultData);
	        }catch (MalformedURLException e){
	            e.printStackTrace();
	        }catch (IOException e){
	            e.printStackTrace();
	        }
	    }

  - POST方法，方法的参数在http请求中：
 

		public String javaHttpPost(String url){
	        String params ;
	        String str = null ;
	        try{
	            params = "username="+ URLEncoder.encode("hello","UTF-8")+"&password="+URLEncoder.encode("eoe","UTF-8");
	            byte[] postData = params.getBytes() ;
	            URL urlPath = new URL(url) ;
                // 打开连接
	            HttpURLConnection urlConnection = (HttpURLConnection)urlPath.openConnection();
	            urlConnection.setConnectTimeout(5000);
                // post请求 设置为true
	            urlConnection.setDoOutput(true);
	            urlConnection.setUseCaches(false);
				//  设置请求类型  默认为GET
	            urlConnection.setRequestMethod("POST");
	            urlConnection.setInstanceFollowRedirects(true);
				// 设置连接Content-Type
	            urlConnection.setRequestProperty("Content-Type" , "application/x-www-form-urlencode");
	            urlConnection.connect();
	
	            DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
				// 要上传的参数
	            dataOutputStream.write(postData);
	            dataOutputStream.flush();
	            dataOutputStream.close();
	            Log.i("-------POST-------",urlConnection.getResponseCode()+"");
	            if (urlConnection.getResponseCode() == 200){
	                byte[] data = readInputStream(urlConnection.getInputStream());
	                  str = new String(data ,"UTF-8");
	                Log.i("-----POST------",str);
	            }
	        }catch (UnsupportedEncodingException e){
	            e.printStackTrace();
	        }catch (MalformedURLException e){
	            e.printStackTrace();
	        }catch (IOException e){
	            e.printStackTrace();
	        }catch (Exception e){
	            e.printStackTrace();
	        }
	        return str ;
	    }
  
##Socket通信##

要实现Socket通信至少要有一个客户端一个服务端：
 
  - 服务端，创建服务端套接字并绑定到一个端口上，套接字设置监听模式 等待连接请求，接受连接请求后进行通信，返回等待下一个连接请求。

		public class Server implements Runnable {  
	        public static final String SERVERIP = "192.168.252.146";  
	        public static final int SERVERPORT = 8080 ;
	      
	        public void run() {  
	            try {  
	                System.out.println("Server: Connecting...");  
	      
	                ServerSocket serverSocket = new ServerSocket(SERVERPORT);  
	                while (true) {  
	                    Socket client = serverSocket.accept();  
	                    System.out.println("Server: Receiving...");  
	                    try {  
	                        BufferedReader in = new BufferedReader(  
	                                new InputStreamReader(client.getInputStream()))；
	 
	                        PrintWriter out = new PrintWriter(new BufferedWriter(  
	                                new OutputStreamWriter(client.getOutputStream())),true);  
	                          
	                        String str = in.readLine();  
	                        if (str != null ) {              
	                            out.println("You sent to server message is:   " + str);  
	                            out.flush();  
	                                   
	                            File file = new File ("C://android.txt");  
	                            FileOutputStream fops = new FileOutputStream(file);   
	                            byte [] b = str.getBytes();  
	                            for ( int i = 0 ; i < b.length; i++ )  
	                            {  
	                                fops.write(b[i]);  
	                            }  
	                            System.out.println("Server: Received: '" + str + "'");  
	                        } else {  
	                            System.out.println("Not receiver anything from client!");  
	                        }  
	                    } catch (Exception e) {  
	                        System.out.println("Server: Error 1");  
	                        e.printStackTrace();  
	                    } finally {  
	                        client.close();  
	                        System.out.println("Server: Done.");  
	                    }  
	                }  
	            } catch (Exception e) {  
	                System.out.println("Server: Error 2");  
	                e.printStackTrace();  
	            }  
	        }  
	          
	        public static void main(String [] args ) {  
	            Thread desktopServerThread = new Thread(new Server());  
	            desktopServerThread.start();  
	      
	        }  

  - 客户端，创建客户端套接字，需要指定服务端IP地址与端口号，连接到服务端，与服务端进行通信，关闭套接字。
 	
		public String  connect(){
	        Socket socket = null ;
	        String backMessage = null ;
	        try{
	            InetAddress inetAddress = InetAddress.getByName("192.168.252.146");
	            Log.i("TAG","TCP   connecting  "+inetAddress.toString());
	            socket = new Socket(inetAddress ,8080);
	            Log.i(TAG ,"TCP   sending");
	            
	            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
	            String message = et_message.getText().toString() ;
	            out.println(message);
	            out.flush();
	            
	            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            backMessage = in.readLine();
	            Log.i(TAG  , "Server 返回"+backMessage);
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	            Log.i(TAG ,"192.168.252.146 is unkown");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally {
	            try{
	                socket.close();
	            } catch ( Exception e) {
	                e.printStackTrace();
	            }
	        }
	        return backMessage ;
	    }

##监听网络状态


监听网络状态需要用到ConnectivityManager，它可以监听手机网络状态，当手机状态改变时发送广播，当一个网络连接失败进行故障切换，使用ConnectivityManager时需要在AndroidManifest中加入权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> 。


		public static boolean isNetWorkAvailable(Context context){
	        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        if (connectivityManager == null){
	            Log.i("Network","Unavailable");
	            return false ;
	        }else{
	            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
	            if (infos!=null){
	                for (int i= 0;i<infos.length ; i++){
	                    if (infos[i].getState() == NetworkInfo.State.CONNECTED){
	                        Log.i("Network"," available");
	                        return true ;
	                    }
	                }
	            }
	        }
	        return false ;
	    }

	