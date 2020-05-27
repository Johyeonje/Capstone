<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.sql.*"
    pageEncoding="UTF-8"%>
   <%@page import="java.io.InputStream"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="java.io.EOFException"%>
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
	String SUB_ID;
	String STU_IDs = null;
	StringBuffer b;
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
		InputStream inputStream = request.getInputStream();
		DataInputStream dis = new DataInputStream(inputStream);
        b = new StringBuffer();
        try {
            for (String ch; (ch = dis.readUTF()) != null;)  {
                b.append(ch);
                b.append(lineEnd);
            }
        } catch (EOFException e) {
            b.delete(b.length()-lineEnd.length(),b.length());
        }
        b.append(lineEnd);
        System.out.print(b.toString());
        SUB_ID = b.toString();
        Class.forName(jdbc_driver);
		conn = DriverManager.getConnection(jdbc_url,"capstone", "1q2w3e4r");
		String sub = "select stu_id, stu_name from student where stu_id IN (select stu_id from lecture where sub_id = " + SUB_ID + ")";
		pstmt = conn.prepareStatement(sub);
		rs = pstmt.executeQuery();
		OutputStream outputStream = response.getOutputStream();
		DataOutputStream dos = new DataOutputStream(outputStream);
		while (rs.next()) {
			String STU_ID = rs.getString(1);
			if (STU_IDs == null)
				STU_IDs = STU_ID;
			else
				STU_IDs = STU_IDs + STU_ID;
			String STU_NAME = rs.getString(2);
			dos.writeUTF(STU_ID + "\t" + STU_NAME);
		}
		dos.flush();
		dos.close();
		outputStream.close();
		session.setAttribute("STU_IDs", STU_IDs);
	} catch (Exception e) {
		System.out.println(e);
	}
%>
</body>
</html>