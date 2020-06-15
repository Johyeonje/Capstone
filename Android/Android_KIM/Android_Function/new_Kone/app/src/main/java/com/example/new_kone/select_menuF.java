package com.example.new_kone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.concurrent.ExecutionException;

public class select_menuF extends Activity {
    String User_Class;
    String checked = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_menu);
        final LinearLayout lm = (LinearLayout)findViewById(R.id.select_menu); // select_menu라는 xml 파일에 lm LinearLayout을 만든다.
        Integer Class_number = 0; // 수업의 개수를 숫자로 나타내기위해서 처음은 0 , 과목의 수 만큼 증가함. (전체수 -1)
        String url = "http://rbghoneroom402.iptime.org:48526/JSP/Subject.jsp"; // 과목 정보를 받아올 JSP 정보를 입력한다.
        Intent intent = getIntent();
        final String Session_key = intent.getExtras().getString("Session_key"); // 값 넘어옴 확인o
        // 아래는 위의 URL과 Session_key를 넘겨 해당 사용자의 정보를 받아오기 위한 함수.


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT); // 만든 LinearLayout의 가로 길이와 세로 길이를 정한다.
        Request_user_info(url,Session_key); // 세션키를 보내 과목 정보를 받아오기 위해.
        final String[] Class = User_Class.split("\\n+");// "\\n+"는 줄바꿈을 확인하기위한 문자.

        for(int i=0;i<Class.length;i++) { // Class의 길이 만큼 읽는다. 과목 만큼 버튼이 생성된다.

            LinearLayout select_menu = new LinearLayout(this);
            select_menu.setOrientation(LinearLayout.VERTICAL);

            final Button btn = new Button(this); // 버튼을 새로 생성한다.

            String[] subject_name = Class[i].split("\t");

            btn.setId(Class_number);
            btn.setText(subject_name[1]);
            btn.setTextSize(40);
            btn.setLayoutParams(params);

            final String set_menu = Class[i];
            select_menu.addView(btn);
            lm.addView(select_menu);

            final String Convert_Class_number = Integer.toString(Class_number);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String set_menu = Convert_Class_number; // 배열이 아닌 스트링 형태로 보내는 것을 주의! 배열 형태로 보내면 넘어가서 값을 비교할때 배열을 비교해서 같음을 볼 수 없음.
                    Intent intent = new Intent(getApplicationContext(), before_take_photo.class); // 다음 값을 액티비티에서 액티비티로 넘겨줌
                    intent.putExtra("class_number",Convert_Class_number);
                    intent.putExtra("Select_menu",set_menu); // 다음 "select_menu"라는 이름으로 set_menu를 넣고 다음 액티비티를 연다.
                    intent.putExtra("Session_key",Session_key); // 세션키를 넘겨준다.
                    intent.putExtra("All_subject",User_Class);
                    intent.putExtra("checked",checked);
                    startActivity(intent);

                }
            });


            Class_number = Class_number +1; // Class의 ID명을 정하기 위해
            // 처음 아이디는 0부터 시작한다.
        }





        // make_menu(User_Class);

        /*


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

         */

    }

//다음 세션키를 받아와서 JSP로 넘겨주는 코드를 작성해야함. 수정 완료. 2020/05/25
    public void Request_user_info(String URL,String Session_key)
    {
        //String user_Subject; // JSP에서 정보를 받아오는지 확인하기위한 변수

        Request_user_network.NetworkTask networkTask = new Request_user_network.NetworkTask(URL,Session_key,getBaseContext());
        try {
            User_Class = networkTask.execute().get();
            //Toast.makeText(this, User_Class,Toast.LENGTH_LONG).show(); 확인용 Toast
        } catch (Exception e) {
            Toast.makeText(this, "데이터를 읽어 올 수 없습니다.",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
