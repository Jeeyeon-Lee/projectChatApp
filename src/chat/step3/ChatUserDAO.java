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

    public void createUser(String id, String password) {
        String sql = "INSERT INTO user (아이디, 패스워드) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.println("statement 생성 성공");
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUser(String id, String password) {
        String sql = "SELECT * FROM user WHERE id = ? AND password = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.println("statement 생성 성공");
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // 일치하는 사용자가 존재하면 true, 그렇지 않으면 false 반환
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
