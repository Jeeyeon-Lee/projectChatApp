package chat.step3;

import java.awt.Color;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LeeServer extends Thread{
	/*선언부*/
	List<LeeServerThread> globalList = null;
	ServerSocket server = null;
	Socket socket = null;
	JFrame jf = new JFrame();
	JTextArea jta_log = new JTextArea(10,60);
	JScrollPane jsp_log = new JScrollPane(jta_log
			 , JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
			 , JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	/*정의메소드*/
	//포트연결, 대기, 소켓연결 시 서버스레드 생성 및 시작
	@Override
	public void run() {
	    globalList = new Vector<>();
	    boolean isStop = false;
	    try {
	        server = new ServerSocket(3005);
	        jta_log.append(getTime() + " | Server Ready.........");
	        jta_log.append(getTime() + " | client  연결 요청 대기 중...");
	        while (!isStop) {
	            socket = server.accept();
	            jta_log.append(getTime() + " | client info:" + socket.getInetAddress() + "");

	            // 클라이언트와의 통신을 담당하는 LeeServerThread 객체 생성 및 시작
	            LeeServerThread lst = new LeeServerThread(this, socket);
	            lst.start();

	            // 생성된 LeeServerThread 객체를 globalList에 추가
	            globalList.add(lst);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	//시간표시 메소드
    private String getTime() {
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
	/*메인메소드 객체 생성, 시작, 창구현*/
	public static void main(String[] args) {
		LeeServer cs = new LeeServer();
		System.out.println(cs);
		cs.initDisplay();
		cs.start();
	}
}
