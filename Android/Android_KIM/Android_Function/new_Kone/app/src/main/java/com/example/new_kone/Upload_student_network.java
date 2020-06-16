package com.example.new_kone;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Upload_student_network {
    public static class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url, fileName, session_key, STU_ID;
        private Context context;

        public NetworkTask(String url, String fileName, String session_key, String STU_ID, Context context) {
            this.url = url;
            this.fileName = fileName;
            this.context = context;
            this.session_key = session_key;
            this.STU_ID = STU_ID;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = HttpURLConnection(url, "", fileName, session_key, STU_ID);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        }
    }

    public static String HttpURLConnection(String urlString, String params, String fileName, String session_key, String STU_ID) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        FileInputStream mFileInputStream = null;
        try {
            mFileInputStream = new FileInputStream(new File(fileName));
            URL connectUrl = new URL(urlString+";jsessionid="+session_key.substring(11,43));
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + STU_ID + ".jpg\"" + lineEnd);
            dos.writeBytes(lineEnd);
            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            dos.flush();
            /*
            TextDeliver GetText = new TextDeliver(conn);
            String text = GetText.GetText();
             */
            return null;
        } catch (Exception e) {
            System.out.print(e);
            return null;
            // TODO: handle exception
        } finally {
            if(mFileInputStream != null) {
                try {
                    mFileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
