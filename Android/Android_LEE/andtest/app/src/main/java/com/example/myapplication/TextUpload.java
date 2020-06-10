package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class TextUpload {
    public static class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private String[] text;
        private Context context;

        public NetworkTask(String url, String[] text, Context context) {
            this.url = url;
            this.text = text;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = HttpURLConnection (url, "", text); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);       //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }
    }

    public static String HttpURLConnection(String urlString, String params, String[] text) {
        try {
            URL connectUrl = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            TextDeliver SendText = new TextDeliver(conn);
            // [2-2]. parameter 전달 및 데이터 읽어오기.
            for (String s : text) {
                SendText.append(s);
            }
            SendText.SendText();
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;
            return Arrays.toString(text);
        } catch (Exception e) {
            return null;
            // TODO: handle exception
        }
    }
}