#数据持久化之序列化#


##简介##

对象的序列化是指把Java对象转换为字节序列并存储至一个存储媒介的过程，序列化对象的原因大概分为三种：
 
 - 永久性保存对象，保存字节序列到本地文件中；
 - 对象在网络中传递；
 - 对象在IPC间传递。
 
##序列化方式##

序列化一般分为两种，即Serializable接口和Parcelable接口，如下：
 
  - Serializable接口是Java中的接口，其实现很简单，只需让序列化的类实现Serializable接口即可，系统会自动实现序列化，但是这种方式会产生大量的临时变量，从而引起频繁的GC，具体如下：
  
		
		public class SerializablePeople implements Serializable {
		    String name ;
		    String gender ;
		    int age ;
		    static final long serialVersionUID = 90090990 ;
		
		    public SerializablePeople(String name , String gender , int  age  ){
		        this.name = name ;
		        this.gender = gender ;
		        this.age =age ;
		    }
		
		    public String getName() {
		        return name;
		    }
		
		    public void setName(String name) {
		        this.name = name;
		    }
		
		    public String getGender() {
		        return gender;
		    }
		
		    public void setGender(String gender) {
		        this.gender = gender;
		    }
		
		    public int getAge() {
		        return age;
		    }
		
		    public void setAge(int age) {
		        this.age = age;
		    }
		
		}
  在Activity中的传递方式：

		//Serializable传递
		SerializablePeople serializablePeople = new SerializablePeople(name , gender , age);
        Intent intent = new Intent(MainActivity.this, SerializableActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("serializablePeople",serializablePeople);
        intent.putExtras(bundle);
        startActivity(intent);

    在Activity中的传递方式：

        //Serializable接收
		Intent intent = getIntent();
        SerializablePeople serializablePeople = (SerializablePeople)intent.getSerializableExtra("serializablePeople");
        String name = serializablePeople.getName();
        String gender = serializablePeople.getGender() ;
        int age = serializablePeople.getAge() ;

  - Parcelable接口是Android自带的，相对性能比较高，更好实现对象在IPC间的传递，Parcelable接口的实现需要实现两个方法和一个静态接口，分别是writeToParcel()，describeContents()，Parcelable.Creator接口，具体如下：
  

		public class ParcelablePeople implements Parcelable {
		    String name ;
		    String gender ;
		    int  age ;
		
		    public ParcelablePeople(String name , String gender , int  age  ){
		        this.name = name ;
		        this.gender = gender ;
		        this.age =age ;
		    }
		
		    public ParcelablePeople(Parcel source){
		        name = source.readString();
		        gender = source.readString();
		        age = source.readInt() ;
		    }
		
		    public String getName() {
		        return name;
		    }
		
		    public void setName(String name) {
		        this.name = name;
		    }
		
		    public String getGender() {
		        return gender;
		    }
		
		    public void setGender(String gender) {
		        this.gender = gender;
		    }
		
		    public int getAge() {
		        return age;
		    }
		
		    public void setAge(int age) {
		        this.age = age;
		    }
		
		    @Override
		    public int describeContents() {
		        return 0;
		    }
		
		    @Override
		    public void writeToParcel(Parcel parcel, int i) {
		        parcel.writeString(name);
		        parcel.writeString(gender);
		        parcel.writeInt(age);
		    }
		
		    public final static Parcelable.Creator<ParcelablePeople> CREATOR = new Parcelable.Creator<ParcelablePeople>() {
		        @Override
		        public ParcelablePeople createFromParcel(Parcel parcel) {
		            return new ParcelablePeople(parcel);
		        }
		
		        @Override
		        public ParcelablePeople[] newArray(int i) {
		            return new ParcelablePeople[i];
		        }
		    };
		}



    在Activity中的传递方式：

		ParcelablePeople parcelablePeople = new ParcelablePeople(name , gender , age );
        parcelablePeople.setName(name);
        parcelablePeople.setGender(gender);
        parcelablePeople.setAge(age);

        Intent intent = new Intent(MainActivity.this,ParcelableActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("parcelablePeople",parcelablePeople);
        intent.putExtras(bundle) ;
        startActivity(intent);

    在Activity中的接收方式

		Intent intent = getIntent();
        ParcelablePeople parcelablePeople = (ParcelablePeople)intent.getParcelableExtra("parcelablePeople");
        String name = parcelablePeople.getName();
        String gender = parcelablePeople.getGender() ;
        int age = parcelablePeople.getAge() ;


##Parcelable与Serializable的区别##

可以看出Parcelable性能比较高，但是使用比较复杂，Serializable使用简单，但是效率却比较低。

  - 当使用内存的时候Parcelable比Serializable性能高；
  - Serializable在序列化时产生大量的临时变量，会引起频繁的GC；
  - Parcelable不能使用在将对象存储在磁盘上。
