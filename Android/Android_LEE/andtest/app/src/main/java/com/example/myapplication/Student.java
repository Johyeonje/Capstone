package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Student {
    public static class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url, SUB_ID, cookie;
        private Context context;

        public NetworkTask(String url, String cookie, String SUB_ID, Context context) {
            this.url = url;
            this.cookie = cookie;
            this.SUB_ID = SUB_ID;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = HttpURLConnection (url, "", cookie, SUB_ID); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);       //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다
        }
    }

    public static String HttpURLConnection(String urlString, String params, String cookie, String SUB_ID) {
        String lineEnd = "\r\n";
        try {
            URL connectUrl = new URL(urlString + ";jsessionid="+cookie.substring(11,43));
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // [2-2]. parameter 전달 및 데이터 읽어오기.
            dos.writeUTF(SUB_ID);
            StringBuffer b;
            b = new StringBuffer();
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            try {
                for (String ch; (ch = dis.readUTF()) != null;)  {
                    b.append(ch);
                    b.append(lineEnd);
                }
            } catch (EOFException e) {
                b.delete(b.length()-lineEnd.length(),b.length());
            }
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;
            dos.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            dos.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.
            dis.close();
            return b.toString();
        } catch (Exception e) {
            return null;
            // TODO: handle exception
        }
    }
}
