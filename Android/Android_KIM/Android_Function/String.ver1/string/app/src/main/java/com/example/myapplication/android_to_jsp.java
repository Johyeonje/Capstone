package com.example.myapplication;

import java.io.DataInputStream;
import java.io.InputStream;

public class android_to_jsp {

    // jsp에서 text를 받을때.
/*
    request.setCharacterEncoding("utf-8"); // 다음 utf8 형식으로 요청을 한다.
        try {
        System.out.println("text시작");
        StringBuffer b;
        InputStream inputStream = request.getInputStream();
        DataInputStream dis = new DataInputStream(inputStream);
        b = new StringBuffer();
        for (String ch; (ch = dis.readUTF()) != null;)  {
            b.append(ch);
        }
        System.out.print(b.toString());
        inputStream.close();
    } catch (Exception e) {
        System.out.println("실패!");
    }

 */
}
