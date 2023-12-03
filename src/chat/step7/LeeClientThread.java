package chat.step7;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class LeeClientThread extends Thread{
	// 선언부
	LeeClient lc = null;
	
	// 생성자
	public LeeClientThread(LeeClient lc) {
		this.lc = lc;		
	}
	
	// 메소드
	public void run() {
	    boolean isStop = false;
	    while (!isStop) {
	        try {
				String msg = "";                           // 빈 문자열, 선언만 한 상태
	            msg = (String) lc.ois.readObject();
	            StringTokenizer st = null;
	            int protocol = 0;
				if(msg != null) {                            // 입력된 메시지가 있다면
	                st = new StringTokenizer(msg, ",");
                    protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
					case 100:{
						String nickname = st.nextToken();
						lc.jta_display.append(nickname + " 님이 입장하셨습니다.\n");
					} break;
				
					case 200: {
						String nickname = st.nextToken();
						String message = st.nextToken();
						lc.jta_display.append("[" + nickname + "] : " + message + "\n");
						lc.jta_display.setCaretPosition(lc.jta_display.getDocument().getLength());
					}break;	
					
	                case 300: { // 프로토콜 처리 메시지
	                    String result = st.nextToken();
	                    if (result.equals("success")) {
	                        String nickname = st.nextToken();
	                        lc.chatplay(nickname); //채팅 창 띄우기
	                        lc.setVisible(false); // 로그인 창 숨기기
	                    } else if (result.equals("fail")) {
	                    	JOptionPane.showMessageDialog(lc, "아이디와 비밀번호를 확인해주세요.",
	                                "로그인 실패", JOptionPane.ERROR_MESSAGE);
	                    }
                    }
	                break;
				} // switch				
			} catch (Exception e) {
		        e.printStackTrace();
			} 
		} 
	} 
}