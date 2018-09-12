public class Message {

    Integer from_user;  //User that sent the message
    Integer to_user;    //User that should receive this message
    Code code;          //Message code
    String data;        //Message generic data
    long timestamp;     //Message timestamp

    static final int BROADCAST_CODE = 255;  //Broadcast id
    static final int INTERNAL_ID = 0;       //Internal id

    Message(Integer from_user, Integer to_user, Code code, String data) {
        this.from_user = from_user;
        this.to_user = to_user;
        this.code = code;
        this.data = data;
        this.timestamp = getTimestamp();
    }

    private Message(Integer from_user, Integer to_user, Code code, String data, String timestamp) {
        this.from_user = from_user;
        this.to_user = to_user;
        this.code = code;
        this.data = data;
        this.timestamp = Long.parseLong(timestamp);
    }

    /**
     * Get timestamp in the format of unix time
     * @return
     */
    private long getTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    /**
     * Encode message as a CSV line
     * @return
     */
    String encode() {
        return String.join(",", this.from_user.toString(), this.to_user.toString(), this.code.toString(), this.data, String.valueOf(this.timestamp));
    }

    /**
     * Get a CSV line and returns a Message object
     * @param message_to_decode
     * @return
     */
    static Message decode(String message_to_decode) {
        String s[] = message_to_decode.split(",");
        return new Message(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Code.valueOf(s[2]), s[3], s[4].trim());
    }

    /**
     * Code enum
     */
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
