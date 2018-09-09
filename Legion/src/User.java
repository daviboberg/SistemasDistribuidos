import java.security.PublicKey;

public class User {

    public Integer id;
    public PublicKey publicKey;

    public User(Integer id, PublicKey publicKey) {
        this.id = id;
        this.publicKey = publicKey;
    }

}
