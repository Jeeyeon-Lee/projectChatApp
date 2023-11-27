package chat.step3;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ChatLoginForm extends JFrame implements ActionListener {
    String nickname = ""; // nickname 변수 선언
	String imgPath="src/chat/step3/image/";
	JLabel jlb_id = new JLabel("아이디");
	JLabel jlb_pw = new JLabel("패스워드");
	Font jl_font = new Font("휴먼매직체", Font.BOLD, 17);
	JTextField jtf_id = new JTextField("test");
	JPasswordField jpf_pw = new JPasswordField("123");
	JButton jbtn_login = new JButton(
			new ImageIcon(imgPath+"login.png"));
	JButton jbtn_join = new JButton(
			new ImageIcon(imgPath+"confirm.png"));
	ImageIcon ig = new ImageIcon(imgPath+"main.PNG");
    private Connection conn = null;

	public ChatLoginForm(){
	    initDisplay();
	    }
	class mypanal extends JPanel {
		public void paintComponent(Graphics g) {
			g.drawImage(ig.getImage(), 0, 0, null);
			setOpaque(false);
			super.paintComponents(g);
		}
	}
	
	//첫 로그인 창
	public void initDisplay() {
		setContentPane(new mypanal());
		jbtn_join.addActionListener(this);
		jbtn_login.addActionListener(this);
		this.setLayout(null);
		this.setTitle("자바채팅 ver.1");
		this.setSize(350, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(800, 250);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		jlb_id.setBounds(45, 200, 80, 40);
		jlb_id.setFont(jl_font);
		jtf_id.setBounds(110, 200, 185, 40);
		this.add(jlb_id);
		this.add(jtf_id);
		jlb_pw.setBounds(45, 240, 80, 40);
		jlb_pw.setFont(jl_font);
		jpf_pw.setBounds(110, 240, 185, 40);
		this.add(jlb_pw);
		this.add(jpf_pw);
		jbtn_login.setBounds(175, 285, 120, 40);
		this.add(jbtn_login);
		jbtn_join.setBounds(45, 285, 120, 40);
		this.add(jbtn_join);		
	}
	//두번째 닉네임 입력창 
	
	public static void main(String[] args) throws Exception {
		new ChatLoginForm();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
	    Object obj = e.getSource();
	    if (obj == jbtn_join) {
	        System.out.println("가입버튼 클릭");
	        String mem_id = jtf_id.getText();
	        char[] mem_pwd = jpf_pw.getPassword();
	        if ("".equals(mem_id) || mem_pwd.length == 0) {
	            JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 확인하세요.", "경고", 2);
	            return;
	        }
	        join(mem_id, new String(mem_pwd));
	        // char 배열로 가져온 비밀번호를 String으로 변환하여 사용
	    } else if (obj == jbtn_login) {
	        System.out.println("로그인버튼 클릭");
	        if ("".equals(jtf_id.getText()) || jpf_pw.getPassword().length == 0) {
	            JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 확인하세요.", "경고", 2);
	            return;
	        }
	        try {
	            String mem_id = jtf_id.getText();
	            char[] mem_pwd = jpf_pw.getPassword();
	            if (login(mem_id, new String(mem_pwd))) {
	                JOptionPane.showMessageDialog(this, mem_id + "님의 접속을 환영합니다.");
	                this.setVisible(false);
	                jtf_id.setText("");
	                jpf_pw.setText("");
	                //new WaitingRoom(this);
	            } else {
	                JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 확인하세요.", "경고", 2);
	            }
	            // char 배열로 가져온 비밀번호를 String으로 변환하여 사용
	        } catch (Exception ae) {
	            ae.printStackTrace();
	        }
	    }
	}
	
	// Connection 객체를 생성하여 반환하는 메소드
	private Connection getConnection() {
	    Connection connection = null;
	    try {
	        Class.forName(DBConnectionMgr._DRIVER);
	        connection = DriverManager.getConnection(DBConnectionMgr._URL, DBConnectionMgr._USER, DBConnectionMgr._PW);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return connection;
	}	

	// 회원가입 메소드
    public void join(String username, String password) {
	    String sql = "INSERT INTO \"user\" (\"아이디\", \"패스워드\") VALUES (?, ?)";
	    try {
	        conn = getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, username);
	        pstmt.setString(2, password);
	        pstmt.executeUpdate();
	        JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.");
	        jtf_id.setText("");
	        jpf_pw.setText("");

	        // 닉네임 조회
	        LeeClient lc = new LeeClient(nickname); // 닉네임 전달
	        String nickname = lc.getNicknameFromDatabase(); // 중복 변수 선언 제거
	        if (nickname != null) {
	            lc.init();
	            this.setVisible(false);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBConnectionMgr.freeConnection(conn);
	    }
	}

	// 로그인 메소드
	public boolean login(String username, String password) {
	    String sql = "SELECT * FROM \"user\" WHERE \"아이디\" = ? AND \"패스워드\" = ?";
	    try {
	        conn = getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, username);
	        pstmt.setString(2, password);
	        ResultSet rs = pstmt.executeQuery();
	        LeeClient lc = new LeeClient(nickname); // 닉네임 전달
	        String nickname = lc.getNicknameFromDatabase(); // 중복 변수 선언 제거
	        if (nickname != null) {
	            lc.init();
	            this.setVisible(false);
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBConnectionMgr.freeConnection(conn);
	    }
	    return false;
	}
}