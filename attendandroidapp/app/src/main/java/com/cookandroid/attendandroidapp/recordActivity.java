package com.cookandroid.attendandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class recordActivity extends AppCompatActivity {

    ArrayList courseList;
    private FragmentManager fragmentManager;
    private fragment1Activity fragment1;
    private fragment2Activity fragment2;
    private fragment3Activity fragment3;
    private fragment4Activity fragment4;
    private FragmentTransaction transaction;

    static String select_item;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        fragmentManager = getSupportFragmentManager();

        fragment1 = new fragment1Activity();
        fragment2 = new fragment2Activity();
        fragment3 = new fragment3Activity();
        fragment4 = new fragment4Activity();


        final String[] courseList = {"과목1", "과목2", "과목3", "과목4"};

        Intent intent = getIntent();
        final String text1 = intent.getStringExtra("text1");
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment1).commit();
        Bundle bundle = new Bundle();
        bundle.putString("text1",text1); //fragment1로 번들 전달
        fragment1.setArguments(bundle);


        final Spinner spinner2 = findViewById(R.id.spinner2);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, courseList);
        spinner2.setAdapter(adapter1);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


                select_item = courseList[arg2];
                transaction = fragmentManager.beginTransaction();
                switch (arg2) {
                    case 0:
                        transaction.replace(R.id.frameLayout, fragment1).commitAllowingStateLoss();
          //fragment 생성//requestActivity에 fragment1을 띄워줌
                        break;
                    case 1:
                        transaction.replace(R.id.frameLayout, fragment2).commitAllowingStateLoss();
                        break;
                    case 2:
                        transaction.replace(R.id.frameLayout, fragment3).commitAllowingStateLoss();
                        break;
                    case 3:
                        transaction.replace(R.id.frameLayout, fragment4).commitAllowingStateLoss();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()) {
                case R.id.login_btn:
                    Intent intent1 = new Intent(getApplicationContext(), loginActivity.class);

                    startActivity(intent1);
                    break;

                case R.id.home_btn:
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent2);
                    break;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

    }
