/*
 * This Java source file was generated by the Gradle 'init' task.
 * This server is an implementation of the json-rpc java server 
 * retrieved from https://github.com/arteam/simple-json-rpc/tree/master/server
 * and modified to include code from https://medium.com/martinomburajr/java-create-your-own-hello-world-server-2ca33b6957e
 */
package json.rpc.java;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@JsonRpcService
public class App {
	
	public App(int port) throws IOException {
		App.listen(port);
	}
	
	// start the JSON-RPC server
    public static void main(String[] args) throws TimeoutException, PrecheckStatusException, ReceiptStatusException, IOException {
    	System.out.println(args);
    	listen(8080);
    } 
	
	public static void listen(Integer port) throws IOException {
		JavaHttpServer server = new JavaHttpServer(port, "/", new HttpRequestHandler());
		server.start();
		System.out.println("Server is started and listening on port "+ port);
	}
	
	@SuppressWarnings("serial")
	@JsonRpcError(code = -32032, message ="Generic error")
	public class GenericException extends Exception {
		
	}
	
	
}
