package dbc;
import dbc.core.*;
import dbc.crypto.rsa.ipfsHash;
import dbc.crypto.rsa.rsaEncDec;
import dbc.net.MSconnect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
//import java.util.Base64;
import java.util.HashMap;
//import com.google.gson.GsonBuilder;


public class BCbuilder {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    //public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
    public static Connection con= MSconnect.ConnectDB();
    public static BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    public static int difficulty = 3;
    //public static float minimumTransaction = 0.1f;
    public static Account accountA;
    public static Account accountB;
    public static Transaction genesisTransaction;

    public static void print(String s){System.out.println(s);}
    public static void main(String[] args) throws Exception {
        //add our blocks to the blockchain ArrayList:
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        //Create test Accounts:
        accountA = new Account("A","Apass");//admin account
        accountB = new Account("B","Bpass");
        Account first = new Account("first","first");//genesis account

        genesisTransaction = new Transaction(first.publicKey,accountA.publicKey,new data(first.keys.publicKey,null,"first"));//from , to , data
        genesisTransaction.generateSignature(first.privateKey);
        genesisTransaction.transactionId="0";
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId));// to , data , id
        print("Creating and mining first block");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //test the chain
        Block b1=new Block(genesis.hash);
        print("sending file request to B");
        b1.addTransaction(accountA.sendObj(accountB.publicKey,new data(accountA.keys.publicKey,null,"text")));
        addBlock(b1);

        Block b2=new Block(b1.hash);
        print("Serving request");
        print("Enter the filepath");
        byte[] AesKey= rsaEncDec.encryptFile(br.readLine(),accountA.keys.publicKey);
        b2.addTransaction(accountB.sendObj(accountA.publicKey,new data(accountA.keys.publicKey, ipfsHash.getIpfshash(),new String(AesKey, StandardCharsets.UTF_8))));
        addBlock(b2);
        print(b2.transactions.get(0).transactionId);
        ipfsHash.downloadFromIPFS(ipfsHash.fileToIPFS(),accountA.keys.privateKey,AesKey);
        isChainValid();

    }
    public static ResultSet sql(String q)
    {
        try
        {
            PreparedStatement pst=con.prepareStatement(q);
            if(q.contains("select")||q.contains("Select")||q.contains("SELECT"))
                return pst.executeQuery();
            else
            {pst.execute();
                return null;}
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        //HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
        //tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                /*if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }*/

                /*for(TransactionInput input: currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }*/

                /*for(TransactionOutput output: currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }*/

                /*if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }*/

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    private void updateTrans(Transaction t){
        sql("insert into trans ");

    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);

    }
}
