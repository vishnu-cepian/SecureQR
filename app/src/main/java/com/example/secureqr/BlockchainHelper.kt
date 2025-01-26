package com.example.secureqr

import android.util.Log
import android.widget.Toast
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import com.example.secureqr.blockchain.QRHashRegistry
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Convert
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.core.methods.response.EthTransaction
import org.web3j.protocol.core.methods.response.Transaction
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.utils.Numeric
import java.math.BigInteger

class BlockchainHelper (private val context : Context) {
init {
    connectToEthereum()
}
    private val web3j: Web3j = Web3j.build(HttpService("https://sepolia.infura.io/v3/3764887512834ed9b5b729eaba91ee42"))
    private val contractAddress = "0xe469542Ca15A06D13597DA19a9EB15E3d97F8EF3"
    private val privateKey = "de849daa7b187f7ce7e15fa7d966211d66f6600e2f2756a3b15093871bdff76b"

//    0x3d95E7390ecE6d062e5047fd82d33D285797Ef3E
//    0xEfa9f16F650fC2a1e84B6c1fbca9ef799e2664Ad
    private fun loadContract(): QRHashRegistry {
        val credentials = Credentials.create(privateKey)

        System.out.println("BlockchainHelper: Connected to wallet address: ${credentials.address}")
//        try {
//            val walletAddress = credentials.address
//            val balanceResponse: EthGetBalance = web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).send()
//            val balanceInWei = balanceResponse.balance
//            System.out.println("Balance: ${balanceInWei}")
//        } catch (e : Exception) {
//            System.out.println("Exception: ${e.message}")
//        }

        val customGasProvider = object : ContractGasProvider {
            override fun getGasPrice(contractFunc: String?): BigInteger {
                return Convert.toWei("20", Convert.Unit.GWEI).toBigInteger()
            }
            override fun getGasPrice(): BigInteger {
                return Convert.toWei("20", Convert.Unit.GWEI).toBigInteger()
            }
            override fun getGasLimit(contractFunc: String?): BigInteger {
                return BigInteger.valueOf(50000)
            }
            override fun getGasLimit(): BigInteger {
                return BigInteger.valueOf(50000)
            }
        }

        return QRHashRegistry.load(contractAddress, web3j, credentials, customGasProvider)
    }


    fun checkIfHashExists(qrHash: String, callback: (Boolean) -> Unit) {
//        System.out.println("Inside checkIfHashExists")
        Toast.makeText(context, "inside checkIfHashExists", Toast.LENGTH_SHORT).show()
        val contract = loadContract()
        System.out.println("contracct : ${contract}")
        val hashBytes = qrHash.toByteArray()
//        val hashBytes32 = ByteArray(32)
//        System.arraycopy(hashBytes, 0, hashBytes32, 0, hashBytes.size.coerceAtMost(32)) // Copy hashBytes into hashBytes32

        val byteArray = Numeric.hexStringToByteArray("0x$qrHash")

        // Ensure the byte array is exactly 32 bytes
        require(byteArray.size == 32) { "Hash must be exactly 32 bytes!" }

        // Create a Bytes32 object
        val hashBytes32 = Bytes32(byteArray)

        // Print the readable hex string representation of the Bytes32
        println("HashBytes32 (Hex): ${Numeric.toHexString(hashBytes32.value)}")

        Thread {
            try {
//                addHashToBlockchain(qrHash) { success ->
//                    if (success) {
//                        System.out.println("Hash added to blockchain successfully.")
//                    } else {
//                        System.out.println("Failed to add hash to blockchain.")
//                    }
//                }
                println("${hashBytes32.value}")
                val isMalicious = contract.isHashMalicious(hashBytes32.value).send()
                System.out.println("isMalicious: $isMalicious")
                callback(isMalicious)
            } catch (e: Exception) {
                System.out.println("Error checking hash: ${e.message}")
//
                callback(false)
            }
        }.start()
    }


    fun addHashToBlockchain(qrHash: String, callback: (Boolean) -> Unit) {
        val contract = loadContract()
        System.out.println("Before byte array: $qrHash")
        val hashBytes = qrHash.toByteArray()
        System.out.println("Before bytes array 32: ${hashBytes}")
//        val hashBytes32 = ByteArray(32)
//        System.arraycopy(hashBytes, 0, hashBytes32, 0, hashBytes.size.coerceAtMost(32))
//        System.out.println("converted : ${Numeric.toHexString(hashBytes32)}")

        val byteArray = Numeric.hexStringToByteArray("0x$qrHash")

        // Ensure the byte array is exactly 32 bytes
        require(byteArray.size == 32) { "Hash must be exactly 32 bytes!" }

        // Create a Bytes32 object
        val hashBytes32 = Bytes32(byteArray)

        // Print the readable hex string representation of the Bytes32
        println("HashBytes32 (Hex): ${Numeric.toHexString(hashBytes32.value)}")
        Thread {
                try {
                    System.out.println("qrHash: ${hashBytes32}")
                    val transactionReceipt = contract.addQRHash(hashBytes32.value).send()
                    System.out.println("transactionReceipt: ${transactionReceipt}")
                    getTransactionDetails(transactionReceipt.transactionHash)
                    val success = transactionReceipt.status == "0x1" // Status 0x1 means success
                    System.out.println("isSuccess: ${success}")
//                    val credentials = Credentials.create(privateKey)
//                    val walletAddress = credentials.address
//                    val balanceResponse: EthGetBalance =
//                        web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).send()
//                    val balanceInWei = balanceResponse.balance
//                    System.out.println("Balance: ${balanceInWei}")
                    callback(success) //replace true with success
                } catch (e: Exception) {
                    System.out.println("Error adding hash: ${e.message}")
                    callback(false)
                }
            }.start()
    }


    private fun connectToEthereum() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                System.out.println("Inside connect TO ethereum")
                val web3j: Web3j = Web3j.build(HttpService("https://sepolia.infura.io/v3/3764887512834ed9b5b729eaba91ee42"))
                System.out.println("-- $web3j")
                val version = web3j.web3ClientVersion().send().web3ClientVersion

                withContext(Dispatchers.Main) {

                    System.out.println("Connection to client version: $version")

//                    val credentials = Credentials.create(privateKey)
//                    val walletAddress = credentials.address
//                    val balanceResponse: EthGetBalance = web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).send()
//                    val balanceInWei = balanceResponse.balance
//                    System.out.println("Balance: $balanceInWei")

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    System.out.println("Error: ${e.message}")
                }
            }
        }
    }
    fun getTransactionDetails(transactionHash: String) {
        // Connect to the Ethereum network using Web3j (make sure the correct network is set)
//        val web3j = Web3j.build(HttpService("https://sepolia.infura.io/v3/YOUR_INFURA_PROJECT_ID"))

        Thread {
            try {
                // Retrieve the transaction details using the transaction hash
                val transaction: EthTransaction = web3j.ethGetTransactionByHash(transactionHash).send()

                // Check if the transaction exists
                if (transaction.result != null) {
                    val tx: Transaction = transaction.result

                    // Print the details of the transaction
                    println("Transaction Hash: ${tx.hash}")
                    println("From: ${tx.from}")
                    println("To: ${tx.to}")
                    println("Value: ${tx.value}")
                    println("Gas: ${tx.gas}")
                    println("Input Data: ${tx.input}") // This is the content of the transaction
                } else {
                    println("Transaction not found.")
                }
            } catch (e: Exception) {
                println("Error retrieving transaction details: ${e.message}")
            }
        }.start()
    }
}
