package com.example.send;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btn;
    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn = findViewById(R.id.btn);
        text1 = findViewById(R.id.text1);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v)
            { //EditText의 text를 저장
                String texts = text1.getText().toString(); //RequestActivity로 전달할 인텐트 생성
                Intent intent = new Intent(MainActivity.this, request.class); //text값 인텐트에 저장
                intent.putExtra("texts",texts); //RequestActivity 시작
                startActivity(intent);
            }
        });
    }
    }

