package com.example.myapplication;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainmenuActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //==================================5=5=5=5=55=5====================프래그먼트 추가=======
        Button menu= (Button)findViewById(R.id.menu);
        Button home= (Button)findViewById(R.id.home);
        Button option= (Button)findViewById(R.id.option);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame1,new menu()).commit();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame1,new home()).commit();

            }
        });

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame1,new option()).commit();

            }
        });

        //================================main.xml안의 버튼들 기능 부여=============================
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),button1FUNC.class);
                startActivity(intent);
            }
        });
        //=====================다른 액티비티로 넘어갈수 있다.========================================

    }

    public void refresh(){

        //viewPage
    }
}
