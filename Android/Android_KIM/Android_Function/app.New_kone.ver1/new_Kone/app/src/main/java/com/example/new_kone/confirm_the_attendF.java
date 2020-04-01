package com.example.new_kone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class confirm_the_attendF extends Activity {

    String Subject; // 현재 출석체크 확인 창에서 선택된 과목을 가지고오려고 만든 전역 변수
    String Date; // 현재 출석체크 확인을 위해 창에서 선택된 날짜를 저장하기위한 변수
    EditText selected_class,selected_date;
    CalendarView calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_the_attend);

        selected_class = findViewById(R.id.selected_class_confirm); // **오류 발생 했음: 여기서 Unexpected Implicit~ 오류가 난다면 변수를 선언할때 Edittext를 가지고 온건지 Textview를 가지고온건지 확인하기.!
        selected_date = findViewById(R.id.selected_date_confirm);
        calendar = findViewById(R.id.choose_date);

        final String[] Class = {"과목1","과목2","과목3","과목4","과목5"}; // 이 부분을 수정하면됨. 수정 할것은 testHere에 보관함. split를 사용하면 됨. // 다음 받아온 s를 이용하여 변경해야함.
        final Spinner spinner = (Spinner)findViewById(R.id.class_choose_in_confirm);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,Class);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selected_class.setText("선택된 과목:" + adapterView.getItemAtPosition(i));
                //다음 스트링을 보내기 위해서.

                //Intent intent = new Intent(getApplicationContext(),sendEmailPasswordF.class);
                String sClass  = spinner.getSelectedItem().toString(); // 다음 스피너에 선택된 변수를 가지고 온다. sClass에.
                //intent.putExtra("sClass",sClass); // 위의 sClass를 가져와 다음 액티비티로 넘기기 위해 가져온다.
                Subject = sClass; //전역변수 Subject에 다음 스피너에서 고른 클래스가 들어간다.

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //아무것도 선택이 안됬을때 설정을 넣는 곳.
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                selected_date.setText("선택된 날짜:"+ year+"/" +(month+1)+ "/" +dayOfMonth);

            }
        });

    }
}
