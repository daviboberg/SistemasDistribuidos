public class Message {

    public Integer from_user;
    public Integer to_user;
    public Code code;
    public String data;

    public static final int BROADCAST_CODE = 255;

    public Message(Integer from_user, Integer to_user, Code code) {
        this.from_user = from_user;
        this.to_user = to_user;
        this.code = code;
    }

    public Message(Integer from_user, Integer to_user, Code code, String data) {
        this.from_user = from_user;
        this.to_user = to_user;
        this.code = code;
        this.data = data;
    }

    public String encode() {
        return String.join(",", this.from_user.toString(), this.to_user.toString(), this.code.toString(), this.data);
    }

    public static Message decode(String message_to_decode) {
        String s[] = message_to_decode.split(",");
        return new Message(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Code.valueOf(s[2]), s[3]);
    }

    public enum Code {
        NEW_USER,
        REQUEST_RESOURCE,
        REQUEST_DENIED,
        REQUEST_ACCEPTED,
        EXIT,
        INTERNAL_INPUT,
        DEAD_PEER,
        RESOURCE_TIMEOUT,
        GENERIC
    }
}
