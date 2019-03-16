package pl.piomin.service.blockchain.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.1.1.
 */
public class Data_sol_Data extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060028054600160a060020a0319163317905561091c806100326000396000f3fe608060405234801561001057600080fd5b506004361061007e577c010000000000000000000000000000000000000000000000000000000060003504630995d2fe81146100835780632f779e3b1461019e5780636aaad277146102cb5780639f64158214610305578063aafd338b146103ab578063e0e3671c146103d1575b600080fd5b6101296004803603602081101561009957600080fd5b8101906020810181356401000000008111156100b457600080fd5b8201836020820111156100c657600080fd5b803590602001918460018302840111640100000000831117156100e857600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506103f7945050505050565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561016357818101518382015260200161014b565b50505050905090810190601f1680156101905780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610129600480360360408110156101b457600080fd5b8101906020810181356401000000008111156101cf57600080fd5b8201836020820111156101e157600080fd5b8035906020019184600183028401116401000000008311171561020357600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929594936020810193503591505064010000000081111561025657600080fd5b82018360208201111561026857600080fd5b8035906020019184600183028401116401000000008311171561028a57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061048a945050505050565b6102f1600480360360208110156102e157600080fd5b5035600160a060020a0316610680565b604080519115158252519081900360200190f35b6101296004803603602081101561031b57600080fd5b81019060208101813564010000000081111561033657600080fd5b82018360208201111561034857600080fd5b8035906020019184600183028401116401000000008311171561036a57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061069e945050505050565b6102f1600480360360208110156103c157600080fd5b5035600160a060020a031661079a565b6102f1600480360360208110156103e757600080fd5b5035600160a060020a0316610841565b80516060906000908390600019810190811061040f57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535081516000908390600119810190811061045457fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535090919050565b600254606090600160a060020a0316331461050657604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b816001846040518082805190602001908083835b602083106105395780518252601f19909201916020918201910161051a565b51815160209384036101000a6000190180199092169116179052920194855250604051938490038101909320845161057a9591949190910192509050610855565b50816001846040518082805190602001908083835b602083106105ae5780518252601f19909201916020918201910161058f565b51815160209384036101000a600019018019909216911617905292019485525060405193849003810190932084516105ef9591949190910192509050610855565b805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156106735780601f1061064857610100808354040283529160200191610673565b820191906000526020600020905b81548152906001019060200180831161065657829003601f168201915b5050505050905092915050565b600160a060020a031660009081526020819052604090205460ff1690565b606060016106ab836103f7565b6040518082805190602001908083835b602083106106da5780518252601f1990920191602091820191016106bb565b518151600019602094850361010090810a820192831692199390931691909117909252949092019687526040805197889003820188208054601f600260018316159098029095011695909504928301829004820288018201905281875292945092505083018282801561078e5780601f106107635761010080835404028352916020019161078e565b820191906000526020600020905b81548152906001019060200180831161077157829003601f168201915b50505050509050919050565b600254600090600160a060020a0316331461081657604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b50600160a060020a03166000908152602081905260409020805460ff19166001179081905560ff1690565b600254600160a060020a0390811691161490565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061089657805160ff19168380011785556108c3565b828001600101855582156108c3579182015b828111156108c35782518255916020019190600101906108a8565b506108cf9291506108d3565b5090565b6108ed91905b808211156108cf57600081556001016108d9565b9056fea165627a7a72305820e2aaf0d3de7361765551061acd74e8c95af0b7a5583d2a1f22305f55d73572910029";

    public static final String FUNC_GETFILENUM = "getFileNum";

    public static final String FUNC_WRITEDB = "writeDB";

    public static final String FUNC_CHECKPERMITTED = "checkPermitted";

    public static final String FUNC_READDB = "readDB";

    public static final String FUNC_ADDPERMITTED = "addPermitted";

    public static final String FUNC_CHECKOWNER = "checkOwner";

    @Deprecated
    protected Data_sol_Data(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Data_sol_Data(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Data_sol_Data(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Data_sol_Data(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<Utf8String> getFileNum(Utf8String _id) {
        final Function function = new Function(FUNC_GETFILENUM, 
                Arrays.<Type>asList(_id), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> writeDB(Utf8String _FileId, Utf8String _hash) {
        final Function function = new Function(
                FUNC_WRITEDB, 
                Arrays.<Type>asList(_FileId, _hash), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Bool> checkPermitted(Address _roleAddr) {
        final Function function = new Function(FUNC_CHECKPERMITTED, 
                Arrays.<Type>asList(_roleAddr), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Utf8String> readDB(Utf8String _dataId) {
        final Function function = new Function(FUNC_READDB, 
                Arrays.<Type>asList(_dataId), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> addPermitted(Address _roleAddr) {
        final Function function = new Function(
                FUNC_ADDPERMITTED, 
                Arrays.<Type>asList(_roleAddr), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Bool> checkOwner(Address _userAddr) {
        final Function function = new Function(FUNC_CHECKOWNER, 
                Arrays.<Type>asList(_userAddr), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    @Deprecated
    public static Data_sol_Data load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Data_sol_Data(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Data_sol_Data load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Data_sol_Data(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Data_sol_Data load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Data_sol_Data(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Data_sol_Data load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Data_sol_Data(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Data_sol_Data> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Data_sol_Data.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Data_sol_Data> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Data_sol_Data.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Data_sol_Data> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Data_sol_Data.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Data_sol_Data> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Data_sol_Data.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
