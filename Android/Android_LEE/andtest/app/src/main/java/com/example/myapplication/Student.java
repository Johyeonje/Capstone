package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
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
        try {
            URL connectUrl = new URL(urlString + ";jsessionid="+cookie.substring(11,43));
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            TextDeliver textDeliver = new TextDeliver(conn);
            textDeliver.SendText(SUB_ID);
            // [2-2]. parameter 전달 및 데이터 읽어오기.
            String text = textDeliver.GetText();
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;
            return text;
        } catch (Exception e) {
            return null;
            // TODO: handle exception
        }
    }
}
