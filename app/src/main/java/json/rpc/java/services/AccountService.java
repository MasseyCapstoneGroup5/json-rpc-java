package json.rpc.java.services;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcOptional;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcParam;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;
import com.hedera.hashgraph.sdk.*;
import json.rpc.java.Sdk;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import json.rpc.java.exceptions.HederaException;
import json.rpc.java.exceptions.InternalException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@JsonRpcService
public class AccountService {
    @JsonRpcMethod
    public Map<String, Object> createAccount(
            @JsonRpcOptional @JsonRpcParam("publicKey") String publicKey,
            @JsonRpcOptional @JsonRpcParam("initialBalance") Long initialBalance,
            @JsonRpcOptional @JsonRpcParam("receiverSignatureRequired") Boolean receiverSignatureRequired,
            @JsonRpcOptional @JsonRpcParam("maxAutomaticTokenAssociations") Integer maxAutomaticTokenAssociations,
            @JsonRpcOptional @JsonRpcParam("stakedAccountId") String stakedAccountId,
            @JsonRpcOptional @JsonRpcParam("stakedNodeId") Long stakedNodeId,
            @JsonRpcOptional @JsonRpcParam("declineStakingReward") Boolean declineStakingReward,
            @JsonRpcOptional @JsonRpcParam("accountMemo") String accountMemo,
            @JsonRpcOptional @JsonRpcParam("privateKey") String privateKey,
            @JsonRpcOptional @JsonRpcParam("autoRenewPeriod") Long autoRenewPeriod
    ) throws InternalException, HederaException {

        Client client = Sdk.getInstance().getClient();
        try {
            //Create the transaction
            AccountCreateTransaction transaction = new AccountCreateTransaction();
            if (publicKey != null) transaction.setKey(PublicKey.fromString(publicKey));
            if (initialBalance != null) transaction.setInitialBalance(Hbar.fromTinybars(initialBalance));
            if (receiverSignatureRequired != null) transaction.setReceiverSignatureRequired(receiverSignatureRequired);
            if (maxAutomaticTokenAssociations != null)
                transaction.setMaxAutomaticTokenAssociations(maxAutomaticTokenAssociations);
            if (stakedAccountId != null) transaction.setStakedAccountId(AccountId.fromString(stakedAccountId));
            if (stakedNodeId != null) transaction.setStakedNodeId(stakedNodeId);
            if (declineStakingReward != null) transaction.setDeclineStakingReward(declineStakingReward);
            if (accountMemo != null) transaction.setAccountMemo(accountMemo);
            if (autoRenewPeriod != null) transaction.setAutoRenewPeriod(Duration.ofSeconds(autoRenewPeriod));
            if (privateKey != null) {
                //Sign the transaction with the private key
                transaction.freezeWith(client);
                transaction = transaction.sign(PrivateKey.fromString(privateKey));
            }
            

            //Submit the transaction to a Hedera network
            TransactionResponse txResponse = transaction.execute(client);
            //Request the receipt of the transaction
            TransactionReceipt receipt = txResponse.getReceipt(client);

            HashMap<String, Object> receiptMap = new HashMap<>();
            receiptMap.put("accountId", Objects.requireNonNull(receipt.accountId).toString());
            receiptMap.put("status", receipt.status.toString());

            return receiptMap;
        } catch (PrecheckStatusException e) {
            throw new HederaException(e.status.toString(), e.getMessage());
        } catch (ReceiptStatusException e) {
            throw new HederaException(e.receipt.status.toString(), e.getMessage());
        } catch (TimeoutException e) {
            throw new InternalException(e.getMessage());
        }
    }


    @JsonRpcMethod
    public Map<String, Object> getAccountInfo(@JsonRpcParam("accountId") String accountId) throws InternalException, HederaException {

        Client client = Sdk.getInstance().getClient();
        try {
            //Create the account info query
            AccountInfoQuery query = new AccountInfoQuery().setAccountId(AccountId.fromString(accountId));
            //Submit the query to a Hedera network
            AccountInfo accountInfo = query.execute(client);

            // Convert accountInfo Object to map (not all values have been implemented)
            HashMap<String, Object> accountInfoMap = new HashMap<>();
            accountInfoMap.put("accountId", accountInfo.accountId.toString());
            accountInfoMap.put("balance", accountInfo.balance.getValue());
            accountInfoMap.put("key", accountInfo.key.toString());
            accountInfoMap.put("accountMemo", accountInfo.accountMemo);
            accountInfoMap.put("autoRenewPeriod", accountInfo.autoRenewPeriod);

            return accountInfoMap;
        } catch (PrecheckStatusException e) {
            throw new HederaException(e.status.toString(), e.getMessage());
        } catch (TimeoutException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @JsonRpcMethod
    public Map<String, Object> updateAccountKey(
            @JsonRpcParam("accountId") String accountId,
            @JsonRpcParam("newPublicKey") String newPublicKey,
            @JsonRpcParam("oldPrivateKey") String oldPrivateKey,
            @JsonRpcParam("newPrivateKey") String newPrivateKey
    ) throws InternalException, HederaException {

        Client client = Sdk.getInstance().getClient();
        try {
            // update the key on the account
            // Create the transaction to replace the key on the account
            AccountUpdateTransaction transaction = new AccountUpdateTransaction()
                    .setAccountId(AccountId.fromString(accountId))
                    .setKey(PublicKey.fromString(newPublicKey))
                    .freezeWith(client);

            //Sign the transaction with the key
            transaction = transaction.sign(PrivateKey.fromString(oldPrivateKey));
            transaction = transaction.sign(PrivateKey.fromString(newPrivateKey));

            //Submit the transaction to a Hedera network
            TransactionResponse txResponse = transaction.execute(client);
            //Request the receipt of the transaction
            TransactionReceipt receipt = txResponse.getReceipt(client);

            HashMap<String, Object> receiptMap = new HashMap<>();
            receiptMap.put("status", receipt.status.toString());
            return receiptMap;

        } catch (PrecheckStatusException e) {
            throw new HederaException(e.status.toString(), e.getMessage());
        } catch (ReceiptStatusException e) {
            throw new HederaException(e.receipt.status.toString(), e.getMessage());
        } catch (TimeoutException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @JsonRpcMethod
    public Map<String, Object> updateAccountMemo(
            @JsonRpcParam("accountId") String accountId,
            @JsonRpcParam("key") String key,
            @JsonRpcParam("memo") String memo
    ) throws InternalException, HederaException {

        Client client = Sdk.getInstance().getClient();
        try {
            // update the account memo field
            // Create the transaction to update the memo on the account
            AccountUpdateTransaction transaction = new AccountUpdateTransaction()
                    .setAccountId(AccountId.fromString(accountId))
                    .setAccountMemo(memo)
                    .freezeWith(client);

            //Sign the transaction with the key
            transaction = transaction.sign(PrivateKey.fromString(key));

            //Submit the transaction to a Hedera network
            TransactionResponse txResponse = transaction.execute(client);
            //Request the receipt of the transaction
            TransactionReceipt receipt = txResponse.getReceipt(client);

            HashMap<String, Object> receiptMap = new HashMap<>();
            receiptMap.put("status", receipt.status.toString());
            return receiptMap;

        } catch (PrecheckStatusException e) {
            throw new HederaException(e.status.toString(), e.getMessage());
        } catch (ReceiptStatusException e) {
            throw new HederaException(e.receipt.status.toString(), e.getMessage());
        } catch (TimeoutException e) {
            throw new InternalException(e.getMessage());
        }
    }
    @JsonRpcMethod
    public Map<String, Object> deleteAccount(
            @JsonRpcParam("accountId") String accountId,
            @JsonRpcParam("accountKey") String accountKey,
            @JsonRpcParam("recipientId") String recipientId
    ) throws InternalException, HederaException {

        Client client = Sdk.getInstance().getClient();
        try {
            // update the account memo field
            // Create the transaction to update the memo on the account
            AccountDeleteTransaction transaction = new AccountDeleteTransaction()
                    .setAccountId(AccountId.fromString(accountId))
                    .setTransferAccountId(AccountId.fromString(recipientId))
                    .freezeWith(client);

            //Sign the transaction with the key
            transaction = transaction.sign(PrivateKey.fromString(accountKey));

            //Submit the transaction to a Hedera network
            TransactionResponse txResponse = transaction.execute(client);
            //Request the receipt of the transaction
            TransactionReceipt receipt = txResponse.getReceipt(client);

            HashMap<String, Object> receiptMap = new HashMap<>();
            receiptMap.put("status", receipt.status.toString());
            return receiptMap;

        } catch (PrecheckStatusException e) {
            throw new HederaException(e.status.toString(), e.getMessage());
        } catch (ReceiptStatusException e) {
            throw new HederaException(e.receipt.status.toString(), e.getMessage());
        } catch (TimeoutException e) {
            throw new InternalException(e.getMessage());
        }
    }


}
