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
public class SafeBusinessQR extends Contract {
    public static final String BINARY = "{\r\n"
            + "\t\"generatedSources\": [],\r\n"
            + "\t\"linkReferences\": {},\r\n"
            + "\t\"object\": \"608060405234801561001057600080fd5b50610427806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80631d6f36ec146100465780636db6ae4214610076578063ed0abd15146100a6575b600080fd5b610060600480360381019061005b9190610216565b6100c2565b60405161006d91906102c1565b60405180910390f35b610090600480360381019061008b9190610216565b610108565b60405161009d91906102c1565b60405180910390f35b6100c060048036038101906100bb9190610216565b61014d565b005b600080836040516100d391906102aa565b9081526020016040518091039020600083815260200190815260200160002060009054906101000a900460ff16905092915050565b6000828051602081018201805184825260208301602085012081835280955050505050506020528060005260406000206000915091509054906101000a900460ff1681565b600160008360405161015f91906102aa565b9081526020016040518091039020600083815260200190815260200160002060006101000a81548160ff0219169083151502179055505050565b60006101ac6101a78461030d565b6102dc565b9050828152602081018484840111156101c457600080fd5b6101cf848285610369565b509392505050565b6000813590506101e6816103da565b92915050565b600082601f8301126101fd57600080fd5b813561020d848260208601610199565b91505092915050565b6000806040838503121561022957600080fd5b600083013567ffffffffffffffff81111561024357600080fd5b61024f858286016101ec565b9250506020610260858286016101d7565b9150509250929050565b61027381610353565b82525050565b60006102848261033d565b61028e8185610348565b935061029e818560208601610378565b80840191505092915050565b60006102b68284610279565b915081905092915050565b60006020820190506102d6600083018461026a565b92915050565b6000604051905081810181811067ffffffffffffffff82111715610303576103026103ab565b5b8060405250919050565b600067ffffffffffffffff821115610328576103276103ab565b5b601f19601f8301169050602081019050919050565b600081519050919050565b600081905092915050565b60008115159050919050565b6000819050919050565b82818337600083830152505050565b60005b8381101561039657808201518184015260208101905061037b565b838111156103a5576000848401525b50505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b6103e38161035f565b81146103ee57600080fd5b5056fea2646970667358221220fe81eabe6149f72b5354969afcd995585ac7686d51140370c999571390d0ad4864736f6c63430008000033\",\r\n"
            + "\t\"opcodes\": \"PUSH1 0x80 PUSH1 0x40 MSTORE CALLVALUE DUP1 ISZERO PUSH2 0x10 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x427 DUP1 PUSH2 0x20 PUSH1 0x0 CODECOPY PUSH1 0x0 RETURN INVALID PUSH1 0x80 PUSH1 0x40 MSTORE CALLVALUE DUP1 ISZERO PUSH2 0x10 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH1 0x4 CALLDATASIZE LT PUSH2 0x41 JUMPI PUSH1 0x0 CALLDATALOAD PUSH1 0xE0 SHR DUP1 PUSH4 0x1D6F36EC EQ PUSH2 0x46 JUMPI DUP1 PUSH4 0x6DB6AE42 EQ PUSH2 0x76 JUMPI DUP1 PUSH4 0xED0ABD15 EQ PUSH2 0xA6 JUMPI JUMPDEST PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH2 0x60 PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 PUSH2 0x5B SWAP2 SWAP1 PUSH2 0x216 JUMP JUMPDEST PUSH2 0xC2 JUMP JUMPDEST PUSH1 0x40 MLOAD PUSH2 0x6D SWAP2 SWAP1 PUSH2 0x2C1 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST PUSH2 0x90 PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 PUSH2 0x8B SWAP2 SWAP1 PUSH2 0x216 JUMP JUMPDEST PUSH2 0x108 JUMP JUMPDEST PUSH1 0x40 MLOAD PUSH2 0x9D SWAP2 SWAP1 PUSH2 0x2C1 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST PUSH2 0xC0 PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 PUSH2 0xBB SWAP2 SWAP1 PUSH2 0x216 JUMP JUMPDEST PUSH2 0x14D JUMP JUMPDEST STOP JUMPDEST PUSH1 0x0 DUP1 DUP4 PUSH1 0x40 MLOAD PUSH2 0xD3 SWAP2 SWAP1 PUSH2 0x2AA JUMP JUMPDEST SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 KECCAK256 PUSH1 0x0 DUP4 DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND SWAP1 POP SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 DUP3 DUP1 MLOAD PUSH1 0x20 DUP2 ADD DUP3 ADD DUP1 MLOAD DUP5 DUP3 MSTORE PUSH1 0x20 DUP4 ADD PUSH1 0x20 DUP6 ADD KECCAK256 DUP2 DUP4 MSTORE DUP1 SWAP6 POP POP POP POP POP POP PUSH1 0x20 MSTORE DUP1 PUSH1 0x0 MSTORE PUSH1 0x40 PUSH1 0x0 KECCAK256 PUSH1 0x0 SWAP2 POP SWAP2 POP SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND DUP2 JUMP JUMPDEST PUSH1 0x1 PUSH1 0x0 DUP4 PUSH1 0x40 MLOAD PUSH2 0x15F SWAP2 SWAP1 PUSH2 0x2AA JUMP JUMPDEST SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 KECCAK256 PUSH1 0x0 DUP4 DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 PUSH1 0x0 PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH1 0xFF MUL NOT AND SWAP1 DUP4 ISZERO ISZERO MUL OR SWAP1 SSTORE POP POP POP JUMP JUMPDEST PUSH1 0x0 PUSH2 0x1AC PUSH2 0x1A7 DUP5 PUSH2 0x30D JUMP JUMPDEST PUSH2 0x2DC JUMP JUMPDEST SWAP1 POP DUP3 DUP2 MSTORE PUSH1 0x20 DUP2 ADD DUP5 DUP5 DUP5 ADD GT ISZERO PUSH2 0x1C4 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH2 0x1CF DUP5 DUP3 DUP6 PUSH2 0x369 JUMP JUMPDEST POP SWAP4 SWAP3 POP POP POP JUMP JUMPDEST PUSH1 0x0 DUP2 CALLDATALOAD SWAP1 POP PUSH2 0x1E6 DUP2 PUSH2 0x3DA JUMP JUMPDEST SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 DUP3 PUSH1 0x1F DUP4 ADD SLT PUSH2 0x1FD JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST DUP2 CALLDATALOAD PUSH2 0x20D DUP5 DUP3 PUSH1 0x20 DUP7 ADD PUSH2 0x199 JUMP JUMPDEST SWAP2 POP POP SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 DUP1 PUSH1 0x40 DUP4 DUP6 SUB SLT ISZERO PUSH2 0x229 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH1 0x0 DUP4 ADD CALLDATALOAD PUSH8 0xFFFFFFFFFFFFFFFF DUP2 GT ISZERO PUSH2 0x243 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH2 0x24F DUP6 DUP3 DUP7 ADD PUSH2 0x1EC JUMP JUMPDEST SWAP3 POP POP PUSH1 0x20 PUSH2 0x260 DUP6 DUP3 DUP7 ADD PUSH2 0x1D7 JUMP JUMPDEST SWAP2 POP POP SWAP3 POP SWAP3 SWAP1 POP JUMP JUMPDEST PUSH2 0x273 DUP2 PUSH2 0x353 JUMP JUMPDEST DUP3 MSTORE POP POP JUMP JUMPDEST PUSH1 0x0 PUSH2 0x284 DUP3 PUSH2 0x33D JUMP JUMPDEST PUSH2 0x28E DUP2 DUP6 PUSH2 0x348 JUMP JUMPDEST SWAP4 POP PUSH2 0x29E DUP2 DUP6 PUSH1 0x20 DUP7 ADD PUSH2 0x378 JUMP JUMPDEST DUP1 DUP5 ADD SWAP2 POP POP SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 PUSH2 0x2B6 DUP3 DUP5 PUSH2 0x279 JUMP JUMPDEST SWAP2 POP DUP2 SWAP1 POP SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 PUSH1 0x20 DUP3 ADD SWAP1 POP PUSH2 0x2D6 PUSH1 0x0 DUP4 ADD DUP5 PUSH2 0x26A JUMP JUMPDEST SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 PUSH1 0x40 MLOAD SWAP1 POP DUP2 DUP2 ADD DUP2 DUP2 LT PUSH8 0xFFFFFFFFFFFFFFFF DUP3 GT OR ISZERO PUSH2 0x303 JUMPI PUSH2 0x302 PUSH2 0x3AB JUMP JUMPDEST JUMPDEST DUP1 PUSH1 0x40 MSTORE POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 PUSH8 0xFFFFFFFFFFFFFFFF DUP3 GT ISZERO PUSH2 0x328 JUMPI PUSH2 0x327 PUSH2 0x3AB JUMP JUMPDEST JUMPDEST PUSH1 0x1F NOT PUSH1 0x1F DUP4 ADD AND SWAP1 POP PUSH1 0x20 DUP2 ADD SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 DUP2 MLOAD SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 DUP2 SWAP1 POP SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 DUP2 ISZERO ISZERO SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 DUP2 SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST DUP3 DUP2 DUP4 CALLDATACOPY PUSH1 0x0 DUP4 DUP4 ADD MSTORE POP POP POP JUMP JUMPDEST PUSH1 0x0 JUMPDEST DUP4 DUP2 LT ISZERO PUSH2 0x396 JUMPI DUP1 DUP3 ADD MLOAD DUP2 DUP5 ADD MSTORE PUSH1 0x20 DUP2 ADD SWAP1 POP PUSH2 0x37B JUMP JUMPDEST DUP4 DUP2 GT ISZERO PUSH2 0x3A5 JUMPI PUSH1 0x0 DUP5 DUP5 ADD MSTORE JUMPDEST POP POP POP POP JUMP JUMPDEST PUSH32 0x4E487B7100000000000000000000000000000000000000000000000000000000 PUSH1 0x0 MSTORE PUSH1 0x41 PUSH1 0x4 MSTORE PUSH1 0x24 PUSH1 0x0 REVERT JUMPDEST PUSH2 0x3E3 DUP2 PUSH2 0x35F JUMP JUMPDEST DUP2 EQ PUSH2 0x3EE JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP JUMP INVALID LOG2 PUSH5 0x6970667358 0x22 SLT KECCAK256 INVALID DUP2 0xEA 0xBE PUSH2 0x49F7 0x2B MSTORE8 SLOAD SWAP7 SWAP11 0xFC 0xD9 SWAP6 PC GAS 0xC7 PUSH9 0x6D51140370C9995713 SWAP1 0xD0 0xAD 0x48 PUSH5 0x736F6C6343 STOP ADDMOD STOP STOP CALLER \",\r\n"
            + "\t\"sourceMap\": \"58:411:0:-:0;;;;;;;;;;;;;;;;;;;\"\r\n"
            + "}";

    private static String librariesLinkedBinary;

    public static final String FUNC_ISPRODUCTAUTHENTIC = "isProductAuthentic";

    public static final String FUNC_REGISTERPRODUCT = "registerProduct";

    public static final String FUNC_REGISTEREDPRODUCTS = "registeredProducts";

    @Deprecated
    protected SafeBusinessQR(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SafeBusinessQR(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SafeBusinessQR(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SafeBusinessQR(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Boolean> isProductAuthentic(String company, byte[] qrHash) {
        final Function function = new Function(FUNC_ISPRODUCTAUTHENTIC, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(company), 
                new org.web3j.abi.datatypes.generated.Bytes32(qrHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> registerProduct(String company, byte[] qrHash) {
        final Function function = new Function(
                FUNC_REGISTERPRODUCT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(company), 
                new org.web3j.abi.datatypes.generated.Bytes32(qrHash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> registeredProducts(String param0, byte[] param1) {
        final Function function = new Function(FUNC_REGISTEREDPRODUCTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0), 
                new org.web3j.abi.datatypes.generated.Bytes32(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static SafeBusinessQR load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeBusinessQR(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SafeBusinessQR load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeBusinessQR(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SafeBusinessQR load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new SafeBusinessQR(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SafeBusinessQR load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SafeBusinessQR(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SafeBusinessQR> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SafeBusinessQR.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SafeBusinessQR> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SafeBusinessQR.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<SafeBusinessQR> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SafeBusinessQR.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SafeBusinessQR> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SafeBusinessQR.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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
