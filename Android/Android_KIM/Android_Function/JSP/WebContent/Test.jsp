<%@page import="java.io.File"%>
<%@page import="java.io.IOException"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		//이미지를 저장할 경로 입력.
		request.setCharacterEncoding("utf-8");
		
		String folderTypePath = "D:/Study/Capstone/DoNotTouch";
		String name = new String();
		String a1 = new String();
		a1 = "과목1";
		String fileName = new String();
		String subject = request.getParameter("Class_FileInputStream");
		String Person[] = {"man1", "man2", "man2", "man2", "man2", "man2", "man2", "man2", "man2", "man2"};
		
		int sizeLimit = 20 * 1024 * 1024; // 5메가까지 제한 넘어서면 예외발생
		try {
			System.out.println("연결시작 " + fileName);
			MultipartRequest multi = new MultipartRequest(request, folderTypePath, sizeLimit, new DefaultFileRenamePolicy());
			Enumeration files = multi.getFileNames();

			//파일 정보가 있다면
			if (files.hasMoreElements()) {
				name = (String) files.nextElement();
				fileName = multi.getFilesystemName(name);
			}
			if(subject.equals(a1))
			{
				System.out.println("다음 정보가 왔습니다" + subject );
			}
			System.out.println("이미지를 저장하였습니다. : " + fileName);
			out.clear();
			out = pageContext.pushBody();
			OutputStream outputStream = response.getOutputStream();
			DataOutputStream dos = new DataOutputStream(outputStream);
			for (String s : Person) {
				dos.writeUTF(s); // 다음은 s를 android로 쏴주는 함수.
			}
			dos.close(); // 다 사용하고 닫는다.
			outputStream.close();
			
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("conda run -n dlib python "+folderTypePath+"/face.py "+folderTypePath+" "+fileName);
		} catch (IOException e) {
			System.out.println("실패!");
		}
	%>

</body>
</html>