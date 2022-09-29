package json.rpc.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
 
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import json.rpc.java.methods.Account;
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
     
    public void handle(HttpExchange t) throws IOException {
 
        //Create a response form the request query parameters
    	String method = t.getRequestMethod().toLowerCase();
    	String response = "";
    	if (method.equals("get")) {
    		//URI uri = t.getRequestURI();
            //response = createResponseFromQueryParams(uri);
    		response = getRequest();
    	} else if (method.equals("post")) {
    		InputStream body = t.getRequestBody();
    		StringWriter writer = new StringWriter();
        	IOUtils.copy(body, writer, "UTF-8");
    		response = postRequest(writer.toString());
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
    	JSONObject params = (JSONObject) body_json.get("params");
    	Method method_to_call = null;
		try {
			method_to_call = account(method, params);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "Hello";
    }
    
    
    private static String getRequest() {
    	return null;
    }
    
    // json.rpc.java.methods.Account
	private static Method account(String method, JSONObject args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException { 
		Class<Account> account = Account.class;
		Iterator<String> keys = args.keys();
		ArrayList<String> string_keys = new ArrayList<>();
		while (keys.hasNext()) {
			string_keys.add(keys.next().toLowerCase());
		}
		
		for (Method account_method: account.getMethods()) {
			if (account_method.getName().toLowerCase().contains(method.toLowerCase())) {
				if (account_method.getParameterCount() == args.length()) {
					if (checkFunction(string_keys, get_parameter_names(account_method))) {
						return account_method;
					}
				}
			}
		}
		return null;
	}
	
	private static ArrayList<String> get_parameter_names(Method account_method) {
		ArrayList<String> parameters = new ArrayList<>();
		for (Class<?> param: account_method.getParameterTypes()) {
			String param_name = param.getSimpleName().toLowerCase();
			parameters.add(param_name);
		}
		return parameters;
	}
	
	private static String invoke_method(Method method, JSONObject args) {
		try {
			// convert each parameter to the correct type
			System.out.println("convert each parameter value to the desired type");
			
			//method.invoke(args, null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static Boolean checkFunction(ArrayList<String> keys, ArrayList<String> parameters) {
		return (keys.stream().filter(parameters::contains).count() == keys.size());
	}
     
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

