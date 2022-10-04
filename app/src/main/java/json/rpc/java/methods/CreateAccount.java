package json.rpc.java.methods;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.hedera.hashgraph.sdk.TransactionResponse;

public class CreateAccount {
	private static Client client = Sdk.client;
	
	@JsonRpcMethod
	public static AccountId createAccount(Object obj_args) {
		/**
		 * @param publicKey required
		 * @param initialBalance optional 
		 * @param receiverSignatureRequired optional
		 * @param maxAutomaticTokenAssociations optional
		 * @param stakedAccountId optional
		 * @param declineStakingReward optional
		 * @param accountMemo optional
		 */
		Integer initialBalance = 1000;
		Boolean receiverSignatureRequired = null;
		Integer maxAutomaticTokenAssociations = null;
		AccountId stakedAccountId = null;
		AccountId stakedNodeId = null;
		Boolean declineStakingReward = null;
		String accountMemo = null;
		
		JSONObject args = (JSONObject) obj_args;
		Iterator<String> keys = args.keys();	
		List<String> string_keys = Stream.of(keys).map(i -> keys.next().toLowerCase()).collect(Collectors.toList());
		
		for (String key: string_keys) {
			if (key.toLowerCase().equals("initalbalance")) {
				initialBalance = (Integer) args.get(key);
			} else if (key.toLowerCase().equals("receiversignaturerequired")) {
				receiverSignatureRequired = (Boolean) args.get(key);
			} else if (key.toLowerCase().equals("maxautomatictokenassociations")) {
				maxAutomaticTokenAssociations = (Integer) args.get(key);
			} else if (key.toLowerCase().equals("stakedaccountid")) {
				stakedAccountId = (AccountId) args.get(key);
			} else if (key.toLowerCase().equals("stakednodeid")) {
				stakedNodeId = (AccountId) args.get(key);
			} else if (key.toLowerCase().equals("declinestakingreward")) {
				declineStakingReward = (Boolean) args.get(key);
			} else if (key.toLowerCase().equals("accountmemo")) {
				accountMemo = args.getString(key);
			}
		}
//		
//		// TODO: error checking here, make sure publicKey is specified
		PublicKey publicKey = PublicKey.fromString((String) args.get("publicKey"));
		PrivateKey newAccountPrivateKey = PrivateKey.generateED25519();
		PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();
		AccountId newAccountId = null;
      //Create new account and assign the public key
      TransactionResponse newAccount = null;
//		try {
		try { 
			newAccount = new AccountCreateTransaction()
					.setKey(newAccountPublicKey)
			        .setInitialBalance(Hbar.fromTinybars(initialBalance))
			        .execute(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			newAccountId = newAccount.getReceipt(client).accountId;
		} catch (Exception e) {
			System.out.println("2");
			e.printStackTrace();
		}
			        
			System.out.println(client);
			
			System.out.println("here");
//		} catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (PrecheckStatusException e) {
//			e.printStackTrace();
//		} catch (ReceiptStatusException e) {
//			e.printStackTrace();
//		}
		
		System.out.println("new account id : " + newAccountId.toString());

		return newAccountId;
	}
	

}
