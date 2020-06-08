package com.example.new_kone;
// 코드를 볼때 위에 주석문에 숫자는 순서를 얘기하며 1은 카메라 요청 허용 방법을
// 2는 다음액티비티로 넘어가는 방법
// 3은 카메라 사용하는 방법을 부분으로 표현하기위해 넣은 것이다.
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;\


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {  // AppCompatActivity는 예전 버젼의 Activity이다.



    private ViewPager mViewPager;
    androidx.appcompat.app.ActionBar bar;
   // android.support.v7.app.ActionBar bar; // 액션바를 사용하기위한 선언
    private FragmentManager fm; // 프래그먼트를 관리하기위한 선언
    private ArrayList<Fragment> fList; // 위에 탭을 관리하기 위해 선언
    private WebView mWebView;
    private WebSettings mWebSettings;
    String Session_k;

    @Override // 위에서 상속을 받는 다는 뜻
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.frag_container_);
        fm = getSupportFragmentManager();
        bar = getSupportActionBar();
        bar.setDisplayShowTitleEnabled(false); // 액션바를 보이게
        bar.setTitle("FACE ATTENDANCE"); //  액션바의 이름을 설정 ***
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab1 = bar.newTab().setText("홈").setTabListener(tabListener);
        ActionBar.Tab tab2 = bar.newTab().setText("게시판").setTabListener(tabListener);
        ActionBar.Tab tab3 = bar.newTab().setText("캘린더").setTabListener(tabListener);

        bar.addTab(tab1);
        bar.addTab(tab2);
        bar.addTab(tab3);


        // 아래는 Login 화면에서 넘겨 받은 Session_key 정보
        Intent intent = getIntent();
        Session_k = intent.getExtras().getString("Session_key"); // 세션값이 넘어온것 확인 o.
        fList = new ArrayList<Fragment>();

        fList.add(home_pageF.newInstance(Session_k));
        fList.add(board_pageF.newInstance());
        fList.add(calendar_pageF.newInstance());

        mViewPager.setOnPageChangeListener(viewPagerListener);

        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(fm, fList);
        mViewPager.setAdapter(adapter);

        //Bundle bundle = new Bundle();
        //bundle.putString("Session_key",Session_k);

    }

    ViewPager.SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            bar.setSelectedNavigationItem(position);
        }
    };
//터치를 했을떄
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
