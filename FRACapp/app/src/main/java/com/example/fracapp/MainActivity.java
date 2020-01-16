package com.example.fracapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private boardfragActivity boardfragment;
    private homefragActivity homefragment;
    private timefragActivity timefragment;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("FRAC");

        fragmentManager = getSupportFragmentManager();

        boardfragment = new boardfragActivity();
        homefragment = new homefragActivity();
        timefragment = new timefragActivity();

        Button time_btn = findViewById(R.id.time_btn);

        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout1, timefragment).commitAllowingStateLoss();
            }
        });

        Button board_btn = findViewById(R.id.board_btn);

        board_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout2, boardfragment).commitAllowingStateLoss();
            }
        });

        Button home_btn = findViewById(R.id.home_btn);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout3, homefragment).commitAllowingStateLoss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}

