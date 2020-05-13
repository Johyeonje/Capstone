<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.DataInputStream"%>
<%@page import="java.io.EOFException"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.StringTokenizer" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>
	<%
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String jdbc_driver = "com.mysql.cj.jdbc.Driver";
		String jdbc_url = "jdbc:mysql://127.0.0.1/capstonedb";
		String PRO_ID = null, PWD = null;
		//이미지를 저장할 경로 입력.
		request.setCharacterEncoding("utf-8");
		StringBuffer b;
		try {
			String lineEnd = "\r\n";
			long time = System.currentTimeMillis(); 
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String str = dayTime.format(new Date(time));
			System.out.println("login시작"+str);
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
            b.append("\r\n");
            StringTokenizer st = new StringTokenizer(b.toString());
            PRO_ID = st.nextToken();
            PWD = st.nextToken();
            inputStream.close();
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("실패!");
		}

		try{
			String lineEnd = "\r\n";
			long time = System.currentTimeMillis(); 
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String str = dayTime.format(new Date(time));
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
				session.setAttribute("PRO_ID", PRO_ID);
				session.setAttribute("PWD", PWD);
				System.out.println((String)session.getAttribute("PRO_ID"));
				System.out.println((String)session.getAttribute("PWD"));
				System.out.println(session.getId());
				Cookie c = new Cookie("Session_ID",session.getId());
				c.setMaxAge(60*5);
				c.setPath("/");
				response.addCookie(c);
			}
			else {
				System.out.println("로그인 실패");
				System.out.println(PRO_ID + "\n" + PWD);
				response.sendRedirect("Logout.jsp");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	%>
</body>
</html>