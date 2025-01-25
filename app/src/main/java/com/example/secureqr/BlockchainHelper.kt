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
import org.web3j.protocol.core.DefaultBlockParameterName
//import org.web3j.crypto.Credentials
import org.web3j.utils.Convert
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.tx.gas.ContractGasProvider
//import java.util.Arrays
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
    private val contractAddress = "0xEfa9f16F650fC2a1e84B6c1fbca9ef799e2664Ad"
    private val privateKey = "de849daa7b187f7ce7e15fa7d966211d66f6600e2f2756a3b15093871bdff76b"

    // Load smart contract
    private fun loadContract(): QRHashRegistry {
        val credentials = Credentials.create(privateKey)

//        Toast.makeText(context, "credentials : $credentials", Toast.LENGTH_SHORT).show()
        System.out.println("BlockchainHelper: Connected to walled address: ${credentials.address}")
        try {
            val walletAddress = credentials.address
            val balanceResponse: EthGetBalance =
                web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).send()
            val balanceInWei = balanceResponse.balance
            System.out.println("Balance: ${balanceInWei}")
        } catch (e : Exception) {
            System.out.println("Eception: ${e.message}")
        }
    val customGasProvider = object : ContractGasProvider {
        override fun getGasPrice(contractFunc: String?): BigInteger {
            return Convert.toWei("20", Convert.Unit.GWEI).toBigInteger() // Custom gas price
        }

        override fun getGasPrice(): BigInteger {
            return Convert.toWei("20", Convert.Unit.GWEI).toBigInteger() // Custom gas price
        }

        override fun getGasLimit(contractFunc: String?): BigInteger {
            return BigInteger.valueOf(50000) // Custom gas limit
        }

        override fun getGasLimit(): BigInteger {
            return BigInteger.valueOf(50000) // Custom gas limit
        }
    }
        return QRHashRegistry.load(contractAddress, web3j, credentials, customGasProvider)
        }

    // Check if hash is malicious
    fun checkIfHashExists(qrHash: String, callback: (Boolean) -> Unit) {
        System.out.println("Inside checkIfHashExists")
        Toast.makeText(context, "inside checkIfHashExists", Toast.LENGTH_SHORT).show()
        val contract = loadContract()
        System.out.println("contracct : ${contract}")
        val hashBytes = qrHash.toByteArray()
        val hashBytes32 = ByteArray(32)
        System.arraycopy(hashBytes, 0, hashBytes32, 0, hashBytes.size.coerceAtMost(32)) // Copy hashBytes into hashBytes32
        Thread {
            try {
                val isMalicious = contract.isHashMalicious(hashBytes32).send()
                System.out.println("isMalicious: $isMalicious")
                callback(isMalicious)
            } catch (e: Exception) {
                System.out.println("Error checking hash: ${e.message}")
//                Log.e("QRScan", "Error checking hash: ${e.message}")
                callback(false) // In case of error, return false
            }
        }.start()
    }

//     Add new hash to the blockchain (for malicious hash)
    fun addHashToBlockchain(qrHash: String, callback: (Boolean) -> Unit) {
        val contract = loadContract()
        val hashBytes = qrHash.toByteArray()
        System.out.println("BEfore bytes array 32: ${hashBytes}")
        val hashBytes32 = ByteArray(32)
        System.arraycopy(hashBytes, 0, hashBytes32, 0, hashBytes.size.coerceAtMost(32))

    val gasPrice = Convert.toWei("20", Convert.Unit.GWEI).toBigInteger() // Gas price in Gwei
    val gasLimit = BigInteger.valueOf(50000)

    Thread {
            try {
                System.out.println("qrHash: ${hashBytes32}")
                val transactionReceipt = contract.addQRHash(hashBytes32).send()
                System.out.println("transationReceipt: ${transactionReceipt}")
                val success = transactionReceipt.status == "0x1" // Status 0x1 means success
                System.out.println("isSuccess: ${success}")
                val credentials = Credentials.create(privateKey)
                val walletAddress = credentials.address
                val balanceResponse: EthGetBalance =
                    web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).send()
                val balanceInWei = balanceResponse.balance
                System.out.println("Balance: ${balanceInWei}")
                callback(success)
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
//                    Toast.makeText(context, "Connected to Ethereum client Version : $version", Toast.LENGTH_SHORT).show()
                System.out.println("Connection to client version: $version")
                    val credentials = Credentials.create(privateKey)
                    val walletAddress = credentials.address
                    val balanceResponse: EthGetBalance =
                        web3j.ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).send()
                    val balanceInWei = balanceResponse.balance
                    System.out.println("Balance: $balanceInWei")

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    System.out.println("Error: ${e.message}")

                }
            }
        }
    }
    // Helper function to convert ByteArray to Bytes32
//    fun toBytes32(input: ByteArray): Bytes32 {
//        if (input.size > 32) {
//            // Trim if input exceeds 32 bytes
//            return Bytes32(input.copyOfRange(0, 32))
//        } else if (input.size < 32) {
//            // Pad with zeros if input is less than 32 bytes
//            val padded = ByteArray(32)
//            System.arraycopy(input, 0, padded, 0, input.size)
//            return Bytes32(padded)
//        }
//        return Bytes32(input)
//    }
}
