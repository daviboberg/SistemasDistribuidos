import java.net.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Legion {

    private String ip;                      //Multicast IP
    private Integer port;                   //Port
    private Integer my_id;                  //Id of peer

    private List<User> known_users;         //List of known users
    private BlockingQueue<Message> queue;   //Queue of messages of Legion
    private List<Resource> resources;       //List of resources HELD or WANTED

    private Sender sender;                  //Sender thread

    private Cryptography cryptography;      //Cryptography class

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

            //Lets join multicat group
            InetAddress group = InetAddress.getByName(this.ip);
            MulticastSocket socket = new MulticastSocket(this.port);
            socket.joinGroup(group);

            //Start sender thread
            this.sender = new Sender(socket, group, this.port);
            this.sender.start();

            //Start input thread
            Input input = new Input(this.queue, this.my_id);
            input.start();

            //Start receiver thread
            Receiver receiver = new Receiver(socket, this.queue);
            receiver.start();

            //Lets send message to everyone announcing our arrival
            this.connect();
        } catch (Exception e) {
            System.out.println("Conexão encerrada");
        }
    }

    void run() {
        //Print help menu
        this.printHelp();
        try {
            while (true) {
                //Get message from queue
                Message message = this.queue.take();

                boolean is_not_from_myself = !message.from_user.equals(my_id);
                boolean is_to_myself = message.to_user.equals(this.my_id);
                boolean is_to_everyone = message.to_user.equals(Message.BROADCAST_CODE);

                //If this message was not sent by me, and is destined to me or broadcast, I should process a response.
                if (is_not_from_myself && (is_to_myself || is_to_everyone)) {
                    this.processMessage(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            //Broadcast to everyone that we arrived
            announceNewUser(Message.BROADCAST_CODE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(Message message) throws Exception {
        switch (message.code) {
            case INTERNAL_INPUT:
                //Messages coming from Input Thread
                internalInput(message);
                break;

            case NEW_USER:
                //A new user connected. If we already know him we should just ignore
                if (isKnownUser(message.from_user))
                    return;

                //Let's inform in the terminal that this user arrived
                System.out.println("New user connected with id:" + message.from_user.toString());

                //Create the user and save his public key
                User new_user;
                PublicKey publicKey = Cryptography.stringToPublicKey(message.data);
                new_user = new User(message.from_user, publicKey);
                this.known_users.add(new_user);

                //Let's announce our existence to him
                announceNewUser(message.from_user);
                break;

            case DEAD_PEER:
            case EXIT:
                //A user exited or died. Let's forget him
                removeKnownUserById(message.from_user);
                break;

            case REQUEST_RESOURCE:
                //Accept or deny a resource
                allowOrDenyResourceRequest(message);
                return;

            case REQUEST_ACCEPTED:
            case REQUEST_DENIED:
                //Process answer to resource requests
                this.addPeerToResourceResponse(message);
                break;

            case RESOURCE_TIMEOUT:
                //Timeout of resource wait for answers. Let's get the resource.
                Resource resource = this.getResourceById(Integer.parseInt(message.data));
                if (resource == null) {
                    break;
                }

                //Let's find all peers that didn't answer our request.
                List<User> no_response_peers = resource.getAllPeersWithoutResponse(this.known_users);

                //We should consider this peers as dead.
                if (!no_response_peers.isEmpty()) {
                    this.notificationOfDeadPeers(no_response_peers);
                }
                //Let's update the status of our resource.
                resource.updateResourceStatus(this.known_users);
                break;
        }
    }

    /**
     * Notify the existence of dead peers
     * @param dead_peers    List of dead peers
     * @throws InterruptedException
     */
    private void notificationOfDeadPeers(List<User> dead_peers) throws InterruptedException {
        for (User user : dead_peers) {
            //Let's broadcast the dead of this peers.
            Message message = new Message(this.my_id, Message.BROADCAST_CODE, Message.Code.DEAD_PEER, user.id.toString());
            this.sendMessage(message);
        }
    }

    /**
     * Get resource by ID
     * @param resource_id Resource id
     * @return Resource
     */
    private Resource getResourceById(Integer resource_id) {
        for (Resource r : this.resources) {
            if (r.id.equals(resource_id)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Process if we should allow or deny the request of a resource
     * @param message Message of request of resource
     * @throws Exception
     */
    private void allowOrDenyResourceRequest(Message message) throws Exception {
        Integer user_to_respond = message.from_user;
        //Wee accept by default.
        Message.Code status = Message.Code.REQUEST_ACCEPTED;
        String resource_id = message.data;

        Resource resource = this.getResourceById(Integer.parseInt(resource_id));
        boolean i_have_resource = resource != null && resource.obtained;
        boolean i_want_resource_and_have_priority = resource != null && resource.timestamp < message.timestamp;

        //If I already have the resource or I also want this resource and I have priority. I will deny the request.
        if (i_have_resource || i_want_resource_and_have_priority) {
            status = Message.Code.REQUEST_DENIED;
            //Add this user to the list of people that I need to notify after releasing this resource.
            this.addUserToReleaseQueue(user_to_respond, Integer.parseInt(resource_id));
        }

        //Let's encrypt this resource id and send the answer.
        String encrypted_resource_id = this.cryptography.encrypt(resource_id);
        Message response_message = new Message(this.my_id, user_to_respond, status, encrypted_resource_id);
        this.sendMessage(response_message);
    }


    /**
     * Adds user to the list of users waiting for this resource
     * @param user_to_respond   User_id to be added
     * @param resource_id   resource id
     */
    private void addUserToReleaseQueue(Integer user_to_respond, Integer resource_id) {
        for ( Resource r : this.resources) {
            if (r.id.equals(resource_id)) {
                r.release_list.add(user_to_respond);
                return;
            }
        }
    }


    /**
     * Announce new user
     * @param to_user User to send message. Can be broadcast
     * @throws InterruptedException
     */
    private void announceNewUser(Integer to_user) throws InterruptedException {
        Message message = new Message(this.my_id, to_user, Message.Code.NEW_USER, Cryptography.publicKeyToString(this.cryptography.public_key));
        this.sendMessage(message);
    }

    /**
     * Add the response of a peer to the resource response list
     * @param message
     * @throws Exception
     */
    private void addPeerToResourceResponse(Message message) throws Exception {
        User user = this.getUserFromId(message.from_user);
        //We need to decrypt the resource_id using this user public key
        //If he is fake, the decrypted resource will not be valid and we will do nothing with it.
        String decrypted_resource = Cryptography.decrypt(message.data, user.publicKey);
        Integer requested_resource = Integer.parseInt(decrypted_resource);

        for (Resource resource : this.resources) {
            if (resource.id.equals(requested_resource)) {
                resource.addPeerResponse(message.from_user, message.code);
                resource.updateResourceStatus(this.known_users);
            }
        }

    }

    /**
     * @param id User id
     * @return User or null
     */
    private User getUserFromId(Integer id) {
        for (User user : this.known_users) {
            if (user.id.equals(id)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Process messages coming from the input thread
     * @param message
     * @throws Exception
     */
    private void internalInput(Message message) throws Exception {
        String[] message_split = message.data.split(" ");

        switch (message_split[0]) {
            case "exit":
                //We need to broadcast that we are leaving.
                message.to_user = Message.BROADCAST_CODE;
                message.code = Message.Code.EXIT;
                message.from_user = this.my_id;
                this.sendMessage(message);
                //Let's sleep to be sure that our message was sent.
                Thread.sleep(1000);
                System.exit(0);
                break;
            case "request":
                //We are requesting a resource
                if (message.data.isEmpty())
                    return;
                message.to_user = Message.BROADCAST_CODE;
                message.code = Message.Code.REQUEST_RESOURCE;
                String resource_id = message_split[1];
                message.data = resource_id;
                message.from_user = this.my_id;

                //Let's request it
                if (this.requestResource(Integer.parseInt(resource_id), message.timestamp)) {
                    //Only if this resource is not in HELD or WANTED we should request it.
                    this.sendMessage(message);
                }
                break;
            case "release":
                if (message.data.isEmpty())
                    return;

                //Release resource
                this.releaseResource(Integer.parseInt(message_split[1]));
                break;
            case "status":
                if (message.data.isEmpty())
                    return;
                Resource resource = this.getResourceById(Integer.parseInt(message_split[1]));
                //If we don't know this resource is because it is RELEASED.
                if (resource == null) {
                    System.out.println("Status: RELEASED");
                    return;
                }
                //If we obtained this resource it is HELD
                if (resource.obtained) {
                    System.out.println("Status: HELD");
                    return;
                }
                //If we didn't obtain this resource we want it
                System.out.println("Status: WANTED");
                break;
            case "resources-obtained":
                System.out.println("Resources obtained: ");
                for (Resource r : this.resources) {
                    if (r.obtained) {
                        System.out.println(r.id.toString());
                    }
                }
                break;
             default:
                 //By default it's print the help
                 this.printHelp();
        }
    }

    /**
     * Release resource
     * @param resource_id
     * @throws Exception
     */
    private void releaseResource(int resource_id) throws Exception {
        for (Resource resource : this.resources) {
            boolean found_resource = resource.id.equals(resource_id);
            if (!found_resource)
                continue;

            boolean is_mine_resource = resource.obtained;
            if (!is_mine_resource) {
                System.out.println("Resource is not obtained by me");
                return;
            }

            //Inform all users that want this resource that it is available.
            this.releaseResourceToUsers(resource);
            //Remove the resource from our list
            this.resources.remove(resource);
            return;
        }
    }

    /**
     * Notify users wanting this resource that it is available
     * @param resource
     * @throws Exception
     */
    private void releaseResourceToUsers(Resource resource) throws Exception {
        String encrypted_resource_id = this.cryptography.encrypt(resource.id.toString());
        for (Integer user_id : resource.release_list) {
            Message m = new Message(this.my_id, user_id, Message.Code.REQUEST_ACCEPTED, encrypted_resource_id);
            this.sendMessage(m);
        }
    }

    /**
     * Request a resource
     * @param requested_resource_id resource id
     * @param timestamp timestamp to be used in the new resource
     * @return
     */
    private boolean requestResource(Integer requested_resource_id, long timestamp) {
        for (Resource requested_resource : this.resources) {
            //Resource HELD, return false so we don't try to request it again.
            boolean is_already_obtained = requested_resource.id.equals(requested_resource_id) && requested_resource.obtained;
            if (is_already_obtained) {
                System.out.println("Resource already obtained");
                return false;
            }

            //Resource WANTED, return false so we don't try to request it again.
            boolean is_already_requested = requested_resource.id.equals(requested_resource_id);
            if (is_already_requested) {
                System.out.println("Resource already requested");
                return false;
            }
        }

        //Create timeout task.
        ResourceTimerTask resource_task = new ResourceTimerTask(this.my_id, requested_resource_id, this.queue);
        //Create resource
        Resource requested_resource = new Resource(requested_resource_id, resource_task);
        //Set the resource timestamp to be the same as the internal message timestamp.
        requested_resource.timestamp = timestamp;
        //Add to our list of resources
        this.resources.add(requested_resource);
        return true;
    }

    private void sendMessage(Message message) throws InterruptedException {
        sender.queue.put(message);
    }

    /**
     * Remove a user
     * @param user_id
     */
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

        //Let's update our resource status
        for (Resource resource : this.resources) {
            resource.updateResourceStatus(this.known_users);
        }
    }

    /**
     * Check if we know a user given it's id
     * @param user_id
     * @return
     */
    private boolean isKnownUser(Integer user_id) {
        for (User user : this.known_users) {
            if (user.id.equals(user_id)) {
                return true;
            }
        }
        return false;
    }

    private void printHelp() {
        System.out.println("Legion P2P multicast resource management.");
        System.out.println("Made by Bruno Clemente and Davi Boberg.");
        System.out.println("Commands:");
        System.out.println("");
        System.out.println("request <1-100>\t\t Request resource.");
        System.out.println("release <1-100>\t\t Release resource.");
        System.out.println("status <1-100>\t\t  Status of resource.");
        System.out.println("resources-obtained\t\t Show all resources obtained.");
        System.out.println("help\t\t Show this info.");
        System.out.println("exit\t\t Closes peer.");

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
            //Let's inform that a timeout occurred
            Message message = new Message(0, this.user_id, Message.Code.RESOURCE_TIMEOUT, this.resource_id.toString());
            this.legion_queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
