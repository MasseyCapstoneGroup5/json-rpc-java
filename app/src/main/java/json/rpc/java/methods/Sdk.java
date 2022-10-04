package json.rpc.java.methods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;

public class Sdk {
	static Client client;
	/*
	 * @param operatorAccountId
	 * @param operatorPrivateKey
	 * @param nodeIp (optional)
	 * @param nodeAccountId (optional)
	 * @param mirrorNetworkIp (optional)
	 */
	
	public static Client setup(AccountId operatorAccountId, PrivateKey operatorPrivateKey, String nodeIp, Long nodeAccountId, 
			List<String> mirrorNetworkIp) throws InterruptedException {
		if (nodeIp != null && nodeAccountId != null && mirrorNetworkIp != null) {
			// Create client
			Map<String, AccountId> node = new HashMap<>();
			node.put(nodeIp, new AccountId(nodeAccountId));
			Sdk.client = Client.forNetwork(node).setMirrorNetwork(mirrorNetworkIp);
		} else {
			// Default to testnet client
			Sdk.client = Client.forTestnet();
		}
		operatorAccountId = AccountId.fromString("0.0.47769083");
		operatorPrivateKey = PrivateKey.fromString("302e020100300506032b657004220420ae09145d483b0e78cf2cb962856419b46903c7b4b018093521e7402df41b2e4c");
		
		Sdk.client.setOperator(operatorAccountId, operatorPrivateKey);
		return Sdk.client;
	}
	
	
	public static boolean reset() {
		Sdk.client = null;
		return true;
	}
}
