package com.example.new_kone;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class take_photoF extends Activity {



    ImageView imageView1;
    //==========================3=3=3=3=3======변경후 필요 변수=========================
    Button btn_save_photo;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_uri;
    //==============================3=3=3=3=3=3= 변경후 필요변수========================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo);

        imageView1 = findViewById(R.id.imageView1);
        //========================3=3=3=3=3=3=========변경후 =================================
        btn_save_photo = findViewById(R.id.CONFIRM);
        //========================3=3=3=3=3=3=========변경후 =================================

        Button btnReturn = (Button)findViewById(R.id.back1);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //=========================3=3=3=3=3=3=변경후========================================
        // 버튼을 눌렀을때
        btn_save_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                        String [] permission ={Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        open_camera();
                    }
                }
                else{
                    open_camera();
                }
            }
        });

        // 스피너 사용 부분 ====================================4=4=4=4=4=44=========================

        final String[] Class = {"과목1","과목2","과목3","과목4","과목5"};

        Spinner spinner = (Spinner)findViewById(R.id.class_choose);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,Class);
        spinner.setAdapter(adapter);


        //=============================4=4=4=4=4=4=4================================================

    }

    private void open_camera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NEW PICTURE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    open_camera();
                }
                else{
                    Toast.makeText(this,"저장소와 카메라를 허용승인이 필요합니다.",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) // 카메라에 기본 탑재가 되어있다. 사진을 찍고 그 사진을 사용하기를 원하면 Result_ok를 가져온다.
        {
            imageView1.setImageURI(image_uri); // 비트맵 객체를 이용해서 보여줌.
            Toast.makeText(this,"출석체크 사진을 저장하였습니다.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"출석체크 사진이 찍히지 않았습니다.",Toast.LENGTH_SHORT).show();
        }
    }

}
