package chat.step5;
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
	            String msg = (String) lc.ois.readObject();
	            StringTokenizer st = null;
	            int protocol = 0;

	            if (!msg.isEmpty()) {
	                st = new StringTokenizer(msg, ",");
	                if (st.hasMoreTokens()) {
	                    protocol = Integer.parseInt(st.nextToken());
	                } else {
	                    continue; // 토큰이 없는 경우 다음 반복으로 건너뜁니다.
	                }
	            } else {
	                continue; // 메시지가 없는 경우 다음 반복으로 건너뜁니다.
				}
				switch(protocol) {
					case 100:{
						String nickName = st.nextToken();
						lc.jta_display.append(nickName + " 님이 입장하셨습니다.\n");
					} break;
				
					case 200: {
						String nickName = st.nextToken();
						String message = st.nextToken();
						lc.jta_display.append("[" + nickName + "] : " + message + "\n");
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
				System.out.println(e.toString());
			} // try..catch
		} //while
	} // run
}