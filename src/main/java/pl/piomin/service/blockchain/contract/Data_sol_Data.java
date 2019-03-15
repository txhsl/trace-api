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
    private static final String BINARY = "608060405234801561001057600080fd5b5060028054600160a060020a03191633179055610925806100326000396000f3fe608060405234801561001057600080fd5b5060043610610073577c010000000000000000000000000000000000000000000000000000000060003504630995d2fe81146100785780632f779e3b146101935780636aaad277146102c05780639f64158214610307578063aafd338b146103ad575b600080fd5b61011e6004803603602081101561008e57600080fd5b8101906020810181356401000000008111156100a957600080fd5b8201836020820111156100bb57600080fd5b803590602001918460018302840111640100000000831117156100dd57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506103e0945050505050565b6040805160208082528351818301528351919283929083019185019080838360005b83811015610158578181015183820152602001610140565b50505050905090810190601f1680156101855780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61011e600480360360408110156101a957600080fd5b8101906020810181356401000000008111156101c457600080fd5b8201836020820111156101d657600080fd5b803590602001918460018302840111640100000000831117156101f857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929594936020810193503591505064010000000081111561024b57600080fd5b82018360208201111561025d57600080fd5b8035906020019184600183028401116401000000008311171561027f57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610473945050505050565b6102f3600480360360208110156102d657600080fd5b503573ffffffffffffffffffffffffffffffffffffffff16610676565b604080519115158252519081900360200190f35b61011e6004803603602081101561031d57600080fd5b81019060208101813564010000000081111561033857600080fd5b82018360208201111561034a57600080fd5b8035906020019184600183028401116401000000008311171561036c57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506106a1945050505050565b6102f3600480360360208110156103c357600080fd5b503573ffffffffffffffffffffffffffffffffffffffff1661079d565b8051606090600090839060001981019081106103f857fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535081516000908390600119810190811061043d57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535090919050565b60025460609073ffffffffffffffffffffffffffffffffffffffff1633146104fc57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b816001846040518082805190602001908083835b6020831061052f5780518252601f199092019160209182019101610510565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381019093208451610570959194919091019250905061085e565b50816001846040518082805190602001908083835b602083106105a45780518252601f199092019160209182019101610585565b51815160209384036101000a600019018019909216911617905292019485525060405193849003810190932084516105e5959194919091019250905061085e565b805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156106695780601f1061063e57610100808354040283529160200191610669565b820191906000526020600020905b81548152906001019060200180831161064c57829003601f168201915b5050505050905092915050565b73ffffffffffffffffffffffffffffffffffffffff1660009081526020819052604090205460ff1690565b606060016106ae836103e0565b6040518082805190602001908083835b602083106106dd5780518252601f1990920191602091820191016106be565b518151600019602094850361010090810a820192831692199390931691909117909252949092019687526040805197889003820188208054601f60026001831615909802909501169590950492830182900482028801820190528187529294509250508301828280156107915780601f1061076657610100808354040283529160200191610791565b820191906000526020600020905b81548152906001019060200180831161077457829003601f168201915b50505050509050919050565b60025460009073ffffffffffffffffffffffffffffffffffffffff16331461082657604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b5073ffffffffffffffffffffffffffffffffffffffff166000908152602081905260409020805460ff19166001179081905560ff1690565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061089f57805160ff19168380011785556108cc565b828001600101855582156108cc579182015b828111156108cc5782518255916020019190600101906108b1565b506108d89291506108dc565b5090565b6108f691905b808211156108d857600081556001016108e2565b9056fea165627a7a723058207779b749630648f564eb4b786970c642dc972360beaaae29f882dabcbc4ed86c0029";

    public static final String FUNC_GETFILENUM = "getFileNum";

    public static final String FUNC_WRITEDB = "writeDB";

    public static final String FUNC_CHECKPERMITTED = "checkPermitted";

    public static final String FUNC_READDB = "readDB";

    public static final String FUNC_ADDPERMITTED = "addPermitted";

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
