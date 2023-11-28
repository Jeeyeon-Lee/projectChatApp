
package chat.step6;

import java.awt.Color;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import chat.step3.DBConnectionMgr;
import chat.step3.LeeClient;

public class LeeServer extends Thread{
	/*선언부*/
//	명단 리스트
	List<LeeServerThread> globalList = null;
//	서버 관련
	ServerSocket server = null;
	Socket socket = null;
	
//	화면 관련
	JFrame jf = new JFrame();
	JTextArea jta_log = new JTextArea(10,60);
	JScrollPane jsp_log = new JScrollPane(jta_log
			 , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
			 , JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//	DB 관련
	DBConnectionMgr dbMgr = new DBConnectionMgr();
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	/*정의메소드*/
	//포트연결, 대기, 소켓연결 시 서버스레드 생성 및 시작
	@Override
	public void run() {
		globalList = new Vector<>();
		boolean isStop = false;
		try {
			server = new ServerSocket(1004);
			jta_log.append(getTime()+" | Server Ready.........\n");
            jta_log.append(getTime() + " | client 연결 요청 대기 중...\n");
			while(!isStop) {
				socket = server.accept();
				jta_log.append(getTime()+" | client 접속 :"+socket.getInetAddress()+"\n");	
				LeeServerThread lst = new LeeServerThread(this);
				lst.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//시간표시 메소드
    public String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date());
    }
    //창 구현 메소드
	public void initDisplay() {
		jta_log.setLineWrap(true);
		jf.setBackground(Color.orange);
		jf.add("Center", jsp_log);
		jf.setTitle("서버측 로그 출력화면 제공...");
		jf.setSize(600, 300);
		jf.setVisible(true);
		jf.setLocation(0, 100);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//DB에서 회원 정보 가져오기
	public List<Map<String,Object>> getUserList(){
		List<Map<String,Object>> uList = null;
		StringBuilder sql = new StringBuilder();
		try {
			sql.append("SELECT id, password, nickname FROM USERS");
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			uList = new ArrayList<>();
			Map<String,Object> rmap =  null;
			while(rs.next()) {
				rmap = new HashMap<>();
				rmap.put("id", rs.getString("id"));
				rmap.put("password", rs.getString("password"));
				rmap.put("nickname", rs.getString("nickname"));
				uList.add(rmap);
			}
		} catch (SQLException se) {
			System.out.println(se.toString());
		    se.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.toString());
		    e.printStackTrace();
		}
		System.out.println(uList);
		return uList;
	}
	// 로그인 메소드
	public boolean login(String id, String password) {
	    StringBuilder sql = new StringBuilder();
	    try {
	        sql.append("SELECT * FROM USERS WHERE ID = ? AND PASSWORD = ?");
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
	        pstmt.setString(1, id);
	        pstmt.setString(2, password);
			rs = pstmt.executeQuery();
	        if (rs.next()) {
	            if (rs.getString("ID").equals(id) && rs.getString("PASSWORD").equals(password)) {
	            System.out.println("로그인 성공!!");
	            return true;
	        	}
	        }
	    } catch (SQLException se) {
			System.out.println(se.toString());
		    se.printStackTrace();
	    } finally {
	        // 리소스 해제
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}
	
	// 회원가입 메소드
		public void join(String id, String password) {
		    StringBuilder sql = new StringBuilder();
		    try {
		    	sql.append("INSERT INTO USERS (ID, PASSWORD) VALUES (?, ?)");
				con = dbMgr.getConnection();
				pstmt = con.prepareStatement(sql.toString());
		        pstmt.setString(1, id);
		        pstmt.setString(2, password);
		        int result = pstmt.executeUpdate();
		        if (result > 0) {
		            JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        // 리소스 해제
		        try {
		            if (pstmt != null) pstmt.close();
		            if (con != null) con.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}
	/*메인메소드 객체 생성, 시작, 창구현*/
	public static void main(String[] args) {
		LeeServer cs = new LeeServer();
		System.out.println(cs);
		cs.initDisplay();
		cs.getUserList();
		cs.start();
		cs.login("지연", "123");
	}
}
