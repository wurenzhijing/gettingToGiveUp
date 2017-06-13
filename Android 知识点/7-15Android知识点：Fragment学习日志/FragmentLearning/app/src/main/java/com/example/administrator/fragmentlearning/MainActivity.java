package com.example.administrator.fragmentlearning;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements Fragment1.onMyListener {

    private TextView tv_fragment1,tv_fragment2,tv_fragment3,tv_message;
    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;

    private int currentPosition;



    private List<Fragment> fragmentList = new ArrayList<>();

    final View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_fragment1:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.tv_fragment2:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.tv_fragment3:
                    viewPager.setCurrentItem(2);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_message = (TextView)findViewById(R.id.tv_message);

        tv_fragment1 = (TextView)findViewById(R.id.tv_fragment1);
        tv_fragment2 = (TextView)findViewById(R.id.tv_fragment2);
        tv_fragment3 = (TextView)findViewById(R.id.tv_fragment3);

        tv_fragment1.setOnClickListener(myClickListener);
        tv_fragment2.setOnClickListener(myClickListener);
        tv_fragment3.setOnClickListener(myClickListener);

        viewPager = (ViewPager)findViewById(R.id.viewPager);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
//        Fragment2 ff =(Fragment2)getSupportFragmentManager().findFragmentById(R.layout.fragment2);
//        ff.tv_message.setText(message);




        fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        tv_fragment1.setTextColor(Color.GREEN);
                        tv_fragment2.setTextColor(Color.BLACK);
                        tv_fragment3.setTextColor(Color.BLACK);
                        break;
                    case 1:
                        tv_fragment2.setTextColor(Color.GREEN);
                        tv_fragment1.setTextColor(Color.BLACK);
                        tv_fragment3.setTextColor(Color.BLACK);
                        break;
                    case 2:
                        tv_fragment3.setTextColor(Color.GREEN);
                        tv_fragment2.setTextColor(Color.BLACK);
                        tv_fragment1.setTextColor(Color.BLACK);
                        break;
                }
                currentPosition = position ;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    public Fragment getFragment2(){
        return fragment2;
    }

    @Override
    public void test(String str) {
        tv_message.setText("Fragment1传递的信息：  "+str);

        fragment2.setData(str);

    }


}
