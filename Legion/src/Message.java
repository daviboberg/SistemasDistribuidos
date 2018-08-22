public class Message {

    public String message;
    public Integer id;

    public Message(String message, Integer id) {
        this.message = message;
        this.id  = id;
    }

    public Message() {}

    public String encode() {
        return this.id.toString() + "," + this.message;
    }

    public static Message decode(String message_to_decode) {
        String s[] = message_to_decode.split(",");
        return new Message(s[1], Integer.parseInt(s[0]));
    }
}
