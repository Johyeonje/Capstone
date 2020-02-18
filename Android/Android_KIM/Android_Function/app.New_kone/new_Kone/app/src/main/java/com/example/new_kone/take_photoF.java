package com.example.new_kone;

//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

//===========================================5=5=5=5=5============================================
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//==========================================5=55=5=5=5=5=========================================

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class take_photoF extends Activity {
//==================55=5=5=5=5=5============= JSP로 보내는 부분====================================규호형===============================================

    private static final int REQ_CODE_SELECT_IMAGE = 100;
    @RequiresApi(api = Build.VERSION_CODES.M)

//==================================================5=5=5=5=5=5====================================규호형===============================================

   ImageView imageView1;
   EditText editText1;
    //==========================3=3=3=3=3======변경후 필요 변수=========================
    Button btn_save_photo;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_uri;
    String Subject; // 다음 과목을 넣기 위해.
    //==============================3=3=3=3=3=3= 변경후 필요변수========================

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo);

        imageView1 = findViewById(R.id.imageView1);
        editText1 = findViewById(R.id.select_class);
        //========================3=3=3=3=3=3=========변경후 =================================
        btn_save_photo = findViewById(R.id.CONFIRM);
        //========================3=3=3=3=3=3=========변경후 =================================

        //============================5=5=5=5=5=5================jsp적용 =====================
        Button Send_To_Jsp = (Button) findViewById(R.id.Send_To_JSP);

        Send_To_Jsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
        //============================================5=5=5=5=5=5=============================

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
//다음은 카메라.저장소.인터넷 사용이 허가가 안되있을때 오류가 날 수 있음으로 허가를 받고 사용하기 위해서 행하는 것.
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){// 빌드 버젼을 체크하고
                    if(checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  // 다음에 인터넷에 사용 허가를 받는것을 추가 하였다.~~~~~~~~ 변경 된거....
                                    PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
                        String [] permission ={Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
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

        final String[] Class = {"과목1","과목2","과목3","과목4","과목5"}; // 이 부분을 수정하면됨. 수정 할것은 testHere에 보관함. split를 사용하면 됨. // 다음 받아온 s를 이용하여 변경해야함.
        final Spinner spinner = (Spinner)findViewById(R.id.class_choose);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,Class);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
// i번째에 해당되는것을 선택할 수 있다.

                editText1.setText("다음 과목에 대한 인증을 실행합니다:" + adapterView.getItemAtPosition(i));
                // 다음 스트링을 보내기 위해서.


                Intent intent = new Intent(getApplicationContext(),sendEmailPasswordF.class);
                String sClass  = spinner.getSelectedItem().toString(); // 다음 스피너에 선택된 변수를 가지고 온다. sClass에.
                //intent.putExtra("sClass",sClass); // 위의 sClass를 가져와 다음 액티비티로 넘기기 위해 가져온다.
                Subject = sClass; //전역변수 Subject에 다음 스피너에서 고른 클래스가 들어간다.

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //=============================4=4=4=4=4=4=4================================================

    }

    private void open_camera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NEW PICTURE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE); // 다음 결과 값을 받고 새로운 액티비티를 생성하기 위해 다음과 같은걸 쓴다.
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //===============================================================================================나의 코드
        if (resultCode == RESULT_OK) // 카메라에 기본 탑재가 되어있다. 사진을 찍고 그 사진을 사용하기를 원하면 Result_ok를 가져온다.
        {
            imageView1.setImageURI(image_uri); // 비트맵 객체를 이용해서 보여줌.
            Toast.makeText(this,"출석체크 사진을 저장하였습니다.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"출석체크 사진이 찍히지 않았습니다.",Toast.LENGTH_SHORT).show();
        }
        //===============================================================================================나의 코드
        //=================================================5=5=5=5=5=55===================================규호형===================================================

        Toast.makeText(getBaseContext(), "resultCode : " + data, Toast.LENGTH_SHORT).show();
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String img_path=null;
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                    String subject = Subject;

                    DoFileUpload("http://rbghoneroom402.iptime.org:48526/JSP/Test.jsp", img_path, subject);  //해당 함수를 통해 이미지 전송.  경로를 설정. 다음 JSP로 보내기위한.
                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show(); //다음 이미지 저장공간 경로를 보여줌.
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT) // 다음 버전에서 사용할 수 있다는 뜻.
    public String HttpURLConnection(String urlString, String params, String fileName ,String subject) { // filename다음에 과목을 추가함.
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            FileInputStream mFileInputStream = new FileInputStream(new File(fileName));
            FileInputStream Class_FileInputStream = new FileInputStream(subject); // Class_FileInputStream에 과목을 넣는다.
            URL connectUrl = new URL(urlString);
            Log.d("Test", "mFileInputStream  is " + mFileInputStream +"Class is" +Class_FileInputStream);
            // HttpURLConnection 통신
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes("Class_FileInputStream" + Class_FileInputStream);
            dos.writeBytes(lineEnd);
            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            Log.d("Test", "image byte is " + bytesRead);
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
            Log.e("Test", "File is written");
            mFileInputStream.close();
            Class_FileInputStream.close();
            dos.flush();
            // finish upload...
            // get response
            StringBuffer b;
            try (InputStream is = conn.getInputStream()) {
                b = new StringBuffer();
                for (int ch = 0; (ch = is.read()) != -1; ) {
                    b.append((char) ch);
                }
                is.close();
            }
            Log.e("Test", b.toString());
            return b.toString();

        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            return null;
            // TODO: handle exception // 오류 선언
        }
    }

    public String getImagePathToUri(Uri data) {  // 절대경로 설정 하는 곳
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.d("test", imgPath);
        return imgPath; // imgPath를 가져온다.
    }

    public void DoFileUpload(String apiUrl, String absolutePath, String subject) {
        NetworkTask networkTask = new NetworkTask(apiUrl, absolutePath, subject); // 다음 은 NetworkTask타입의 networkTask를 만든다는 뜻. 그리고 apiURI와 absolutewPath를 매개변수로 넣는다.
        networkTask.execute(); // NetworkTask를 실행한다는 뜻,
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url, fileName, subject;

        public NetworkTask(String url, String fileName, String subject) { //다음 URI와 FileName과 subject를 보냄.
            this.url = url;
            this.fileName = fileName;
            this.subject = subject;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = HttpURLConnection (url, "", fileName, subject); // 해당 URL로 부터 결과물을 얻어온다. //
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            Toast.makeText(getBaseContext(), "Responce : " + s, Toast.LENGTH_SHORT).show();
        }
    }
/*
    private void checkSelfPermission() {
        String temp = "";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        { temp += Manifest.permission.READ_EXTERNAL_STORAGE + " "; } //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        { temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " "; }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        { temp += Manifest.permission.INTERNET + " "; }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        { temp += Manifest.permission.CAMERA + " "; }
        if (TextUtils.isEmpty(temp) == false)
        { // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1); }
        else { // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show(); }
    }


 */
    //========================5=5=5=5=55=5==========================================================규호형===============================================================
//
}
