package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class button1FUNC extends Activity {

    ImageView imageView1;
    //==========================3=3=3=3=3======변경후 필요 변수=========================
    Button btn_save_photo;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_uri;
    //==============================3=3=3=3=3=3= 변경후 필요변수========================
    //==============================4=4=4=4=4=4=4====================================
    Button send_to_jsp;
    private String lineEnd;
    private Object boundary;

    //==============================4=4=4=4=4=4=4=======================================
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button1);

        imageView1 = findViewById(R.id.imageView1);
        //========================3=3=3=3=3=3=========변경후 =================================
        btn_save_photo = findViewById(R.id.CONFIRM);
        //========================3=3=3=3=3=3=========변경후 =================================
        //========================4=4=4=4=4=4================jsp로 보내기=====================
        send_to_jsp =findViewById(R.id.Send_To_JSP);
        //=========================4=4=4=4=4=4=4==============================================
        //===========================4=4=4=4=4=4=4=4==========================서버로 보내기=====
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
        //==============================4=4=4=4=4=4========================================



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
        //=====================3=3=3=3=3=3=3= 변경후 =============================================

    }

    //======================================================333=3=3=3=3=교체전======================
    /*
//=====================================3=3=3=3=3=3===============사진찍기===========================
    public void showCameraBtn(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,1);
    }
//=======================================3=3=3=3=3=3=3========사진 찍은거 반환해오기=================
    @Override
    protected void onActivityResut(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) // 카메라에 기본 탑재가 되어있다. 사진을 찍고 그 사진을 사용하기를 원하면 Result_ok를 가져온다.
        {
            Bitmap bitmap = data.getParcelableExtra("data"); // data라는 이름으로 사진 찍은걸 가져오겠다.
            imageView1.setImageBitmap(bitmap);
        }
    }
    //사진을 찍으면 사진은 data라는 이름으로 imageView1에 저장된다.
//==========================================3=3=3=3=3=3=3=3========================================
*/
    //===============================3=3=3=3=3=3=3== 교체전========================================


    //========================================3=3=3=3=3=3=3===교체후 ===============================
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

    //========================================3=3=3=3=3=3=3===교체후 ===============================

}
