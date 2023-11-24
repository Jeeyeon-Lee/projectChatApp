package chat.step3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ChatLoginService {
    private Connection conn;

    public ChatLoginService() {
    	DBConnectionMgr dbConnectionMgr = new DBConnectionMgr();
        conn = dbConnectionMgr.getConnection();
    }

    // 회원가입 메소드
    public void registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try {
        	PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            JOptionPane.showInputDialog(this, "회원가입이 완료되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 로그인 메소드 
    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try {
        	PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // 일치하는 사용자가 존재하면 true, 그렇지 않으면 false 반환
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getChatLogs() {
        String sql = "SELECT * FROM chat_logs";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String content = rs.getString("content");
                System.out.println(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
