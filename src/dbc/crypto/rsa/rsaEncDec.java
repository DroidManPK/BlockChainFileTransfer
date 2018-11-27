package dbc.crypto.rsa;

//import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class rsaEncDec {

    static String fp,encryptedFilePath;
    private static Key aeskey;
    private static byte[] iv=new byte[16];
    //IvParameterSpec ivspec = new IvParameterSpec(iv);

    private static byte[] readFile(String filepath) throws Exception
    {
        File f=new File(filepath);
        rsaEncDec.fp=f.getAbsolutePath();
        FileInputStream fip=new FileInputStream(f);
        byte[] bytefile=new byte[(int)f.length()];
        fip.read(bytefile);
        fip.close();
        return bytefile;
    }

    private static void aesEnc(String filepath) throws Exception {
        keygen k=new keygen();
        aeskey=k.genAesKeys();

        SecureRandom sr=new SecureRandom();
        sr.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        byte[] bytefile=rsaEncDec.readFile(filepath);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, rsaEncDec.aeskey,ivspec);
        byte[] encrypted = cipher.doFinal(bytefile);

        File file=new File(fp.substring(0,fp.lastIndexOf('.'))+"(aes_encryted).txt");
        rsaEncDec.encryptedFilePath=file.getAbsolutePath();
        FileOutputStream fop=new FileOutputStream(file);
        fop.write(encrypted);
        fop.flush();
        fop.close();

        System.out.println("hash generated is "+ipfsHash.fileToIPFS());
    }

    //Reads the file and converts it into byte array and Applies rsa encryption with public key of reciever
    private static byte[] encrypt(String filePath,PublicKey pkey) throws Exception{
        rsaEncDec.aesEnc(filePath);
        Cipher ciper=Cipher.getInstance("RSA");
        ciper.init(Cipher.ENCRYPT_MODE,pkey);

        return ciper.doFinal(rsaEncDec.aeskey.getEncoded());
    }

    private static void decrypt(String filepath,PrivateKey pkey,byte[] aesKey) throws Exception
    {
        //Thread.sleep(2000);
        byte[] bytefile=rsaEncDec.readFile(filepath);

        Cipher ciper=Cipher.getInstance("RSA");
        ciper.init(Cipher.DECRYPT_MODE,pkey);
        byte[] decrytaeskey=ciper.doFinal(aesKey);     //Decrypt aes byte array key with rsa

        Key key=new SecretKeySpec(decrytaeskey,"AES");      //Convert byte array to aes SecretKey type

        //Now decrypt file using aes key
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE,key,new IvParameterSpec(iv));
        byte[] original = cipher.doFinal(bytefile); //Original byte file
        //Main.print("file decrypted : "+new String(original, StandardCharsets.UTF_8));
        File file=new File(fp+"(decryted)");
        FileOutputStream fop=new FileOutputStream(file);
        fop.write(original);        //Write it to file
        fop.flush();
        fop.close();
    }

    public static byte[] encryptFile(String filePath,PublicKey pkey) throws Exception
    {
        return rsaEncDec.encrypt(filePath,pkey);        //Return rsa encrypted aes key
    }

    public static void decryptFile(String filePath,PrivateKey pkey,byte[] aesKey) throws Exception
    {
        rsaEncDec.decrypt(filePath,pkey,aesKey);
    }
}
