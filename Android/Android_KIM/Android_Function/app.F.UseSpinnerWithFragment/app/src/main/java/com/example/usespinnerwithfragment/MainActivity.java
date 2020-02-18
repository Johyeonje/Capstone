package com.example.usespinnerwithfragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {


    Spinner spinner;
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner1);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        // 스피너 사용 구간.

        ArrayAdapter<String > adapter = new ArrayAdapter<>(MainActivity.this,R.layout.custom_spinner,getResources().getStringArray(R.array.fragments));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position)
                {
                    case 0:
                        setFragment(fragment1);
                        break;
                    case 1:
                        setFragment(fragment2);
                        break;
                    case 2:
                        setFragment(fragment3);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
// ================아무것도 선택이 안됬을때 =======================================
            }
        });
    }
 // =====================================================프래그먼트를 넣는 함수 =======================
    public void setFragment(Fragment fragment){ // spinner는 TapHost와 다른 Fragment를 사용한다 androidx 를 사용한다. 그래야 fragmentTransaction.replace에서  fragment 창을 설정 할 수 있다.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.FramLayout1, fragment);
        fragmentTransaction.commit();
    }
    //===========================================================================================
}
