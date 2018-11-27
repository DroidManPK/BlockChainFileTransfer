package dbc.crypto;

import dbc.crypto.rsa.keygen;
import dbc.crypto.rsa.rsaEncDec;
import dbc.crypto.rsa.ipfsHash;

import java.util.Scanner;

public class Main {
    public static void print(String s){System.out.println(s);}
    public static void main(String[] args) throws Exception {
        Scanner ss=new Scanner(System.in);
        keygen k=new keygen();
        k.genKeys();
        //System.out.println("private "+k.privateKey);
        //System.out.println("public"+k.publicKey);
        System.out.println("Enter file path");
        String encfile=ss.nextLine();
        byte[] AesKey=rsaEncDec.encryptFile(encfile,k.publicKey);
        String s;
        System.out.println("Enter file to download and decrypt ");
        s=ss.nextLine();
        ipfsHash.downloadFromIPFS(s,k.privateKey,AesKey);
        //launch(args);
        System.exit(0);
    }
}
