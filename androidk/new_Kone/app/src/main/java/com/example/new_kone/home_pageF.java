package com.example.new_kone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class home_pageF extends Fragment {

    public static android.support.v4.app.Fragment newInstance() {
        home_pageF fragment = new home_pageF();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { // 다음 함수가 생성될때 실행하는 파트
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { // 다음 화면이 띄워지고 실행하는 부분 Activity에서 oncreate부분에 사용하는 것들을 이 곳에 넣으면 된다.

        View view = inflater.inflate(R.layout.home_page,container,false);

      Button btn1 = (Button)view.findViewById(R.id.take_photo);

      btn1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getActivity(), take_photoF.class);
              startActivity(intent);
          }
      });

    return view;
    }
}
