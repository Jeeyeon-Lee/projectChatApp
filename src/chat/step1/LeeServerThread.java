package chat.step1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LeeServerThread extends Thread{

	/*선언부*/
    private Socket client;
    private ObjectOutputStream oos;

    /*생성자*/
    public LeeServerThread(Socket clientSocket, ObjectOutputStream outputStream) {
        this.client = clientSocket;
        this.oos = outputStream;
    }
	/*사용자 메소드*/
    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());

            while (true) {
                String message = (String) inputStream.readObject();
                LeeServer server = new LeeServer();
                server.broadcast(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
