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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class attendActivity extends AppCompatActivity {

    Button captureButton;
    Button processButton;
    private static final int CAMERA_CAPTURE=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final String[] course = {"과목1", "과목2", "과목3", "과목4"};

        Spinner spinner = findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter1;
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, course);
        spinner.setAdapter(adapter1);


        captureButton = findViewById(R.id.captureButton);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/image.jpg")));
                startActivityForResult(i, CAMERA_CAPTURE);
            }
        });

        processButton = findViewById(R.id.processButton);

        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap captureBmp = null;
                File file = new File("/sdcard/image.jpg");
                try {
                    captureBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                } catch(IOException e) {
                    e.printStackTrace();
                }

                int width = captureBmp.getWidth();
                int height = captureBmp.getHeight();
                Bitmap tmpBmp = captureBmp.copy(Bitmap.Config.ARGB_8888,true);

                for(int y = 0; y < height; y++) {
                    for(int x = 0; x<width; x++) {
                        int value = captureBmp.getPixel(x, y);
                        if(value<0xff808080)
                            tmpBmp.setPixel(x, y, 0xff000000);
                        else
                            tmpBmp.setPixel(x, y, 0xffffffff);
                    }
                }
                ImageView image = findViewById(R.id.image);
                image.setImageBitmap(tmpBmp);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap captureBmp = null;

        if(resultCode == RESULT_OK && requestCode == CAMERA_CAPTURE) {
            File file = new File("/sdcard/image.jpg");
            try {
                captureBmp = MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.fromFile(file));
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }
            ImageView image = findViewById(R.id.image);
            image.setImageBitmap(captureBmp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}


