import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Legion {

    private String ip;
    private Integer port;
    private Integer id;
    private MulticastSocket socket;
    private List<Integer> known_users;
    private BlockingQueue<Message> queue;

    private Sender sender;
    private Input input;
    private Receiver receiver;

    public Legion(String ip, Integer port, Integer id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.known_users = new ArrayList<Integer>();
        this.queue = new LinkedBlockingQueue<Message>();
    }

    public void init() {
        try {
            InetAddress group = InetAddress.getByName(this.ip);
            this.socket = new MulticastSocket(this.port);
            this.socket.joinGroup(group);

            this.sender = new Sender(this.socket, group, this.port, this.id);
            this.sender.start();

            this.input = new Input(this.queue);
            this.input.start();

            this.receiver = new Receiver(this.socket, this.queue);
            this.receiver.start();

            this.connect();
        } catch (IOException e) {
            System.out.println("Conexão encerrada");
        }
    }

    public void run() {
        try {
            while (true) {
                Message message = this.queue.take();

                if (message == null) {
                    continue;
                }

                if (!message.user_id.equals(id)) {
                    this.ProcessMessage(message);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            this.sender.queue.put(new Message(this.id, Message.Code.NEW_USER));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void ProcessMessage(Message message) throws InterruptedException {
        switch (message.code) {
            case NEW_USER:
                if (isKnown(message.user_id))
                    return;

                this.known_users.add(message.user_id);
                System.out.println("New user connected with id:" + message.user_id.toString());
                sender.queue.put(new Message(this.id, Message.Code.NEW_USER));

                break;
            case INTERNAL_INPUT:
                message.code = Message.Code.GENERIC;
                message.user_id = this.id;
                sender.queue.put(message);
                break;
            case GENERIC:
                System.out.println(message.user_id);
        }
    }

    private boolean isKnown(Integer id) {
       for (int i = 0; i < this.known_users.size(); i++) {
           if (id.equals(this.known_users.get(i)))
               return true;
       }
        return false;
    }
}
