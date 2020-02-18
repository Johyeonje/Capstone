package com.cookandroid.attendandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class loginActivity extends AppCompatActivity {
    EditText id, passward;
    String ID, PASSWARD,idcheck,pacheck;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        id = findViewById(R.id.id);
        passward = findViewById(R.id.passward);
        Button login = findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ID = id.getText().toString();
                PASSWARD = passward.getText().toString();

                if(passward.equals(pacheck) && id.equals(idcheck)) {

                    Toast.makeText(getApplicationContext(),"로그인이 정상적으로 처리되었습니다.",Toast.LENGTH_SHORT);
                }
                else {

                    Toast.makeText(getApplicationContext(),"비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT);
                }
            }
        });

    }

        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item){

            switch (item.getItemId()) {
                case R.id.login_btn:
                    Intent intent1 = new Intent(getApplicationContext(), loginActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.home_btn:
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent2);
                    break;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }


}

