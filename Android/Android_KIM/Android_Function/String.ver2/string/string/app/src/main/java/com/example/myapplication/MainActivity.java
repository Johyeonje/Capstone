package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.os.Build.ID;

public class MainActivity extends AppCompatActivity {
    Button button2;
    EditText Email,Password;
    String url = "http://rbghoneroom402.iptime.org:48526/JSP/Text.jsp";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final String user_EMail;
        final String user_PassWord;
        final String[] user_info = new String[2];

        button2 = findViewById(R.id.button2);
        Email = findViewById(R.id.E_mail);
        Password = findViewById(R.id.PASSWORD);

        final String user_EMail = Email.getText().toString();
        user_info[0] = user_EMail;
        user_PassWord = Password.getText().toString();
        user_info[1] = "Password";



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Send_user("http://rbghoneroom402.iptime.org:48526/JSP/Text.jsp",user_info);
                //NetworkTask networkTask = new NetworkTask(url, null);
                //networkTask.execute();
            }
        });


    }



    public class NetworkTask extends AsyncTask<Void, String, String>
    {
        private String url;
        private ContentValues values;

        public NetworkTask(String url,ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result =requestHttpURLConnection.request(url, values);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //text1.setText(s); 다음은 AsyncTask가 끝나고  받아올 것을 보여줄 코드부분.
        }
    }

    public void Send_user(String Url, String[] info){

        LoginAnd.NetworkTask networkTask = new LoginAnd.NetworkTask(Url,info, getBaseContext());
        networkTask.execute();
    }

}
















/*
    public class NetworkTask extends AsyncTask<String, Void, String> {

        private String url;
        String sendMSG;


        public NetworkTask(String url) {
            this.url = url;
        }
    @Override
    protected String doInBackground(String... strings) {
        String result; // 요청 결과를 저장할 변수.

        result = HttpURLConnection("http://rbghoneroom402.iptime.org:48526/JSP/text.jsp");// 해당 URL로 부터 결과물을 얻어온다.
        try {
            String str;
            URL url = new URL(result); // 다음 데이터를 보낼 JSP가 실행되는 주소를 입력하면 된다.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****;charset=utf-8");


            //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                //jsp에서 보낸 값을 받겠죠?
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }

            } else {
                Log.i("통신 결과", conn.getResponseCode()+"에러");
                // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
      text.setText(s);
    }
}

    public String HttpURLConnection(String Uri) {

        BufferedReader br = null;
        String returnText = "";
        String data = "";
        try {

            URL connectUrl = new URL(Uri);
            // HttpURLConnection 통신
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****;charset=utf-8");

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            //DataOutputStream osw = new DataOutputStream(conn.getOutputStream());
            //sendMSG = "messege=";
            //sendMSG = "id="+strings[0]+"&pwd="+strings[1];
            // GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
            //회원가입처럼 보낼 데이터가 여러 개일 경우 &로 구분하여 작성합니다.
            osw.write("hi");
/*
            osw.writeBytes("Content-Disposition: form-data; name=\"hangul\"");
            //한글 부분만 writeUTF 을 사용하여 전송하면 깨지지않고 보낼 수 있다.
            osw.writeBytes("Content-Disposition: form-data; name=\"hangul\"");
            osw.writeBytes(sendMSG);//OutputStreamWriter에 담아 전송합니다.
            osw.writeUTF("한글");
 */
    /*
            osw.flush();

            // write data
            StringBuffer b;
            try (InputStream is = conn.getInputStream()) {
                b = new StringBuffer();
                for (int ch = 0; (ch = is.read()) != -1; ) {
                    b.append((char) ch);
                }
            }
            returnText = b.toString();

        } catch (Exception e) {
            return null;
            // TODO: handle exception
        }



        return returnText;

    }
*/

    /*
    public class NetworkTask1 extends AsyncTask<String, Void, String> {
        //public String ip ="172.22.229.37"; //자신의 IP번호
        String sendMsg;
        String receiveMsg;
        String serverip = "http://rbghoneroom402.iptime.org:48526/JSP/text.jsp"; // 연결할 jsp주소

        NetworkTask1(String sendmsg){
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
*/

// 다음은 입력한 값을 받아 올때 사용하는 함수. class 간의 데이터 전송이 필요하다.
//아래 함수는 사용을 안함.

    /*
    public static class info_Book extends Application {

        public static String user_id;
        public static String user_pw;

        public static String getUser_id() {
            return user_id;
        }
        public static void setUser_id(String user_id) {
            info_Book.user_id = user_id;
        }

        public static String getUser_pw() {
            return user_pw;
        }

        public static void setUser_pw(String user_pw) {
            info_Book.user_pw = user_pw;
        }

    }

     */

    /*
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sendmsg = "vision_write";
                String result = "hi"; //자신이 보내고싶은 값을 보내시면됩니다
                try{
                    String rst = new NetworkTask(sendmsg).execute(result,"vision_write").get();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

         */

