<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="java.io.EOFException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<%
		//이미지를 저장할 경로 입력.
		request.setCharacterEncoding("utf-8");
		StringBuffer b;
		try {
			String lineEnd = "\r\n";
			long time = System.currentTimeMillis(); 
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String str = dayTime.format(new Date(time));
			System.out.println("text시작"+str);
			InputStream inputStream = request.getInputStream();
			DataInputStream dis = new DataInputStream(inputStream);
            b = new StringBuffer();
            try {
                for (String ch; (ch = dis.readUTF()) != null;)  {
                    b.append(ch);
                    b.append(lineEnd);
                }
            } catch (EOFException e) {
                b.delete(b.length()-lineEnd.length(),b.length());
            }
            b.append(lineEnd);
            System.out.print(b.toString());
            inputStream.close();
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("실패!");
		}
	%>
</body>
</html>