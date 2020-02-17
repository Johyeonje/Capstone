package connection;
import java.sql.*;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Vision_Board{
   private static Vision_Board vision_board = new Vision_Board();

   public static Vision_Board getVision_Board() {
      return vision_board;
   }

   private String returns;
   private Connection con = null;
   private PreparedStatement pstmt = null;
   private ResultSet rs = null;

   public String select() {
      try {
        returns ="";
         Context init = new InitialContext();
         DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
         con = ds.getConnection();
         String query = "SELECT * FROM MARKET_VISION ORDER BY NUM DESC";
         pstmt = con.prepareStatement(query);
         rs = pstmt.executeQuery();
         
         while(rs.next()) {
            returns +=rs.getString("DAY")+"\t"+rs.getString("CONTENT")+"\t";
         } // end while
      } catch (Exception e) {
         e.printStackTrace();
      } // end try~catch

      finally {
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
             if (con != null)
                try {
                   con.close();
                } catch (SQLException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
             }
      }
      return returns;
   }// end select()

}