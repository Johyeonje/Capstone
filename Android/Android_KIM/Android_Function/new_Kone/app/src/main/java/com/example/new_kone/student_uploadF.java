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

    ImageView imageView1;
    //================15==15==15==15==15===15==========================================
    EditText selected_date;
    CalendarView calendarView;
    //================15==15==15==15==15===15==========================================
    //==========================3=3=3=3=3======변경후 필요 변수=========================
    Button btn_save_photo;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_uri;
    String Subject; // 다음 과목을 넣기 위해.
    String selected_m;
    String selected_m_c;
    String session_key;
    String student_list;//학생들의 학번,이름 포함.
    String checked_student_list;// 학생들 출석여부.
    String checked = null;
    String Class_number;
    String subject_code; // 과목코드.
    String url = "http://rbghoneroom402.iptime.org:48526/JSP/DBwrite.jsp";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.student_upload);

        imageView1 = findViewById(R.id.imageView1);
        EditText show_class_name = findViewById(R.id.select_class);
        btn_save_photo = findViewById(R.id.CONFIRM);
        Button Send_To_Jsp = (Button) findViewById(R.id.Send_To_JSP);


        //========================3=3=3=3=3=3=========변경후 =================================
        //=========액티비디로 값을 받아올때 부분===================13==13==13==13==13==13===13====================
        final Intent intent = getIntent(); // select_menu에서 보낸 값을 받아온다.
        Class_number = intent.getExtras().getString("class_number");
        selected_m = intent.getExtras().getString("select_menu");
        selected_m_c = intent.getExtras().getString("select_menu_code");
        session_key = intent.getExtras().getString("session_key");
        student_list = intent.getExtras().getString("student_list_info");
        final String selected_menu = intent.getExtras().getString("selected_menu");
        final String User_subject = intent.getExtras().getString("User_subject");

        //====================================13==13==13==13==13==13===13====================
        //============================5=5=5=5=5=5================jsp적용 =====================


        show_class_name.setText("최종적으로 선택된 과목:" + selected_m);

        Send_To_Jsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                //finish();
            }
        });

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
        //============================================5=5=5=5=5=5=============================

        Button btnReturn = (Button) findViewById(R.id.back1);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), before_take_photo.class);
                /*
                intent.putExtra("class_number",Class_number);
                intent.putExtra("checked", checked);
                intent.putExtra("select_menu",selected_m);
                intent.putExtra("select_menu_code",selected_m_c);
                intent.putExtra("session_key",session_key);
                intent.putExtra("student_list_info",student_list);
                 */
                intent.putExtra("class_number",Class_number);
                intent.putExtra("Select_menu",selected_menu); // 다음 "select_menu"라는 이름으로 set_menu를 넣고 다음 액티비티를 연다.
                intent.putExtra("Session_key",session_key); // 세션키를 넘겨준다.
                intent.putExtra("All_subject",User_subject);
                intent.putExtra("checked",checked);
                startActivity(intent);
                finish();
            }
        });

        //=========================3=3=3=3=3=3=변경후========================================
        // 버튼을 눌렀을때
        btn_save_photo.setOnClickListener(new View.OnClickListener() {
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


    }
    private void open_camera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NEW PICTURE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE); // 다음 결과 값을 받고 새로운 액티비티를 생성하기 위해 다음과 같은걸 쓴다// .
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
        //===============================================================================================나의 코드
        if (resultCode == RESULT_OK) // 카메라에 기본 탑재가 되어있다. 사진을 찍고 그 사진을 사용하기를 원하면 Result_ok를 가져온다.
        {
            imageView1.setImageURI(image_uri); // 비트맵 객체를 이용해서 보여줌.
            Toast.makeText(this, "출석체크 사진을 저장하였습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "출석체크 사진이 찍히지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
        //===============================================================================================나의 코드
        //=================================================5=5=5=5=5=55===================================규호형===================================================

        Toast.makeText(getBaseContext(), "resultCode : " + data, Toast.LENGTH_SHORT).show();
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String img_path = null;
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                    String subject = Subject;
                    //아래 함수가 사진을 보내기위한 함수,
                    DoFileUpload("http://rbghoneroom402.iptime.org:48526/JSP/Test.jsp", img_path);  //해당 함수를 통해 이미지 전송.  경로를 설정. 다음 JSP로 보내기위한.
                    check_user_info(student_list,session_key,url);
                    //===================위의 논리 이유 ==========
                    //    과목을 먼저 선정을 하여 다음
                    //
                    //
                    //===================위의 논리 이유 ===========
                    //DoStringUpload(subject); // 과목을 가져온다.
                    //=====================11===========11===========11==========11==========11=================================

                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show(); //다음 이미지 저장공간 경로를 보여줌.
                    //=================11============11=============11===============11=========11===============================다음은 텍스트를 보내기 위해서
                    //이미지를 비트맵형식으로 반환 . 아래는 그냥 이미지 사진을 줄여서 비트맵에 보여주기 위한 거 =========================9=9=9=9=9=====
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    int reWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
                    int reHeight = (int) (getWindowManager().getDefaultDisplay().getHeight());

                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                    Bitmap image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true); // 사이즈를 조절
                    ImageView image = (ImageView) findViewById(R.id.imageView1);  //이미지를 띄울 위젯 ID값, 조절된 이미지가 띄워짐.
                    image.setImageBitmap(image_bitmap_copy); // image에 다음 비트맵의 이미지가 씌워짐.
                    //=========================================================================================================9=9=9=9=9=9=======
                } catch (Exception e) {
                    e.printStackTrace(); // 에러 메시지를 발생시키게 하는 문장. 근원지를 찾아 단계별로 찾게하는 문장.
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data); // super는 부모에게 있는 변수를 사용하기 위해 있는 선언자이다.

        //=================================================5=5=5=5=5=55=5======================================규호형==========================-====================
    }

    //=====================5=5=5=5=55===============jsp 연동 ======================================= 규호형================================================================



    public String getImagePathToUri(Uri data) {  // 절대경로 설정 하는 곳
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        return imgPath; // imgPath를 가져온다.
    }

    public void DoFileUpload(String apiUrl, String absolutePath) {
        take_photoF.NetworkTask networkTask = new take_photoF.NetworkTask(apiUrl, absolutePath);
        networkTask.execute();
    }

    public void check_user_info(String student_list,String Session_key,String url) {
        Request_check_info_network.NetworkTask networkTask = new Request_check_info_network.NetworkTask(student_list,url,Session_key,getBaseContext());
        try {
            checked_student_list = networkTask.execute().get();
        } catch (ExecutionException e) {
            Toast.makeText(this, "데이터를 읽어 올 수 없습니다.",Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Interruptted 오류! ",Toast.LENGTH_LONG).show();
        }
    }


    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url, fileName;

        public NetworkTask(String url, String fileName) { //다음 URI와 FileName과 subject를 보냄.
            this.url = url;
            this.fileName = fileName;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = HttpURLConnection(url, "", fileName); // 해당 URL로 부터 결과물을 얻어온다. //
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            // s는 받아온 데이터.
            checked =s; // 다음은 받아온 데이터
/*
            Intent intent1 = new Intent(getApplicationContext(), before_take_photo.class);
            intent1.putExtra("student_list",s);
            intent1.putExtra("Progress",Progress);

 */


            Toast.makeText(getBaseContext(), "Responce : " + s, Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT) // 다음 버전에서 사용할 수 있다는 뜻.
    public String HttpURLConnection(String urlString, String params, String fileName) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            FileInputStream mFileInputStream = new FileInputStream(new File(fileName));
            URL connectUrl = new URL(urlString + ";jsessionid=" + session_key.substring(11,43));
            // HttpURLConnection 통신
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + selected_m_c +".jpg" + "\"" + lineEnd); //
            //파일 이름 설정;
            dos.writeBytes(lineEnd);
            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            mFileInputStream.close();
            dos.flush();

            StringBuffer b;
            b = new StringBuffer();
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            try {
                for (String ch; (ch = dis.readUTF()) != null; ) {
                    b.append(ch);
                    b.append(lineEnd);
                }
            } catch (EOFException e) {
                b.delete(b.length() - lineEnd.length(), b.length());
            }
            checked_student_list = b.toString();
            return b.toString();
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }
}
