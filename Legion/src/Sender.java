import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Sender extends Thread{

    private final Integer port;         //Multicast port
    private MulticastSocket socket;     //Multicast socket
    private InetAddress group;          //Multicast group
    BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();      //My blocking message queue

    Sender(MulticastSocket s, InetAddress group, Integer port) {
        this.socket = s;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            while (true) {
                //Awaits a message in the queue
                Message message = queue.take();
                if (message == null)
                    continue;

                //Send the message
                this.send(message);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Conexão encerrada");
        }
    }

    /**
     * Send a message by socket
     * @param message
     * @throws IOException
     */
    private void send(Message message) throws IOException{
        //Transform message in a CSV line
        String str_message = message.encode();
        //Get it in bytes
        byte[] m = str_message.getBytes();
        //Create datagram packet
        DatagramPacket messageOut = new DatagramPacket(m, m.length, this.group, this.port);
        //Send packet using socket
        this.socket.send(messageOut);
    }
}