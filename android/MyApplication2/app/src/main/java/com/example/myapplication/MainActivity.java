package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    //====================================2=2=2=2=2=================아직 확실x====================
    private final int MY_PERMISSIONS_REQUEST_CAMERA=1001;
    //====================================2=2=2=2=2==============================================

    TextView signin,email_text,password_text;// xml에서 변수들을 불러올 때 처음 선언해 주는 것
    LinearLayout LinearLayout_log_in;
 // 위에 것들은 아직 사용안함.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 현재 자바에서 사용할 xml에서 변수를 끌어오려면 등록해야함 위에.===========================

        //==============================1=1=1=1=1========================카메라 권한 요청허용=========
        //카메라가 요청이 허용이 되었는지 안되었는지 체크
        int permssionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        //메니페스트에 카메라가 권한이 요청이 되어있는지 안되어있는지 확인을 한다.
        //그 다음 아래로 넘어간다. 카메라 사용이 허용됬다면 PERMISSION_GRANTED 상태이다.
        //권한 요청
        if (permssionCheck!= PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"권한 승인이 필요합니다",Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(this,"얼굴인식 출석을 위해 카메라 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } //사용자가 거부를 하면 true를 반환한다.
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
                Toast.makeText(this,"얼굴인식 출석을 위해 카메라 권한이 필요합니다.", Toast.LENGTH_LONG).show();

            }
        }
        //==============================1=1=1=1=1==============================================

        // ===============================2=2=2=2=2========================페이지 넘기기위한 ===
        //새로운 유저 가입 화면을 보여주기 위해서
        TextView signin  = findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),newUser.class);
                startActivity(intent);
            }
        });

        // 아이디 찾는 화면을 보여주기위해서
        TextView findmyid = findViewById(R.id.findmyid);
        findmyid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),findmyid.class);
                startActivity(intent);
            }
        });

        // 로그인화면으로 가기위해서
        TextView login1 =(TextView)findViewById(R.id.login1);
        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainmenuActivity.class);
                startActivity(intent);
            }
        });

    }
// 1. 값을 가져온다.
// 2. 클릭을 확인한다.
// 3.  1번의 값을 다음 액티비티로 넘긴다.
//=======================================2=2=2=2=2===============================================

    //===================================2=2=2=2=2============================권한요청이 됬는지 확인==
    @Override // 권한요청에 대한 사용자에 대답에 다음과 같이 응답.
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this,"아직 승인받지 않았습니다.",Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
    //==================================2=2=2=2=2================================================
}
