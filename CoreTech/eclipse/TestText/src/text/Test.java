package text;

public class Test {
	public static void main(String[] args) {
		String Recieved = "20001	201221892	0\r\n" + 
				"20001	201421927	0\r\n" + 
				"20001	201421936	0\r\n" + 
				"20001	201521889	0";
		String[] Recieved2;
		
		System.out.println(Recieved);
		
		Recieved2 = Recieved.split("\r\n");
		
		for(String s:Recieved2) {
			System.out.println(s);
		}
		for(String s:Recieved2) {
			String[] q = s.split("	");
			for(String p:q) {
				System.out.println(p);
			}
		}
	}
}
