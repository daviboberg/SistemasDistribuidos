import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Legion {

    private String ip;
    private Integer port;
    private Integer id;

    private List<Integer> known_users;
    private BlockingQueue<Message> queue;
    private List<Resource> resources;

    private Sender sender;

    Legion(String ip, Integer port, Integer id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.known_users = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.queue = new LinkedBlockingQueue<>();
    }

    void init() {
        try {
            InetAddress group = InetAddress.getByName(this.ip);
            MulticastSocket socket = new MulticastSocket(this.port);
            socket.joinGroup(group);

            this.sender = new Sender(socket, group, this.port, this.id);
            this.sender.start();

            Input input = new Input(this.queue, this.id);
            input.start();

            Receiver receiver = new Receiver(socket, this.queue);
            receiver.start();

            this.connect();
        } catch (IOException e) {
            System.out.println("Conexão encerrada");
        }
    }

    void run() {
        try {
            while (true) {
                Message message = this.queue.take();

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
            responseToNewUser(Message.BROADCAST_CODE);
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

                responseToNewUser(message.from_user);
                break;

            case EXIT:
                this.known_users.remove(message.from_user);
                break;

            case REQUEST_RESOURCE:
                acceptOrDenyResourceRequest(message);
                return;

            case REQUEST_ACCEPTED:
            case REQUEST_DENIED:
                this.addPeerToResourceResponse(message);
                break;

            case RESOURCE_TIMEOUT:
                Resource resource = this.getResourceById(Integer.parseInt(message.data));
                if (resource == null) {
                    break;
                }

                List<Integer> no_response_peers = resource.getAllPeersWithoutResponse(this.known_users);

                if (!no_response_peers.isEmpty()) {
                    this.notificationOfDeadPeers(no_response_peers);
                }
                break;

            case GENERIC:
                System.out.println(message.data);
        }
    }

    private void notificationOfDeadPeers(List<Integer> dead_peers) throws InterruptedException {
        for (Integer id : dead_peers) {
            this.queue.put(new Message(this.id, Message.BROADCAST_CODE, Message.Code.DEAD_PEER, id.toString()));
        }
    }

    private Resource getResourceById(Integer resource_id) {
        for (Resource r : this.resources) {
            if (r.id.equals(resource_id)) {
                return r;
            }
        }
        return null;
    }

    private void acceptOrDenyResourceRequest(Message message) throws InterruptedException {
        if (this.containsResource(Integer.parseInt(message.data))) {
            sender.queue.put(new Message(this.id, message.from_user,  Message.Code.REQUEST_DENIED, message.data));
            return;
        }
        sender.queue.put(new Message(this.id, message.from_user,  Message.Code.REQUEST_ACCEPTED, message.data));
    }

    private void responseToNewUser(Integer to_user) throws InterruptedException {
        sender.queue.put(new Message(this.id, to_user, Message.Code.NEW_USER));
    }

    private void addPeerToResourceResponse(Message message) {
        for (Resource r : this.resources) {
            if (r.id.equals(Integer.parseInt(message.data))) {
                r.addPeerResponse(message.from_user, message.code);
                r.updateResourceStatus(this.known_users);
            }
        }
    }

    private boolean containsResource(Integer requested_id) {
        for ( Resource r : this.resources) {
            if (r.id.equals(requested_id)) {
                return true;
            }
        }
        return false;
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
                String resource_id = message.data.split(" ")[1];
                message.data = resource_id;

                this.requestResource(Integer.parseInt(resource_id));
                break;
            case "resources_obtained":
                System.out.println("Recurso obtidos: ");
                for (Resource r : this.resources) {
                    if (r.obtained) {
                        System.out.println(r.id.toString());
                    }
                }
                break;
        }
        message.from_user = this.id;
        sender.queue.put(message);
    }

    private void requestResource(Integer resource_id) {
        ResourceTimerTask resource_task = new ResourceTimerTask(this.id, resource_id, this.queue);
        Resource requested_resource = new Resource(resource_id, resource_task);
        this.resources.add(requested_resource);
    }

}

class ResourceTimerTask extends TimerTask {

    private Integer user_id;
    private Integer resource_id;
    private BlockingQueue<Message> legion_queue;

    ResourceTimerTask(Integer user_id, Integer resource_id, BlockingQueue<Message> legion_queue) {
        this.user_id = user_id;
        this.resource_id = resource_id;
        this.legion_queue = legion_queue;
    }

    @Override
    public void run() {
        try {
            Message message = new Message(0, this.user_id, Message.Code.RESOURCE_TIMEOUT, this.resource_id.toString());
            this.legion_queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
