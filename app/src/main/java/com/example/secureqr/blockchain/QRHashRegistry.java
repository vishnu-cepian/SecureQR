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
            + "\t\"object\": \"608060405234801561001057600080fd5b50610204806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80630bfb26e8146100465780632648ac0814610062578063ed9e682114610092575b600080fd5b610060600480360381019061005b919061014e565b6100c2565b005b61007c6004803603810190610077919061014e565b6100f0565b6040516100899190610186565b60405180910390f35b6100ac60048036038101906100a7919061014e565b610110565b6040516100b99190610186565b60405180910390f35b600160008083815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b60006020528060005260406000206000915054906101000a900460ff1681565b600080600083815260200190815260200160002060009054906101000a900460ff169050919050565b600081359050610148816101b7565b92915050565b60006020828403121561016057600080fd5b600061016e84828501610139565b91505092915050565b610180816101a1565b82525050565b600060208201905061019b6000830184610177565b92915050565b60008115159050919050565b6000819050919050565b6101c0816101ad565b81146101cb57600080fd5b5056fea26469706673582212204721aa5828b8466782549853a2a98ad63b22b42e6ec8adaf56752b38c16e45f664736f6c63430008000033\",\r\n"
            + "\t\"opcodes\": \"PUSH1 0x80 PUSH1 0x40 MSTORE CALLVALUE DUP1 ISZERO PUSH2 0x10 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH2 0x204 DUP1 PUSH2 0x20 PUSH1 0x0 CODECOPY PUSH1 0x0 RETURN INVALID PUSH1 0x80 PUSH1 0x40 MSTORE CALLVALUE DUP1 ISZERO PUSH2 0x10 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP PUSH1 0x4 CALLDATASIZE LT PUSH2 0x41 JUMPI PUSH1 0x0 CALLDATALOAD PUSH1 0xE0 SHR DUP1 PUSH4 0xBFB26E8 EQ PUSH2 0x46 JUMPI DUP1 PUSH4 0x2648AC08 EQ PUSH2 0x62 JUMPI DUP1 PUSH4 0xED9E6821 EQ PUSH2 0x92 JUMPI JUMPDEST PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH2 0x60 PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 PUSH2 0x5B SWAP2 SWAP1 PUSH2 0x14E JUMP JUMPDEST PUSH2 0xC2 JUMP JUMPDEST STOP JUMPDEST PUSH2 0x7C PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 PUSH2 0x77 SWAP2 SWAP1 PUSH2 0x14E JUMP JUMPDEST PUSH2 0xF0 JUMP JUMPDEST PUSH1 0x40 MLOAD PUSH2 0x89 SWAP2 SWAP1 PUSH2 0x186 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST PUSH2 0xAC PUSH1 0x4 DUP1 CALLDATASIZE SUB DUP2 ADD SWAP1 PUSH2 0xA7 SWAP2 SWAP1 PUSH2 0x14E JUMP JUMPDEST PUSH2 0x110 JUMP JUMPDEST PUSH1 0x40 MLOAD PUSH2 0xB9 SWAP2 SWAP1 PUSH2 0x186 JUMP JUMPDEST PUSH1 0x40 MLOAD DUP1 SWAP2 SUB SWAP1 RETURN JUMPDEST PUSH1 0x1 PUSH1 0x0 DUP1 DUP4 DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 PUSH1 0x0 PUSH2 0x100 EXP DUP2 SLOAD DUP2 PUSH1 0xFF MUL NOT AND SWAP1 DUP4 ISZERO ISZERO MUL OR SWAP1 SSTORE POP POP JUMP JUMPDEST PUSH1 0x0 PUSH1 0x20 MSTORE DUP1 PUSH1 0x0 MSTORE PUSH1 0x40 PUSH1 0x0 KECCAK256 PUSH1 0x0 SWAP2 POP SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND DUP2 JUMP JUMPDEST PUSH1 0x0 DUP1 PUSH1 0x0 DUP4 DUP2 MSTORE PUSH1 0x20 ADD SWAP1 DUP2 MSTORE PUSH1 0x20 ADD PUSH1 0x0 KECCAK256 PUSH1 0x0 SWAP1 SLOAD SWAP1 PUSH2 0x100 EXP SWAP1 DIV PUSH1 0xFF AND SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 DUP2 CALLDATALOAD SWAP1 POP PUSH2 0x148 DUP2 PUSH2 0x1B7 JUMP JUMPDEST SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 PUSH1 0x20 DUP3 DUP5 SUB SLT ISZERO PUSH2 0x160 JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST PUSH1 0x0 PUSH2 0x16E DUP5 DUP3 DUP6 ADD PUSH2 0x139 JUMP JUMPDEST SWAP2 POP POP SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH2 0x180 DUP2 PUSH2 0x1A1 JUMP JUMPDEST DUP3 MSTORE POP POP JUMP JUMPDEST PUSH1 0x0 PUSH1 0x20 DUP3 ADD SWAP1 POP PUSH2 0x19B PUSH1 0x0 DUP4 ADD DUP5 PUSH2 0x177 JUMP JUMPDEST SWAP3 SWAP2 POP POP JUMP JUMPDEST PUSH1 0x0 DUP2 ISZERO ISZERO SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH1 0x0 DUP2 SWAP1 POP SWAP2 SWAP1 POP JUMP JUMPDEST PUSH2 0x1C0 DUP2 PUSH2 0x1AD JUMP JUMPDEST DUP2 EQ PUSH2 0x1CB JUMPI PUSH1 0x0 DUP1 REVERT JUMPDEST POP JUMP INVALID LOG2 PUSH5 0x6970667358 0x22 SLT KECCAK256 SELFBALANCE 0x21 0xAA PC 0x28 0xB8 CHAINID PUSH8 0x82549853A2A98AD6 EXTCODESIZE 0x22 0xB4 0x2E PUSH15 0xC8ADAF56752B38C16E45F664736F6C PUSH4 0x43000800 STOP CALLER \",\r\n"
            + "\t\"sourceMap\": \"58:378:0:-:0;;;;;;;;;;;;;;;;;;;\"\r\n"
            + "}";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDQRHASH = "addQRHash";

    public static final String FUNC_BENIGNHASHES = "BenignHashes";

    public static final String FUNC_ISHASHBENIGN = "isHashBenign";

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

    public RemoteFunctionCall<TransactionReceipt> addQRHash(byte[] _qrHash) {
        final Function function = new Function(
                FUNC_ADDQRHASH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_qrHash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> BenignHashes(byte[] param0) {
        final Function function = new Function(FUNC_BENIGNHASHES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isHashBenign(byte[] _qrHash) {
        final Function function = new Function(FUNC_ISHASHBENIGN, 
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
