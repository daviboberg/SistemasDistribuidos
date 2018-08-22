import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Sender extends Thread{

    private final Integer port;
    private MulticastSocket socket;
    private InetAddress group;
    private Integer id;
    public BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();

    public Sender(MulticastSocket s, InetAddress group, Integer port, Integer id) {
        this.socket = s;
        this.group = group;
        this.port = port;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = queue.take();
                if (message == null)
                    continue;

                this.send(message);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Conexão encerrada");
        }
    }

    private void send(Message message) throws IOException{
        String str_message = message.encode();
        byte[] m = str_message.getBytes();
        DatagramPacket messageOut = new DatagramPacket(m, m.length, this.group, this.port);
        this.socket.send(messageOut);
    }
}