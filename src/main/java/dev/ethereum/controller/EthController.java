package dev.ethereum.controller;

import dev.ethereum.model.EthTransactionDTO;
import dev.ethereum.service.EthTransactionService;
import dev.ethereum.service.EthBalanceAtCertainTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/ethereum")
public class EthController {

    @Autowired
    private EthTransactionService ethTransactionService;
    @Autowired
    private EthBalanceAtCertainTimeService ethBalanceAtCertainTimeService;
    @GetMapping("/crawl")
    public List<EthTransactionDTO> crawlTransactions(@RequestParam String walletAddress, @RequestParam Long blockNumber, @RequestParam(required = false) String apiKey) {
        return ethTransactionService.crawlTransactions(walletAddress, blockNumber);
    }

    @GetMapping("/balance")
    public double getEthBalanceAtTimestamp(@RequestParam String walletAddress, @RequestParam String timestamp, @RequestParam(required = false) String apiKey) throws ParseException {
        return ethBalanceAtCertainTimeService.getEthBalanceAtTimestamp(walletAddress, timestamp);
    }
}
