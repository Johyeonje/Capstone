package connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Vision_Write {
	private static Vision_Write vision = new Vision_Write();

	public static Vision_Write getWrite() {
		return vision;
	}
	private String returns = "";
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null;

	public String write(String content) {
	    try {
		Context init = new InitialContext();
		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		conn = ds.getConnection();

		Statement stmt = conn.createStatement();
		String seq = "select max(num) from market_vision";
		ResultSet rs = stmt.executeQuery(seq);

		int num = -1;
		if (rs.next())
			num = rs.getInt(1);
		num++;

		String nowTime = getCurrentTime("YYYY,M,d");
		System.out.println(nowTime);

		System.out.println("시간확인" + nowTime);

		String sql = "insert into MARKET_VISION(num,day,content)
                              values('"+ num +"','" + nowTime + "','" + content + "')"; <%-  SQL입력하면됨.-%>
		pstmt = conn.prepareStatement(sql);
		pstmt.executeUpdate();
		returns = "success"; <%-입력이 완료 됬으면 success를 android로 보냄-%>

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
	    if (pstmt != null)
		try {
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if (rs != null)
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if (conn != null)
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return returns;
       }
        public static String getCurrentTime(String timeFormat) {
	       return new SimpleDateFormat(timeFormat).format(System.currentTimeMillis());
        }
}