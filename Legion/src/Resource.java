import java.util.*;

public class Resource {

    public Integer id;
    private Timer timer;
    private TimerTask task;
    boolean obtained;

    private Map<Integer, Message.Code> peers_response;

    Resource(Integer id, TimerTask task) {
        this.id = id;
        this.task = task;
        this.timer = this.initTimer();
        this.obtained = false;
    }

    void updateResourceStatus(List<Integer> peers_to_answer) {
        for ( Integer v : peers_to_answer ) {
            Message.Code peer_response = this.peers_response.get(v);
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
        this.peers_response.put(peer_id, peer_request_status);
    }

    List<Integer> getAllPeersWithoutResponse(List<Integer> known_users) {
        List<Integer> no_response_peers = new ArrayList<Integer>();

        for ( Integer v : known_users ) {
            Message.Code peer_response = this.peers_response.get(v);
            if (peer_response == null) {
                no_response_peers.add(v);
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
