package json.rpc.java.methods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;

public class Sdk {
	private static final Sdk sdk = new Sdk();
	static Client client = null;

	private Sdk() {
	}
	
	public static Sdk getInstance() {
		return Sdk.sdk;
	}
	
	public static Client getClient() {
		return Sdk.client;
	}
	
	public static Client setup(String operatorAccountId, String operatorPrivateKey, String nodeIp, Long nodeAccountId, 
			List<String> mirrorNetworkIp) throws InterruptedException {
		/*
		 * @param operatorAccountId
		 * @param operatorPrivateKey
		 * @param nodeIp (optional)
		 * @param nodeAccountId (optional)
		 * @param mirrorNetworkIp (optional)
		 */
		if (nodeIp != null && nodeAccountId != null && mirrorNetworkIp != null) {
			// Create client
			Map<String, AccountId> node = new HashMap<>();
			node.put(nodeIp, new AccountId(nodeAccountId));
			Sdk.client = Client.forNetwork(node).setMirrorNetwork(mirrorNetworkIp);
		} else {
			// Default to testnet client
			Sdk.client = Client.forTestnet();
		}

		Sdk.client.setOperator(AccountId.fromString(operatorAccountId), PrivateKey.fromString(operatorPrivateKey));
		return Sdk.client;
	}
	
	
	public static boolean reset() {
		Sdk.client = null;
		return true;
	}
}
