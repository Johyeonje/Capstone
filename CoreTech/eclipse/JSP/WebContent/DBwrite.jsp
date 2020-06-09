<%@ page language="java" contentType="text/html; charset=EUC-KR" import="java.sql.*"
    pageEncoding="UTF-8"%>
<%@page import="text.TextDeliver"%>
<!DOCTYPE html>
<html>
<body>
<%
	if (session.getAttribute("PRO_ID") == null) {
	    response.sendRedirect("Logout.jsp");
	    return;
	}
	Connection conn = null;
	PreparedStatement pstmt = null;
	String jdbc_driver = "com.mysql.cj.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://127.0.0.1/capstonedb";
	String SUB_ID = null;
	String STU_ID = null;
	String FLAG = null;
	String[] Received = null;
	TextDeliver textDeliver = new TextDeliver(request, response);
	Received = textDeliver.GetText().split("\r\n");
	
	try{
		Class.forName(jdbc_driver);
		conn = DriverManager.getConnection(jdbc_url,"capstone", "1q2w3e4r");
		String sql = "update lecture set FLAG = ? where SUB_ID = ? AND STU_ID = ?";
		pstmt = conn.prepareStatement(sql);
		for(String s:Received) {
			String[] q = s.split("	");
			pstmt.setString(1, q[2]);
			pstmt.setString(2, q[0]);
			pstmt.setString(3, q[1]);
			System.out.println(pstmt);
			pstmt.executeUpdate();
		}
	} catch (Exception e) {
		System.out.println(e);
	}
%>
</body>
</html>