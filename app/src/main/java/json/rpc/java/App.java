/*
 * This Java source file was generated by the Gradle 'init' task.
 * This server is an implementation of the json-rpc java server 
 * retrieved from https://github.com/arteam/simple-json-rpc/tree/master/server
 * and modified to include code from https://medium.com/martinomburajr/java-create-your-own-hello-world-server-2ca33b6957e
 */
package json.rpc.java;

import json.rpc.java.methods.Account;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeoutException;
import org.json.*;

@JsonRpcService
public class App {
	
	public App() {

	}

    public static void main(String[] args) throws TimeoutException, PrecheckStatusException, ReceiptStatusException {
    	System.out.println("-- JSON-RPC java server running --");
    	listen(80);
    } 
	
	public static void listen(Integer port) {
		try {
		      ServerSocket ss = new ServerSocket(port);
		      for (;;) {
		        Socket client = ss.accept();
		        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		        PrintWriter out = new PrintWriter(client.getOutputStream());
		        
		        // generic response
		        out.print("HTTP/1.1 200 \r\n"); // Version & status code
		        out.print("Content-Type: text/plain\r\n"); // The type of data
		        out.print("Connection: close\r\n"); // Will close stream
		        out.print("\r\n"); // End of headers

		        String line;
		        StringBuilder stringBuilder = new StringBuilder();
		        while ((line = in.readLine()) != null) {
		          if (line.length() == 0)
		            break;
		          out.print(line + "\r\n");
		          stringBuilder.append(line);
		        }
		        
		        String response = parseRequest(stringBuilder.toString());
		        out.print(response);

		        // Close socket, breaking the connection to the client, and
		        // closing the input and output streams
		        out.close(); // Flush and close the output stream
		        in.close(); // Close the input stream
		        client.close(); // Close the socket itself
		      } // Now loop again, waiting for the next connection
		    }
		    catch (Exception e) {
		      System.err.println(e);
		      System.err.println("Usage: java HttpMirror <port>");
		    }
	}
	
	
	private static String parseRequest(String request) {
		System.out.println("Request: ");
		System.out.println(request);
		//JSONObject obj = new JSONObject(request);
		//String method = obj.getString("method");
		//JSONObject args = obj.getJSONObject("params");
		// implement some kind of mapping between the methods and the names? 
		// names will also determine the expected parameters too
		return "Request received: ";
	}
	
	private static Object callFunction(Method method, Object args) { 
		Class<Account> account = Account.class;
		for (Method account_method: account.getMethods()) {
			System.out.println(account_method);
		}
		return null;
	}
	
	@SuppressWarnings("serial")
	@JsonRpcError(code = -32032, message ="Generic error")
	public class GenericException extends Exception {
		
	}
	
	
}
