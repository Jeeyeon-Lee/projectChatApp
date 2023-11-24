package chat.step3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatUserDAO {
    private Connection conn;

    public ChatUserDAO() {
    	DBConnectionMgr dbConnectionMgr = new DBConnectionMgr();
        conn = dbConnectionMgr.getConnection();
    }

    public void createUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // 일치하는 사용자가 존재하면 true, 그렇지 않으면 false 반환
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
