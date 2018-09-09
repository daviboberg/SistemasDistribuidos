import java.net.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Legion {

    private String ip;
    private Integer port;
    private Integer my_id;

//    private HashMap<Integer, PublicKey> known_users;
    private List<User> known_users;
    private BlockingQueue<Message> queue;
    private List<Resource> resources;

    private Sender sender;

    private Cryptography cryptography;

    Legion(String ip, Integer port, Integer my_id) {
        this.ip = ip;
        this.port = port;
        this.my_id = my_id;
        this.known_users = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.queue = new LinkedBlockingQueue<>();
        this.cryptography = new Cryptography();
    }

    void init() {
        try {
            this.cryptography.generateKeys();

            InetAddress group = InetAddress.getByName(this.ip);
            MulticastSocket socket = new MulticastSocket(this.port);
            socket.joinGroup(group);

            this.sender = new Sender(socket, group, this.port, this.my_id);
            this.sender.start();

            Input input = new Input(this.queue, this.my_id);
            input.start();

            Receiver receiver = new Receiver(socket, this.queue);
            receiver.start();

            this.connect();
        } catch (Exception e) {
            System.out.println("Conexão encerrada");
        }
    }

    void run() {
        try {
            while (true) {
                Message message = this.queue.take();

                boolean is_not_from_myself = !message.from_user.equals(my_id);
                boolean is_to_myself = message.to_user.equals(this.my_id);
                boolean is_to_everyone = message.to_user.equals(Message.BROADCAST_CODE);

                if (is_not_from_myself && (is_to_myself || is_to_everyone)) {
                    this.processMessage(message);
                }
            }
        } catch (InterruptedException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
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

    private void processMessage(Message message) throws InterruptedException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        switch (message.code) {
            case INTERNAL_INPUT:
                internalInput(message);
                break;

            case NEW_USER:
                if (isKnownUser(message.from_user))
                    return;

                System.out.println("New user connected with id:" + message.from_user.toString());

                User new_user;
                PublicKey publicKey = Cryptography.stringToPublicKey(message.data);
                new_user = new User(message.from_user, publicKey);
                this.known_users.add(new_user);

                responseToNewUser(message.from_user);
                break;

            case EXIT:
                removeKnownUserById(message.from_user);
                break;

            case REQUEST_RESOURCE:
                acceptOrDenyResourceRequest(message);
                return;

            case REQUEST_ACCEPTED:
            case REQUEST_DENIED:
                this.addPeerToResourceResponse(message);
                break;

            case DEAD_PEER:
                break;
            case RESOURCE_TIMEOUT:
                Resource resource = this.getResourceById(Integer.parseInt(message.data));
                if (resource == null) {
                    break;
                }

                List<User> no_response_peers = resource.getAllPeersWithoutResponse(this.known_users);

                if (!no_response_peers.isEmpty()) {
                    this.notificationOfDeadPeers(no_response_peers);
                }
                break;

            case GENERIC:
                System.out.println("Generic message: " + message.data);
        }
    }

    private void notificationOfDeadPeers(List<User> dead_peers) throws InterruptedException {
        for (User user : dead_peers) {
            this.queue.put(new Message(this.my_id, Message.BROADCAST_CODE, Message.Code.DEAD_PEER, user.id.toString()));
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
        Integer user_to_respond = message.from_user;
        Message.Code status = Message.Code.REQUEST_ACCEPTED;

        boolean i_have_resource = this.containsResource(Integer.parseInt(message.data));
        if (i_have_resource) {
            status = Message.Code.REQUEST_DENIED;
        }

        Message response_message = new Message(this.my_id, user_to_respond, status, message.data);
        this.sendMessage(response_message);
    }

    private void responseToNewUser(Integer to_user) throws InterruptedException {
        Message message = new Message(this.my_id, to_user, Message.Code.NEW_USER, Cryptography.publicKeyToString(this.cryptography.publicKey));
        this.sendMessage(message);
    }

    private void addPeerToResourceResponse(Message message) {
        System.out.println(this.resources.size());
        for (Resource resource : this.resources) {
            Integer requested_resource = Integer.parseInt(message.data);
            if (resource.id.equals(requested_resource)) {
                resource.addPeerResponse(message.from_user, message.code);
                resource.updateResourceStatus(this.known_users);
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
        String[] message_split = message.data.split(" ");

        switch (message_split[0]) {
            case "exit":
                message.to_user = Message.BROADCAST_CODE;
                message.code = Message.Code.EXIT;
                break;
            case "request":
                message.to_user = Message.BROADCAST_CODE;
                message.code = Message.Code.REQUEST_RESOURCE;
                String resource_id = message_split[1];
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
        message.from_user = this.my_id;
        this.sendMessage(message);
    }

    private void requestResource(Integer requested_resource_id) {
        System.out.println(this.resources.size());
        for (Resource requested_resource : this.resources) {
            boolean is_already_obtained = requested_resource.id.equals(requested_resource_id) && requested_resource.obtained;
            if (is_already_obtained) {
                System.out.println("Resource already obtained");
                return;
            }

            boolean is_already_requested = requested_resource.id.equals(requested_resource_id);
            if (is_already_requested) {
                System.out.println("Resource already requested");
                return;
            }
        }

        ResourceTimerTask resource_task = new ResourceTimerTask(this.my_id, requested_resource_id, this.queue);
        Resource requested_resource = new Resource(requested_resource_id, resource_task);
        this.resources.add(requested_resource);
    }

    private void sendMessage(Message message) throws InterruptedException {
        sender.queue.put(message);
    }

    private void removeKnownUserById(Integer user_id) {
        User user_to_remove = null;
        for (User user : this.known_users) {
            if (user.id.equals(user_id)) {
                user_to_remove = user;
            }
        }

        if (user_to_remove != null) {
            this.known_users.remove(user_to_remove);
        }
    }

    private boolean isKnownUser(Integer user_id) {
        for (User user : this.known_users) {
            if (user.id.equals(user_id)) {
                return true;
            }
        }
        return false;
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
