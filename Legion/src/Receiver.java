import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

class Receiver{

    MulticastSocket socket;

    public Receiver(MulticastSocket socket) {
        this.socket = socket;
    }

    public Message getMessage() {
        String message = "";
        try {
                byte[] buffer = new byte[1000];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                this.socket.receive(messageIn);
                message = new String(messageIn.getData());
        } catch (IOException e) {
            System.out.println("Conexão encerrada");
        }
        return Message.decode(message);
    }
}