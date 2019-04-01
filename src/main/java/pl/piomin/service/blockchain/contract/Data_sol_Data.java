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
    private static final String BINARY = "608060405234801561001057600080fd5b5060038054600160a060020a03191633179055610a17806100326000396000f3fe608060405234801561001057600080fd5b50600436106100a5576000357c0100000000000000000000000000000000000000000000000000000000900480639f641582116100785780639f6415821461033c578063aec2e3e4146103e2578063afd8b1d11461041c578063dd90984214610442576100a5565b80630995d2fe146100aa5780632f779e3b146101c557806339e20523146102f2578063893d20e814610334575b600080fd5b610150600480360360208110156100c057600080fd5b8101906020810181356401000000008111156100db57600080fd5b8201836020820111156100ed57600080fd5b8035906020019184600183028401116401000000008311171561010f57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610468945050505050565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561018a578181015183820152602001610172565b50505050905090810190601f1680156101b75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610150600480360360408110156101db57600080fd5b8101906020810181356401000000008111156101f657600080fd5b82018360208201111561020857600080fd5b8035906020019184600183028401116401000000008311171561022a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929594936020810193503591505064010000000081111561027d57600080fd5b82018360208201111561028f57600080fd5b803590602001918460018302840111640100000000831117156102b157600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610536945050505050565b6103186004803603602081101561030857600080fd5b5035600160a060020a03166106c2565b60408051600160a060020a039092168252519081900360200190f35b61031861076e565b6101506004803603602081101561035257600080fd5b81019060208101813564010000000081111561036d57600080fd5b82018360208201111561037f57600080fd5b803590602001918460018302840111640100000000831117156103a157600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061077e945050505050565b610408600480360360208110156103f857600080fd5b5035600160a060020a031661087a565b604080519115158252519081900360200190f35b6104086004803603602081101561043257600080fd5b5035600160a060020a031661088e565b6104086004803603602081101561045857600080fd5b5035600160a060020a0316610935565b60607f300000000000000000000000000000000000000000000000000000000000000082600184510381518110151561049d57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053507f300000000000000000000000000000000000000000000000000000000000000082600284510381518110151561050057fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535090919050565b606081600261054485610468565b6040518082805190602001908083835b602083106105735780518252601f199092019160209182019101610554565b51815160209384036101000a600019018019909216911617905292019485525060405193849003810190932084516105b49591949190910192509050610953565b508160026105c185610468565b6040518082805190602001908083835b602083106105f05780518252601f1990920191602091820191016105d1565b51815160209384036101000a600019018019909216911617905292019485525060405193849003810190932084516106319591949190910192509050610953565b805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156106b55780601f1061068a576101008083540402835291602001916106b5565b820191906000526020600020905b81548152906001019060200180831161069857829003601f168201915b5050505050905092915050565b600354600090600160a060020a0316331461073e57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b506001805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392831617908190551690565b600354600160a060020a03165b90565b6060600261078b83610468565b6040518082805190602001908083835b602083106107ba5780518252601f19909201916020918201910161079b565b518151600019602094850361010090810a820192831692199390931691909117909252949092019687526040805197889003820188208054601f600260018316159098029095011695909504928301829004820288018201905281875292945092505083018282801561086e5780601f106108435761010080835404028352916020019161086e565b820191906000526020600020905b81548152906001019060200180831161085157829003601f168201915b50505050509050919050565b600154600160a060020a0390811691161490565b600354600090600160a060020a0316331461090a57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b50600160a060020a03166000908152602081905260409020805460ff19166001179081905560ff1690565b600160a060020a031660009081526020819052604090205460ff1690565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061099457805160ff19168380011785556109c1565b828001600101855582156109c1579182015b828111156109c15782518255916020019190600101906109a6565b506109cd9291506109d1565b5090565b61077b91905b808211156109cd57600081556001016109d756fea165627a7a723058201d3c53056bb502bc319a5d94f30fdda21278435034fbbe4e86d07af6e1e2d24a0029";

    public static final String FUNC_GETFILENUM = "getFileNum";

    public static final String FUNC_WRITEDB = "writeDB";

    public static final String FUNC_SETWRITER = "setWriter";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC_READDB = "readDB";

    public static final String FUNC_CHECKWRITER = "checkWriter";

    public static final String FUNC_ADDREADER = "addReader";

    public static final String FUNC_CHECKREADER = "checkReader";

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

    public RemoteCall<TransactionReceipt> setWriter(Address _roleAddr) {
        final Function function = new Function(
                FUNC_SETWRITER, 
                Arrays.<Type>asList(_roleAddr), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Address> getOwner() {
        final Function function = new Function(FUNC_GETOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Utf8String> readDB(Utf8String _dataId) {
        final Function function = new Function(FUNC_READDB, 
                Arrays.<Type>asList(_dataId), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Bool> checkWriter(Address _roleAddr) {
        final Function function = new Function(FUNC_CHECKWRITER, 
                Arrays.<Type>asList(_roleAddr), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> addReader(Address _roleAddr) {
        final Function function = new Function(
                FUNC_ADDREADER, 
                Arrays.<Type>asList(_roleAddr), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Bool> checkReader(Address _roleAddr) {
        final Function function = new Function(FUNC_CHECKREADER, 
                Arrays.<Type>asList(_roleAddr), 
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
