package com.example.administrator.messageserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MyServiceAIDL extends Service {

    class MyBinder extends IMyServiceAIDL.Stub {

        @Override
        public String getValue(Person person) throws RemoteException {
            person.setName(person.getName() + "aidl 添加的名字 hhh");
            return person.toString();
        }

        @Override
        public Person getPerson() throws RemoteException {
            Person person = new Person();
            person.setName("hebin");
            person.setGender("nan");
            person.setAge(22);
            return person;
        }
    }


    public MyServiceAIDL() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }


}
