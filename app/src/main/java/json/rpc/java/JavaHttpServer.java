package json.rpc.java;

import java.io.IOException;
import java.net.InetSocketAddress;
 
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class JavaHttpServer {
	
    private HttpServer httpServer;
    
    /**
     * Instantiates a new simple http server.
     *
     * @param port the port
     * @param context the context
     * @param handler the handler
     */
    public JavaHttpServer(int port, String context, HttpHandler handler) {
        try {
            //Create HttpServer which is listening on the given port 
        	InetSocketAddress socket = new InetSocketAddress(port);
            httpServer = HttpServer.create(socket, 0);
            //Create a new context for the given context and handler
            httpServer.createContext(context, handler);
            //Create a default executor
            httpServer.setExecutor(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
 
    /**
     * Start.
     */
    public void start() {
        this.httpServer.start();
    }
    
    public void setTimeout(int timeout) {
    	this.httpServer.stop(timeout);
    }
}
