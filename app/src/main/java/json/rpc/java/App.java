/*
 * This Java source file was generated by the Gradle 'init' task.
 * This server is an implementation of the json-rpc java server 
 * retrieved from https://github.com/arteam/simple-json-rpc/tree/master/server
 * and modified to include code from https://medium.com/martinomburajr/java-create-your-own-hello-world-server-2ca33b6957e
 */
package json.rpc.java;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;
import java.io.IOException;

@JsonRpcService
public class App {
	
	public App(int port) throws IOException {
		App.listen(port);
	}
	
	// start the JSON-RPC server
    public static void main(String[] args) throws IOException {
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
