package text;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;


public class TextDeliver {
	
	private StringBuffer b = new StringBuffer();
	private HttpServletRequest request;
	private HttpServletResponse response;
	public TextDeliver(HttpServletRequest request, HttpServletResponse response) { //�ؽ�Ʈ ����
		this.request = request;
		this.response = response;
	}
	
	public String GetText() { // �ؽ�Ʈ ����
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
	            b.delete(b.length()-lineEnd.length(),b.length());
	        }
	        System.out.println("���� ���� : \r\n" + b.toString());
		} catch (Exception e) {
			return e.toString();
		}
        return b.toString();
	}
	
	public void append(String text) { // �ؽ�Ʈ ����
		b.append(text);
	}
	
	public void SendText() { // ����� �ؽ�Ʈ �۽�
		try {
			DataOutputStream dos = new DataOutputStream(response.getOutputStream());
			if (b.substring(b.length()-2, b.length()).equals("\r\n")) { // �� �� �ٹٲ� ����
				b.delete(b.length()-2, b.length());
			}
			dos.writeUTF(b.toString());
			System.out.println("���� ���� : \r\n" + b.toString());
			dos.flush();
			dos.close();
		} catch (Exception e) {
			
		}
	}
	
	public void SendText(String text) { // ����� �ؽ�Ʈ �۽�
        try {
            DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            if (text.substring(text.length()-2, text.length()).equals("\r\n")) { // �� �� �ٹٲ� ����
                text = text.substring(0, text.length()-2);
            }
            dos.writeUTF(text);
            System.out.println("���� ���� : \r\n" + text);
            dos.flush();
            dos.close();
        } catch (Exception e) {

        }
    }
}
