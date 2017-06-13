/**
 * User: wurenzhijing
 * Date: 2016-08-03
 * Time: 11:27
 * FIXME
 */
package com.example.administrator.testLearning;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import org.robolectric.util.ActivityController;

import static com.example.administrator.testLearning.MainActivity.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class ,sdk = 18)
public class MainActivityTest {

    Button btn_activity , btn_service , btn_broadcast ;
    EditText et_content ;
    MainActivity activity = Robolectric.setupActivity(MainActivity.class) ;

    @Test
    public void testActivity(){
        assertNotNull(activity);
        assertEquals(activity.getTitle(),"InentLearning");
    }

    @Before
    public void setUp(){
        btn_activity = (Button)activity.findViewById(R.id.bt_activity) ;
        btn_broadcast = (Button)activity.findViewById(R.id.bt_broadcast) ;
        btn_service = (Button)activity.findViewById(R.id.bt_service) ;
        et_content = (EditText)activity.findViewById(R.id.et_message) ;
        ShadowLog.stream = System.out ;
    }

    @Test
    public void testLifecycle(){
        ActivityController<MainActivity> activityController = Robolectric.buildActivity(MainActivity.class).create().start() ;
        Activity activity =  activityController.get() ;
        TextView tv = (TextView)activity.findViewById(R.id.tv_life ) ;
        assertEquals("onCreate" , tv.getText().toString());

        activityController.resume() ;
        assertEquals("onResume" , tv.getText().toString());

        activityController.destroy() ;
        assertEquals("onDestroy" , tv.getText().toString());
    }

    @Test
    public void testStartActivity(){
        et_content.setText("dsafsgagagaggdsa");
        btn_activity.performClick();
        assertTrue(btn_activity.isEnabled());

        Intent intent_activity = new Intent( activity, OtherActivity.class) ;
        Bundle bundle = new Bundle();
        bundle.putString("message", et_content.getText().toString());
        bundle.putParcelable("book", pBook);
        intent_activity.putExtras(bundle);

        Intent resultIntent = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(intent_activity , resultIntent) ;
    }

    @Test
    public void testBroadcast(){
        et_content.setText("dsafsgagagaggdsa");
        btn_broadcast.performClick() ;
        assertTrue(btn_broadcast.isEnabled());

        ShadowApplication shadowApplication = ShadowApplication.getInstance() ;
        String action = "aaaaaa" ;
        Intent intent = new Intent(action);
        Bundle bundle = new Bundle();
        bundle.putString("message", et_content.getText().toString());
        bundle.putSerializable("book", mBook);
        intent.putExtras(bundle);
        //  test wheather rigister
        assertTrue(shadowApplication.hasReceiverForIntent(intent));

        MyReceiver myReceiver = new MyReceiver();
        myReceiver.onReceive(RuntimeEnvironment.application ,intent);
        //  test  onReceive result
        SharedPreferences sharedPreferences = shadowApplication.getSharedPreferences("book" , Context.MODE_PRIVATE);
        assertEquals( "java编程思想" ,sharedPreferences.getString("book_name",""))  ;

        //  test Toast
        assertEquals(ShadowToast.getTextOfLatestToast() , "intent通过广播传递的数据： dsafsgagagaggdsa\nintent通过广播传递的对象: "
                + sharedPreferences.getString("book_name","")
                +" "+sharedPreferences.getString("book_author","")
                +" "+sharedPreferences.getString("book_ISBN","")
                +" "+sharedPreferences.getString("book_price",""));
    }

    @Test
    public void testService(){
        btn_service.performClick() ;
        assertTrue(btn_service.isEnabled());

        et_content.setText("dsafsgagagaggdsa");

        ShadowApplication shadowApplication = ShadowApplication.getInstance();
        Intent intent = new Intent(activity, MyService.class);
        intent.putExtra("message", et_content.getText().toString());
        shadowApplication.startService(intent);

        Log.i("-----------" , intent+"") ;

        MyService s = new MyService() ;
        int i = s.onStartCommand(intent , 0, 0) ;
        Log.i("------jj-----" , "   "+i) ;
        SharedPreferences sharedPreferences = shadowApplication.getSharedPreferences("message" , Context.MODE_PRIVATE);
        assertEquals( "dsafsgagagaggdsa" ,sharedPreferences.getString("content",""))  ;
    }

    @Test
    public void testFragment(){
        MyFragment myFragment = new MyFragment() ;
        SupportFragmentTestUtil.startFragment(myFragment);
        assertNotNull(myFragment.getView());
    }

}

