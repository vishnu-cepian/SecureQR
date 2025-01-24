package com.example.secureqr

import android.util.Log
import android.widget.Toast
//import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import com.example.secureqr.blockchain.QRHashRegistry
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//import android.util.Log
//import org.web3j.protocol.Web3j
//import org.web3j.protocol.http.HttpService
import java.math.BigInteger

// Blockchain Helper Class
class BlockchainHelper (private val context : Context) {
init {
    connectToEthereum()
}
    private val web3j: Web3j = Web3j.build(HttpService("https://sepolia.infura.io/v3/3764887512834ed9b5b729eaba91ee42"))
//        .also {
////        val clientVersion = it.web3ClientVersion().send().getWeb3ClientVersion()
//        Toast.makeText(context, "credentials : ${it.web3ClientVersion().send().getWeb3ClientVersion()}", Toast.LENGTH_SHORT).show()
//    }

//    https://mainnet.infura.io/v3/3764887512834ed9b5b729eaba91ee42
    private val contractAddress = "0x3736c6beFAdfa5F751815f1E327fE438140890E8"
    private val privateKey = "3764887512834ed9b5b729eaba91ee42"

    // Load smart contract
    private fun loadContract(): QRHashRegistry {
        val credentials = Credentials.create(privateKey)
        Toast.makeText(context, "credentials : $credentials", Toast.LENGTH_SHORT).show()
        System.out.println("BlockchainHelper: Connected to Ethereum client version: $credentials")

        return QRHashRegistry.load(contractAddress, web3j, credentials, DefaultGasProvider())
    }

    // Check if hash is malicious
    fun checkIfHashExists(qrHash: String, callback: (Boolean) -> Unit) {
        Toast.makeText(context, "inside checkIfHashExists", Toast.LENGTH_SHORT).show()
        val contract = loadContract()
        val hashBytes = qrHash.toByteArray()
        Toast.makeText(context, "contract : $contract", Toast.LENGTH_SHORT).show()
        Toast.makeText(context, "hashBytes: $hashBytes", Toast.LENGTH_SHORT).show()
        Thread {
            try {
                val isMalicious = contract.isHashMalicious(hashBytes).send()
                callback(isMalicious)
            } catch (e: Exception) {

                Log.e("QRScan", "Error checking hash: ${e.message}")
                callback(false) // In case of error, return false
            }
        }.start()
    }

    // Add new hash to the blockchain (for malicious hash)
    fun addHashToBlockchain(qrHash: String, callback: (Boolean) -> Unit) {
        val contract = loadContract()
        val hashBytes = qrHash.toByteArray()
        Thread {
            try {
                val transactionReceipt = contract.addQRHash(hashBytes).send()
                val success = transactionReceipt.status == "0x1" // Status 0x1 means success
                callback(success)
            } catch (e: Exception) {
                Log.e("QRScan", "Error adding hash: ${e.message}")
                callback(false)
            }
        }.start()
    }
    private fun connectToEthereum() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val web3j: Web3j = Web3j.build(HttpService("https://sepolia.infura.io/v3/YOUR_INFURA_PROJECT_ID"))
                val version = web3j.web3ClientVersion().send().web3ClientVersion
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Connected to Ethereum client Version : $version", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    System.out.println("Error: ${e.message}")

                }
            }
        }
    }
}
