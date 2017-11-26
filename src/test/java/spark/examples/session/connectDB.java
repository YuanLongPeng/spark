package spark.examples.session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class connectDB {
	

    public void connectDB() {
    	
    }
	
    public Connection getConnection() throws ClassNotFoundException, SQLException {
  	  Class.forName("oracle.jdbc.OracleDriver");  
  	  Connection conn = DriverManager.getConnection(  
  	    "jdbc:oracle:thin:@140.117.69.58:1521:ORCL", "GROUP01", "group11111");
  	  
  	  return conn;
    }
  
	   
	/*   Statement stmt = conn.createStatement();  
	     
	    ResultSet rset =  stmt  
	      .executeQuery("select * from AUTHOR");  
	    
	     while (rset.next())  
	      System.out.println(rset.getString(1)); // Print col 1  
	    
	    
	  rset.close();  
	  
	      
	    
	    conn.close();*/  

}
