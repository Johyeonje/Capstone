<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.DataOutputStream"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
<%
	out.clear();				//https://rainny.tistory.com/269 참고
	out=pageContext.pushBody();
	OutputStream outputStream = response.getOutputStream();
	DataOutputStream dos = new DataOutputStream(outputStream);
	dos.writeUTF("로그인 실패");
	session.invalidate();
	dos.flush();
	dos.close();
%>

</body>
</html>