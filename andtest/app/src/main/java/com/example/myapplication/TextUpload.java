package com.example.myapplication;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TextUpload {
    public static class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private String[] text;

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
            urlConn.setRequestProperty("Context_Type", "text/html;cahrset=UTF-8");

            // [2-2]. parameter 전달 및 데이터 읽어오기.
            OutputStream os = urlConn.getOutputStream();
            for (String s : text) {
                os.write(s.getBytes("UTF-8"));
                os.write("\r\n".getBytes("UTF-8"));
            }
            os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

            // [2-3]. 연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;
            return null;
//            URL connectUrl = new URL(urlString);
//            // HttpURLConnection 통신
//            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Connection", "Keep-Alive");
//            conn.setRequestProperty("Content-Type", "text/html;cahrset=UTF-8");
//            // write data
//            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
//            for (String s : text) {
//                dos.writeUTF(s);
//            }
//            dos.flush();
//            dos.close();
//            return null;
        } catch (Exception e) {
            return null;
            // TODO: handle exception
        }
    }
}
