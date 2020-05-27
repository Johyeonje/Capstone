<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.sql.*"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="text.TextDeliver" %>
<!DOCTYPE html>
<html>
<body>
<%
	if (session.getAttribute("PRO_ID") == null) {
	    response.sendRedirect("Logout.jsp");
	    return;
	}
	String PRO_ID = (String)session.getAttribute("PRO_ID");
	String PWD = (String)session.getAttribute("PWD");
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	TextDeliver textDeliver = new TextDeliver(request, response);
	String jdbc_driver = "com.mysql.cj.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://127.0.0.1/capstonedb";
	try{
		String lineEnd = "\r\n";
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));
		System.out.println("Subject시작"+str);
		Class.forName(jdbc_driver);
		conn = DriverManager.getConnection(jdbc_url,"capstone", "1q2w3e4r");
		//String sql = "insert into lecture values(?,?)";
		String sub = "SELECT SUB_ID, SUB_NAME FROM SUB WHERE PRO_ID = " + PRO_ID;
		pstmt = conn.prepareStatement(sub);
		rs = pstmt.executeQuery();
		out.clear();
		out=pageContext.pushBody();
		while (rs.next()) {
			String SUB_ID = rs.getString(1);
			String SUB_NAME = rs.getString(2);
			textDeliver.append(SUB_ID + "\t" + SUB_NAME + lineEnd);
		}
		textDeliver.SendText();
	} catch (Exception e) {
		System.out.println(e);
	}
%>
</body>
</html>