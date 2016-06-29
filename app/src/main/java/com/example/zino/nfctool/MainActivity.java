package com.example.zino.nfctool;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(myPagerAdapter);
    }

    public void btnClick(View view) {
        if (view.getId() == R.id.bt_read) {
            viewPager.setCurrentItem(0); //읽기용 프레그먼트로 이동
        } else if (view.getId() == R.id.bt_write) {
            viewPager.setCurrentItem(1); //쓰기용 프레그먼트로 이동
        }
    }
}






