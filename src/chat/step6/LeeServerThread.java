package chat.step6;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;

public class LeeServerThread extends Thread{

/*선언부*/
	LeeServer leeServer;
	Socket client;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	String chatName;
/*생성자 쓰레드호출, 클래스, 소켓 동기화*/
	public LeeServerThread(LeeServer leeServer) {
		System.out.println("LeeServerThread 연결");
		this.leeServer = leeServer;
		this.client = leeServer.socket;
		leeServer.jta_log.append("접속"+client.getInetAddress()+"\n");
		try {
			oos = new ObjectOutputStream(client.getOutputStream());
			ois = new ObjectInputStream(client.getInputStream());
			String msg = (String)ois.readObject();
			//입장한 사람들에게 정보 받아오기 보내기 반복 -> for문
			leeServer.jta_log.append("여기"+client.getInetAddress()+msg+"\n");
			for(LeeServerThread lst : leeServer.globalList) {
				this.send(lst.getName()+"\n");
			}
			leeServer.globalList.add(this);
			this.broadCasting(msg);
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/*정의메소드*/
	//모두에게 메시지 전송 메소드
	public void broadCasting(String message) {
		for(LeeServerThread lst : leeServer.globalList) {
			lst.send(message);
		}
	}
	//응답 메소드
    public void sendResponse(String response) {
        try {
            oos.writeObject(response);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	//클라이언트에게 말하기 메소드
	public void send(String message) {
		try {
			oos.writeObject(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//클라이언트 ois 듣기 / oos 말하기
	@Override 
	public void run() {
		String msg = null;
		boolean isStop = false;
		try {
			while(!isStop) {
				msg = (String)ois.readObject();
				leeServer.jta_log.append(msg+"\n");
	            StringTokenizer st = new StringTokenizer(msg, ",");
	            int protocol = Integer.parseInt(st.nextToken());

	            switch (protocol) {
	                case 200: {
	                    leeServer.jta_log.append("ChatServerThread : 200번 청취완료");
	                    String nickname = st.nextToken();
	                    String message = st.nextToken();
	                    broadCasting(200 + "|" + nickname + "|" + message);
	                }
	                break;
	                case 300: {
	                    String id = st.nextToken();
	                    String password = st.nextToken();
	                    boolean loginResult = leeServer.login(id, password);
	                    if (loginResult) {
	                        sendResponse("301,success");
	                    } else {
	                        sendResponse("302,fail");
	                    }
	                }
	                break;
	                case 400: {
	                    String id = st.nextToken();
	                    String password = st.nextToken();
	                    leeServer.join(id, password);
	                }
	                break;
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}