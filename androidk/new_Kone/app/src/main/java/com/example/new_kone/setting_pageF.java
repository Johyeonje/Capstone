package com.example.new_kone;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class setting_pageF extends AppCompatActivity {

    Button version_btn;
    Button info_btn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        version_btn = findViewById(R.id.version_btn);
        info_btn = findViewById(R.id.info_btn);

        version_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),version_pageF.class);
                startActivity(intent);
            }
        });

        info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),info_pageF.class);
                startActivity(intent);
            }
        });
    }
}
