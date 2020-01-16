package com.example.howtousefragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class frag_a extends Fragment { //현재는 프래그먼트를 만들때 Fragment.app을 사용 x 사용시 오류 발생 원인은 잘 모르겠음. 탭 호스트 방식으로 구현.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_a, container, false); //프래그먼트 a가 바로 뜰 수 있도록 설정
    }
}
