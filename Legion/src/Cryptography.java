import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;

public class Cryptography {

    public PrivateKey privateKey;
    public PublicKey publicKey;

    public void generateKeys() throws Exception {
        Map<String, Object> keys = getRSAKeys();

        this.privateKey = (PrivateKey) keys.get("private");
        this.publicKey = (PublicKey) keys.get("public");
    }

    private static Map<String,Object> getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        Map<String, Object> keys = new HashMap<String,Object>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);
        return keys;
    }

    private static String decrypt(String encryptedText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    }

    private static String encrypt(String plainText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public static String publicKeyToString(PublicKey publicKey) {
        byte[] byte_array = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(byte_array);
    }

    public static PublicKey stringToPublicKey(String public_key_string) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byte_array = Base64.getDecoder().decode(public_key_string);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(byte_array);
        KeyFactory keyFact = KeyFactory.getInstance("RSA");
        return keyFact.generatePublic(spec);
    }
}
