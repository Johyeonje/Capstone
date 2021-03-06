package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
public class MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_SELECT_IMAGE = 100;
    private static final int REQ_CODE_UPDATE_IMAGE = 200;
    public String cookie=null, SUB_ID=null, SUB, Students;;
    EditText IDbox;
    EditText PWbox;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSelfPermission();
        final Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);
        Button btn3 = (Button) findViewById(R.id.btn3);
        Button btn4 = (Button) findViewById(R.id.btn4);
        Button btn5 = (Button) findViewById(R.id.btn5);
        IDbox = findViewById(R.id.IDbox);
        PWbox = findViewById(R.id.PWbox);
        IDbox.setText("10001");
        PWbox.setText("10001");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  DoADS("http://rbghoneroom402.iptime.org:48526/ADS/Ads.jsp", IDbox.getText().toString());
//                DoTextUpload("http://rbghoneroom402.iptime.org:48526/JSP/Text.jsp", Person);
//                if (cookie == null) {
//                    DoLogin("http://rbghoneroom402.iptime.org:48526/JSP/Login.jsp", IDbox.getText().toString(), PWbox.getText().toString());
//                    btn1.setText("LOGOUT");
//                } else {
//                    DoLogout("http://rbghoneroom402.iptime.org:48526/JSP/Logout.jsp");
//                    btn1.setText("LOGIN");
//                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoDBRead("http://rbghoneroom402.iptime.org:48526/JSP/Subject.jsp");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoGetStudents("http://rbghoneroom402.iptime.org:48526/JSP/Student.jsp", SUB_ID);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQ_CODE_UPDATE_IMAGE);
            }
        });
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
    }

    private void checkSelfPermission() {
        String temp = "";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        { temp += Manifest.permission.READ_EXTERNAL_STORAGE + " "; } //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        { temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " "; }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        { temp += Manifest.permission.INTERNET + " "; }
        if (TextUtils.isEmpty(temp) == false)
        { // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1); }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) try {
                String img_path = null;

                img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                DoFileUpload("http://rbghoneroom402.iptime.org:48526/JSP/Test.jsp", img_path);  //해당 함수를 통해 이미지 전송.
                //DoFileUpload("http://rbghoneroom402.iptime.org:48526/ADS/Ads.jsp", img_path);

                //이미지를 비트맵형식으로 반환
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                int reWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
                int reHeight = (int) (getWindowManager().getDefaultDisplay().getHeight());

                //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                Bitmap image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, reWidth, reHeight, true);
                ImageView image = (ImageView) findViewById(R.id.imageView);  //이미지를 띄울 위젯 ID값
                image.setImageBitmap(image_bitmap_copy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == REQ_CODE_UPDATE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) try {
                String img_path = null;

                img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                DoFaceUpdate("http://rbghoneroom402.iptime.org:48526/JSP/Update_Face.jsp", img_path, IDbox.getText().toString());  //해당 함수를 통해 이미지 전송.

                //이미지를 비트맵형식으로 반환
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                int reWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
                int reHeight = (int) (getWindowManager().getDefaultDisplay().getHeight());

                //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                Bitmap image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, reWidth, reHeight, true);
                ImageView image = (ImageView) findViewById(R.id.imageView);  //이미지를 띄울 위젯 ID값
                image.setImageBitmap(image_bitmap_copy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        return imgPath;
    }

    public void DoFileUpload(String apiUrl, String absolutePath) {
        FileUpload.NetworkTask networkTask = new FileUpload.NetworkTask(apiUrl, absolutePath, cookie, SUB_ID, getBaseContext());
        networkTask.execute();
    }

    public void DoFaceUpdate(String apiUrl, String absolutePath, String STU_ID) {
        FaceUpdate.NetworkTask networkTask = new FaceUpdate.NetworkTask(apiUrl, absolutePath, cookie, IDbox.getText().toString(), getBaseContext());
        networkTask.execute();
    }

    public void DoDBRead(String apiUrl) {
        DBRead.NetworkTask networkTask = new DBRead.NetworkTask(apiUrl, cookie, getBaseContext());
        try {
            SUB = networkTask.execute().get();
            SUB_ID = SUB.substring(0,5);
            Toast.makeText(getBaseContext(), SUB, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void DoLogin(String apiUrl,String ID, String PWD) {
        Login.NetworkTask networkTask = new Login.NetworkTask(apiUrl, ID, PWD, getBaseContext());
        try {
            cookie = networkTask.execute().get();
            Toast.makeText(getBaseContext(), cookie.substring(11,43), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void DoLogout(String apiUrl) {
        Login.NetworkTask networkTask = new Login.NetworkTask(apiUrl, cookie, getBaseContext());
        networkTask.execute();
        Toast.makeText(getBaseContext(), "LogOut", Toast.LENGTH_SHORT).show();
        cookie=null;
    }

    public void DoADS(String apiUrl, String num) {
        ADS.NetworkTask networkTask = new ADS.NetworkTask(apiUrl, num, getBaseContext());
        try {
            String received = networkTask.execute().get();
        } catch (Exception e) {
        }
    }

    public void DoGetStudents(String apiUrl, String SUB_ID) {
        Student.NetworkTask networkTask = new Student.NetworkTask(apiUrl, cookie, SUB_ID,  getBaseContext());
        try {
            Students = networkTask.execute().get();
            Toast.makeText(getBaseContext(), Students, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}