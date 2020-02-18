package com.example.send;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class request extends AppCompatActivity {

    Fragment fragment;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request);

        Intent intent = getIntent();
        String texts = intent.getStringExtra("texts"); //fragment 생성
        fragment = new fragment(); //requestActivity에 fragment1을 띄워줌
        getSupportFragmentManager().beginTransaction().replace(R.id.request,fragment).commit(); //번들객체 생성, text값 저장
        Bundle bundle = new Bundle();
        bundle.putString("texts",texts); //fragment1로 번들 전달
        fragment.setArguments(bundle);

    }
}
