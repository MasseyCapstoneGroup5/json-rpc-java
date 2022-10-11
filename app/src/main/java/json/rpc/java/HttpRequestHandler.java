package json.rpc.java;

import com.github.arteam.simplejsonrpc.server.JsonRpcServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import json.rpc.java.services.AccountService;
import json.rpc.java.services.KeyService;
import json.rpc.java.services.SdkService;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import java.util.stream.Collectors;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@SuppressWarnings("restriction")
public class HttpRequestHandler implements HttpHandler {
    private static final HttpRequestHandler instance = new HttpRequestHandler();

    public static HttpRequestHandler getInstance() {
        return instance;
    }

    public void handle(HttpExchange t) throws IOException {
        String response;
        int httpStatus;
        if (t.getRequestMethod().equals("POST")) {
            httpStatus = 200;
            response = postRequest(IOUtils.toString(t.getRequestBody(), StandardCharsets.UTF_8));
        } else {
            httpStatus = 404;
            response = "Cannot " + t.getRequestMethod() + " " + t.getRequestURI();
        }

        //Set and send the response headers
        t.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        t.sendResponseHeaders(httpStatus, response.getBytes().length);

        //Write the response string
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static String postRequest(String body) {

        ArrayList<Object> services = new ArrayList<>();
        services.add(new SdkService());
        services.add(new AccountService());
        services.add(new KeyService());

        JsonRpcServer rpcServer = new JsonRpcServer();
        JSONObject json_body = new JSONObject(body);
        String method = json_body.getString("method");
        JSONObject params = json_body.getJSONObject("params");
        
        if (method == "call") {
        	if (json_body.getString("params").contains("func")) {
        		method = json_body.getJSONObject("params").getString("func");
        		params.remove("func");
        	} else {
        		method = json_body.getJSONObject("params").getString("callClass");
        		params = json_body.getJSONObject("params").getJSONObject("methods");
        	}
        }
        
        String call_method = method;

        String response = null;
        for (Object service : services) {
        	List<Method> methods = Stream.of(service.getClass().getMethods()).filter(i -> i.getName().toLowerCase().contains(call_method.toLowerCase())).collect(Collectors.toList());
        	if (methods.size() > 0 ) {
        		response = rpcServer.handle(body, service);
	            if (!(response.contains("-32601") && response.contains("Method not found"))) {
		              // Method found
		            break;
		        }
        	}
        }
        return response;
    }

}

