package json.rpc.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
//import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import json.rpc.java.methods.CreateAccount;
import json.rpc.java.methods.Sdk;
import org.json.*;


/**
 * @author ashraf
 * Code retrieved from https://examples.javacodegeeks.com/core-java/sun/net-sun/httpserver-net-sun/httpserver-net-sun-httpserver-net-sun/com-sun-net-httpserver-httpserver-example/
 *
 */
@SuppressWarnings("restriction")
public class HttpRequestHandler implements HttpHandler {
     
    private static final String F_NAME = "fname";
    private static final String L_NAME = "lname";
     
    private static final int PARAM_NAME_IDX = 0;
    private static final int PARAM_VALUE_IDX = 1;
     
    private static final int HTTP_OK_STATUS = 200;
     
    private static final String AND_DELIMITER = "&";
    private static final String EQUAL_DELIMITER = "=";
    
    private static Client client = null; 
     
    public void handle(HttpExchange t) throws IOException {
        //Create a response form the request query parameters
    	String method = t.getRequestMethod().toLowerCase();
    	String response = "";
    	if (method.equals("get")) {
    		//URI uri = t.getRequestURI();
            //response = createResponseFromQueryParams(uri);
    		response = getRequest();
    	} else if (method.equals("post")) {
    		if (t.getAttribute("method") == "reset") {
    			Boolean s_reset = Sdk.reset();
    			if (s_reset == true) { 
    				response = "Reset client";
    			} else {
    				response = "Failed to reset client";
    			}
    			
    		} else {
        		InputStream body = t.getRequestBody();
        		StringWriter writer = new StringWriter();
            	IOUtils.copy(body, writer, "UTF-8");
            	String str_body = writer.toString();
            	System.out.println(str_body);
        		response = postRequest(str_body);
    		}
    	}
    	
        //URI uri = t.getRequestURI();
        //String response = createResponseFromQueryParams(uri);
        System.out.println("Response: " + response);
        //Set the response header status and length
        t.sendResponseHeaders(HTTP_OK_STATUS, response.getBytes().length);
        //Write the response string
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
    
    private static String postRequest(String body) {
    	JSONObject body_json = new JSONObject(body);
    	String method = body_json.get("method").toString();
    	JSONObject params = null;
    	try  {
    		params = (JSONObject) body_json.get("params");
    	} catch (Exception e) {
    		
    	}
    	Method method_to_call = null;
    	Object response = null;
    	
		try {
			//method_to_call = account(method, params);
			if (method.toLowerCase().equals("setup")) {
				AccountId operatorAccountId = AccountId.fromString((String) params.get("operatorAccountId"));
				PrivateKey operatorPrivateKey = PrivateKey.fromString((String) params.get("operatorPrivateKey"));
				client = Sdk.setup(operatorAccountId, operatorPrivateKey, null, null, null);
			} else if (method.toLowerCase().contains("createaccount")) {
				method_to_call = createAccount(method);
			}

			
			if (method.toLowerCase().equals("reset")) {
				Boolean reset = Sdk.reset();
				
				if (reset) {
					response = "Successfully reset client";
				} else {
					response = "Unable to reset client";
				}
			} else if (method.toLowerCase().contains("setup")) {
				response = "Successfully setup client";
				
			} else if (method_to_call != null) {
				response = invoke_method(method_to_call, params);
			} else if (method.toLowerCase() != "setup" && method.toLowerCase() != "reset") {
				return method + " isn't a function";
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
    	return response.toString();
    }
    
    
    private static String getRequest() {
    	return null;
    }
    
    // json.rpc.java.methods.Account
    // check through the methods and find one that you can call from the method name and parameter names passed through
//	private static Method account(String method, JSONObject args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException { 
//		Class<Account> account = Account.class;
//		Iterator<String> keys = args.keys();
//		List<String> string_keys = Stream.of(keys).map(i -> keys.next().toLowerCase()).collect(Collectors.toList());
//		List<Method> methods = Stream.of(account.getMethods()).filter(i -> i.getName().toLowerCase().contains(method.toLowerCase()) 
//				&& checkFunction(string_keys, get_parameter_names(i))).collect(Collectors.toList());
//		return methods.get(0);
//	}
	
//	private static List<String> get_parameter_names(Method account_method) {
//		return Stream.of(account_method.getParameterTypes()).map(i -> i.getSimpleName().toLowerCase()).collect(Collectors.toList());
//	}
    
    private static Method createAccount(String method) {
    	Class<CreateAccount> account = CreateAccount.class;
    	List<Method> methods = Stream.of(account.getMethods()).filter(i -> i.getName().toLowerCase().contains(method.toLowerCase())).collect(Collectors.toList());
    	return methods.get(0);
    }
	
	private static Object invoke_method(Method method, JSONObject args) {
		Object response = null;
		try {
			System.out.println("Invoking method " + method.getName());
			response = method.invoke(method, args);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	// check to see if passed in parameters match the parameters for the method
	// baring in mind that methods will be overloaded
//	private static Boolean checkFunction(List<String> keys, List<String> parameters) {
//		return (keys.stream().filter(parameters::contains).count() == keys.size());
//	}
//     
    /**
     * Creates the response from query params.
     *
     * @param uri the uri
     * @return the string
     */
    private String createResponseFromQueryParams(URI uri) {
         
        String fName = "";
        String lName = "";
        //Get the request query
        String query = uri.getQuery();
        if (query != null) {
            String[] queryParams = query.split(AND_DELIMITER);
            if (queryParams.length > 0) {
                for (String qParam : queryParams) {
                    String[] param = qParam.split(EQUAL_DELIMITER);
                    if (param.length > 0) {
                        for (int i = 0; i < param.length; i++) {
                            if (F_NAME.equalsIgnoreCase(param[PARAM_NAME_IDX])) {
                                fName = param[PARAM_VALUE_IDX];
                            }
                            if (L_NAME.equalsIgnoreCase(param[PARAM_NAME_IDX])) {
                                lName = param[PARAM_VALUE_IDX];
                            }
                        }
                    }
                }
            }
        }
         
        return "Hello, " + fName + " " + lName;
    }
}

