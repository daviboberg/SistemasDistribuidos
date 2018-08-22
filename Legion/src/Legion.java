import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Legion {

    public static void run(String ip, Integer port, Integer id) {
        MulticastSocket socket = null;
        try {
            InetAddress group = InetAddress.getByName(ip);
            socket = new MulticastSocket(port);
            socket.joinGroup(group);


            Sender sender;
            sender = new Sender(socket, group, port, id);
            sender.start();

            Receiver receiver;
            receiver = new Receiver(socket);
            while (true) {
                Message message = receiver.getMessage();
                if (!message.id.equals(id)) {
                    System.out.println(message.message);
                }
            }

        } catch (SocketException e) {
            System.out.println("Conexão encerrada");
        } catch (IOException e) {
            System.out.println("Conexão encerrada");
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

}
