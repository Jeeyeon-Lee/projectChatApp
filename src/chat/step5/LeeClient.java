package chat.step5;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class LeeClient extends JFrame implements ActionListener{
	
	// 선언부
	// 통신 관련 전변
	Socket socket = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;
	//대화창 관련
	JPanel chatPanel = new JPanel();
	JPanel chatSouthPanel = new JPanel();
	JTextField jtf_msg = new JTextField(20);
	JButton jbtn_send = new JButton("전송");	
	//JButton jbtn_exit = new JButton("나가기");
	JTextArea jta_display = null;
	JScrollPane jsp = null;
	
	//로그인창
	String nickname="";
	String imgPath="src/chat/step5/image/";
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
	
	
	// 생성자
	public LeeClient() {
		jtf_msg.addActionListener(this);
		jbtn_send.addActionListener(this);		
	}
	
	class mypanal extends JPanel {
		public void paintComponent(Graphics g) {
			g.drawImage(ig.getImage(), 0, 0, null);
			setOpaque(false);
			super.paintComponents(g);
		}
	}
	// 메소드
	// 회원가입, 로그인 창
	public void initDisplay() {
		setContentPane(new mypanal());
		jbtn_join.addActionListener(this);
		jbtn_login.addActionListener(this);
		this.setLayout(null);
		this.setTitle("자바채팅 ver.1");
		this.setSize(350, 600);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocation(800, 250);
		this.setVisible(true);
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
	
	//채팅창(로그인 완료 후 구현되어야 함) 
	public void chatplay(String nickname) {
		chatPanel.setLayout(new BorderLayout());
		chatSouthPanel.setLayout(new BorderLayout());
		chatSouthPanel.add("Center",jtf_msg);
		chatSouthPanel.add("East",jbtn_send);
		
		jta_display = new JTextArea();
		jta_display.setLineWrap(true);
		jta_display.setOpaque(false);
		Font font = new Font("굴림체",Font.BOLD,16);
		jta_display.setFont(font);
		jsp = new JScrollPane(jta_display);
		
		chatPanel.add("Center",jsp);
		chatPanel.add("South",chatSouthPanel);
		
	    jtf_msg.setBounds(10, 10, 200, 30);
	    jbtn_send.setBounds(220, 10, 70, 30);
	    jsp.setBounds(10, 50, 280, 500);		
		
	    JFrame chatFrame = new JFrame();
	    chatFrame.setSize(500, 600);
	    chatFrame.setVisible(true);
	    chatFrame.setTitle(nickname);
	    chatFrame.add(chatPanel, BorderLayout.CENTER);		
	    chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    chatFrame.setVisible(true);

	}
	public void init() {
	    try {
	        socket = new Socket("192.168.35.246", 1004);
	        oos = new ObjectOutputStream(socket.getOutputStream());
	        ois = new ObjectInputStream(socket.getInputStream());
	        oos.writeObject("100,,"); // 수정된 부분: 빈 문자열을 포함한 `,`로 구분된 문자열 전송
	        LeeClientThread lct = new LeeClientThread(this);
	        lct.start();			
	    } catch (Exception e) {
	        System.out.println(e.toString());
	    }		
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		LeeClient lc = new LeeClient();
		lc.initDisplay();
		lc.init();
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();
		String msg = jtf_msg.getText();
		//메시지 보내기 버튼
		if((obj == jtf_msg) ||(obj == jbtn_send)) {
			try {
				oos.writeObject("200," + nickname + "," + msg);
				jtf_msg.setText("");				
			} catch (Exception e) {
				System.out.println(e.toString());
	            e.printStackTrace();
			}
		//로그인 버튼
	    }else if (obj == jbtn_login) {
	        String id = jtf_id.getText();
	        String password = new String(jpf_pw.getPassword());
	        try {
	            oos.writeObject("300," + id + "," + password);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    //회원가입버튼
	    }else if (obj == jbtn_join) {
	        String id = jtf_id.getText();
	        String password = new String(jpf_pw.getPassword());

	        try {
	            oos.writeObject("400," + id + "," + password);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
}