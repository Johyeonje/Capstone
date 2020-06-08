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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

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
        String subject_code; // 과목코드 저장.
        String code_and_studentcode_and_checkinfo = "";
        String choose_Subject; // 다음 과목을 넣기 위해, 다음 사진 찍는 곳으로 넘겨준다.
        String sub_student_info,choose_date,choose_class_code,session_key; //
        String Result; // take_photo 부분에서 학생의 데이터를 받아온다.
        String checked_student_list;
        String Progress,Progress1; // 정주행인지 역주행인지 파악.
    // sub_student_info : 가져온 학생들의 값.
    // choose_date
    // choose_class_code : 결정 과목 코드
    // session_key : 세션키저장

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
        final String url = "http://rbghoneroom402.iptime.org:48526/JSP/Student.jsp"; // 과목 정보를 받아올 JSP 정보를 입력한다.
        Intent intent = getIntent(); // select_menu에서 보낸 값을 받아온다. // take_photo에서의 값을 가져온다.
        String class_number =intent.getExtras().getString("class_number");
        int convert_class_nuber_to_int = Integer.parseInt(class_number);
        String selected_menu = intent.getExtras().getString("Select_menu"); // 정상적인 값 확인 O 받은 값 = 코드랑 과목명이 들어옴.
        final String Session_key = intent.getExtras().getString("Session_key"); // 정상적인 값 확인 O
        Progress = intent.getExtras().getString("Progress");

        Intent intent1 = getIntent();
        checked_student_list = intent1.getExtras().getString("student_list"); // take_photo에서 받은 값.
        Progress1 = intent1.getExtras().getString("Progress");

        session_key = Session_key;
        String User_subject = intent.getExtras().getString("All_subject"); // 정상적인 값 확인 O


        // 학생 check정보를 수정하기위해 포함.
        String result = intent.getExtras().getString("student_list");
        Result = result;// take_photo에서 받은 값을 저장한다.


        String code_and_subject[] = selected_menu.split("\t"); // 교과목코드,과목이름.
        String code = code_and_subject[0];
        subject_code = code;

        Student_info(url,Session_key,code);
        //selected_m = selected_menu;
        //sub_student_info = student_list; // 선택된 과목의 학생 목록

        Button btnReturn = (Button) findViewById(R.id.back1);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //=========================3=3=3=3=3=3=변경후========================================


        // 스피너 사용 부분 ====================================4=4=4=4=4=44=========================

        final String[] Class = User_subject.split("\\n+");
        final Spinner spinner = (Spinner) findViewById(R.id.class_choose);

        //============================스피너 선택창======================================================

        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Class);
        spinner.setAdapter(adapter);
        spinner.setSelection(convert_class_nuber_to_int);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // i번째에 해당되는것을 선택할 수 있다.
                String Divide_Code_Subject_name[];

                //Toast.makeText(getBaseContext(),"다음과목을 인증합니다."+adapterView.getItemAtPosition(i),Toast.LENGTH_LONG).show();
                editText1.setText("변경할 과목은" + adapterView.getItemAtPosition(i)+ "입니다.");
                //다음 스트링을 보내기 위해서.

                String Last_choose  = spinner.getSelectedItem().toString(); // 다음 스피너에 선택된 변수를 가지고 온다. sClass에.
                choose_Subject = Last_choose; //전역변수 Subject에 다음 스피너에서 고른 클래스가 들어간다.

                Divide_Code_Subject_name = choose_Subject.split("\t");
                String code = Divide_Code_Subject_name[0];

                choose_class_code = code;
                String subject = Divide_Code_Subject_name[1];

                Toast.makeText(getBaseContext(),"다음과목을 인증합니다 : "+subject,Toast.LENGTH_LONG).show();

                Student_info(url,Session_key,code);


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
                intent.putExtra("select_menu_code",choose_class_code); // 수업의 코드
                intent.putExtra("select_menu",choose_Subject); // 수업 명
                intent.putExtra("session_key",session_key);//세션키
                //intent.putExtra("student_list_info",sub_student_info);
                intent.putExtra("student_list_info",code_and_studentcode_and_checkinfo);
                //intent.putExtra("choose_date",choose_date);
                startActivity(intent);
            }
        });

        //===============다음 버튼===================================================================

        //+=====================================학생 정보 보이는 LinearLayout=====================

        final LinearLayout student_scroll = (LinearLayout) findViewById(R.id.can_see_student);
        //student_scroll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT); // 만든 LinearLayout의 가로 길이와 세로 길이를 정한다. 설정을 이렇게 한다는 뜻이다.




        // 순서=====================================================================================

        if(Progress.equals("0")&&Progress1.equals("0")) //  a x, b ,c x
        {

        }
        else if(Progress.equals("0")&&Progress1.equals("1")) // a x,b ← c
        {

        }
        else if(Progress.equals("1")&&Progress1.equals("0")) // a → b ,c x
        {

        }
        else if(Progress.equals("1")&&Progress1.equals("1"))// a and b ← c
        {

        }
        //==========================================================================================

        String[] student_list = sub_student_info.split("\\n+");// "\\n+"는 줄바꿈을 확인하기위한 문자.

        for(int i=0;i<student_list.length;i++) { // Class의 길이 만큼 읽는다. 과목 만큼 버튼이 생성된다.
            //final Button btn = new Button(this); // 버튼을 새로 생성한다.

            LinearLayout student_list_inner = new LinearLayout(this);
            student_list_inner.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout state = new LinearLayout(this);
            state.setOrientation(LinearLayout.HORIZONTAL);
            String check_number = null;


            String[] student_list_and_check = student_list[i].split(" "); // 학번,이름 그리고 출석여부를 나누기위한 함수. student_list_and_check는 계속 초기화됨.
            check_number = student_list_and_check[1];
            final TextView textview = new TextView(this); // 새로운 텍스트 뷰 생성.
            //{
                textview.setText(student_list_and_check[0]);
                textview.setTextSize(20);
                textview.setLayoutParams(params);
            //}
            String[] devide_student_code = student_list_and_check[0].split("\t"); // i 번째 학생의 학번과 이름을 나누기. 0 번째 학번 1번째 이름 들어감.
            code_and_studentcode_and_checkinfo = code_and_studentcode_and_checkinfo.concat(subject_code);
            code_and_studentcode_and_checkinfo = code_and_studentcode_and_checkinfo.concat("\t");
            code_and_studentcode_and_checkinfo = code_and_studentcode_and_checkinfo.concat(devide_student_code[0]);// i번째 학생의 코드 첨가.
            code_and_studentcode_and_checkinfo = code_and_studentcode_and_checkinfo.concat("\t");
            code_and_studentcode_and_checkinfo = code_and_studentcode_and_checkinfo.concat(check_number);
            code_and_studentcode_and_checkinfo = code_and_studentcode_and_checkinfo.concat("\r\n");

            //{
            final String set_menu = student_list[i]; // 몇번째 학생인지.
            student_list_inner.addView(textview);
            //}
            //{
            final RadioButton radioButton = new RadioButton(this);
            final RadioButton radioButton1 = new RadioButton(this);
            final RadioButton radioButton2 = new RadioButton(this);

            radioButton.setText("출석");
            radioButton.setLayoutParams(params);
            //radioButton.setChecked(false);
            radioButton.setClickable(false);
            student_list_inner.addView(radioButton);

            radioButton1.setText("지각");
            radioButton1.setLayoutParams(params);
            //radioButton1.setChecked(false);
            radioButton1.setClickable(false);
            student_list_inner.addView(radioButton1);

            radioButton2.setText("결석");
            radioButton2.setLayoutParams(params);
            //radioButton2.setChecked(true);
            radioButton2.setClickable(false);
            student_list_inner.addView(radioButton2);

            // 마지막 학생은 출결여부뒤에 \r이 안붙기때문에 조건 추가.
            if(check_number.equals("0\r")){
                radioButton.setChecked(true);
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
            }
            else if (check_number.equals("1\r")){
                radioButton.setChecked(false);
                radioButton1.setChecked(true);
                radioButton2.setChecked(false);
            }
            else if(check_number.equals("2\r")){
                radioButton.setChecked(false);
                radioButton1.setChecked(false);
                radioButton2.setChecked(true);
            }
            else if(check_number.equals("0")){
                radioButton.setChecked(true);
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
            }
            else if (check_number.equals("1")){
                radioButton.setChecked(false);
                radioButton1.setChecked(true);
                radioButton2.setChecked(false);
            }
            else if(check_number.equals("2")){
                radioButton.setChecked(false);
                radioButton1.setChecked(false);
                radioButton2.setChecked(true);
            }


            //}

            student_scroll.addView(student_list_inner);
            student_scroll.addView(state);



            /*
            checkbox.setText("출석");
            checkbox.setTextSize(20);
            checkbox.setLayoutParams(params);
            student_list_inner.addView(checkbox);
            student_scroll.addView(student_list_inner);

            checkbox.setOnClickListener(new View.OnClickListener() { // 다음 check 박스가 선택이 되는 방법.
                @Override
                public void onClick(View v) {

                }
            });

             */
        }

        //======================================학생 정보 보이는 ==================================

    }


public void Student_info(String url,String session_key,String code){ // 다음은 학생을 가지고와서 스크롤 뷰에 띄우기 위해.

        String Student_list;

        Request_class_student_network.NetworkTask networkTask = new Request_class_student_network.NetworkTask(url,session_key,getBaseContext(),code);
        try {
            Student_list = networkTask.execute().get(); // 학생 리스트 값 받아옴.
            //Toast.makeText(this, Student_list,Toast.LENGTH_LONG).show();
            sub_student_info = Student_list; // 학번,학생이름,출석여부

        } catch (Exception e) {
            Toast.makeText(this, "데이터를 읽어 올 수 없습니다.",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}
