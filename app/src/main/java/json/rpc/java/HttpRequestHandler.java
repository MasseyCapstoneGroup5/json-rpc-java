package json.rpc.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import org.apache.commons.io.IOUtils;
 
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import json.rpc.java.methods.Account;
 
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
    	String method = t.getRequestMethod();
    	InputStream body = t.getRequestBody();
    	System.out.println(method);
    	StringWriter writer = new StringWriter();
    	IOUtils.copy(body, writer, "UTF-8");
    	System.out.println(writer.toString());
        URI uri = t.getRequestURI();
        String response = createResponseFromQueryParams(uri);
        System.out.println("Response: " + response);
        //Set the response header status and length
        t.sendResponseHeaders(HTTP_OK_STATUS, response.getBytes().length);
        //Write the response string
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
    
	// TODO: implement this so the correct function is called with the correct number of arguments etc. 
	private static Object callAccountFunction(String method, Object args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException { 
		Class<Account> account = Account.class;
		for (Method account_method: account.getMethods()) {
			if (account_method.getName().toLowerCase().contains(method.toLowerCase())) {
				System.out.println("call method " + method);
				System.out.println(account_method.getParameterCount());
				//return account_method.invoke(null, null);
			}
		}
		return null;
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
            System.out.println("Query: " + query);
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

