/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package json.rpc.java;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;

@JsonRpcService
public class App {
	@JsonRpcMethod
    public String getGreeting() {
        return "Hello World!";
    }
	
	@JsonRpcMethod
    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
	
	@SuppressWarnings("serial")
	@JsonRpcError(code = -32032, message ="Generic error")
	public class GenericException extends Exception {
		
	}
}
