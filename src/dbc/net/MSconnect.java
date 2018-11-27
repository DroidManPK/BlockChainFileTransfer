package dbc.net;

import java.sql.*;

public class MSconnect {
    static Connection con=null;
    static String ip = "localhost:49172",db = "bcAccounts",un = "sa",pass = "321654987";

    public static Connection ConnectDB(){
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip +";databaseName="+ db + ";user=" + un+ ";password=" + pass + ";";
            con = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            se.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return con;
    }
}
