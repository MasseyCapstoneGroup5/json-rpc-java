package json.rpc.java;

import com.github.arteam.simplejsonrpc.server.JsonRpcServer;

import json.rpc.java.App;

public class JsonRPCJavaServer {
	
	private static final JsonRpcServer rpcServer = new JsonRpcServer();
	private static final App applicationServer = new App();
	

	public static void main(String[] args) {
		String textRequest = "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"method\": \"findByInitials\",\n" +
                "    \"params\": {\n" +
                "        \"firstName\": \"Kevin\",\n" +
                "        \"lastName\": \"Shattenkirk\"\n" +
                "    },\n" +
                "    \"id\": \"92739\"\n" +
                "}";
		String response = rpcServer.handle(textRequest, applicationServer);
		System.out.println(response);
	}

}
