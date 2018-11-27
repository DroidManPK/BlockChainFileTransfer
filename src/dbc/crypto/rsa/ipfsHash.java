package dbc.crypto.rsa;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivateKey;

public class ipfsHash {

    private static String ipfshash=null;

    private static void addFiletoIPFS() throws IOException {
        String filePath=rsaEncDec.encryptedFilePath;
        String[] commands={"C:\\ipfs\\ipfs.exe","add",filePath};
        ProcessBuilder ps=new ProcessBuilder(commands);
        ps.redirectErrorStream(true);
        Process p=ps.start();
        BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));
        String s;
        while ((s=br.readLine())!=null)
        {
            if(s.contains("Qm"))
            {
                ipfsHash.ipfshash=(s.substring(s.indexOf("Qm"),s.lastIndexOf(' '))).trim();
                System.out.println("file added");
            }
        }
    }

    public static String fileToIPFS() throws IOException {
        ipfsHash.addFiletoIPFS();
        return ipfshash;
    }

    public static String getIpfshash(){
        return ipfshash;
    }

    private static void downloadFilefromIPFS(String hash, PrivateKey pkey,byte[] aesKey) throws Exception
    {
        String[] commands={"C:\\ipfs\\ipfs.exe","get",hash};
        ProcessBuilder ps=new ProcessBuilder(commands);
        ps=ps.directory(new File("./"));
        ps.redirectErrorStream(true);
        Process p=ps.start();
        //p.waitFor();
        BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));
        String s;
        while ((s=br.readLine())!=null)
        {
            if(s.contains("100.00%")) {
                System.out.println("Downloaded successfully!!\nFile in Downloads folder");
                rsaEncDec.decryptFile("./" + hash, pkey,aesKey);
            }
        }
    }

    public static void downloadFromIPFS(String hash,PrivateKey pk,byte[] aesKey) throws Exception {
        ipfsHash.downloadFilefromIPFS(hash,pk,aesKey);

    }

}
