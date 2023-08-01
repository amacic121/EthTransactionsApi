async function fetchTransactions(walletAddress, blockNumber) {
    console.log('Fetching transactions...');
    try {
        const apiKey = document.getElementById('apiKey').value.trim();

        if (!apiKey) {
            alert('Please enter an API key.');
            return [];
        }

        const response = await fetch(`/api/ethereum/crawl?walletAddress=${walletAddress}&blockNumber=${blockNumber}&apiKey=${apiKey}`);
        const data = await response.json();
        console.log(data);
        return data;
    } catch (error) {
        console.error('Error fetching data:', error);
        return [];
    }
}

async function displayTransactions() {
    console.log('DOM content loaded');
    const walletAddressInput = document.getElementById('walletAddress');
    const blockNumberInput = document.getElementById('blockNumber');
    const fetchTransactionsBtn = document.getElementById('fetchTransactionsBtn');
    const transactionsContainer = document.getElementById('transactions-container');

    fetchTransactionsBtn.addEventListener('click', async () => {
        const walletAddress = walletAddressInput.value.trim();
        const blockNumber = parseInt(blockNumberInput.value);

        if (!walletAddress || isNaN(blockNumber)) {
            transactionsContainer.innerHTML = '<p>Please enter a valid wallet address, block number</p>';
            return;
        }

        try {
            transactionsContainer.innerHTML = '<p>Loading...</p>';

            const transactions = await fetchTransactions(walletAddress, blockNumber);

            if (transactions.length === 0) {
                transactionsContainer.innerHTML = '<p>No transactions found.</p>';
            } else {
                transactionsContainer.innerHTML = '';
                const table = document.createElement('table');
                table.classList.add('table', 'table-bordered', 'table-striped');
                const thead = document.createElement('thead');
                thead.innerHTML = `
                <tr>
                    <th>Sender</th>
                    <th>Receiver</th>
                    <th>Amount (ETH in Wei)</th>
                </tr>
            `;
                const tbody = document.createElement('tbody');
                transactions.forEach(transaction => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                    <td>${transaction.sender}</td>
                    <td>${transaction.receiver}</td>
                    <td>${transaction.amount} Wei</td>
                `;
                    tbody.appendChild(row);
                });
                table.appendChild(thead);
                table.appendChild(tbody);
                transactionsContainer.appendChild(table);
            }
        } catch (error) {
            transactionsContainer.innerHTML = '<p>Error fetching transactions.</p>';
        }
    });
}
async function fetchEthBalance(walletAddress, timestamp) {

   // const formattedTimestamp = formatDateFromInput(timestamp);
    //console.log("Formatiran Datum:", formattedTimestamp);

    try {
        const apiKey = document.getElementById('apiKey').value.trim(); // Get the API key from the input field

        if (!apiKey) {
            alert('Please enter an API key.');
            throw new Error("API key is missing.");
        }


        const ethGetBalanceResponse = await fetch(`/api/ethereum/balance?walletAddress=${walletAddress}&timestamp=${timestamp}&apiKey=${apiKey}`);
        const ethGetBalanceData = await ethGetBalanceResponse.text();

        console.log("ETH Balance Response:", ethGetBalanceData);

        return ethGetBalanceData;
    } catch (error) {
        throw new Error("Failed to fetch ETH balance.");
    }
}

async function displayEthBalance() {
    const walletAddressInput = document.getElementById('walletAddress');
    const timestampInput = document.getElementById('timestamp');
    const fetchEthBalanceBtn = document.getElementById('fetchEthBalanceBtn');
    const ethBalanceResult = document.getElementById('ethBalanceAtTimestamp');

    fetchEthBalanceBtn.addEventListener('click', async () => {
        const timestamp = timestampInput.value.trim();
        const walletAddress = walletAddressInput.value.trim();

        if (!timestamp) {
            ethBalanceResult.textContent = 'Please enter a valid timestamp';
            return;
        }

        //const formattedTimestamp = formatDateFromInput(timestamp);

        try {
            ethBalanceResult.textContent = 'Fetching ETH balance.......';
            const ethBalance = await fetchEthBalance(walletAddress, timestamp);
            ethBalanceResult.innerHTML = `<span class="highlighted">ETH Balance at ${timestamp}: ${ethBalance} ETH</span>`;
        } catch (error) {
            ethBalanceResult.textContent = 'Failed to fetch ETH balance.';
        }
    });
}

// function formatDateFromInput(inputDate) {
//     const date = new Date(inputDate);
//     const year = date.getUTCFullYear();
//     const month = String(date.getUTCMonth() + 1).padStart(2, '0');
//     const day = String(date.getUTCDate()).padStart(2, '0');
//     return `${year}-${month}-${day}`;
// }

document.addEventListener('DOMContentLoaded', function () {
    displayTransactions();
    displayEthBalance();
});

