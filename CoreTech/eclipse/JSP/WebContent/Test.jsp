<%@page import="java.io.File"%>
<%@page import="java.util.Date"%>
<%@page import="java.io.IOException"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.text.SimpleDateFormat"%>
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
		request.setCharacterEncoding("utf-8");
		String folderTypePath = "D:/Study/Capstone/CoreTech";
		String name = new String();
		String fileName = new String();
		int sizeLimit = 20 * 1024 * 1024; // 5메가까지 제한 넘어서면 예외발생
		try {
			long time = System.currentTimeMillis(); 
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String str = dayTime.format(new Date(time));
			System.out.println("연결시작 "+str);
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
			OutputStream outputStream = response.getOutputStream();
			DataOutputStream dos = new DataOutputStream(outputStream);
			Runtime runtime = Runtime.getRuntime();
			System.out.println("PYTHON 시작");
			Process process = runtime.exec("conda run -n tf python "+folderTypePath+"/testing.py "+folderTypePath+" "+fileName+" "+session.getAttribute("PRO_ID"));
			process.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s;
			while ((s = br.readLine())!= null) {
				if (s.length()>10) {
					dos.writeUTF(s.substring(s.length()-10, s.length()-4));
					System.out.println(s.substring(s.length()-10, s.length()-4));
				} else if(s.length() != 0){
					dos.writeUTF(s);
					System.out.println(s);
				}
			}
			dos.close();
			outputStream.close();
			br.close();
			process.destroy();
		} catch (IOException e) {
			System.out.println("실패!");
		}
	%>
</body>
</html>