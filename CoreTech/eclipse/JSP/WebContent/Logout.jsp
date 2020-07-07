<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="text.TextDeliver"%>
<!DOCTYPE html>
<html>
<body>
<%
	out.clear();				//https://rainny.tistory.com/269 참고
	out=pageContext.pushBody();
	TextDeliver textDeliver = new TextDeliver(request,response);
	textDeliver.SendText("LOGOUT");
	session.invalidate();
%>
</body>
</html>