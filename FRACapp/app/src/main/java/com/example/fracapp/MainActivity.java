package com.example.fracapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity<sm> extends AppCompatActivity {

    private ViewPager mViewPager;
    private ActionBar bar; // 액션바를 사용하기위한 선언
    private FragmentManager fm; // 프래그먼트를 관리하기위한 선언
    private ArrayList<Fragment> fList;
    int MAX_PAGE=2;
    Fragment cur_fragment=new Fragment();
    private ActionBar.TabListener tabListener;

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

        ViewPager viewPager=(ViewPager)findViewById(R.id.slide_container);
        viewPager.setAdapter(new adapter(getSupportFragmentManager()));
}

    ViewPager.SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            bar.setSelectedNavigationItem(position);
        }
    };


    private class adapter extends FragmentPagerAdapter {

        public adapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if(position<0 || MAX_PAGE<=position)
                return null;
            switch (position){
                case 0:
                    cur_fragment=new noticeActivity();
                    break;
                case 1:
                    cur_fragment=new todolistActivity();
                    break;
            }
            return cur_fragment;
        }
        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

    }


