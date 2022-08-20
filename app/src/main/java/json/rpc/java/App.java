/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package json.rpc.java;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import io.github.cdimascio.dotenv.Dotenv;


@JsonRpcService
public class App {	
	@JsonRpcMethod
    public String getGreeting() {
        return "Hello World!";
    }
	
	//@JsonRpcMethod
    public static void main(String[] args) {
        //System.out.println(new App().getGreeting());
        //Grab your Hedera testnet account ID and private key
        AccountId myAccountId = AccountId.fromString(Dotenv.load().get("MY_ACCOUNT_ID"));
        PrivateKey myPrivateKey = PrivateKey.fromString(Dotenv.load().get("MY_PRIVATE_KEY"));  
    }
	
	@SuppressWarnings("serial")
	@JsonRpcError(code = -32032, message ="Generic error")
	public class GenericException extends Exception {
		
	}
}