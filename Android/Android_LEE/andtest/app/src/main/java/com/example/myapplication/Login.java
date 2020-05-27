package com.example.myapplication;
import android.content.Context;
import android.os.AsyncTask;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login {
    public static class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url, ID, PWD, cookie;
        private Context context;

        public NetworkTask(String url, String ID, String PWD, Context context) { //Login 용
            this.url = url;
            this.ID = ID;
            this.PWD = PWD;
            this.cookie=null;
            this.context = context;
        }
        public NetworkTask(String url, String cookie, Context context) { //Logout 용
            this.url = url;
            this.cookie = cookie;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result=null; // 요청 결과를 저장할 변수.
            if (cookie==null)
                result = HttpURLConnection (url, "", ID, PWD); // 해당 URL로 부터 결과물을 얻어온다.
            else
                result = HttpURLConnection (url, "", cookie); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);       //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다
        }
    }

    public static String HttpURLConnection(String urlString, String params, String ID, String PWD) { //Login 용
        String lineEnd = "\r\n";
        String cookie=null;
        try {
            URL connectUrl = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // [2-2]. parameter 전달 및 데이터 읽어오기.
            dos.writeUTF(ID);
            dos.writeUTF(PWD);
            String cookieTemp = conn.getHeaderField("Set-Cookie");
            if (cookieTemp != null)
            {
                cookie = cookieTemp;
            }
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;
            dos.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            dos.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.
            return cookie;
        } catch (Exception e) {
            return null;
            // TODO: handle exception
        }
    }

    public static String HttpURLConnection(String urlString, String params, String cookie) { //Logout 용
        String lineEnd = "\r\n";
        try {
            URL connectUrl = new URL(urlString+";jsessionid="+cookie.substring(11,43));
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cookie",cookie);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // [2-2]. parameter 전달 및 데이터 읽어오기.
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;
            return cookie;
        } catch (Exception e) {
            return null;
            // TODO: handle exception
        }
    }
}