package chat.step3;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class LeeClient extends JFrame implements ActionListener{
	
	// 선언부
	Socket socket = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;
	String nickname = null;
	JPanel jp = new JPanel();
	JPanel jp_south = new JPanel();
	JTextField jtf_msg = new JTextField(20);
	JButton jbtn_send = new JButton("전송");	
	//JButton jbtn_exit = new JButton("나가기");
    JTextArea jta_display = new JTextArea(); // JTextArea를 멤버 변수로 선언하고 초기화합니다.
	JScrollPane jsp = null;
	
	
	// 생성자
	public LeeClient() {
		jtf_msg.addActionListener(this);
		jbtn_send.addActionListener(this);		
	}
	// 메소드
	public void initDisplay() {
		nickname = getNicknameFromDatabase(); // 오라클에서 닉네임 조회
		jp.setLayout(new BorderLayout());
		jp_south.setLayout(new BorderLayout());
		jp_south.add("Center",jtf_msg);
		jp_south.add("East",jbtn_send);
        jta_display.setLineWrap(true); // JTextArea의 속성을 설정합니다.
		jta_display.setLineWrap(true);
		jta_display.setOpaque(false);
		Font font = new Font("굴림체",Font.BOLD,16);
		jta_display.setFont(font);
		jsp = new JScrollPane(jta_display);
		jp.add("Center",jsp);
		jp.add("South",jp_south);
		this.setSize(500, 600);
		this.setVisible(true);
		this.setTitle(nickname);
		this.add(jp);		
	}
	
    public void init() {
        try {
            socket = new Socket("192.168.35.246", 1004);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject(100 + "," + nickname);
            LeeClientThread lct = new LeeClientThread(this);
            lct.start();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        initDisplay(); // initDisplay() 메소드를 init() 메소드의 마지막에 호출합니다.
    }
 // 오라클에서 닉네임 조회 메소드
    public String getNicknameFromDatabase() {
        String nickname = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 오라클 DB 연결 설정
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@127.0.0.1:1523:orcl11";
            String username = "scott";
            String password = "tiger";
            conn = DriverManager.getConnection(url, username, password);

            // 닉네임 조회 쿼리 실행
            String sql = "SELECT * FROM \"user\" WHERE \"아이디\" = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "사용자 아이디");
            rs = pstmt.executeQuery();

            // 결과 처리
            if (rs.next()) {
                nickname = rs.getString("닉네임");
                System.out.println(nickname);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 해제
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return nickname;
    }
	public static void main(String[] args) {
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    LeeClient lc = new LeeClient();
	    ChatLoginForm lf = new ChatLoginForm();
	    lc.init();
	    // lc.initDisplay(); // 첫 번째 호출은 주석 처리합니다.
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();
		String msg = jtf_msg.getText();
		
		if((obj == jtf_msg) ||(obj == jbtn_send)) {
			try {
				oos.writeObject(200 + "," + nickname + "," + msg);
				jtf_msg.setText("");				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}
}
