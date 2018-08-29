import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class Input extends Thread {

    BlockingQueue<Message> legion_queue;
    private Integer user_id;


    public Input(BlockingQueue<Message> legion_queue, Integer user_id) {
        this.legion_queue = legion_queue;
        this.user_id = user_id;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        try {
            while(true) {
                String message = scanner.nextLine();
                legion_queue.put(new Message(0, this.user_id,  Message.Code.INTERNAL_INPUT, message));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
