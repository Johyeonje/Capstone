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

    }
        public void clickHandler (View view) {
            transaction = fragmentManager.beginTransaction();

            switch (view.getId()) {
                case R.id.time_btn:
                    transaction.replace(R.id.frame, timefragment).commitAllowingStateLoss();
                    break;
                case R.id.home_btn:
                    transaction.replace(R.id.frame, homefragment).commitAllowingStateLoss();
                    break;
                case R.id.board_btn:
                    transaction.replace(R.id.frame, boardfragment).commitAllowingStateLoss();
                    break;
            }
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

    }


