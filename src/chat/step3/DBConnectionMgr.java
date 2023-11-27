package chat.step3;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnectionMgr {
	static DBConnectionMgr dbMGR = null;
	Connection con              = null;  // java.sql.Connection -> 특정 데이터베이스와의 연결
	PreparedStatement pstmt = null;  //java.sql.PreparedStatement -> 미리 컴파일된 SQL 문
	ResultSet rs                    = null;  //java.sql.ResultSet
	public static final String _DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String _URL= "jdbc:oracle:thin:@127.0.0.1:1523:orcl11";
	public static final String _USER = "scott";
	public static final String _PW = "tiger";
	static LeeServerThread lst = null;
	public DBConnectionMgr(LeeServerThread lst) {
	    DBConnectionMgr.lst = lst;
	}
	public DBConnectionMgr() {
	}
	/*정의메소드*/
	public static DBConnectionMgr getInstance() {
		if(dbMGR ==null) dbMGR = new DBConnectionMgr(lst);//전변에 대한 null 체크 후 객체를 생성함
		return dbMGR;
	}
	public Connection getConnection()//리턴타입이 인터페이스 -> 확장성 좋음, 결합도 낮춰짐
	{
		/*예외의 상황이 발생할 수 있음. 이를 어떻게 처리할지 미리 기입해두는 try catch
		 *멀티블록이 가능함. 단, 하위에서 상위클래스로 처리함 */
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");  //java.lang.Class<T> -> java reflection API 공부, F/W만들 수 있음. (https://jeongkyun-it.tistory.com/225)
			System.out.println("클래스 로딩 성공!!");
 			con = DriverManager.getConnection(_URL,_USER,_PW); //파라미터값(String url, String user, String password)
 			System.out.println("데이터베이스 연결 성공!!");
		}catch (ClassNotFoundException e) { //클래스 못 찾을 때 
			System.out.println("ojdbc6.jar를 설정하지 않았다. 그래서 클래스를 못 찾는다.");
		}catch (Exception e) {                     //비번이 맞지 않을 때 
			e.printStackTrace();
		}
		return con;
	}
	
	public static void freeConnection(Connection con) {
	    try {
	        if (con != null)
	            con.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public static void freeConnection(PreparedStatement pstmt, Connection con) {
	    try {
	        if (pstmt != null)
	            pstmt.close();
	        if (con != null)
	            con.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public static void freeConnection(CallableStatement cstmt, Connection con){
		try {
			if(cstmt !=null) cstmt.close();
			if(con !=null) con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}



