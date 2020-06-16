package com.example.new_kone;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class student_uploadF extends Activity {

    //================15==15==15==15==15===15==========================================
    EditText selected_date;
    CalendarView calendarView;
    //================15==15==15==15==15===15==========================================
    //==========================3=3=3=3=3======변경후 필요 변수=========================
    Button btn_save_photo;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int REQ_CODE_SELECT_IMAGE = 100;
    private static final int REQ_CODE_UPDATE_IMAGE = 200;

    Uri image_uri;
    String url = "http://rbghoneroom402.iptime.org:48526/JSP/Update_Face.jsp";
    String Session_key;
    String UserID;
    ImageView imageView1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.student_upload);

        imageView1 = findViewById(R.id.imageView1);
        Button take_photo = findViewById(R.id.take_photo);
        Button Upload = findViewById(R.id.Upload);
        Button Return = findViewById(R.id.back);

        Intent intent = getIntent(); // select_menu에서 보낸 값을 받아온다.
        Session_key = intent.getExtras().getString("Session_key");
        UserID = intent.getExtras().getString("UserID");

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//다음은 카메라.저장소.인터넷 사용이 허가가 안되있을때 오류가 날 수 있음으로 허가를 받고 사용하기 위해서 행하는 것.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 빌드 버젼을 체크하고
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  // 다음에 인터넷에 사용 허가를 받는것을 추가 하였다.~~~~~~~~ 변경 된거....
                                    PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        open_camera();
                    }
                } else {
                    open_camera();
                }
            }
        });

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

            }
        });

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void open_camera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NEW PICTURE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, REQ_CODE_SELECT_IMAGE); // 다음 결과 값을 받고 새로운 액티비티를 생성하기 위해 다음과 같은걸 쓴다// .
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    open_camera();
                } else {
                    Toast.makeText(this, "저장소와 카메라를 허용승인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK)
        {
            imageView1.setImageURI(image_uri); // 비트맵 객체를 이용해서 보여줌.
            Toast.makeText(this, "출석체크 사진을 저장하였습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "출석체크 사진이 찍히지 않았습니다.", Toast.LENGTH_SHORT).show();
        }

        if(requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String img_path = null;
                    img_path = getImagePathToUri(data.getData());

                    DoFaceUpdate(url, img_path,UserID);
                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show();

                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    int reWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
                    int reHeight = (int) (getWindowManager().getDefaultDisplay().getHeight());

                    Bitmap image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true); // 사이즈를 조절
                    ImageView image = (ImageView) findViewById(R.id.imageView1);  //이미지를 띄울 위젯 ID값, 조절된 이미지가 띄워짐.
                    image.setImageBitmap(image_bitmap_copy); // image에 다음 비트맵의 이미지가 씌워짐.

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public String getImagePathToUri(Uri data) {  // 절대경로 설정 하는 곳
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        String imgPath = cursor.getString(column_index);
        return imgPath;
    }

    public void DoFaceUpdate(String apiUrl, String absolutePath, String STU_ID) {
        Upload_student_network.NetworkTask networkTask = new Upload_student_network.NetworkTask(apiUrl, absolutePath, Session_key, STU_ID, getBaseContext());
        networkTask.execute();
    }

}
