<%@page import="java.io.File"%>
<%@page import="java.io.IOException"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>

	<%
		//이미지를 저장할 경로 입력.
		String folderTypePath = request.getRealPath("/uploadFile");
		String name = new String();
		String fileName = new String();
		int sizeLimit = 5 * 1024 * 1024; // 5메가까지 제한 넘어서면 예외발생
		try {
			MultipartRequest multi = new MultipartRequest(request, folderTypePath, sizeLimit, new DefaultFileRenamePolicy());
			Enumeration files = multi.getFileNames();

			//파일 정보가 있다면
			if (files.hasMoreElements()) {
				name = (String) files.nextElement();
				fileName = multi.getFilesystemName(name);
			}
			System.out.println("이미지를 저장하였습니다. : " + fileName);
		} catch (IOException e) {
			out.println("안드로이드 부터 이미지를 받아옵니다.");
		}
	%>

</body>
</html>