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
    private static final String BINARY = "608060405234801561001057600080fd5b506108cf806100206000396000f3fe608060405234801561001057600080fd5b506004361061005e576000357c0100000000000000000000000000000000000000000000000000000000900480634420e48614610063578063bf31c934146100e7578063f8e7e58d146102bc575b600080fd5b6100a56004803603602081101561007957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103fa565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b610241600480360360608110156100fd57600080fd5b81019080803590602001909291908035906020019064010000000081111561012457600080fd5b82018360208201111561013657600080fd5b8035906020019184600183028401116401000000008311171561015857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803590602001906401000000008111156101bb57600080fd5b8201836020820111156101cd57600080fd5b803590602001918460018302840111640100000000831117156101ef57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610481565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610281578082015181840152602081019050610266565b50505050905090810190601f1680156102ae5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61037f600480360360408110156102d257600080fd5b8101908080359060200190929190803590602001906401000000008111156102f957600080fd5b82018360208201111561030b57600080fd5b8035906020019184600183028401116401000000008311171561032d57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506106ca565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156103bf5780820151818401526020810190506103a4565b50505050905090810190601f1680156103ec5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6000816000803373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550339050919050565b606060008060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690508073ffffffffffffffffffffffffffffffffffffffff16631b02961f8686866040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b8381101561057a57808201518184015260208101905061055f565b50505050905090810190601f1680156105a75780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156105e05780820151818401526020810190506105c5565b50505050905090810190601f16801561060d5780820380516001836020036101000a031916815260200191505b5095505050505050600060405180830381600087803b15801561062f57600080fd5b505af1158015610643573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250602081101561066d57600080fd5b81019080805164010000000081111561068557600080fd5b8281019050602081018481111561069b57600080fd5b81518560018202830111640100000000821117156106b857600080fd5b50509291905050509150509392505050565b606060008060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690508073ffffffffffffffffffffffffffffffffffffffff16632eee817985856040518363ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018083815260200180602001828103825283818151815260200191508051906020019080838360005b838110156107be5780820151818401526020810190506107a3565b50505050905090810190601f1680156107eb5780820380516001836020036101000a031916815260200191505b50935050505060006040518083038186803b15801561080957600080fd5b505afa15801561081d573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250602081101561084757600080fd5b81019080805164010000000081111561085f57600080fd5b8281019050602081018481111561087557600080fd5b815185600182028301116401000000008211171561089257600080fd5b50509291905050509150509291505056fea165627a7a7230582098011720830e99ccc8c3c773bc3e67f575440bcf4abc180f973bf0f44b9613120029";

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_WRITERC = "writeRC";

    public static final String FUNC_READRC = "readRC";

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

    public RemoteCall<TransactionReceipt> writeRC(Uint256 _propertyID, Utf8String _dataID, Utf8String _data) {
        final Function function = new Function(
                FUNC_WRITERC, 
                Arrays.<Type>asList(_propertyID, _dataID, _data), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Utf8String> readRC(Uint256 _propertyID, Utf8String _dataID) {
        final Function function = new Function(FUNC_READRC, 
                Arrays.<Type>asList(_propertyID, _dataID), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
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
