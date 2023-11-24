package chat.step3;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
	JTextArea jta_display = null;
	JScrollPane jsp = null;
	
	
	// 생성자
	public LeeClient() {
		jtf_msg.addActionListener(this);
		jbtn_send.addActionListener(this);		
	}
	// 메소드
	public void initDisplay() {
		nickname = JOptionPane.showInputDialog("닉네임을 입력하세요.");
		jp.setLayout(new BorderLayout());
		jp_south.setLayout(new BorderLayout());
		jp_south.add("Center",jtf_msg);
		jp_south.add("East",jbtn_send);
		jta_display = new JTextArea();
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
//			socket = new Socket("172.16.2.11",1004);
			socket = new Socket("192.168.35.246",1004);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			oos.writeObject(100 + "," + nickname);
			LeeClientThread lct = new LeeClientThread(this);
			lct.start();			
		} catch (Exception e) {
			System.out.println(e.toString());
		}		
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
//		LoginForm lf = new LoginForm();
		LeeClient lc = new LeeClient();
		lc.initDisplay();
		lc.init();
//		LoginForm lf = new LoginForm();
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