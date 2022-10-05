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
	@JsonRpcMethod
	public static String createAccount(Object obj_args) {
		/**
		 * @param publicKey required
		 * @param initialBalance optional 
		 * @param receiverSignatureRequired optional
		 * @param maxAutomaticTokenAssociations optional
		 * @param stakedAccountId optional
		 * @param declineStakingReward optional
		 * @param accountMemo optional
		 */
		
		String response = "";
		
		Client client = Sdk.getClient();
		if (client == null) {
			return "Please run setup API call first";
		}
		Integer initialBalance = 1000;
		Boolean receiverSignatureRequired = null;
		Integer maxAutomaticTokenAssociations = null;
//		AccountId stakedAccountId = null;
//		Long stakedNodeId = null;
//		Boolean declineStakingReward = null;
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
//			} else if (key.toLowerCase().equals("stakedaccountid")) {
//				stakedAccountId = (AccountId) args.get(key);
//			} else if (key.toLowerCase().equals("stakednodeid")) {
//				stakedNodeId = (Long) args.get(key);
//			} else if (key.toLowerCase().equals("declinestakingreward")) {
//				declineStakingReward = (Boolean) args.get(key);
			} else if (key.toLowerCase().equals("accountmemo")) {
				accountMemo = args.getString(key);
			}
		}
//		
//		// TODO: error checking here + make sure publicKey is specified
		
        PrivateKey newAccountPrivateKey = PrivateKey.generateED25519();
        PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();
        AccountId newAccountId = null;
        TransactionResponse newAccount = null;
        AccountCreateTransaction accountCreateTransaction = null;
		try {
			accountCreateTransaction = new AccountCreateTransaction()
			        .setKey(newAccountPublicKey)
			        .setInitialBalance( Hbar.fromTinybars(initialBalance));
			if (receiverSignatureRequired != null) {
				accountCreateTransaction.setReceiverSignatureRequired(receiverSignatureRequired);
			}
			if (maxAutomaticTokenAssociations != null && maxAutomaticTokenAssociations != 0) {
				accountCreateTransaction.setMaxAutomaticTokenAssociations(maxAutomaticTokenAssociations);
			}
			// TODO: below methods aren't available for Java
//			if (stakedAccountId != null) {
//				accountCreateTransaction.setStakedAccountId(stakedAccountId);
//			}
			
//			if (stakedNodeId != null) {
//				accountCreateTransaction.setStakedNodeId(stakedNodeId);
//			}
			
//			if (declineStakingReward != null) {
//				accountCreateTransaction.setDeclineStakingReward(declineStakingReward);
//			}
//			
			if (accountMemo != null) {
				accountCreateTransaction.setAccountMemo(accountMemo);
			}
			newAccount = accountCreateTransaction.execute(client);
			newAccountId = newAccount.getReceipt(client).accountId;
			response = "Successfully created new account";
		} catch (TimeoutException | PrecheckStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReceiptStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return newAccountId.toString();
	}
	

}
