package com.example.administrator.processlearning;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service {

    class MyBinder extends IMyService.Stub{

        @Override
        public String getValue(Person1 person) throws RemoteException {
            person.setName(person.getName()+"添加的名字 hhh");
            return person.toString();
        }

        @Override
        public Person1 getPerson() throws RemoteException {
            Person1 person = new Person1();
            person.setName("hebin");
            person.setGender("nan");
            person.setAge(22);
            return person;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    public MyService() {
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


}
