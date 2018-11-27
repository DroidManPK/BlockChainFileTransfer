package dbc.net;



import dbc.crypto.Main;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.net.*;
import java.io.*;

public class Client extends Thread{
    public static SocketFactory sf = SSLSocketFactory.getDefault();
    public static String ip;
    public static int port;
    @Override
    public void run(){
        try {
            Socket s = sf.createSocket(ip, port);
            Main.print("Transmission initiated");
            //handle msg



        /*DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str = "", str2 = "";
        while (!str.equals("stop")) {
            str = br.readLine();
            dout.writeUTF(str);
            dout.flush();
            str2 = din.readUTF();
            System.out.println("Server says: " + str2);
        }

        dout.close();
        s.close();*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}