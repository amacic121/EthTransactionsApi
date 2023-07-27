package dev.ethereum.model;

import lombok.*;
import java.math.BigInteger;

@Builder
@Data
public class EthTransactionDTO{

    private BigInteger amount;

    private String sender;

    private String receiver;
}
