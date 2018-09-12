import java.util.*;

public class Resource {

    Integer id;                     //Resource ID
    List<Integer> release_list;     //List of users that I need to inform that the resource is avalible after release
    private Timer timer;            //Timer of timeout task
    private TimerTask task;         //Timeout task
    boolean obtained;               //Is resource obtained?
    long timestamp;

    private  HashMap<Integer, Message.Code> peers_response; //List of response from peers

    Resource(Integer id, TimerTask task) {
        this.id = id;
        this.task = task;
        this.timer = this.initTimer();
        this.obtained = false;
        this.peers_response = new HashMap<>();
        this.release_list = new ArrayList<>();
    }

    /**
     * Update this resource status
     * @param peers_that_need_to_answer List of all peers that we know
     */
    void updateResourceStatus(List<User> peers_that_need_to_answer) {
        for ( User user : peers_that_need_to_answer ) {
            Message.Code peer_response = this.peers_response.get(user.id);
            if (peer_response == null) {
                //If there is no response from at least one, nothing changed.
                return;
            }

            if (peer_response.equals(Message.Code.REQUEST_DENIED)) {
                //If at least one answer denied, nothing changed.
                return;
            }
        }
        //We obtained the resource
        this.obtained = true;
    }

    /**
     * Add a peer respone to our list
     * @param peer_id
     * @param peer_request_status
     */
    void addPeerResponse(Integer peer_id, Message.Code peer_request_status) {
        System.out.println("Peer id: " + peer_id + " Request status: " + peer_request_status);
        this.peers_response.put(peer_id, peer_request_status);
    }

    /**
     * Get a list of all peers that didn't answer our request
     * @param known_users
     * @return
     */
    List<User> getAllPeersWithoutResponse(List<User> known_users) {
        List<User> no_response_peers = new ArrayList<User>();

        for ( User user : known_users ) {
            Message.Code peer_response = this.peers_response.get(user.id);
            if (peer_response == null) {
                no_response_peers.add(user);
            }
        }
        return no_response_peers;
    }

    /**
     * Create and start out timer. The timeout is 5 seconds.
     * @return
     */
    private Timer initTimer() {
        this.timer = new Timer("Timer");
        long delay = 5000L;
        this.timer.schedule(this.task, delay);
        return timer;
    }

    public boolean equals(Resource resource) {
        return this.id.equals(resource.id);
    }

    public boolean equals(Integer resource_id) {
        return this.id.equals(resource_id);
    }

}
