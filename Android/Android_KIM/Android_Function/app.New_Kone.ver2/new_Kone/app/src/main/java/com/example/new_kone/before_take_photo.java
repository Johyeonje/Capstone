package com.example.new_kone;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class before_take_photo extends Activity {
        private static final float FONT_SIZE = 10; // 생성되는 이름의 폰트 크기
        ImageView imageView1;
        EditText editText1;
    //================15==15==15==15==15===15==========================================
        EditText selected_date;
        CalendarView calendarView;
    //================15==15==15==15==15===15==========================================
    //==========================3=3=3=3=3======변경후 필요 변수=========================
        Button next_step;
        String choose_Subject; // 다음 과목을 넣기 위해, 다음 사진 찍는 곳으로 넘겨준다.
        String selected_m,sub_student_info,choose_date;

    //==============================3=3=3=3=3=3= 변경후 필요변수========================
    //================================7=7=7=7=7=7=7=7==============스크롤바 학생을 넣는 방법=====
    LinearLayout linearLayout;
    //================================7=7=7=7=7=7=7=7==============스크롤바 학생을 넣는 방법=====

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.befor_take_photo);

        imageView1 = findViewById(R.id.imageView1);
        editText1 = findViewById(R.id.select_class);
        //========================3=3=3=3=3=3=========변경후 =================================
        next_step = findViewById(R.id.Next);
        //========================3=3=3=3=3=3=========변경후 =================================
        //=========액티비디로 값을 받아올때 부분===================13==13==13==13==13==13===13====================
        Intent intent = getIntent(); // select_menu에서 보낸 값을 받아온다.
        String selected_menu = intent.getExtras().getString("select_menu");
        String student_list = intent.getExtras().getString("sub_student");
        selected_m = selected_menu;
        sub_student_info = student_list; // 선택된 과목의 학생 목록
        //====================================13==13==13==13==13==13===13====================

        Button btnReturn = (Button) findViewById(R.id.back1);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //==============================7=7=7=7=7=7=7===========================================

        linearLayout = findViewById(R.id.can_see_student);
        //Student(sub_student_info); 학생들을 가지고와 자르기 위해서.
        //================================7=7=7=7=7=77==========================================


        //=========================3=3=3=3=3=3=변경후========================================


        // 스피너 사용 부분 ====================================4=4=4=4=4=44=========================

        final String[] Class = {"과목1", "과목2", "과목3", "과목4", "과목5"}; // 이 부분을 수정하면됨. 수정 할것은 testHere에 보관함. split를 사용하면 됨. // 다음 받아온 s를 이용하여 변경해야함.
        final Spinner spinner = (Spinner) findViewById(R.id.class_choose);

        String sub1 = "sub1";
        String sub2 = "sub2";
        String sub3 = "sub3";
        String sub4 = "sub4";
        String sub5 = "sub5";

        //============================스피너 선택창======================================================


        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Class);
        spinner.setAdapter(adapter);
        // spinner.setSelection(4);
        while (true) {
            if (sub1.equals(selected_menu)) {
                Toast.makeText(this, "과목1이 선택되었습니다.", Toast.LENGTH_LONG).show();
                spinner.setSelection(0); // 스피너는 0부터 초기값이 0부터 시작됨.
                break;
            } else if (sub2.equals(selected_menu)) {
                Toast.makeText(this, "과목2가 선택되었습니다.", Toast.LENGTH_LONG).show();
                spinner.setSelection(1);
                break;
            } else if (sub3.equals(selected_menu)) {
                Toast.makeText(this, "과목3이 선택되었습니다.", Toast.LENGTH_LONG).show();
                spinner.setSelection(2);
                break;
            } else if (sub4.equals(selected_menu)) {
                Toast.makeText(this, "과목4가 선택되었습니다.", Toast.LENGTH_LONG).show();
                spinner.setSelection(3);
                break;
            } else if (sub5.equals(selected_menu)) {
                Toast.makeText(this, "과목5가 선택되었습니다.", Toast.LENGTH_LONG).show();
                spinner.setSelection(4);
                break;
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
// i번째에 해당되는것을 선택할 수 있다.

                editText1.setText("다음 과목에 대한 인증을 실행합니다:" + adapterView.getItemAtPosition(i));
                //다음 스트링을 보내기 위해서.


                //Intent intent = new Intent(getApplicationContext(),sendEmailPasswordF.class);
                String sClass  = spinner.getSelectedItem().toString(); // 다음 스피너에 선택된 변수를 가지고 온다. sClass에.
                //intent.putExtra("sClass",sClass); // 위의 sClass를 가져와 다음 액티비티로 넘기기 위해 가져온다.
                choose_Subject = sClass; //전역변수 Subject에 다음 스피너에서 고른 클래스가 들어간다.

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //아무것도 선택이 안됬을때 설정을 넣는 곳.
            }
        });


        //=============================4=4=4=4=4=4=4================================================

        //===============================15==15==15==15===15========================================
        //날짜 선택 창
        selected_date = findViewById(R.id.selected_date);
        calendarView = findViewById(R.id.select_date);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                selected_date.setText("선택된 날짜:"+ year+"/" +(month+1)+ "/" +dayOfMonth);
                String date = ("선택된 날짜:"+year+"/"+(month+1)+"/"+dayOfMonth);
                choose_date = date;
            }
        });

        //===============================15==15==15==15===15========================================
        //===============다음 버튼===================================================================

        next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), take_photoF.class); // 다음 값을 액티비티에서 액티비티로 넘겨줌
                intent.putExtra("select_menu",selected_m); // 다음 다시 선택된 수업을 파악한다.
                intent.putExtra("sub_student",sub_student_info); // 수업 1을 듣는 학생들을 다음과 같이 보낸다. 전체 스트링으로
                intent.putExtra("choose_date",choose_date);
                startActivity(intent);
            }
        });

        //===============다음 버튼===================================================================


    }
    public void Student(String info){ // 다음은 학생을 가지고와서 스크롤 뷰에 띄우기 위해.

        String originString = info;
        final String[] Class = originString.split("\\s+");


        TextView view1 = new TextView(this);
        view1.setText(info);
        view1.setTextSize(FONT_SIZE);
        view1.setTextColor(Color.BLACK);

        //layout_width, layout_height, gravity 설정
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        view1.setLayoutParams(lp);

        //부모 뷰에 추가
        linearLayout.addView(view1);

    }
}
