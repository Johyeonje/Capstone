<%@page import="java.io.InputStream"%>
<%@page import="java.io.DataInputStream"%>
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
            b = new StringBuffer();
            for (int ch = 0; (ch = inputStream.read()) != -1; ) {
                b.append((char) ch);
            }
            System.out.print(b.toString());
            inputStream.close();
		} catch (Exception e) {
			System.out.println("실패!");
		}
	%>

</body>
</html>