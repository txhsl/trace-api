package pl.piomin.service.blockchain.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
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
public class System_sol_System extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5061068e806100206000396000f3fe608060405234801561001057600080fd5b5060043610610068577c010000000000000000000000000000000000000000000000000000000060003504634420e486811461006d578063b1e685d4146100af578063bf31c934146101df578063df71496114610313575b600080fd5b6100936004803603602081101561008357600080fd5b5035600160a060020a0316610339565b60408051600160a060020a039092168252519081900360200190f35b61016a600480360360608110156100c557600080fd5b600160a060020a03823516916020810135918101906060810160408201356401000000008111156100f557600080fd5b82018360208201111561010757600080fd5b8035906020019184600183028401116401000000008311171561012957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610375945050505050565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101a457818101518382015260200161018c565b50505050905090810190601f1680156101d15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61016a600480360360608110156101f557600080fd5b8135919081019060408101602082013564010000000081111561021757600080fd5b82018360208201111561022957600080fd5b8035906020019184600183028401116401000000008311171561024b57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929594936020810193503591505064010000000081111561029e57600080fd5b8201836020820111156102b057600080fd5b803590602001918460018302840111640100000000831117156102d257600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506104e6945050505050565b6100936004803603602081101561032957600080fd5b5035600160a060020a0316610644565b3360008181526020819052604090208054600160a060020a03841673ffffffffffffffffffffffffffffffffffffffff19909116179055919050565b600160a060020a038084166000908152602081815260408083205481517f2eee8179000000000000000000000000000000000000000000000000000000008152600481018881526024820193845287516044830152875160609793909316958695632eee8179958b958b9591936064909201928601918190849084905b8381101561040a5781810151838201526020016103f2565b50505050905090810190601f1680156104375780820380516001836020036101000a031916815260200191505b50935050505060006040518083038186803b15801561045557600080fd5b505afa158015610469573d6000803e3d6000fd5b505050506040513d6000823e601f3d908101601f19168201604052602081101561049257600080fd5b8101908080516401000000008111156104aa57600080fd5b820160208101848111156104bd57600080fd5b81516401000000008111828201871017156104d757600080fd5b50909998505050505050505050565b336000908152602081815260408083205490517f1b02961f000000000000000000000000000000000000000000000000000000008152600481018781526060602483018181528851606485015288519196600160a060020a03909516958695631b02961f958c958c958c9591949193604484019360840192880191908190849084905b83811015610581578181015183820152602001610569565b50505050905090810190601f1680156105ae5780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b838110156105e15781810151838201526020016105c9565b50505050905090810190601f16801561060e5780820380516001836020036101000a031916815260200191505b5095505050505050600060405180830381600087803b15801561063057600080fd5b505af1158015610469573d6000803e3d6000fd5b600160a060020a03908116600090815260208190526040902054169056fea165627a7a72305820946f3846d77140fa63b397d00ad0339030dda333f249795bbcc83d9822a36aff0029";

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_READRC = "readRC";

    public static final String FUNC_WRITERC = "writeRC";

    public static final String FUNC_GETRC = "getRC";

    @Deprecated
    protected System_sol_System(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected System_sol_System(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected System_sol_System(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected System_sol_System(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> register(Address _scAddress) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(_scAddress), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Utf8String> readRC(Address userAddr, Uint256 _propertyID, Utf8String _dataID) {
        final Function function = new Function(FUNC_READRC, 
                Arrays.<Type>asList(userAddr, _propertyID, _dataID), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> writeRC(Uint256 _propertyID, Utf8String _dataID, Utf8String _data) {
        final Function function = new Function(
                FUNC_WRITERC, 
                Arrays.<Type>asList(_propertyID, _dataID, _data), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Address> getRC(Address userAddr) {
        final Function function = new Function(FUNC_GETRC, 
                Arrays.<Type>asList(userAddr), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    @Deprecated
    public static System_sol_System load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new System_sol_System(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static System_sol_System load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new System_sol_System(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static System_sol_System load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new System_sol_System(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static System_sol_System load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new System_sol_System(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<System_sol_System> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(System_sol_System.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<System_sol_System> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(System_sol_System.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<System_sol_System> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(System_sol_System.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<System_sol_System> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(System_sol_System.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
