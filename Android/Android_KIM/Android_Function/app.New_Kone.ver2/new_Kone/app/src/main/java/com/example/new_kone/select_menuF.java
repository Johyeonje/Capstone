package com.example.new_kone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class select_menuF extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_menu);
        String url = "http://rbghoneroom402.iptime.org:48526/JSP/Login.jsp";
        String Session_key = getIntent().getStringExtra("Session_Key");
        // 아래는 위의 URL과 Session_key를 넘겨 해당 사용자의 정보를 받아오기 위한 함수.
        Request_user_info(url,Session_key);

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
//다음 세션키를 받아와서 JSP로 넘겨주는 코드를 작성해야함. 수정 필요.
    public void Request_user_info(String URL,String Session_key)
    {
        String info; //
        Request_user_network.NetworkTask networkTask = new Request_user_network.NetworkTask(URL,Session_key,getBaseContext());

        try {
            info = networkTask.execute().get();
            Toast.makeText(getBaseContext(), info.substring(0,8), Toast.LENGTH_SHORT).show(); // 쿠키의 1부터 7까지의 스트링을 가지고온다.Session일때 성공
            //다른거 뜰때는 로그인 실패.
        } catch (Exception e) {
            Toast.makeText(this,"data 읽기에 실패 했습니다.", Toast.LENGTH_LONG).show();
        }

    }
}
