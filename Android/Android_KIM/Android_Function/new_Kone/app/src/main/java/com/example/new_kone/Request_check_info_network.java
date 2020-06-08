package com.example.new_kone;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request_check_info_network {

    public static class NetworkTask extends AsyncTask<Void, Void, String> {

        private String student_list;
        private String url;
        private String session_key;
        private Context context;

        public NetworkTask(String student_list,String url,String session_key, Context context) {
            this.student_list = student_list;
            this.url = url;
            this.session_key = session_key;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = HttpURLConnection(student_list,url, "", session_key);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    public static String HttpURLConnection(String student_list,String urlString, String params, String Session_key) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            URL connectUrl = new URL(urlString + ";jsessionid=" + Session_key.substring(11,43));
            // Session_key.sub(11,43)이 같이 와야 Subject.jsp에 접속 할 수 있다.
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // [2-2]. parameter 전달 및 데이터 읽어오기.
            dos.writeUTF(student_list); // 과목 코드 넣기
            dos.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.

            StringBuffer i;
            i = new StringBuffer();
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            try{
                for (String ch; (ch = dis.readUTF())!= null;){
                    i.append(ch);
                    i.append(lineEnd);
                }
            } catch (EOFException e) {
                i.delete(i.length()-lineEnd.length(),i.length());
            }
            return i.toString();

        } catch (Exception e) {
            return null;
            // TODO: handle exception
        }
    }

}
