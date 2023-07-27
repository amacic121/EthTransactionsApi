package dev.ethereum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {

    @Value("${infura.api.key}")
    private String infuraApiKey;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService("https://mainnet.infura.io/v3/" + infuraApiKey));
    }
}
