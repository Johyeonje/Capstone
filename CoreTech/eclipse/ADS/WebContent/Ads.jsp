<%@page import="java.io.File"%>
<%@page import="java.util.Date"%>
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
		out.clear();
		out=pageContext.pushBody();
		String Path = "D:/Study/Capstone/CoreTech/Image/";
		String WASPath = "D:/Study/Capstone/CoreTech/eclipse/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/ADS/Image/";
		String name = new String();
		String s1 = "8 0 0 0 0 0 0 0 0 0 ";
		String s2 = "0 1 1 2 0 0 3 1 1 0 ";
		String s3 = "0 1 2 4 1 0 2 2 1 0 ";
		String error = "9 9 9 9 9 9 9 9 9 9 ";
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String str = dayTime.format(new Date(time));
		System.out.println("Ads시작 "+str);
		TextDeliver textDeliver = new TextDeliver(request,response);
		name = textDeliver.GetText();
		if (name.length() > 5) {
			if (name.substring(name.length()-4, name.length()).equals(".jpg")) {
				name = name.substring(name.length()-5, name.length()-4);
	        }
		}
		switch (Integer.parseInt(name)) {
			case 1:
				textDeliver.copyFile(Path + "1.jpg", WASPath + "IMG.jpg");
				textDeliver.SendText(s1);
				break;
			case 2:
				textDeliver.copyFile(Path + "2.jpg", WASPath + "IMG.jpg");
				textDeliver.SendText(s2);
				break;
			case 3:
				textDeliver.copyFile(Path + "3.jpg", WASPath + "IMG.jpg");
				textDeliver.SendText(s3);
				break;
			default:
				textDeliver.SendText(error);
		}
	%>
</body>
</html>