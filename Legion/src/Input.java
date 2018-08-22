import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class Input extends Thread {

    BlockingQueue<Message> legion_queue;

    public Input(BlockingQueue<Message> legion_queue) {
        this.legion_queue = legion_queue;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        try {
            while(true) {
                String message = scanner.nextLine();
                legion_queue.put(new Message(0, Message.Code.INTERNAL_INPUT, message));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
