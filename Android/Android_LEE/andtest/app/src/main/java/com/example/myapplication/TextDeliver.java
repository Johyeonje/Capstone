package com.example.myapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.HttpURLConnection;

public class TextDeliver {
    private HttpURLConnection conn;
    private StringBuffer b = new StringBuffer();
    public TextDeliver(HttpURLConnection conn) { //텍스트 수신
        this.conn = conn;
    }

    public String GetText() { // 텍스트 수신
        StringBuffer b;
        try {
            String lineEnd = "\r\n";
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            b = new StringBuffer();
            try {
                for (String ch; (ch = dis.readUTF()) != null;)  {
                    b.append(ch);
                    b.append(lineEnd);
                }
            } catch (EOFException e) {
                b.delete(b.length()-lineEnd.length(),b.length());
            }
            System.out.println("받은 문자 : \r\n" + b.toString());
        } catch (Exception e) {
            return e.toString();
        }
        return b.toString();
    }

    public void append(String text) { // 텍스트 저장
        b.append(text);
    }

    public void SendText() { // 저장된 텍스트 송신
        try {
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            if (b.substring(b.length()-2, b.length()).equals("\r\n")) { // 맨 끝 줄바꿈 제거
                b.delete(b.length()-2, b.length());
            }
            dos.writeUTF(b.toString());
            System.out.println("보낸 문자 : \r\n" + b.toString());
            dos.flush();
            dos.close();
        } catch (Exception e) {

        }
    }

    public void SendText(String text) {
        try {
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            if(text.length()>3) {
                if (text.substring(text.length()-2).equals("\r\n")) { // 맨 끝 줄바꿈 제거
                    text = text.substring(0, text.length()-2);
                }
            }
            dos.writeUTF(text);
            System.out.println("보낸 문자 : \r\n" + text);
            dos.flush();
            dos.close();
        } catch (Exception e) {

        }
    }
}
