<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.sql.*"
    pageEncoding="UTF-8"%>
<%@page import="java.io.EOFException"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="text.TextDeliver"%>
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
	String SUB_ID;
	String STU_IDs = null;
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	out.clear();
	out=pageContext.pushBody();
	String jdbc_driver = "com.mysql.cj.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://127.0.0.1/capstonedb";
	try{
		String lineEnd = "\r\n";
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));
		System.out.println("Student시작"+str);
		TextDeliver textDeliver = new TextDeliver(request,response);
        SUB_ID = textDeliver.GetText();
        Class.forName(jdbc_driver);
		conn = DriverManager.getConnection(jdbc_url,"capstone", "1q2w3e4r");
		String sub = "select stu_id, stu_name from student where stu_id IN (select stu_id from lecture where sub_id = " + SUB_ID + ")";
		pstmt = conn.prepareStatement(sub);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			String STU_ID = rs.getString(1);
			if (STU_IDs == null)
				STU_IDs = STU_ID;
			else
				STU_IDs = STU_IDs + STU_ID;
			String STU_NAME = rs.getString(2);
			textDeliver.append(STU_ID + "\t" + STU_NAME + lineEnd);
		}
		textDeliver.SendText();
		session.setAttribute("STU_IDs", STU_IDs);
	} catch (Exception e) {
		System.out.println(e);
	}
%>
</body>
</html>