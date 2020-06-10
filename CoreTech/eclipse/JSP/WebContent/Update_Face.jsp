<%@page import="java.io.File"%>
<%@page import="java.util.Date"%>
<%@page import="java.io.IOException"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="text.TextDeliver"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>
	<%
		//이미지를 저장할 경로 입력.
		if (session.getAttribute("PRO_ID") == null) {
		    response.sendRedirect("Logout.jsp");
		    return;
		}
		String jdbc_driver = "com.mysql.cj.jdbc.Driver";
		String jdbc_url = "jdbc:mysql://127.0.0.1/capstonedb";
		request.setCharacterEncoding("utf-8");
		String folderTypePath = "D:/Study/Capstone/CoreTech";
		String name = new String();
		String fileName = new String();
		String STU_ID = new String();
		int sizeLimit = 20 * 1024 * 1024; // 20메가까지 제한 넘어서면 예외발생
		try {
			long time = System.currentTimeMillis(); 
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String str = dayTime.format(new Date(time));
			System.out.println("얼굴변경 "+str);
			MultipartRequest multi = new MultipartRequest(request, folderTypePath, sizeLimit, new DefaultFileRenamePolicy());
			Enumeration files = multi.getFileNames();

			//파일 정보가 있다면
			if (files.hasMoreElements()) {
				name = (String) files.nextElement();
				fileName = multi.getFilesystemName(name);
			}
			System.out.println("이미지를 저장하였습니다. : " + fileName);
			out.clear();
			out = pageContext.pushBody();
			TextDeliver textDeliver = new TextDeliver(request,response);
			STU_ID = textDeliver.GetText();
			Runtime runtime = Runtime.getRuntime();
			System.out.println("PYTHON 시작");
			String command = "conda run -n tf python "+folderTypePath+"/PyDlibCropAlign_One.py "+folderTypePath+" "+fileName;
			System.out.println(command);
			Process process = runtime.exec(command);
			process.waitFor();
			textDeliver.SendText("완료");
			process.destroy();
		} catch (IOException e) {
			System.out.println("실패!");
		}
	%>
</body>
</html>