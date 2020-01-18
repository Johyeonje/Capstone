package com.example.fracapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity<sm> extends AppCompatActivity {

    private ViewPager mViewPager;
    private ActionBar bar; // 액션바를 사용하기위한 선언
    private FragmentManager fm; // 프래그먼트를 관리하기위한 선언
    private ArrayList<Fragment> fList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("FRAC");



        mViewPager = (ViewPager) findViewById(R.id.action_container);
        fm = getSupportFragmentManager();
        bar = getSupportActionBar();
        bar.setDisplayShowTitleEnabled(true); // 액션바를 보이게
        bar.setTitle("FACE ATTENDANCE"); //  액션바의 이름을 설정 ***
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    ActionBar.Tab tab1 = bar.newTab().setText("게시판").setTabListener(tabListener);
    ActionBar.Tab tab2 = bar.newTab().setText("홈").setTabListener(tabListener);
    ActionBar.Tab tab3 = bar.newTab().setText("캘린더").setTabListener(tabListener);

        bar.addTab(tab1);
        bar.addTab(tab2);
        bar.addTab(tab3);


    fList = new ArrayList<Fragment>();

        fList.add(boardfragActivity.newInstance());
        fList.add(homefragActivity.newInstance());
        fList.add(timefragActivity.newInstance());

        mViewPager.setOnPageChangeListener(viewPagerListener);

    tabActivity adapter = new tabActivity(fm, fList);
        mViewPager.setAdapter(adapter);


}

    ViewPager.SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            bar.setSelectedNavigationItem(position);
        }
    };

    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    };



    @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

    }

