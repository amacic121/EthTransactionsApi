package dev.ethereum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {

    @Value("${infura.api.key}")
    private String backupApiKey;

    @Bean
    @RequestScope
    public Web3j web3j(@Value("#{request.getParameter('apiKey')}") String apiKey) {
        String infuraApiKey;
        if (apiKey != null) {
            infuraApiKey = apiKey;
        } else {
            infuraApiKey = backupApiKey;
        }
        return Web3j.build(new HttpService("https://mainnet.infura.io/v3/" + infuraApiKey));
    }
}

