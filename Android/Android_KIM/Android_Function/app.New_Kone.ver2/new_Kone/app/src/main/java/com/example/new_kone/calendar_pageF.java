package com.example.new_kone;

//import android.app.Fragment;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.Calendar;
import java.util.zip.Inflater;


public class calendar_pageF extends Fragment {

    EditText edit1; // 메모내용 저장 변수
    CalendarView calendarview1; // 캘린더의 날짜 내용을 저장.
    public static androidx.fragment.app.Fragment newInstance() {
        calendar_pageF fragment = new calendar_pageF();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_page,container,false);

        edit1 = (EditText)view.findViewById(R.id.memo1);
        calendarview1 = (CalendarView)view.findViewById(R.id.calendarView1);
        //현재 캘린더만 보이는 상태이고 기능은 없다.

        return view;
    }
}
