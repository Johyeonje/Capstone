package com.example.new_kone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;

public class select_menuF extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_menu);


        Button subject1 = findViewById(R.id.subject1);
        Button subject2 = findViewById(R.id.subject2);
        Button subject3 = findViewById(R.id.subject3);
        Button subject4 = findViewById(R.id.subject4);
        Button subject5 = findViewById(R.id.subject5);


        subject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String set_menu = "sub1"; // 배열이 아닌 스트링 형태로 보내는 것을 주의! 배열 형태로 보내면 넘어가서 값을 비교할때 배열을 비교해서 같음을 볼 수 없음.
                String set_student = "학생A 학생C 학생H 학생F 학생G"; // 수업1을 듣는 학생들
                Intent intent = new Intent(getApplicationContext(), before_take_photo.class); // 다음 값을 액티비티에서 액티비티로 넘겨줌
                intent.putExtra("select_menu",set_menu); // 다음 "select_menu"라는 이름으로 set_menu를 넣고 다음 액티비티를 연다.
                intent.putExtra("sub_student",set_student); // 수업 1을 듣는 학생들을 다음과 같이 보낸다. 전체 스트링으로
                startActivity(intent);
            }
        });
        subject2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String set_menu = "sub2";
                String set_student = "학생B 학생H 학생K 학생O 학생P";
                Intent intent = new Intent(getApplicationContext(), before_take_photo.class);
                intent.putExtra("select_menu",set_menu);
                intent.putExtra("sub_student",set_student);
                startActivity(intent);
            }
        });
        subject3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String set_menu = "sub3";
                String set_student = "학생A 학생O 학생K 학생G";
                Intent intent = new Intent(getApplicationContext(), before_take_photo.class);
                intent.putExtra("select_menu",set_menu);
                intent.putExtra("sub_student",set_student);
                startActivity(intent);
            }
        });
        subject4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String set_menu = "sub4";
                String set_student = "학생A 학생C 학생H 학생L 학생K";
                Intent intent = new Intent(getApplicationContext(), before_take_photo.class);
                intent.putExtra("select_menu",set_menu);
                intent.putExtra("sub_student",set_student);
                startActivity(intent);
            }
        });
        subject5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String set_menu = "sub5";
                String set_student = "학생Y 학생K 학생H 학생F 학생G";
                Intent intent = new Intent(getApplicationContext(), before_take_photo.class);
                intent.putExtra("select_menu",set_menu);
                intent.putExtra("sub_student",set_student);
                startActivity(intent);
            }
        });

    }

}
