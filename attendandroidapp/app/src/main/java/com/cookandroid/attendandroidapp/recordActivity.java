package com.cookandroid.attendandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class recordActivity extends AppCompatActivity {

    ArrayList courseList;

    final String[] select_item = {""};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        setTitle("E-Attend");

        courseList = new ArrayList();
        courseList.add("과목1");
        courseList.add("과목2");
        courseList.add("과목3");
        courseList.add("과목4");

        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, courseList);
        spinner2.setAdapter(adapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                select_item[0] = String.valueOf(courseList.get(arg2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


    }
}