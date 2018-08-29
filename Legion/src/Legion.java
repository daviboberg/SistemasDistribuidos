import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Legion {

    private String ip;
    private Integer port;
    private Integer id;
    private MulticastSocket socket;
    private List<Integer> known_users;
    private List<Integer> resources;
    private BlockingQueue<Message> queue;

    private Sender sender;
    private Input input;
    private Receiver receiver;

    public Legion(String ip, Integer port, Integer id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.known_users = new ArrayList<Integer>();
        this.resources = new ArrayList<Integer>();
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

                if (!message.from_user.equals(id) && (message.to_user.equals(this.id) || message.to_user.equals(-1))) {
                    this.processMessage(message);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            rasponseToNewUser(Message.BROADCAST_CODE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(Message message) throws InterruptedException {
        switch (message.code) {
            case INTERNAL_INPUT:
                internalInput(message);
                break;

            case NEW_USER:
                if (this.known_users.contains(message.from_user))
                    return;
                this.known_users.add(message.from_user);

                System.out.println("New user connected with id:" + message.from_user.toString());

                rasponseToNewUser(message.from_user);
                break;

            case EXIT:
                this.known_users.remove(message.from_user);
                break;

            case REQUEST_RESOURCE:
                acceptOrDenyResourceRequest(message);
                return;

            case GENERIC:
                System.out.println(message.data);
        }
    }

    private void acceptOrDenyResourceRequest(Message message) throws InterruptedException {
        if (this.resources.contains(Integer.parseInt(message.data))) {
            sender.queue.put(new Message(this.id, message.from_user,  Message.Code.REQUEST_DENIED, message.data));
            return;
        }
        sender.queue.put(new Message(this.id, message.from_user,  Message.Code.REQUEST_ACCEPTED, message.data));
        return;
    }

    private void rasponseToNewUser(Integer to_user) throws InterruptedException {
        sender.queue.put(new Message(this.id, to_user, Message.Code.NEW_USER));
    }

    private void internalInput(Message message) throws InterruptedException {
        switch (message.data) {
            case "exit":
                message.to_user = Message.BROADCAST_CODE;
                message.code = Message.Code.EXIT;
                break;
            case "request":
                message.to_user = Message.BROADCAST_CODE;
                message.code = Message.Code.REQUEST_RESOURCE;
                message.data = message.data.split(" ")[1];
                break;
        }
        message.from_user = this.id;
        sender.queue.put(message);
    }
}
