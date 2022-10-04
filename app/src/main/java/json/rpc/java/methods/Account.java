package json.rpc.java.methods;
import json.rpc.java.methods.Sdk;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.AccountDeleteTransaction;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.AccountInfoQuery;
import com.hedera.hashgraph.sdk.AccountUpdateTransaction;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;

//import org.json.*;

public class Account {
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

		
//		// TODO: error checking here, make sure publicKey is specified
		PublicKey publicKey = PublicKey.fromString((String) args.get("publicKey"));
		PrivateKey newAccountPrivateKey = PrivateKey.generateED25519();
		PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();
		
		AccountId newAccountId = null;
      //Create new account and assign the public key
      TransactionResponse newAccount;
		try {
			newAccount = new AccountCreateTransaction()
			        .setKey(newAccountPublicKey)
			        .setInitialBalance(Hbar.fromTinybars(initialBalance))
			        .execute(client);
			newAccountId = newAccount.getReceipt(client).accountId;
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PrecheckStatusException e) {
			e.printStackTrace();
		} catch (ReceiptStatusException e) {
			e.printStackTrace();
		}
		
		System.out.println("new account id : " + newAccountId.toString());

		return newAccountId;
	}
	

	// Overloaded methods - doesn't work because it's based on classes
	
//	@JsonRpcMethod
//	public static AccountId createAccount(PublicKey publicKey) {
//		Integer accountBalance = 1000;
//		return createAccount(publicKey, accountBalance);
//	}
//	
//	@JsonRpcMethod
//	public static AccountId createAccount(PublicKey publicKey, Integer initialBalance) {
//		// Generate a new key pair
//        PrivateKey newAccountPrivateKey = PrivateKey.generateED25519();
//        PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();
//        
//        AccountId newAccountId = null;
//        
//        //Create new account and assign the public key
//        TransactionResponse newAccount;
//		try {
//			newAccount = new AccountCreateTransaction()
//			        .setKey(newAccountPublicKey)
//			        .setInitialBalance(Hbar.fromTinybars(initialBalance))
//			        .execute(client);
//			newAccountId = newAccount.getReceipt(client).accountId;
//		} catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (PrecheckStatusException e) {
//			e.printStackTrace();
//		} catch (ReceiptStatusException e) {
//			e.printStackTrace();
//		}
//
//		return newAccountId;
//	}
//	
//	@JsonRpcMethod
//	public static AccountInfo getAccountInfo(AccountId accountId) {
//		AccountInfoQuery query = new AccountInfoQuery().setAccountId(accountId);
//		try {
//			return query.execute(client);
//		} catch (TimeoutException | PrecheckStatusException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
//	@JsonRpcMethod
//	public static Status updateAccountKey(AccountId accountId, PublicKey newPublicKey, PrivateKey oldPrivateKey, PrivateKey newPrivateKey) {
//		AccountUpdateTransaction transaction = new AccountUpdateTransaction().setAccountId(accountId).setKey(newPublicKey).freezeWith(null);
//		AccountUpdateTransaction signTx = transaction.sign(oldPrivateKey).sign(newPrivateKey);
//		try {
//			TransactionResponse txResponse = signTx.execute(null);
//			try {
//				TransactionReceipt receipt = txResponse.getReceipt(null);
//				return receipt.status;
//			} catch (ReceiptStatusException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (PrecheckStatusException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//		
//	}
//	
//	@JsonRpcMethod
//	private static TransactionReceipt deleteAccount(AccountId accountId, PrivateKey privateKey, AccountId recipientId) {
//		AccountDeleteTransaction transaction = new AccountDeleteTransaction().setAccountId(accountId).setTransferAccountId(recipientId).freezeWith(null);
//		AccountDeleteTransaction signTx = transaction.sign(privateKey);
//		try {
//			TransactionResponse txResponse = signTx.execute(null);
//			try {
//				TransactionReceipt receipt = txResponse.getReceipt(null);
//				return receipt;
//			} catch (ReceiptStatusException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		} catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (PrecheckStatusException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return null;
//	}

}


