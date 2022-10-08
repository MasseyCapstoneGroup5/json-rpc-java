package json.rpc.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import com.hedera.hashgraph.sdk.Client;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import json.rpc.java.methods.CreateAccount;
import json.rpc.java.methods.Sdk;
import org.json.*;


/**
 * @author ashraf
 * Code retrieved (and modified) from https://examples.javacodegeeks.com/core-java/sun/net-sun/httpserver-net-sun/httpserver-net-sun-httpserver-net-sun/com-sun-net-httpserver-httpserver-example/
 *
 */
@SuppressWarnings("restriction")
public class HttpRequestHandler implements HttpHandler {
	private static final HttpRequestHandler instance = new HttpRequestHandler();

    private HttpRequestHandler(){}

    public static HttpRequestHandler getInstance() {
    	return instance;
    }

    public void handle(HttpExchange t) throws IOException {
    	String method = t.getRequestMethod().toLowerCase();
    	// TODO: need a way to pass error codes back and set the http status based on these
    	String response = "";
    	Integer httpStatus = 200;
    	if (method.equals("get")) {
    		response = getRequest();

    	} else if (method.equals("post")) {
    		if (t.getAttribute("method") == "reset") {
    			Boolean s_reset = Sdk.reset();
    			if (s_reset) {
    				response = "Reset client";
    			} else {
    				response = "Failed to reset client";
    			}

    		} else {
        		InputStream body = t.getRequestBody();
        		StringWriter writer = new StringWriter();
            	IOUtils.copy(body, writer, "UTF-8");
            	String str_body = writer.toString();
        		response = postRequest(str_body);
    		}
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
    	JSONObject body_json = new JSONObject(body);
    	String method = body_json.get("method").toString();
    	JSONObject params = null;
    	String error_response = "";
    	Integer error_code = 0;
    	String jsonrpc = (String) body_json.get("jsonrpc");
    	Integer id = (Integer) body_json.get("id");
    	try  {
    		params = (JSONObject) body_json.get("params");
    	} catch (Exception e) {
    		// TODO error if there are no "params"
    		return "unable to parse body";
    	}
    	Method method_to_call = null;
    	// TODO response to object, return code with it
    	String response = null;

		try {
			//method_to_call = account(method, params);
			if (method.toLowerCase().equals("setup")) {
				Client client = Sdk.setup((String) params.get("operatorAccountId"), (String) params.get("operatorPrivateKey"), null, null, null);
				if (client == null) {
					// TODO: implement error response for this
					error_response = "Unable to complete setup";
				}
			} else if (method.toLowerCase().contains("createaccount")) {
				method_to_call = createAccount(method);
			}

			if (method.toLowerCase().equals("reset")) {
				Boolean reset = Sdk.reset();
				if (reset) {
					response = "Successfully reset client";
				} else {
					error_response = "Unable to reset client";
				}
			} else if (method.toLowerCase().contains("setup")) {
				response = "Successfully setup client";

			} else if (method_to_call != null) {
				response = invoke_method(method_to_call, params);

			} else if ((method.toLowerCase() != "setup" && method.toLowerCase() != "reset") || method_to_call == null) {
				error_code = -32601;
				error_response = method + " isn't a function";
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String return_string;
		if (error_response.length() == 0) {
			return_string = formatSuccessResponse(jsonrpc, id, response);
		} else {
			JSONObject errorMsg = new JSONObject();
			errorMsg.put("code", error_code);
			errorMsg.put("message", error_response);
			return_string = formatErrorResponse(jsonrpc, id, errorMsg);
		}

    	return return_string.toString();
    }


    private static String getRequest() {
    	return null;
    }

    private static Method createAccount(String method) {
    	Class<CreateAccount> account = CreateAccount.class;
    	List<Method> methods = Stream.of(account.getMethods()).filter(i -> i.getName().toLowerCase().contains(method.toLowerCase())).collect(Collectors.toList());
    	return methods.get(0);
    }

	private static String invoke_method(Method method, JSONObject args) {
		String response = null;
			System.out.println("Invoking method " + method.getName());
			try {
				response = (String) method.invoke(method, args);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return response;
	}

    private static String formatSuccessResponse(String jsonRpcNum, Integer id, String message) {
    	JSONObject response_obj = new JSONObject();
		response_obj.put("jsonrpc", jsonRpcNum);
		response_obj.put("id", id);
		response_obj.put("result", message);
		return response_obj.toString();
    }

    private static String formatErrorResponse(String jsonRpcNum, Integer id, JSONObject errorMsg) {
    	JSONObject response_obj = new JSONObject();
		response_obj.put("jsonrpc", jsonRpcNum);
		response_obj.put("id", id);
		response_obj.put("error", errorMsg);
		return response_obj.toString();
    }
}

