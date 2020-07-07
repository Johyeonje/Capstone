package com.example.myapplication;
        import android.content.Context;
        import android.os.AsyncTask;
        import java.net.HttpURLConnection;
        import java.net.URL;

public class ADS {
    public static class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url, num;
        private Context context;

        public NetworkTask(String url, String num, Context context) {
            this.url = url;
            this.num = num;
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result=null; // 요청 결과를 저장할 변수.
            result = HttpURLConnection (url, "", num); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);       //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다
        }
    }

    public static String HttpURLConnection(String urlString, String params, String num) { //Login 용
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
            SendText.SendText(num);
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;
            String received = SendText.GetText();
            return received;
        } catch (Exception e) {
            return null;
            // TODO: handle exception
        }
    }
}