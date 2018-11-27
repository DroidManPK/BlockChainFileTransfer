package dbc.crypto.rsa;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;

public class keygen {
    private KeyPairGenerator keyGen;
    private KeyGenerator keyGenAES;
    public PrivateKey privateKey;
    public PublicKey publicKey;
    //private SecureRandom se;

    public keygen() throws NoSuchAlgorithmException
    {
        keyGenAES=KeyGenerator.getInstance("AES");
        keyGenAES.init(128,new SecureRandom());
        keyGen=KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
    }

    public void genKeys() throws Exception
    {
        KeyPair pair = keyGen.generateKeyPair();
        privateKey= pair.getPrivate();
        publicKey= pair.getPublic();
    }

    public Key genAesKeys()
    {
        return keyGenAES.generateKey();
    }
}
