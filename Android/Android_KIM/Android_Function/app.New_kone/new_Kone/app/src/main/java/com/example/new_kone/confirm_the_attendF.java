package com.example.new_kone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class confirm_the_attendF extends AppCompatActivity {


    private FragmentManager fragmentManager;
    private list1_pageF fragment1;
    private list2_pageF fragment2;
    private list3_pageF fragment3;
    private list4_pageF fragment4;
    private FragmentTransaction transaction;

    static String select_item;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_the_attend);

        fragmentManager = getSupportFragmentManager();

        fragment1 = new list1_pageF();
        fragment2 = new list2_pageF();
        fragment3 = new list3_pageF();
        fragment4 = new list4_pageF();


        final String[] courseList = {"과목1", "과목2", "과목3", "과목4"};


        final Spinner confirm_spinner = findViewById(R.id.confirm_spinner);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, courseList);
        confirm_spinner.setAdapter(adapter1);

        confirm_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


                select_item = courseList[arg2];
                transaction = fragmentManager.beginTransaction();
                switch (arg2) {
                    case 0:
                        transaction.replace(R.id.fragment, fragment1).commitAllowingStateLoss();
                        break;
                    case 1:
                        transaction.replace(R.id.fragment, fragment2).commitAllowingStateLoss();
                        break;
                    case 2:
                        transaction.replace(R.id.fragment, fragment3).commitAllowingStateLoss();
                        break;
                    case 3:
                        transaction.replace(R.id.fragment, fragment4).commitAllowingStateLoss();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
}
