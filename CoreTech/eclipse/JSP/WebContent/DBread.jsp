<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.sql.*"
    pageEncoding="UTF-8"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.DataOutputStream"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String jdbc_driver = "com.mysql.cj.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://127.0.0.1/capstonedb";
	String PRO_ID = "10001";
	String PWD = "10001";
	try{
		Class.forName(jdbc_driver);
		conn = DriverManager.getConnection(jdbc_url,"capstone", "1q2w3e4r");
		//String sql = "insert into lecture values(?,?)";
		String login = "select EXISTS (select * from PROFESSOR where PRO_ID = '" + PRO_ID + "' AND PWD = '" + PWD + "') as success";
		String sub = "SELECT SUB_ID, SUB_NAME FROM SUB";
		pstmt = conn.prepareStatement(login);
		rs = pstmt.executeQuery();
		int success=0;
		while(rs.next())
		{
			success = Integer.parseInt(rs.getString(1));
		}
		if (success == 1) {
			System.out.println("로그인 성공");
			pstmt = conn.prepareStatement(sub);
			rs = pstmt.executeQuery();
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
		}
		else
			System.out.println("로그인 실패");
	} catch (Exception e) {
		System.out.println(e);
	}
%>
</body>
</html>