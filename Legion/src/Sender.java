import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

class Sender extends Thread{

    private final Integer port;
    private MulticastSocket socket;
    private InetAddress group;
    private Integer id;

    public Sender(MulticastSocket s, InetAddress group, Integer port, Integer id) {
        this.socket = s;
        this.group = group;
        this.port = port;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();


                if (message.equals("quit")) {
                    this.socket.leaveGroup(group);
                    this.socket.close();
                    System.out.println("Flwwws");
                    break;
                }

                message = new Message(message, this.id).encode();
                byte[] m = message.getBytes();
                DatagramPacket messageOut = new DatagramPacket(m, m.length, this.group, this.port);
                this.socket.send(messageOut);
            } catch (IOException e) {
                System.out.println("Conexão encerrada");
            }
        }
    }
}