import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class Input extends Thread {

    BlockingQueue<Message> legion_queue;    //Legion queue
    private Integer user_id;                //User id


    public Input(BlockingQueue<Message> legion_queue, Integer user_id) {
        this.legion_queue = legion_queue;
        this.user_id = user_id;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        try {
            while(true) {
                //Get text from keyboard
                String message = scanner.nextLine();
                //Send it as a message to the legion
                legion_queue.put(new Message(Message.INTERNAL_ID, this.user_id,  Message.Code.INTERNAL_INPUT, message));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
