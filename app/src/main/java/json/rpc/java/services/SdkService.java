package json.rpc.java.services;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcOptional;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcParam;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;
import json.rpc.java.Sdk;

import java.util.*;

@JsonRpcService
public class SdkService {

    @JsonRpcMethod
    public HashMap<String, String> setup(
            @JsonRpcParam("operatorAccountId") String operatorAccountId,
            @JsonRpcParam("operatorPrivateKey") String operatorPrivateKey,
            @JsonRpcOptional @JsonRpcParam("nodeIp") String nodeIp,
            @JsonRpcOptional @JsonRpcParam("nodeAccountId") String nodeAccountId,
            @JsonRpcOptional @JsonRpcParam("mirrorNetworkIp") String mirrorNetworkIp
    ) throws InterruptedException {
        String clientType;
        if (nodeIp != null && nodeAccountId != null && mirrorNetworkIp != null) {
            Sdk.getInstance().setupCustomNode(
                    operatorAccountId,
                    operatorPrivateKey,
                    nodeIp,
                    Long.parseLong(nodeAccountId),
                    new ArrayList<>(Collections.singleton(mirrorNetworkIp))
            );
            clientType = "custom";
        } else {
            Sdk.getInstance().setupTestNet(
                    operatorAccountId,
                    operatorPrivateKey
            );
            clientType = "testnet";
        }
        HashMap<String, String> responseMap = new HashMap<>();
        responseMap.put("status", "SUCCESS");
        responseMap.put("message", "Successfully setup " + clientType + " client.");

        return responseMap;
    }

    @JsonRpcMethod
    public HashMap<String, String> reset() {
        Sdk.getInstance().resetClient();
        HashMap<String, String> responseMap = new HashMap<>();
        responseMap.put("status", "SUCCESS");
        return responseMap;
    }
}

