import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

class Cryptography {

    private PrivateKey private_key;  //Private key
    PublicKey public_key;            //Public key

    /**
     * Generate private and public keys
     * @throws Exception
     */
    void generateKeys() throws Exception {
        //Get instance of the pair generator
        KeyPairGenerator key_pair_generator = KeyPairGenerator.getInstance("RSA");
        //intialize using 2048bits
        key_pair_generator.initialize(2048);
        //Generate key pair
        KeyPair key_pair = key_pair_generator.generateKeyPair();

        this.private_key = key_pair.getPrivate();
        this.public_key = key_pair.getPublic();
    }

    /**
     *  Decrypt text using a public key
     * @param encrypted_text
     * @param public_key
     * @return
     * @throws Exception
     */
    static String decrypt(String encrypted_text, PublicKey public_key) throws Exception {
        //Get's cipher instance
        Cipher cipher = Cipher.getInstance("RSA");
        //Init cipher in decrypt mode
        cipher.init(Cipher.DECRYPT_MODE, public_key);
        //Decrypt message
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted_text)));
    }

    /**
     * Encrypt text using a private key
     * @param plain_text
     * @param private_key
     * @return
     * @throws Exception
     */
    private static String encrypt(String plain_text, PrivateKey private_key) throws Exception {
        //Get's cipher instance
        Cipher cipher = Cipher.getInstance("RSA");
        //Init cipher in encrypt mode
        cipher.init(Cipher.ENCRYPT_MODE, private_key);
        //Encrypt message
        return Base64.getEncoder().encodeToString(cipher.doFinal(plain_text.getBytes()));
    }

    String encrypt(String plainText) throws Exception {
        return Cryptography.encrypt(plainText, this.private_key);
    }

    /**
     * Convert a public key to a string
     * @param public_key
     * @return
     */
    static String publicKeyToString(PublicKey public_key) {
        byte[] byte_array = public_key.getEncoded();
        return Base64.getEncoder().encodeToString(byte_array);
    }

    /**
     * Convert a string to public key
     * @param public_key_string
     * @return
     */
    static PublicKey stringToPublicKey(String public_key_string) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byte_array = Base64.getDecoder().decode(public_key_string);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(byte_array);
        KeyFactory key_fact = KeyFactory.getInstance("RSA");
        return key_fact.generatePublic(spec);
    }
}
