package dev.ethereum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class EthBalanceAtCertainTimeService {

    @Autowired
    private Web3j web3j;

    public double getEthBalanceAtTimestamp(String walletAddress, String timestamp) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = simpleDateFormat.parse(timestamp);

        long timestampUtc = date.getTime() / 1000;

        try {
            BigInteger blockNumber = getBlockNumberForTimestamp(timestampUtc);

            EthGetBalance ethGetBalance = web3j.ethGetBalance(walletAddress, DefaultBlockParameter.valueOf(blockNumber))
                    .send();

            BigInteger balanceInWei = ethGetBalance.getBalance();
            BigDecimal balanceInEth = Convert.fromWei(balanceInWei.toString(), Convert.Unit.ETHER);

            double balanceInEthDouble = balanceInEth.doubleValue();

            return balanceInEthDouble;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    private BigInteger getBlockNumberForTimestamp(long timestampUtc) throws Exception {
        BigInteger latestBlockNumber = web3j.ethBlockNumber().send().getBlockNumber();
        BigInteger start = BigInteger.ONE;
        BigInteger end = latestBlockNumber;
        BigInteger closestBlockNumber = BigInteger.ZERO;

        while (start.compareTo(end) <= 0) {
            BigInteger mid = start.add(end).divide(BigInteger.TWO);

            EthBlock.Block block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(mid), false)
                    .send()
                    .getBlock();

            if (block == null) {
                break;
            }

            long blockTimestamp = block.getTimestamp().longValue();

            if (blockTimestamp == timestampUtc) {
                return mid;
            } else if (blockTimestamp < timestampUtc) {
                start = mid.add(BigInteger.ONE);
            } else {
                end = mid.subtract(BigInteger.ONE);
            }

            if (Math.abs(blockTimestamp - timestampUtc) < Math.abs(closestBlockNumber.longValue() - timestampUtc)) {
                closestBlockNumber = mid;
            }
        }
        if (closestBlockNumber.equals(BigInteger.ZERO)) {
            throw new Exception("404");
        }
        return closestBlockNumber;
    }
}