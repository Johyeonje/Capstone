<%@page import="java.io.InputStream"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		//이미지를 저장할 경로 입력.
		request.setCharacterEncoding("utf-8");
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
	%>
</body>
</html>