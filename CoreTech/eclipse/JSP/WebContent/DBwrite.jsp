<%@ page language="java" contentType="text/html; charset=EUC-KR" import="java.sql.*"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>
<%
	Connection conn = null;
	PreparedStatement pstmt = null;
	String jdbc_driver = "com.mysql.cj.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://127.0.0.1/capstonedb";
	String[] v1 = {};
	String[] v2 = {};
	String[] v3 = {};
	String[] v4 = {};
	
	try{
		Class.forName(jdbc_driver);
		conn = DriverManager.getConnection(jdbc_url,"capstone", "1q2w3e4r");
		String sql = "insert into lecture values(?,?)";
		pstmt = conn.prepareStatement(sql);
		for(int i = 0; i< v1.length; i++) {
			pstmt.setString(1,v1[i]);
			pstmt.setString(2,v2[i]);
			pstmt.setString(3,v3[i]);
			pstmt.setString(4,v4[i]);
			pstmt.executeUpdate();
		}
		
	} catch (Exception e) {
		System.out.println(e);
	}
%>
</body>
</html>