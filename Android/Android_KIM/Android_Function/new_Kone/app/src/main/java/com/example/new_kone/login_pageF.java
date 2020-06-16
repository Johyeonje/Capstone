package com.example.new_kone;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class login_pageF extends Activity {
    //====================================2=2=2=2=2=================아직 확실x====================
    private final int MY_PERMISSIONS_REQUEST_CAMERA=1001;
    //====================================2=2=2=2=2==============================================
    public String cookie;
    TextView signin,email_text,password_text;// xml에서 변수들을 불러올 때 처음 선언해 주는 것
    LinearLayout LinearLayout_log_in;

    private EditText Email_text,Password_text;
    String url = "http://rbghoneroom402.iptime.org:48526/JSP/Login.jsp"; // 다음은 로그인 정보를 보내기 위한 주소.


    // 위에 것들은 아직 사용안함.
    // 다음은 로그인 자료를 보내기 위해 만든 변수 ===================10100====101010===10100=====101010=
    String n_Email,n_Password; // 사용자가 입력한 이메일과 패스워드를 저장하기 위해서.
    String user_Key; // 사용자의 정보를 담기 위해서
    // ========================================10101000====1010100===101010===101010===10001=1=====

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

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
        //로그인 버튼을 누르면 메인 메뉴로 들어갈수 있도록하는 버튼

        TextView login = findViewById(R.id.login);
        Email_text = findViewById(R.id.email_text);
        Password_text = findViewById(R.id.password_text);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다음은 아이디와 비밀번호를 보내는 구간======================================= 다음 액티비티로 넘기기 위해서.
                String Email = Email_text.getText().toString();
                String Password = Password_text.getText().toString();
                Login(url,Email,Password);

                // 전역변수로 사용하기 위해서 추가.==============================================

                //============================================================================
                // =======10=====10=====10=====10====10====10=============
                /*
                while (true) {
                    if (Email.equals(null)) // 비어있다고 할때는 equals 가 아닌 ==연산자를 사용한다,
                    {
                        Toast.makeText(getApplicationContext(), "아이디를 입력하시오", Toast.LENGTH_LONG).show(); //LENGTH는 문자가 길게 나타날지 짧게 나타날지 알려주는것.
                    } else if (Password.equals(null)) {
                        Toast.makeText(getApplicationContext(), "비밀번호를 입력하시오", Toast.LENGTH_LONG).show();
                    } else {
                        DoFileUpload("http://rbghoneroom402.iptime.org:48526/JSP/Test.jsp", n_Email, n_Password); // 다음 DoFIleUpload부분이 실행이 되야 다음 액티비티로 전환이된다.
                        break;
                 */

                // =======10=====10=====10=====10====10====10=============
/*
// 아래는 다른액티티비로 intent를 활용해 값을 넘기는 것이지만 여기서는 사용을 안하여 쓰지 않는다.
                intent.putExtra("Email_text",Email);
                intent.putExtra("Password_text",Password);
*/
                // =====================================================================
            }
        });
//=============10=====10=====10====10====10===10===10====
// 다음은 API버전이 업그레이드 되면서 네트워크 사용이 항상 Enable로 무조건 UI스레드와 개별 적으로 하게 하는데
        // 그것을 해제하여 UI스레드에서도 네트워크 관련 기능과 디스크 I/O를 가능하게 Disable로 바꿔주는 것이다.
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
    }

    // =======10=====10=====10=====10====10====10=============
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

    public void Login(String url, String ID, String PW){
        String compare;
        String Session_key;
        String origin ="Session_"; // login 성공시 Session값이 나옴으로 다음 값을 넣었다.
        Login_network.NetworkTask networkTask = new Login_network.NetworkTask(url,ID,PW,getBaseContext());
        try {
            cookie = networkTask.execute().get();
            Toast.makeText(getBaseContext(), cookie.substring(0,8), Toast.LENGTH_SHORT).show(); // 쿠키의 1부터 7까지의 스트링을 가지고온다.Session일때 성공

              compare = cookie.substring(0, 8); //비교하기 위하여.
              if (compare.equals(origin)) {
                  Toast.makeText(this, "환영합니다", Toast.LENGTH_LONG).show();
                  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                  Session_key = cookie;
                  //System.out.println(Session_key); 세션키의 값을 가지고 있음
                  //send_key(Session_key); // 프래그먼트로 값을 전달하는 함수.
                  intent.putExtra("Session_key", Session_key);
                  intent.putExtra("UserID",ID);
                  startActivity(intent);
              } else {
                  Toast.makeText(this, "아이디 or 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show();
                  return;
              }



        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

}

//==================================2=2=2=2=2================================================

//============10===========10============10===============10===========10=====================
    /*
    public String HttpURLConnection(String urlString, String params, String n_Email, String n_Password) { // filename다음에 과목을 추가함.
        String lineEnd = "\r\n"; // 1.다음은 경계를 주기 위해서.
        String twoHyphens = "--";//2. "
        String boundary = "*****";//3. "
        try {
            FileInputStream n_Email_FileInputStream = new FileInputStream(n_Email);
            FileInputStream n_Password_FileInputStream = new FileInputStream(n_Password);
            Log.d("Test", "mFileInputStream  is " + n_Email_FileInputStream + "m2FileInputStream is" +n_Password_FileInputStream);
            URL connectUrl = new URL(urlString);
            //
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
           // dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(  n_Email +" "+ n_Password); // 이메일과 패스워드를 보낸다. 띄어쓰기로 구문을 나눈다. 다음 스트링으로 보낸다.
            dos.writeBytes(lineEnd);
                        dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.e("Test", "File is written");
             n_Email_FileInputStream.close();
             n_Email_FileInputStream.close();
            dos.flush();
            // finish upload...
            // get response
            StringBuffer b;
            try (InputStream is = conn.getInputStream()) {
                b = new StringBuffer();
                for (int ch = 0; (ch = is.read()) != -1; ) {
                    b.append((char) ch);
                }
               // is.close();
            }
            Log.e("Test", b.toString());
            return b.toString();

        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            return null;
            // TODO: handle exception // 오류 선언
        }
    }

    public void DoFileUpload( String apiUrl,String Email, String Password) {
        login_pageF.NetworkTask networkTask = new login_pageF.NetworkTask(apiUrl,Email,Password); // jsp로 보낼 경로와 이메일 패스워드를 입력한다.
        networkTask.execute(); // NetworkTask를 실행한다는 뜻.
    }
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url,Email,Password;

        public NetworkTask(String url,String Email, String Password) { //다음 URI와 FileName과 subject를 보냄.
            this.url = url;
            this.Email =Email;
            this.Password = Password;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = HttpURLConnection (url, "", Email,Password); // 해당 URL로 부터 결과물을 얻어온다.  HttpURLConnection함수는 위쪽에.
            return result;
        }

        @Override
            protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            Toast.makeText(getBaseContext(), "Responce : " + s, Toast.LENGTH_SHORT).show(); // 다음으로 해당 사용자의 정보가 모두 스트링으로 오면된다. "s"에 사용자의 값이 온다.
            INFORMATION = s;//다음 사용자의 정보를 INFORMATION에 넣는다.
        }
    }

     */
//============10===========10============10===============10===========10=====================

    /*
    로그인 페이지를 만들때 위의 Task 방식은 필요없음. 스트링을 보내야 하는데 위의것은 파일을 보내는 것이므로 삭제 요망.
     */

 /*
    public void send_key(String Session_key)
    {

        System.out.println(Session_key);
        Bundle bundle = new Bundle();
        bundle.putString("Session_key",Session_key);
        home_pageF home_pageF = new home_pageF();
        home_pageF.setArguments(bundle);

        //intent.putExtra("Session_key",Session_key); // 받아온 아이디
    }
     */
