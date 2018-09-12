import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.BlockingQueue;

class Receiver extends Thread {

    private MulticastSocket socket;                 //Multicast socket
    private BlockingQueue<Message> legion_queue;    //Legion scoket queue

    Receiver(MulticastSocket socket, BlockingQueue<Message> legion_queue) {
        this.socket = socket;
        this.legion_queue = legion_queue;
    }

    @Override
    public void run() {
        try {
            while(true) {
                //Get message from socket
                Message message = this.getMessage();
                //Send message to the legion queue
                this.legion_queue.put(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get message from socket. Is blocking.
     * @return
     */
    private Message getMessage() {
        String message = "";
        try {
            byte[] buffer = new byte[8000];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            //Awaits a message from the socket
            this.socket.receive(messageIn);
            //Get the message as a string
            message = new String(messageIn.getData());
        } catch (IOException e) {
            System.out.println("Conexão encerrada");
        }
        //Return the message properly typed
        return Message.decode(message);
    }
}