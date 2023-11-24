package chat.step3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChatDAO {
    private Connection conn;

    public ChatDAO() {
    	DBConnectionMgr dbConnectionMgr = new DBConnectionMgr();
        conn = dbConnectionMgr.getConnection();
    }

    public void saveChat(String chatContent) {
        String sql = "INSERT INTO chat_logs (content) VALUES (?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, chatContent);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
