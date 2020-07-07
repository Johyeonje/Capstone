package text;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


public class TextDeliver {
	
	private StringBuffer b = new StringBuffer();
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public TextDeliver(HttpServletRequest request, HttpServletResponse response) { //텍스트 수신
		this.request = request;
		this.response = response;
	}
	
	public String GetText() { // 텍스트 수신
		StringBuffer b;
		try {
			String lineEnd = "\r\n";
			DataInputStream dis = new DataInputStream(request.getInputStream());
	        b = new StringBuffer();
	        try {
	            for (String ch; (ch = dis.readUTF()) != null;)  {
	                b.append(ch);
	                b.append(lineEnd);
	            }
	        } catch (EOFException e) {
	        	if(b.length()>1)
	        		b.delete(b.length()-lineEnd.length(),b.length());
	        }
	        System.out.println("받은 문자 : \r\n" + b.toString());
		} catch (Exception e) {
			return e.toString();
		}
        return b.toString();
	}
	
	public void append(String text) { // 텍스트 저장
		b.append(text);
	}
	
	public void SendText() { // 저장된 텍스트 송신
		try {
			DataOutputStream dos = new DataOutputStream(response.getOutputStream());
			if (b.substring(b.length()-2, b.length()).equals("\r\n")) { // 맨 끝 줄바꿈 제거
				b.delete(b.length()-2, b.length());
			}
			dos.writeUTF(b.toString());
			System.out.println("보낸 문자 : \r\n" + b.toString());
			dos.flush();
			dos.close();
		} catch (Exception e) {
			
		}
	}
	
	public void SendText(String text) { // 텍스트 송신
        try {
            DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            if (text.substring(text.length()-2, text.length()).equals("\r\n")) { // 맨 끝 줄바꿈 제거
                text = text.substring(0, text.length()-2);
            }
            dos.writeUTF(text);
            System.out.println("보낸 문자 : \r\n" + text);
            dos.flush();
            dos.close();
        } catch (Exception e) {

        }
    }
	
	public void copyFile(String filename, String newFilename) throws IOException {
	    File file = new File( filename );
	    File fileNew = new File( newFilename );
	    System.out.println(filename);
	    System.out.println(newFilename);
	    FileInputStream inputStream = new FileInputStream(file);        
	    FileOutputStream outputStream = new FileOutputStream(fileNew);
	      
	    FileChannel fcin =  inputStream.getChannel();
	    FileChannel fcout = outputStream.getChannel();
	      
	    long size = fcin.size();
	    fcin.transferTo(0, size, fcout);
	      
	    fcout.close();
	    fcin.close();
	      
	    outputStream.close();
	    inputStream.close();
	}
}
