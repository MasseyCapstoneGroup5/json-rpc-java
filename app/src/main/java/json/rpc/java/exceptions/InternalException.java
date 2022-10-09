package json.rpc.java.exceptions;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcErrorData;


@JsonRpcError(code = -32603, message = "Internal error")
public class InternalException extends Exception {

    @JsonRpcErrorData
    InternalErrorData internalErrorData;

    public InternalException(String message) {
        internalErrorData = new InternalErrorData(message);
    }

}

class InternalErrorData {

    private final String message;

    public InternalErrorData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

