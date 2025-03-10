package com.example.secureqr

import android.content.Context
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import com.example.secureqr.blockchain.QRHashRegistry
import com.example.secureqr.blockchain.SafeBusinessQR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Convert
import org.web3j.protocol.core.methods.response.EthTransaction
import org.web3j.protocol.core.methods.response.Transaction
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.utils.Numeric
import java.math.BigInteger

class BlockchainHelper (context: Context) {
init {
    connectToEthereum()
}
    private val web3j: Web3j = Web3j.build(HttpService("https://sepolia.infura.io/v3/3764887512834ed9b5b729eaba91ee42"))
    private val contractAddressNormalScan = "0xe469542Ca15A06D13597DA19a9EB15E3d97F8EF3"
    private val privateKey = "de849daa7b187f7ce7e15fa7d966211d66f6600e2f2756a3b15093871bdff76b"
    private val contractAddressBusinessQR = "0x77Af94BebE805a20a29AFaC50483dE2364A2f6Fc"
//    0x3d95E7390ecE6d062e5047fd82d33D285797Ef3E
//    0xEfa9f16F650fC2a1e84B6c1fbca9ef799e2664Ad

//   <------------------------------------------NORMAL SCAN--------------------------------------------------------------------------------------------->
    private fun loadContractNormalScan(): QRHashRegistry {
        val credentials = Credentials.create(privateKey)

        println("BlockchainHelper: Connected to wallet address: ${credentials.address}")

        val customGasProvider = object : ContractGasProvider {
            override fun getGasPrice(contractFunc: String?): BigInteger {
                return Convert.toWei("20", Convert.Unit.GWEI).toBigInteger()
            }
            @Deprecated("Deprecated in Java", ReplaceWith(
                "Convert.toWei(\"20\", Convert.Unit.GWEI).toBigInteger()",
                "org.web3j.utils.Convert",
                "org.web3j.utils.Convert"
            )
            )
            override fun getGasPrice(): BigInteger {
                return Convert.toWei("20", Convert.Unit.GWEI).toBigInteger()
            }
            override fun getGasLimit(contractFunc: String?): BigInteger {
                return BigInteger.valueOf(50000)
            }
            @Deprecated("Deprecated in Java",
                ReplaceWith("BigInteger.valueOf(50000)", "java.math.BigInteger")
            )
            override fun getGasLimit(): BigInteger {
                return BigInteger.valueOf(50000)
            }
        }
        return QRHashRegistry.load(contractAddressNormalScan, web3j, credentials, customGasProvider)
    }

    fun checkIfHashExists(qrHash: String, callback: (Boolean) -> Unit) {

//        Toast.makeText(context, "inside checkIfHashExists", Toast.LENGTH_SHORT).show()
        val contract = loadContractNormalScan()
        println("contracct : $contract")

        val byteArray = Numeric.hexStringToByteArray("0x$qrHash")

        // Ensure the byte array is exactly 32 bytes
        require(byteArray.size == 32) { "Hash must be exactly 32 bytes!" }

        // Create a Bytes32 object
        val hashBytes32 = Bytes32(byteArray)

        // Print the readable hex string representation of the Bytes32
        println("HashBytes32 (Hex): ${Numeric.toHexString(hashBytes32.value)}")

        Thread {
            try {
                println("${hashBytes32.value}")
                val isMalicious = contract.isHashMalicious(hashBytes32.value).send()
                println("isMalicious: $isMalicious")
                callback(isMalicious)
            } catch (e: Exception) {
                println("Error checking hash: ${e.message}")
                callback(false)
            }
        }.start()
    }

fun addHashToBlockchain(qrHash: String, callback: (Boolean) -> Unit) {
    val contract = loadContractNormalScan()
    val credentials = Credentials.create(privateKey)
    val chainId: Long = 11155111 // Sepolia Testnet Chain ID

    // Convert hash to Bytes32
    val byteArray = Numeric.hexStringToByteArray("0x$qrHash")
    require(byteArray.size == 32) { "Hash must be exactly 32 bytes!" }
    val hashBytes32 = Bytes32(byteArray)

    Thread {
        try {
            // Get nonce
            val ethGetTransactionCount = web3j.ethGetTransactionCount(
                credentials.address, DefaultBlockParameterName.LATEST
            ).send()
            val nonce = ethGetTransactionCount.transactionCount

            // Estimate gas
            val gasPrice = web3j.ethGasPrice().send().gasPrice
            val gasLimit = BigInteger.valueOf(200000)

            // Encode function call
            val function = contract.addQRHash(hashBytes32.value)
            val encodedFunction = function.encodeFunctionCall()

            // Create RawTransaction
            val rawTransaction = RawTransaction.createTransaction(
                nonce, gasPrice, gasLimit, contract.contractAddress, BigInteger.ZERO, encodedFunction
            )

            // Sign the transaction with the chain ID
            val signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials)
            val hexValue = Numeric.toHexString(signedMessage)

            // Send the signed transaction
            val sendTransaction = web3j.ethSendRawTransaction(hexValue).send()

            // Wait for transaction receipt
            val transactionHash = sendTransaction.transactionHash
            if (transactionHash == null) {
                println("Failed to send transaction")
                callback(false)
                return@Thread
            }
    getTransactionDetails(transactionHash)
            val transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).send().transactionReceipt
        println("receipt  $transactionReceipt")
            callback(true)
        } catch (e: Exception) {
            println("Error adding hash: ${e.message}")
            callback(false)
        }
    }.start()
}


//   <------------------------------------------------BUSINESS SERVICE------------------------------------------------------------------------------------>

private fun loadContractBusinessScan(): SafeBusinessQR {
    val credentials = Credentials.create(privateKey)

    println("BlockchainHelper: Connected to wallet address: ${credentials.address}")

    val customGasProvider = object : ContractGasProvider {
        override fun getGasPrice(contractFunc: String?): BigInteger {
            return Convert.toWei("20", Convert.Unit.GWEI).toBigInteger()
        }
        @Deprecated("Deprecated in Java", ReplaceWith(
            "Convert.toWei(\"20\", Convert.Unit.GWEI).toBigInteger()",
            "org.web3j.utils.Convert",
            "org.web3j.utils.Convert"
        )
        )
        override fun getGasPrice(): BigInteger {
            return Convert.toWei("20", Convert.Unit.GWEI).toBigInteger()
        }
        override fun getGasLimit(contractFunc: String?): BigInteger {
            return BigInteger.valueOf(50000)
        }
        @Deprecated("Deprecated in Java",
            ReplaceWith("BigInteger.valueOf(50000)", "java.math.BigInteger")
        )
        override fun getGasLimit(): BigInteger {
            return BigInteger.valueOf(50000)
        }
    }
    return SafeBusinessQR.load(contractAddressBusinessQR, web3j, credentials, customGasProvider)
}

    fun addHashToBusinessServiceBlockchain(company: String, qrHash: String, callback: (Boolean) -> Unit){
        val contract = loadContractBusinessScan()
        val credentials = Credentials.create(privateKey)
        val chainId: Long = 11155111 // Sepolia Testnet Chain ID

        // Convert hash to Bytes32
        val byteArray = Numeric.hexStringToByteArray("0x$qrHash")
        require(byteArray.size == 32) { "Hash must be exactly 32 bytes!" }
        val hashBytes32 = Bytes32(byteArray)

        Thread {
            try {
                // Get nonce
                val ethGetTransactionCount = web3j.ethGetTransactionCount(
                    credentials.address, DefaultBlockParameterName.LATEST
                ).send()
                val nonce = ethGetTransactionCount.transactionCount

                // Estimate gas
                val gasPrice = web3j.ethGasPrice().send().gasPrice
                val gasLimit = BigInteger.valueOf(200000)

                // Encode function call
                val function = contract.registerProduct(company,hashBytes32.value)
                val encodedFunction = function.encodeFunctionCall()

                // Create RawTransaction
                val rawTransaction = RawTransaction.createTransaction(
                    nonce, gasPrice, gasLimit, contract.contractAddress, BigInteger.ZERO, encodedFunction
                )

                // Sign the transaction with the chain ID
                val signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials)
                val hexValue = Numeric.toHexString(signedMessage)

                // Send the signed transaction
                val sendTransaction = web3j.ethSendRawTransaction(hexValue).send()

                // Wait for transaction receipt
                val transactionHash = sendTransaction.transactionHash
                if (transactionHash == null) {
                    println("Failed to send transaction")
                    callback(false)
                    return@Thread
                }
                getTransactionDetails(transactionHash)
                val transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).send().transactionReceipt
                println("receipt  $transactionReceipt")

                callback(true)
            } catch (e: Exception) {
                println("Error adding hash: ${e.message}")
                callback(false)
            }
        }.start()
    }
    fun isProductAuthentic(company: String, qrHash: String, callback: (Boolean) -> Unit) {
        val contract = loadContractBusinessScan()
        println("Business contract in isProductAuth: ${contract.contractAddress}")

        val byteArray = Numeric.hexStringToByteArray("0x$qrHash")
        require(byteArray.size == 32) { "Hash must be exactly 32 bytes!" }
        val hashBytes32 = Bytes32(byteArray)
        println("HashBytes32 (Hex) in isProductAuth: ${Numeric.toHexString(hashBytes32.value)}")

        Thread {
            try {
                println("${hashBytes32.value}")
                val isAuthentic = contract.isProductAuthentic(company, hashBytes32.value).send()
                println("isAuthentic: $isAuthentic")
                callback(isAuthentic)
            } catch (e: Exception) {
                println("Error checking hash in isProductAuth: ${e.message}")
                callback(false)
            }
        }.start()
    }


    private fun connectToEthereum() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("Inside connect TO ethereum")
                val web3j: Web3j = Web3j.build(HttpService("https://sepolia.infura.io/v3/3764887512834ed9b5b729eaba91ee42"))
                println("-- $web3j")
                val version = web3j.web3ClientVersion().send().web3ClientVersion

                withContext(Dispatchers.Main) {

                    println("Connection to client version: $version")

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Error: ${e.message}")
                }
            }
        }
    }
    private fun getTransactionDetails(transactionHash: String) {

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
