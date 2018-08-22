
public class Main {

    public static void main(String[] args) {
        Legion legion = new Legion(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        legion.init();
        legion.run();
    }

}
