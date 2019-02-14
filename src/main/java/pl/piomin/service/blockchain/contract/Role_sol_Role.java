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
public class Role_sol_Role extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610886806100206000396000f3fe608060405234801561001057600080fd5b506004361061005e576000357c0100000000000000000000000000000000000000000000000000000000900480631b02961f146100635780632eee817914610238578063e0b21ae414610376575b600080fd5b6101bd6004803603606081101561007957600080fd5b8101908080359060200190929190803590602001906401000000008111156100a057600080fd5b8201836020820111156100b257600080fd5b803590602001918460018302840111640100000000831117156100d457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561013757600080fd5b82018360208201111561014957600080fd5b8035906020019184600183028401116401000000008311171561016b57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506103e4565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101fd5780820151818401526020810190506101e2565b50505050905090810190601f16801561022a5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102fb6004803603604081101561024e57600080fd5b81019080803590602001909291908035906020019064010000000081111561027557600080fd5b82018360208201111561028757600080fd5b803590602001918460018302840111640100000000831117156102a957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506105fa565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561033b578082015181840152602081019050610320565b50505050905090810190601f1680156103685780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6103ce6004803603606081101561038c57600080fd5b8101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080351515906020019092919050505061079f565b6040518082815260200191505060405180910390f35b606060006001600086815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690508073ffffffffffffffffffffffffffffffffffffffff16632f779e3b85856040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808060200180602001838103835285818151815260200191508051906020019080838360005b838110156104ab578082015181840152602081019050610490565b50505050905090810190601f1680156104d85780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156105115780820151818401526020810190506104f6565b50505050905090810190601f16801561053e5780820380516001836020036101000a031916815260200191505b50945050505050600060405180830381600087803b15801561055f57600080fd5b505af1158015610573573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250602081101561059d57600080fd5b8101908080516401000000008111156105b557600080fd5b828101905060208101848111156105cb57600080fd5b81518560018202830111640100000000821117156105e857600080fd5b50509291905050509150509392505050565b6060600080600085815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690508073ffffffffffffffffffffffffffffffffffffffff16639f641582846040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b838110156106bb5780820151818401526020810190506106a0565b50505050905090810190601f1680156106e85780820380516001836020036101000a031916815260200191505b509250505060006040518083038186803b15801561070557600080fd5b505afa158015610719573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f82011682018060405250602081101561074357600080fd5b81019080805164010000000081111561075b57600080fd5b8281019050602081018481111561077157600080fd5b815185600182028301116401000000008211171561078e57600080fd5b505092919050505091505092915050565b600081156107fd578260008086815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610850565b826001600086815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b839050939250505056fea165627a7a723058201b6d5fdf027fa3a4c66952eea78ae7355f3ffd673e757e5e07e178da05c191950029";

    public static final String FUNC_WRITESC = "writeSC";

    public static final String FUNC_READSC = "readSC";

    public static final String FUNC_LINK = "link";

    @Deprecated
    protected Role_sol_Role(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Role_sol_Role(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Role_sol_Role(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Role_sol_Role(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> writeSC(Uint256 _propertyID, Utf8String _dataID, Utf8String _data) {
        final Function function = new Function(
                FUNC_WRITESC, 
                Arrays.<Type>asList(_propertyID, _dataID, _data), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Utf8String> readSC(Uint256 _propertyID, Utf8String _dataID) {
        final Function function = new Function(FUNC_READSC, 
                Arrays.<Type>asList(_propertyID, _dataID), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> link(Uint256 _propertyID, Address addrDB, Bool isRead) {
        final Function function = new Function(
                FUNC_LINK, 
                Arrays.<Type>asList(_propertyID, addrDB, isRead), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Role_sol_Role load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Role_sol_Role(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Role_sol_Role load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Role_sol_Role(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Role_sol_Role load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Role_sol_Role(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Role_sol_Role load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Role_sol_Role(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Role_sol_Role> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Role_sol_Role.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Role_sol_Role> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Role_sol_Role.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Role_sol_Role> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Role_sol_Role.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Role_sol_Role> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Role_sol_Role.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
