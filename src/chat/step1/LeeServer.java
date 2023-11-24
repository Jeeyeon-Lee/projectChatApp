package chat.step1;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class LeeServer {
    private List<ObjectOutputStream> outputStreams;
    private JFrame jf;
    private JTextArea jta_log;
    private JScrollPane jsp_log;
    private Socket client;
    private ServerSocket server;

    public LeeServer() {
        outputStreams = new ArrayList<>();
        jf = new JFrame();
        jta_log = new JTextArea(10, 60);
        jsp_log = new JScrollPane(jta_log);
        client = null;
        server = null;
        initDisplay();
    }

    public void start(int port) {
        try {
            server = new ServerSocket(port);
            log(getTime() + " | Server Ready....");
            log(getTime() + " | client  연결 요청 대기 중...");

            while (true) {
                client = server.accept();
                log(getTime() + " | client info : " + client.getInetAddress() + "접속하였습니다.");

                ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
                outputStreams.add(outputStream);

                LeeServerThread serverThread = new LeeServerThread(client, outputStream);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
        return f.format(new Date());
    }

    public void initDisplay() {
        jf.add("Center", jsp_log);
        jf.setTitle("서버측 로그 출력화면 제공...");
        jf.setSize(400, 400);
        jf.setVisible(true);
        jf.setLocation(200, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void broadcast(String message) {
        for (ObjectOutputStream outputStream : outputStreams) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void log(String message) {
        SwingUtilities.invokeLater(() -> jta_log.append(message + "\n"));
    }

    public static void main(String[] args) {
        LeeServer server = new LeeServer();
        server.start(1004);
    }
}
