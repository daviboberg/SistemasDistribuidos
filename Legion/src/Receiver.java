import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.BlockingQueue;

class Receiver extends Thread {

    private MulticastSocket socket;
    private BlockingQueue<Message> legion_queue;

    public Receiver(MulticastSocket socket, BlockingQueue<Message> legion_queue) {
        this.socket = socket;
        this.legion_queue = legion_queue;
    }

    @Override
    public void run() {
        try {
            while(true) {
                Message message = this.getMessage();
                this.legion_queue.put(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Message getMessage() {
        String message = "";
        try {
            byte[] buffer = new byte[8000];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            this.socket.receive(messageIn);
            message = new String(messageIn.getData());
        } catch (IOException e) {
            System.out.println("Conexão encerrada");
        }
        return Message.decode(message);
    }
}