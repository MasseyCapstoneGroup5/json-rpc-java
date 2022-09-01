/*
 * This Java source file was generated by the Gradle 'init' task.
 * This server is an implementation of the json-rpc java server 
 * retrieved from https://github.com/arteam/simple-json-rpc/tree/master/server
 * and modified to include code from https://medium.com/martinomburajr/java-create-your-own-hello-world-server-2ca33b6957e
 */
package json.rpc.java;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;
import com.github.arteam.simplejsonrpc.server.JsonRpcServer;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.HederaPreCheckStatusException;
import com.hedera.hashgraph.sdk.HederaReceiptStatusException;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.TransferTransaction;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeoutException;
import java.util.Scanner;

@SuppressWarnings({ "unused", "deprecation" })
@JsonRpcService
public class App {
	private PrivateKey privateKey = null;
	private PublicKey publicKey = null;
	
	public App() {

	}

	
	@JsonRpcMethod
    public String getGreeting() {
        return "Hello World!";
    }
	
	@JsonRpcMethod
    public static void main(String[] args) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {
    	listen(80);
    /*    System.out.println(new App().getGreeting());
        //Grab your Hedera testnet account ID and private key
        AccountId myAccountId = AccountId.fromString(Dotenv.load().get("MY_ACCOUNT_ID"));
        PrivateKey myPrivateKey = PrivateKey.fromString(Dotenv.load().get("MY_PRIVATE_KEY"));  
        
        //Create your Hedera testnet client
        Client client = Client.forTestnet();
        client.setOperator(myAccountId, myPrivateKey);
        
        
        // Generate a new key pair
        PrivateKey newAccountPrivateKey = PrivateKey.generateED25519();
        PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();
        
        
        //Create new account and assign the public key
        TransactionResponse newAccount = new AccountCreateTransaction()
                .setKey(newAccountPublicKey)
                .setInitialBalance( Hbar.fromTinybars(1000))
                .execute(client);

        // Get the new account ID
        AccountId newAccountId = newAccount.getReceipt(client).accountId;

        System.out.println("The new account ID is: " +newAccountId);

        //Check the new account's balance
        AccountBalance accountBalance = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .execute(client);

        System.out.println("The new account balance is: " +accountBalance.hbars);

        //Transfer hbar
        TransactionResponse sendHbar = new TransferTransaction()
                .addHbarTransfer(myAccountId, Hbar.fromTinybars(-1000))
                .addHbarTransfer(newAccountId, Hbar.fromTinybars(1000))
                .execute(client);

        System.out.println("The transfer transaction was: " +sendHbar.getReceipt(client).status);

        //Request the cost of the query
        Hbar queryCost = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .getCost(client);

        System.out.println("The cost of this query is: " +queryCost);

        //Check the new account's balance
        AccountBalance accountBalanceNew = new AccountBalanceQuery()
                .setAccountId(newAccountId)
                .execute(client);

        System.out.println("The new account balance is: " +accountBalanceNew.hbars); */
    } 
	
	// connect to server on port 80
	public static void listen(Integer port) {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			Socket connectionSocket = serverSocket.accept();
			
			// create input & output streams for the connection
			InputStream inputToServer = connectionSocket.getInputStream();
			OutputStream outputFromServer = connectionSocket.getOutputStream();
			
			Scanner scanner = new Scanner(inputToServer, "UTF-8");
			PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);
			
			serverPrintOut.println("Enter Peace to exit.");
			
			// server takes input from client and echos it back
			boolean done = false;
			
			while (!done && scanner.hasNextLine()) {
				String line = scanner.nextLine();
				serverPrintOut.println("Echo from your server: " + line);
				
				if (line.toLowerCase().trim().equals("peace")) {
					
					done = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("serial")
	@JsonRpcError(code = -32032, message ="Generic error")
	public class GenericException extends Exception {
		
	}
}
