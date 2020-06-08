package com.example.new_kone;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request_user_network
{
    public static class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private String session_key;
        private Context context;

        public NetworkTask(String url,String session_key, Context context) {
            this.url = url;
            this.session_key = session_key;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = HttpURLConnection (url, "", session_key);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public static String HttpURLConnection(String urlString, String params,String Session_key) {
        String lineEnd = "\r\n";
        String boundary = "*****" ;
        String info =null;
        try {
            URL connectUrl = new URL(urlString + ";jsessionid=" + Session_key.substring(11,43)); //기존의 url과 다음 ";jsessionid="+
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
            //dos.writeUTF(Session_key);
            dos.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.

            // 다음 inputStream을 넣어야됨됨
            // 다음 전달 받은 세션 스트링을 보낸다.
            /*
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null; // 연결이 안돼어있을 경우
            }
            /*
            if(conn.getResponseCode() == conn.HTTP_OK) { // 연결이 되었을때 받는다.
                //DataInputStream dis = new DataInputStream(conn.getInputStream());
                InputStreamReader dis = new InputStreamReader(conn.getInputStream());
                BufferedReader reader = new BufferedReader(dis);
                StringBuffer buffer = new StringBuffer();

                String i;
                while ( ((i =reader.readLine())!= null)){
                    buffer.append(i);
                } //

                info = buffer.toString();
            }
            else{
                Log.i("통신 결과", conn.getResponseCode()+"에러");
            }
            dos.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.
            return info;

             */
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
