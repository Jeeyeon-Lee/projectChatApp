package chat.step3;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
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
	String nickName="";
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
	   // 회원가입 메소드
    public void join(String username, String password) {
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
	
	public static void main(String[] args) throws Exception {
		new ChatLoginForm();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == jbtn_join) {
			System.out.println("가입버튼 클릭");
		} 
		else if (obj == jbtn_login) {
			System.out.println("로그인버튼 클릭");
			if ( "".equals(jtf_id.getText()) || "".equals(jpf_pw.getText()) ){
				JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 확인하세요.", "경고", 2);
				return;
			}
			try {
				String mem_id = jtf_id.getText();
				String mem_pwd = jpf_pw.getText();
				if (nickName.length()==0) {
					JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 확인하세요.", "경고", 2);
					return;
				} else {
					JOptionPane.showMessageDialog(this, nickName + "님의 접속을 환영합니다.");
					this.setVisible(false);
					jtf_id.setText("");
					jpf_pw.setText("");
					//new WaitingRoom(this);
				}
			} catch (Exception ae) { 
				ae.printStackTrace();
			}
		}
	}
}
