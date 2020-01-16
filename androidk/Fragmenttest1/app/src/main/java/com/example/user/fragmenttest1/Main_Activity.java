package com.example.user.fragmenttest1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.drm.DrmStore;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


public class Main_Activity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_CAMERA=1001;

    private ViewPager mViewPager;
    android.support.v7.app.ActionBar bar;
    private FragmentManager fm;
    private ArrayList<Fragment> fList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_);

        //==============================1=1=1=1=1========================카메라 권한 요청허용=========
        //카메라가 요청이 허용이 되었는지 안되었는지 체크
        int permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        //메니페스트에 카메라가 권한이 요청이 되어있는지 안되어있는지 확인을 한다.
        //그 다음 아래로 넘어간다. 카메라 사용이 허용됬다면 PERMISSION_GRANTED 상태이다.
        //권한 요청
        if (permssionCheck!= PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"권한 승인이 필요합니다",Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this,"얼굴인식 출석을 위해 카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } //사용자가 거부를 하면 true를 반환한다.
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
                Toast.makeText(this,"얼굴인식 출석을 위해 카메라 권한이 필요합니다.", Toast.LENGTH_LONG).show();

            }
        }

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