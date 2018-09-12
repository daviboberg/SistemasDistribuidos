import java.security.PublicKey;

class User {

    Integer id;             //User id
    PublicKey publicKey;    //Public key

    User(Integer id, PublicKey publicKey) {
        this.id = id;
        this.publicKey = publicKey;
    }

}
