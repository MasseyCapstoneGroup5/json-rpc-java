package json.rpc.java.services;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcParam;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;
import com.hedera.hashgraph.sdk.PrivateKey;

@JsonRpcService
public class KeyService {

    @JsonRpcMethod
    public String generatePublicKey(@JsonRpcParam("privateKey") String privateKey) {
        return PrivateKey.fromString(privateKey).getPublicKey().toString();
    }

    @JsonRpcMethod
    public String generatePrivateKey() {
        return PrivateKey.generateED25519().toString();
    }
}

