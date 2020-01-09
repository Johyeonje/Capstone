package com.cookandroid.attendandroidapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class attend_courseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_course);
        setTitle("E-Attend");

    }



        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.login_btn:
                    Intent intent1 = new Intent(getApplicationContext(), loginActivity.class);

                    startActivity(intent1);
                    break;

                case R.id.home_btn:
                    Intent intent2 = new Intent(getApplicationContext(), com.cookandroid.attendandroidapp.MainActivity.class);
                    startActivity(intent2);
                    break;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

    }
