package dev.ethereum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EtherumApplication {

	public static void main(String[] args) {
		SpringApplication.run(EtherumApplication.class, args);
	}

}
