package com.example.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;



public class RequestHttpURLConnection{

    String userid = MainActivity.info_Book.user_id;
    String userPassword = MainActivity.info_Book.user_pw;


    public String request(String _url, ContentValues _params) {

        // HttpURLConnection 참조변수.
        HttpURLConnection urlConn = null;
        // URL 뒤에 붙여서 보낼 파라미터.
        StringBuffer sbParams = new StringBuffer();

        /***
         * 1.StingBuffer에 파라미터 연결
         */

        //보낼 데이터가 없을때 파라미터를 비운다.
        if (_params == null)
            sbParams.append("");
            //보낼 데이터가 있으면 파라미터를 채운다.
        else {
            boolean isAnd = false;
            //파라미터 키와 값.
            String key;
            String value;

            for (Map.Entry<String, Object> parameter : _params.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();

                //파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                if (isAnd)
                    sbParams.append("&");

                sbParams.append(key).append("=").append(value);

                //파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                if (!isAnd)
                    if (_params.size() >= 2) {
                        isAnd = true;
                    }

            }
        }

        /***
         * 2.HttpURLConnection을 통해 web의 데이터를 가져온다.
         */

        try {
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();

            //[2-1]. urlConn 설정한다.
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Accept_charset" , "UTF-8");
            urlConn.setRequestProperty("Context_Type","application/x-www-form-urlencoded;charset=UTF-8");
            //String login_info = MainActivity.user_info; // 메인에서 가져온 로그인 정보를 가져옴, 전역변수를 사용함. 교체 요망
            String[] text = {userid,userPassword}; // 다음은 JSP로 넘어갈 값을 말한다.
            //[2-2]. parameter 전달 및 데이터 읽어온다.
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            // [2-2]. parameter 전달 및 데이터 읽어오기.
            for (String s : text) {
                dos.writeUTF(s);
                //dos.writeUTF("안녕 나는 동규야");
            }
            dos.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            dos.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.
            //[2-3]. 연결 요청 확인.
            //실패시 null을 리턴하고 매서드를 종료하게 한다.
            if(urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            //[2-4]. 읽어온 결과물을 리턴한다.
            //요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"UTF-8"));


            //출력물의 라인과 그 합에 대한 변수.
            String line;
            String page ="";

            //라인을 받아와 합친다.
            while((line = reader.readLine()) != null){
                page += line;
            }

            return page;
        } catch (ProtocolException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            if(urlConn != null)
                urlConn.disconnect();
        }

        return null;

    }
}
