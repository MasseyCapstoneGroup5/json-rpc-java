package json.rpc.java;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import json.rpc.java.exceptions.InternalException;

import java.util.*;

public class Sdk {
    private static final Sdk instance;
    static Client client = null;

    private Sdk() {
    }

    static {
        instance = new Sdk();
    }

    public static Sdk getInstance() {
        return instance;
    }

    /**
     * Creates and sets Client custom node
     */
    public void setupCustomNode(
            String operatorAccountId,
            String operatorPrivateKey,
            String nodeIp, Long nodeAccountId,
            List<String> mirrorNetworkIp
    ) throws InterruptedException {
        Map<String, AccountId> node = new HashMap<>();
        node.put(nodeIp, new AccountId(nodeAccountId));
        resetClient();
        client = Client.forNetwork(node).setMirrorNetwork(mirrorNetworkIp);
        client.setOperator(AccountId.fromString(operatorAccountId), PrivateKey.fromString(operatorPrivateKey));
    }

    /**
     * Creates and sets Client for testnet
     */
    public void setupTestNet(String operatorAccountId, String operatorPrivateKey) {
        // Create testnet client
        resetClient();
        Sdk.client = Client.forTestnet();
        Sdk.client.setOperator(AccountId.fromString(operatorAccountId), PrivateKey.fromString(operatorPrivateKey));
    }

    public void resetClient() {
        Sdk.client = null;
    }

    public Client getClient() throws InternalException {
        if (Sdk.client == null) {
            throw new InternalException("Client not setup");
        }
        return Sdk.client;
    }

    public void setClient(Client client) {
        Sdk.client = client;
    }
}
