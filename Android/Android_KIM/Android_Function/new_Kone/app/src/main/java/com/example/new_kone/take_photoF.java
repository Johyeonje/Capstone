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
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class take_photoF extends Activity {
//==================55=5=5=5=5=5============= JSP로 보내는 부분====================================규호형===============================================

    private static final int REQ_CODE_SELECT_IMAGE = 100;
    @RequiresApi(api = Build.VERSION_CODES.M)

//==================================================5=5=5=5=5=5====================================규호형===============================================

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

    //==============================3=3=3=3=3=3= 변경후 필요변수========================

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo);

        imageView1 = findViewById(R.id.imageView1);
        EditText show_class_name = findViewById(R.id.select_class);
        //========================3=3=3=3=3=3=========변경후 =================================
        btn_save_photo = findViewById(R.id.CONFIRM);
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
        Button Send_To_Jsp = (Button) findViewById(R.id.Send_To_JSP);

        show_class_name.setText("최종적으로 선택된 과목:" + selected_m);

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

        Button btnReturn = (Button) findViewById(R.id.back1);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), before_take_photo.class);
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
        //startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE); // 다음 결과 값을 받고 새로운 액티비티를 생성하기 위해 다음과 같은걸 쓴다// .
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
        NetworkTask networkTask = new NetworkTask(apiUrl, absolutePath);
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



// 저장고 1 정리 필요
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
// ==========================================11=====11==========11=======11===============문자를 보내기 위해서=====
/*
    public class Task extends AsyncTask<String, Void, String> {
        String sendMsg;
        String receiveMsg;
        String serverip = "http://rbghoneroom402.iptime.org:48526/JSP/Test.jsp"; // 연결할 jsp주소

        Task(String sendmsg){
            this.sendMsg = sendmsg;
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                URL url = new URL(serverip);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                OutputStreamWriter dos1= new OutputStreamWriter(conn.getOutputStream());

                if(sendMsg.equals("vision_write")){
                    sendMsg = "vision_write="+strings[0]+"&type="+strings[1]; //다음 전달 받은 값이 vision_write라는 값이 왔다? 그럼 넘겨준다,
                }else if(sendMsg.equals("vision_list")){
                    sendMsg = "&type="+strings[0];
                }
                dos1.write(sendMsg);
                dos1.flush();

                // 데이터를 위의 해당되느 jsp주소로 보낸다.

                //위에까지는 데이터를 보내는것 다음은 데이터를 받아오는것
                //onPostExcute를 다음처럼 나타낸것 같다.
                if(conn.getResponseCode() == conn.HTTP_OK) { // 다음 연결이 되었는지 확인.
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러"); //다음 연결이 안됬을때. 나타나는 LOGCAT
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg; // 연결이 되서 결과를 받았으면 receiveMsg 를 받는다.
        }
    }


    // ==========================================11=====11==========11=======11===============문자를 보내기 위해서=====
 */



// 저장고 정리 필요

// 스피너 사용 부분 ====================================4=4=4=4=4=44=========================

        /*
        final String[] Class = {"과목1", "과목2", "과목3", "과목4", "과목5"}; // 이 부분을 수정하면됨. 수정 할것은 testHere에 보관함. split를 사용하면 됨. // 다음 받아온 s를 이용하여 변경해야함.
        final Spinner spinner = (Spinner) findViewById(R.id.class_choose);

        String sub1 = "sub1";
        String sub2 = "sub2";
        String sub3 = "sub3";
        String sub4 = "sub4";
        String sub5 = "sub5";

         */

//============================스피너 선택창======================================================

        /*

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Class);
        spinner.setAdapter(adapter);
        // spinner.setSelection(4);
        while (true) {
            if (sub1.equals(selected_menu)) {
                Toast.makeText(this, "과목1이 선택되었습니다.", Toast.LENGTH_LONG).show();
                spinner.setSelection(0); // 스피너는 0부터 초기값이 0부터 시작됨.
                break;
            } else if (sub2.equals(selected_menu)) {
                Toast.makeText(this, "과목2가 선택되었습니다.", Toast.LENGTH_LONG).show();
                spinner.setSelection(1);
                break;
            } else if (sub3.equals(selected_menu)) {
                Toast.makeText(this, "과목3이 선택되었습니다.", Toast.LENGTH_LONG).show();
                spinner.setSelection(2);
                break;
            } else if (sub4.equals(selected_menu)) {
                Toast.makeText(this, "과목4가 선택되었습니다.", Toast.LENGTH_LONG).show();
                spinner.setSelection(3);
                break;
            } else if (sub5.equals(selected_menu)) {
                Toast.makeText(this, "과목5가 선택되었습니다.", Toast.LENGTH_LONG).show();
                spinner.setSelection(4);
                break;
            }
        }



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
// i번째에 해당되는것을 선택할 수 있다.

                editText1.setText("다음 과목을 인증합니다." + adapterView.getItemAtPosition(i));
                //다음 스트링을 보내기 위해서.


                //Intent intent = new Intent(getApplicationContext(),sendEmailPasswordF.class);
                String sClass  = spinner.getSelectedItem().toString(); // 다음 스피너에 선택된 변수를 가지고 온다. sClass에.
                //intent.putExtra("sClass",sClass); // 위의 sClass를 가져와 다음 액티비티로 넘기기 위해 가져온다.
                Subject = sClass; //전역변수 Subject에 다음 스피너에서 고른 클래스가 들어간다.

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //아무것도 선택이 안됬을때 설정을 넣는 곳.
            }
        });



         */
//=============================4=4=4=4=4=4=4================================================

//===============================15==15==15==15===15========================================
//날짜 선택 창
        /*
        selected_date = findViewById(R.id.selected_date);
        calendarView = findViewById(R.id.select_date);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                selected_date.setText("선택된 날짜:"+ year+"/" +(month+1)+ "/" +dayOfMonth);

            }
        });

         */

//===============================15==15==15==15===15========================================