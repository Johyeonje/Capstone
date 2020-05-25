<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.sql.*"
    pageEncoding="UTF-8"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
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
		OutputStream outputStream = response.getOutputStream();
		DataOutputStream dos = new DataOutputStream(outputStream);
		while (rs.next()) {
			String SUB_ID = rs.getString(1);
			String SUB_NAME = rs.getString(2);
			dos.writeUTF(SUB_ID + "\t" + SUB_NAME);
		}
		dos.flush();
		dos.close();
		outputStream.close();
	} catch (Exception e) {
		System.out.println(e);
	}
%>
</body>
</html>