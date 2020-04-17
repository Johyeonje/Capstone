package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button button,button2;
    String returnText = "" ;
    String MSG;
    TextView text;
    EditText send;
    String url = "http://rbghoneroom402.iptime.org:48526/JSP/Text.jsp";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String[] str = {"str", "str1"};

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        text = findViewById(R.id.text);
        send = findViewById(R.id.send);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkTask networkTask = new NetworkTask(url, str);
                networkTask.execute();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MSG = text.getText().toString();
                NetworkTask networkTask = new NetworkTask(url, str);
                networkTask.execute();
            }
        });


    }
    public static class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private String[] text;
        private Context context;

        public NetworkTask(String url, String[] text) {
            this.url = url;
            this.text = text;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = HttpURLConnection (url, "", text); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다
        }
    }

        public static String HttpURLConnection(String urlString, String params, String[] text) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

                // [2-1]. urlConn 설정.
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
                urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                urlConn.setRequestProperty("Context_Type", "text/html; charset=UTF-8");

                // [2-2]. parameter 전달 및 데이터 읽어오기.
                OutputStream os = urlConn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                for (String s : text) {
                    dos.writeUTF(s);
//                os.write(s.getBytes("UTF-8"));
//                os.write("\r\n".getBytes("UTF-8"));
                }
                os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
                os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.
                return null;
            } catch (Exception e) {
                return null;
                // TODO: handle exception
            }
        }

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
                    sendMsg = "vision_write="+strings[0]+"&type="+strings[1];//다음 전달 받은 값이 vision_write라는 값이 왔다? 그럼 넘겨준다,
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

}


