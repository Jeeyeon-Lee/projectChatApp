package chat.step3;

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
	DBConnectionMgr dbMGR = null;
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
			StringTokenizer st = new StringTokenizer(msg,",");
			st.nextToken(); //첫번째 토큰 : 숫자 세자리 토큰
			chatName = st.nextToken(); //두번째 토큰 : 닉네임토큰 변수로 저장
			//입장한 사람들에게 정보 받아오기 보내기 반복 -> for문
			leeServer.jta_log.append("접속 : "+chatName+" 님이 입장하였습니다.\n");
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
		System.out.println("DBConnectionMGR 연결");
		dbMGR = new DBConnectionMgr(this);
	}
/*정의메소드*/
	//모두에게 메시지 전송 메소드
	public void broadCasting(String message) {
		for(LeeServerThread lst : leeServer.globalList) {
			lst.send(message);
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
				broadCasting(msg);
				StringTokenizer st = null;
				int protocol = 0; //100|200|201|202|500
				if(msg !=null) {
					st = new StringTokenizer(msg,",");
					protocol = Integer.parseInt(st.nextToken());//100
				}
				switch(protocol) {
					case 200:{
						
					}break;
					case 201:{
						String nickName = st.nextToken();
						String message = st.nextToken();
						broadCasting(201
								   +"#"+nickName
								   +"#"+message);
					}break;
					case 202:{
						String nickName = st.nextToken();
						String afterName = st.nextToken();
						String message = st.nextToken();
						this.chatName = afterName;
						broadCasting(202
								+"#"+nickName
								+"#"+afterName
        						+"#"+message);
					}break;
					case 500:{
						String nickName = st.nextToken();
						leeServer.globalList.remove(this);
						broadCasting(500
								+"#"+nickName);
					}break; 
				}/////////////end of switch
			}/////////////////end of while			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

