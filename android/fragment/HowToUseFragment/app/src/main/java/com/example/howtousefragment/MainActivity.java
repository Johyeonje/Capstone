package com.example.howtousefragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LinearLayout button1 = (LinearLayout)findViewById(R.id.btn1);
        LinearLayout button2 = (LinearLayout)findViewById(R.id.btn2);
        LinearLayout button3 = (LinearLayout)findViewById(R.id.btn3);

 // 첫번째 버튼을 클릭했을때 동작하여라=============================================================
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.frameLayout1,new frag_a()).commit();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.frameLayout1,new frag_b()).commit();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction().replace(R.id.frameLayout1,new frag_c()).commit();
            }
        });

//===============================================================================================
    }
}
