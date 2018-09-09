import java.util.*;

public class Resource {

    public Integer id;
    private Timer timer;
    private TimerTask task;
    boolean obtained;

    private  HashMap<Integer, Message.Code> peers_response;

    Resource(Integer id, TimerTask task) {
        this.id = id;
        this.task = task;
        this.timer = this.initTimer();
        this.obtained = false;
        this.peers_response = new HashMap<>();
    }

    void updateResourceStatus(List<User> peers_to_answer) {
        for ( User user : peers_to_answer ) {
            Message.Code peer_response = this.peers_response.get(user.id);
            if (peer_response == null) {
                return;
            }

            if (peer_response.equals(Message.Code.REQUEST_DENIED)) {
                return;
            }
        }

        this.obtained = true;
    }

    void addPeerResponse(Integer peer_id, Message.Code peer_request_status) {
        System.out.println("Peer id: " + peer_id + " Request status: " + peer_request_status);
        this.peers_response.put(peer_id, peer_request_status);
    }

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

    private Timer initTimer() {
        this.timer = new Timer("Timer");
        long delay = 1000L;
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
