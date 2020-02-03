package com.cookandroid.attendandroidapp;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class computercourseActivity extends AppCompatActivity {

    private computeradapterActivity adapter;
    private ListView computer_list;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.computer_course);



            adapter = new computeradapterActivity();
            computer_list = findViewById(R.id.computer_list);

            setData();

            computer_list.setAdapter(adapter);
        }

        // 보통 ListView는 통신을 통해 가져온 데이터를 보여줍니다.
        // arrResId, titles, contents를 서버에서 가져온 데이터라고 생각하시면 됩니다.
        private void setData () {
            TypedArray arrResId = getResources().obtainTypedArray(R.array.resId);
            String[] titles = getResources().getStringArray(R.array.title);
            String[] contents = getResources().getStringArray(R.array.content);

            for (int i = 0; i < arrResId.length(); i++) {
                computerActivity dto = new computerActivity();
                dto.setResId(arrResId.getResourceId(i, 0));
                dto.setTitle(titles[i]);
                dto.setContent(contents[i]);

                adapter.addItem(dto);
            }
        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.login_btn:
                Intent intent1 = new Intent(getApplicationContext(), loginActivity.class);

                startActivity(intent1);
                break;

            case R.id.home_btn:
                Intent intent2 = new Intent(getApplicationContext(), com.cookandroid.attendandroidapp.MainActivity.class);
                startActivity(intent2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    }
