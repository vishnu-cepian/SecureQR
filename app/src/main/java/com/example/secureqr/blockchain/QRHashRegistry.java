package com.example.secureqr.blockchain;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/hyperledger-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.6.2.
 */
@SuppressWarnings("rawtypes")
public class QRHashRegistry extends Contract {
    public static final String BINARY = "{\r\n"
            + "\t\"generatedSources\": [],\r\n"
            + "\t\"linkReferences\": {},\r\n"
            + "\t\"object\": \"608060405234801561001057600080fd5b506102d5806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80630bfb26e81461004657806318b6cf4614610062578063ba9eca0514610092575b600080fd5b610060600480360381019061005b91906101ae565b6100c2565b005b61007c600480360381019061007791906101ae565b610150565b6040516100899190610226565b60405180910390f35b6100ac60048036038101906100a791906101ae565b610179565b6040516100b99190610226565b60405180910390f35b60008082815260200190815260200160002060009054906101000a900460ff1615610122576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161011990610241565b60405180910390fd5b600160008083815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b600080600083815260200190815260200160002060009054906101000a900460ff169050919050565b60006020528060005260406000206000915054906101000a900460ff1681565b6000813590506101a881610288565b92915050565b6000602082840312156101c057600080fd5b60006101ce84828501610199565b91505092915050565b6101e081610272565b82525050565b60006101f3601383610261565b91507f4861736820616c726561647920657869737473000000000000000000000000006000830152602082019050919050565b600060208201905061023b60008301846101d7565b92915050565b6000602082019050818103600083015261025a816101e6565b9050919050565b600082825260208201905092915050565b60008115159050919050565b6000819050919050565b6102918161027e565b811461029c57600080fd5b5056fea2646970667358221220177095fc178d0ba32d406085e615c51bc7f546571aa75c4d7548210000e2dc6b64736f6c63430008000033\",\r\n"
            + "\t\"opcodes\": \"PUSH1 0x80 PUSH1 0x40 MSTORE CALLVALUE DUP1 ISZERO PUSH2 0x10 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x2D5 DUP1 PUSH2 0x20 PUSH1 0x0 CODECOPY PUSH1 0x0 RETURN INVALID PUSH1 0x80 PUSH1 0x40 MSTORE CALLVALUE DUP1 ISZERO PUSH2 0x10 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH1 0x4 CALLDATASIZE LT PUSH2 0x41 JUMPI PUSH1 0x0 CALLDATALOAD PUSH1 0xE0 SHR DUP1 PUSH4 0xBFB26E8 EQ PUSH2 0x46 JUMPI DUP1 PUSH4 0x18B6CF46 EQ PUSH2 0x62 JUMPI DUP1 PUSH4 0xBA9ECA05 EQ PUSH2 0x92 JUMPI JUMPDEST PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH2 0x60 PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 PUSH2 0x5B SWAP2 SWAP1 PUSH2 0x1AE JUMP JUMPDEST PUSH2 0xC2 JUMP JUMPDEST STOP JUMPDEST PUSH2 0x7C PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 PUSH2 0x77 SWAP2 SWAP1 PUSH2 0x1AE JUMP JUMPDEST PUSH2 0x150 JUMP JUMPDEST PUSH1 0x40 MLOAD PUSH2 0x89 SWAP2 SWAP1 PUSH2 0x226 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST PUSH2 0xAC PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 PUSH2 0xA7 SWAP2 SWAP1 PUSH2 0x1AE JUMP JUMPDEST PUSH2 0x179 JUMP JUMPDEST PUSH1 0x40 MLOAD PUSH2 0xB9 SWAP2 SWAP1 PUSH2 0x226 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST PUSH1 0x0 DUP1 DUP3 DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND ISZERO PUSH2 0x122 JUMPI PUSH1 0x40 MLOAD PUSH32 0x8C379A000000000000000000000000000000000000000000000000000000000 DUP2 MSTORE PUSH1 0x4 ADD PUSH2 0x119 SWAP1 PUSH2 0x241 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 REVERT JUMPDEST PUSH1 0x1 PUSH1 0x0 DUP1 DUP4 DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 PUSH1 0x0 PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH1 0xFF MUL NOT AND SWAP1 DUP4 ISZERO ISZERO MUL OR SWAP1 SSTORE POP POP JUMP JUMPDEST PUSH1 0x0 DUP1 PUSH1 0x0 DUP4 DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 PUSH1 0x20 MSTORE DUP1 PUSH1 0x0 MSTORE PUSH1 0x40 PUSH1 0x0 KECCAK256 PUSH1 0x0 SWAP2 POP SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND DUP2 JUMP JUMPDEST PUSH1 0x0 DUP2 CALLDATALOAD SWAP1 POP PUSH2 0x1A8 DUP2 PUSH2 0x288 JUMP JUMPDEST SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 PUSH1 0x20 DUP3 DUP5 SUB SLT ISZERO PUSH2 0x1C0 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH1 0x0 PUSH2 0x1CE DUP5 DUP3 DUP6 ADD PUSH2 0x199 JUMP JUMPDEST SWAP2 POP POP SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH2 0x1E0 DUP2 PUSH2 0x272 JUMP JUMPDEST DUP3 MSTORE POP POP JUMP JUMPDEST PUSH1 0x0 PUSH2 0x1F3 PUSH1 0x13 DUP4 PUSH2 0x261 JUMP JUMPDEST SWAP2 POP PUSH32 0x4861736820616C72656164792065786973747300000000000000000000000000 PUSH1 0x0 DUP4 ADD MSTORE PUSH1 0x20 DUP3 ADD SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 PUSH1 0x20 DUP3 ADD SWAP1 POP PUSH2 0x23B PUSH1 0x0 DUP4 ADD DUP5 PUSH2 0x1D7 JUMP JUMPDEST SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 PUSH1 0x20 DUP3 ADD SWAP1 POP DUP2 DUP2 SUB PUSH1 0x0 DUP4 ADD MSTORE PUSH2 0x25A DUP2 PUSH2 0x1E6 JUMP JUMPDEST SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 DUP3 DUP3 MSTORE PUSH1 0x20 DUP3 ADD SWAP1 POP SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 DUP2 ISZERO ISZERO SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 DUP2 SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH2 0x291 DUP2 PUSH2 0x27E JUMP JUMPDEST DUP2 EQ PUSH2 0x29C JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP JUMP INVALID LOG2 PUSH5 0x6970667358 0x22 SLT KECCAK256 OR PUSH17 0x95FC178D0BA32D406085E615C51BC7F546 JUMPI BYTE 0xA7 0x5C 0x4D PUSH22 0x48210000E2DC6B64736F6C6343000800003300000000 \",\r\n"
            + "\t\"sourceMap\": \"58:458:0:-:0;;;;;;;;;;;;;;;;;;;\"\r\n"
            + "}";

    private static String librariesLinkedBinary;

    public static final String FUNC_MALICIOUSHASHES = "MaliciousHashes";

    public static final String FUNC_ADDQRHASH = "addQRHash";

    public static final String FUNC_ISHASHMALICIOUS = "isHashMalicious";

    @Deprecated
    protected QRHashRegistry(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected QRHashRegistry(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected QRHashRegistry(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected QRHashRegistry(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Boolean> MaliciousHashes(byte[] param0) {
        final Function function = new Function(FUNC_MALICIOUSHASHES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> addQRHash(byte[] _qrHash) {
        final Function function = new Function(
                FUNC_ADDQRHASH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_qrHash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> isHashMalicious(byte[] _qrHash) {
        final Function function = new Function(FUNC_ISHASHMALICIOUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_qrHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static QRHashRegistry load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new QRHashRegistry(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static QRHashRegistry load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new QRHashRegistry(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static QRHashRegistry load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new QRHashRegistry(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static QRHashRegistry load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new QRHashRegistry(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<QRHashRegistry> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(QRHashRegistry.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<QRHashRegistry> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(QRHashRegistry.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<QRHashRegistry> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(QRHashRegistry.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<QRHashRegistry> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(QRHashRegistry.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

//    public static void linkLibraries(List<Contract.LinkReference> references) {
//        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
//    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }
}
