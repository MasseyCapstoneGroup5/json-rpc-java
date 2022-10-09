package json.rpc.java.exceptions;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcErrorData;

@JsonRpcError(code = -32001, message = "Hedera Error")
public class HederaException extends Exception {

    @JsonRpcErrorData
    HederaErrorData hederaErrorData;

    public HederaException(String status, String message) {
        hederaErrorData = new HederaErrorData(status, message);
    }
}

class HederaErrorData {
    private final String status;
    private final String message;

    public HederaErrorData(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}

