package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import java.io.DataInputStream;
import java.io.EOFException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DBRead {
    public static class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url,cookie;
        private Context context;

        public NetworkTask(String url, String cookie, Context context) {
            this.url = url;
            this.context = context;
            this.cookie = cookie;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = HttpURLConnection(url, "", cookie); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다
        }
    }
    public static String HttpURLConnection(String urlString, String params, String cookie) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        if(cookie == null)
            return "Login Please~";
        try {
            URL connectUrl = new URL(urlString+";jsessionid="+cookie.substring(11,43));
            // HttpURLConnection 통신
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cookie",cookie);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            // write data
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
            dis.close();
            return b.toString();
        } catch (Exception e) {
            return e.toString();
            // TODO: handle exception
        }
    }
}