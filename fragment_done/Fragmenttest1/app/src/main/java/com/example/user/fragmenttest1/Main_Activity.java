package com.example.user.fragmenttest1;

import android.drm.DrmStore;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;


public class Main_Activity extends AppCompatActivity {

    private ViewPager mViewPager;
    android.support.v7.app.ActionBar bar;
    private FragmentManager fm;
    private ArrayList<Fragment> fList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_);

            mViewPager = (ViewPager) findViewById(R.id.frag_container_);
        fm = getSupportFragmentManager();
        bar = getSupportActionBar();
        bar.setDisplayShowTitleEnabled(true);
        bar.setTitle("JSEA_Community");
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab1 = bar.newTab().setText("홈").setTabListener(tabListener);
        ActionBar.Tab tab2 = bar.newTab().setText("게시판").setTabListener(tabListener);
        ActionBar.Tab tab3 = bar.newTab().setText("회의록").setTabListener(tabListener);
        ActionBar.Tab tab4 = bar.newTab().setText("공지사항").setTabListener(tabListener);

        bar.addTab(tab1);
        bar.addTab(tab2);
        bar.addTab(tab3);
        bar.addTab(tab4);

        fList = new ArrayList<Fragment>();

        fList.add(Home_frag.newInstance());
        fList.add(Board_frag.newInstance());
        fList.add(Minute_frag.newInstance());
        fList.add(Notice_frag.newInstance());

        mViewPager.setOnPageChangeListener(viewPagerListener);

        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(fm, fList);
        mViewPager.setAdapter(adapter);
    }

    ViewPager.SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener(){
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
}