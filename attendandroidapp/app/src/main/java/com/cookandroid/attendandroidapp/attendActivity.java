package com.cookandroid.attendandroidapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class attendActivity extends AppCompatActivity {

    Button captureButton;
    Button processButton;
    ImageView image;
    Button record_btn;
    private static final int CAMERA_CAPTURE=1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend);
        setTitle("E-Attend");

        final String[] course = {"선택하세요","과목1", "과목2", "과목3", "과목4"};

        Spinner spinner = findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter1;
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, course);
        spinner.setAdapter(adapter1);


        record_btn = findViewById(R.id.record_btn);

        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),recordActivity.class);
                startActivity(intent);
            }
        });

        image = findViewById(R.id.image);
        captureButton = findViewById(R.id.captureButton);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(i, CAMERA_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            Bitmap bitmap = data.getParcelableExtra("data");
            image.setImageBitmap(bitmap);
        }
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}

