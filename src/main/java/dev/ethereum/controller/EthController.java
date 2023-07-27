package dev.ethereum.controller;

import dev.ethereum.model.EthTransactionDTO;
import dev.ethereum.service.EthTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ethereum")
public class EthController {

    @Autowired
    private EthTransactionService ethTransactionService;

    @GetMapping("/crawl")
    public List<EthTransactionDTO> crawlTransactions(@RequestParam String walletAddress, @RequestParam Long blockNumber) {
        return ethTransactionService.crawlTransactions(walletAddress, blockNumber);
    }
}
