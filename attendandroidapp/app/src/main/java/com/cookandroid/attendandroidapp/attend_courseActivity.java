package com.cookandroid.attendandroidapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class attend_courseActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_course);
        setTitle("E-Attend");

        Button computer_btn = findViewById(R.id.computer_btn);

        computer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(),computercourseActivity.class);
                startActivity(intent);
            }
        });

        linearLayout = findViewById(R.id.layout);
        Button make_btn = findViewById(R.id.make_btn);
        context = this;
        final EditText editbox4 = findViewById(R.id.edibox4);

        make_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Button btn = new Button(context);
                 btn.setText(editbox4.getText().toString());
                 linearLayout.addView(btn);

            }
        });
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
