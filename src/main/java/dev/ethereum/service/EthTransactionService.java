package dev.ethereum.service;

import dev.ethereum.model.EthTransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class EthTransactionService {

    @Autowired
    private Web3j web3j;

    public List<EthTransactionDTO> crawlTransactions(String walletAddress, Long blockNumber) {
        List<EthTransactionDTO> transactionList = new ArrayList<>();

        try {
            BigInteger latestBlockNumber = web3j.ethBlockNumber().send().getBlockNumber();

            for (BigInteger i = BigInteger.valueOf(blockNumber); i.compareTo(latestBlockNumber) <= 0; i = i.add(BigInteger.ONE)) {
                EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(i), true)
                        .send()
                        .getBlock();

                List<EthBlock.TransactionResult> transactions = block.getTransactions();

                for (EthBlock.TransactionResult transactionResult : transactions) {

                    Transaction transaction = (Transaction) transactionResult.get();

                    String sender = transaction.getFrom();
                    String receiver = transaction.getTo();
                    BigInteger amount = transaction.getValue();

                    if (receiver == null || sender == null)
                        continue;

                    if (sender.equalsIgnoreCase(walletAddress) || receiver.equalsIgnoreCase(walletAddress)) {
                        EthTransactionDTO transactionInfo = EthTransactionDTO.builder()
                                .sender(sender)
                                .receiver(receiver)
                                .amount(amount)
                                .build();

                        transactionList.add(transactionInfo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactionList;
    }
}
