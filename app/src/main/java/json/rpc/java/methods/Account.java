package json.rpc.java.methods;

import java.util.concurrent.TimeoutException;

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

public class Account {
	private static Client client = Client.forTestnet();
	
	@JsonRpcMethod
	public static AccountInfo getAccountInfo(AccountId accountId) {
		AccountInfoQuery query = new AccountInfoQuery().setAccountId(accountId);
		try {
			return query.execute(client);
		} catch (TimeoutException | PrecheckStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@JsonRpcMethod
	public static AccountId createAccount(PublicKey publicKey) {
		Integer accountBalance = 1000;
		return createAccount(publicKey, accountBalance);
	}
	
	@JsonRpcMethod
	public static AccountId createAccount(PublicKey publicKey, Integer accountBalance) {
		// Generate a new key pair
        PrivateKey newAccountPrivateKey = PrivateKey.generateED25519();
        PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();
        
        AccountId newAccountId = null;
        
        //Create new account and assign the public key
        TransactionResponse newAccount;
		try {
			newAccount = new AccountCreateTransaction()
			        .setKey(newAccountPublicKey)
			        .setInitialBalance(Hbar.fromTinybars(accountBalance))
			        .execute(client);
			
			try { 
				newAccountId = newAccount.getReceipt(client).accountId;
			} catch (ReceiptStatusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PrecheckStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newAccountId;
	}
	
	@JsonRpcMethod
	public static Status updateAccountKey(AccountId accountId, PublicKey newPublicKey, PrivateKey oldPrivateKey, PrivateKey newPrivateKey) {
		AccountUpdateTransaction transaction = new AccountUpdateTransaction().setAccountId(accountId).setKey(newPublicKey).freezeWith(null);
		AccountUpdateTransaction signTx = transaction.sign(oldPrivateKey).sign(newPrivateKey);
		try {
			TransactionResponse txResponse = signTx.execute(null);
			try {
				TransactionReceipt receipt = txResponse.getReceipt(null);
				return receipt.status;
			} catch (ReceiptStatusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PrecheckStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	@JsonRpcMethod
	private static TransactionReceipt deleteAccount(AccountId accountId, PrivateKey privateKey, AccountId recipientId) {
		AccountDeleteTransaction transaction = new AccountDeleteTransaction().setAccountId(accountId).setTransferAccountId(recipientId).freezeWith(null);
		AccountDeleteTransaction signTx = transaction.sign(privateKey);
		try {
			TransactionResponse txResponse = signTx.execute(null);
			try {
				TransactionReceipt receipt = txResponse.getReceipt(null);
				return receipt;
			} catch (ReceiptStatusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PrecheckStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}


