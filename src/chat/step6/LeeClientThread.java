package chat.step6;
import java.util.StringTokenizer;

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
		while(!isStop) {                              
			try {
				String msg = "";                           // 빈 문자열, 선언만 한 상태
				msg = (String)lc.ois.readObject();         // 읽어온 정보를 문자열로 전환
				StringTokenizer st = null;
				int protocol = 0 ;
				
				if(msg != null) {                            // 입력된 메시지가 있다면
					st = new StringTokenizer(msg, ",");
					protocol = Integer.parseInt(st.nextToken()); // 입력메세지를 줄 때 숫자를 넣어서 구분하였으로, 어떤숫자를 받았는지 알기위해 숫자로 형전환
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
				} // switch				
			} catch (Exception e) {
				System.out.println(e.toString());
			} // try..catch
		} //while
	} // run
}