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
		request.setCharacterEncoding("utf-8");
		String folderTypePath = "D:/Study/Capstone/PyAdsChange";
		String name = new String();
		String fileName = new String();
		int sizeLimit = 20 * 1024 * 1024; // 20메가까지 제한 넘어서면 예외발생
		try {
			long time = System.currentTimeMillis(); 
			SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String str = dayTime.format(new Date(time));
			System.out.println("Ads시작 "+str);
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
			
			Runtime runtime = Runtime.getRuntime();
			System.out.println("PYTHON 시작");
			String command = "conda run -n tf python "+folderTypePath+"/testing.py " + folderTypePath + " " + fileName;
			System.out.println(command);
			Process process = runtime.exec(command);
			process.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s;
			TextDeliver textDeliver = new TextDeliver(request,response);
			while ((s = br.readLine())!= null) {
				if(s.length() != 0){
					textDeliver.append(s);
				}
			}
			textDeliver.SendText();
			br.close();
			process.destroy();
		} catch (IOException e) {
			System.out.println("실패!");
		}
	%>
</body>
</html>