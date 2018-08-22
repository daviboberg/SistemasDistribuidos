public class Message {

    public Integer user_id;
    public Code code;
    public String message;

    public Message(Integer id, Code code) {
        this.user_id = id;
        this.code = code;
    }

    public Message(Integer id, Code code, String message) {
        this.user_id  = id;
        this.code = code;
        this.message = message;
    }

    public String encode() {
        return String.join(",", this.user_id.toString(), this.code.toString(), this.message);
    }

    public static Message decode(String message_to_decode) {
        String s[] = message_to_decode.split(",");
        return new Message(Integer.parseInt(s[0]), Code.valueOf(s[1]), s[2]);
    }

    public enum Code {
        NEW_USER,
        REQUEST_RESOURCE,
        EXIT,
        INTERNAL_INPUT,
        GENERIC
    }
}
