package dbc.net;

import dbc.crypto.Main;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.net.*;
import java.io.*;

public class Server extends Thread {

    public static ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();

    @Override
    public void run() {
        try {
            ServerSocket ss = ssf.createServerSocket(2606);
            Main.print("Server started");

            while (ss.isBound() && !ss.isClosed()) {
                Socket s = ss.accept();
                Main.print("RECEIVED A MESSAGE FROM HOST " + s.getInetAddress().getHostAddress());

                //handle client
            }

        /*DataInputStream din=new DataInputStream(s.getInputStream());
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        String str="",str2="";
        while(!str.equals("stop")){
            str=din.readUTF();
            System.out.println("client says: "+str);
            str2=br.readLine();
            dout.writeUTF(str2);
            dout.flush();
        }
        din.close();
        s.close();
        ss.close();
        */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}