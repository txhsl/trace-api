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
public class Role_sol_Role extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610781806100206000396000f3fe608060405234801561001057600080fd5b5060043610610068577c010000000000000000000000000000000000000000000000000000000060003504631b02961f811461006d5780632eee817914610216578063a47d34c2146102c3578063abc1b70414610307575b600080fd5b6101a16004803603606081101561008357600080fd5b813591908101906040810160208201356401000000008111156100a557600080fd5b8201836020820111156100b757600080fd5b803590602001918460018302840111640100000000831117156100d957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929594936020810193503591505064010000000081111561012c57600080fd5b82018360208201111561013e57600080fd5b8035906020019184600183028401116401000000008311171561016057600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610346945050505050565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101db5781810151838201526020016101c3565b50505050905090810190601f1680156102085780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101a16004803603604081101561022c57600080fd5b8135919081019060408101602082013564010000000081111561024e57600080fd5b82018360208201111561026057600080fd5b8035906020019184600183028401116401000000008311171561028257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610519945050505050565b6102f5600480360360608110156102d957600080fd5b50803590600160a060020a03602082013516906040013561067f565b60408051918252519081900360200190f35b61032a6004803603604081101561031d57600080fd5b5080359060200135610707565b60408051600160a060020a039092168252519081900360200190f35b60008381526001602090815260408083205481517f2f779e3b00000000000000000000000000000000000000000000000000000000815260048101928352865160448201528651606095600160a060020a03909316948594632f779e3b948a948a94929384936024830193606490930192918801918190849084905b838110156103da5781810151838201526020016103c2565b50505050905090810190601f1680156104075780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b8381101561043a578181015183820152602001610422565b50505050905090810190601f1680156104675780820380516001836020036101000a031916815260200191505b50945050505050600060405180830381600087803b15801561048857600080fd5b505af115801561049c573d6000803e3d6000fd5b505050506040513d6000823e601f3d908101601f1916820160405260208110156104c557600080fd5b8101908080516401000000008111156104dd57600080fd5b820160208101848111156104f057600080fd5b815164010000000081118282018710171561050a57600080fd5b50909998505050505050505050565b6000828152602081815260408083205490517f9f64158200000000000000000000000000000000000000000000000000000000815260048101838152855160248301528551606095600160a060020a03909416948594639f641582948994938493604490920192918601918190849084905b838110156105a357818101518382015260200161058b565b50505050905090810190601f1680156105d05780820380516001836020036101000a031916815260200191505b509250505060006040518083038186803b1580156105ed57600080fd5b505afa158015610601573d6000803e3d6000fd5b505050506040513d6000823e601f3d908101601f19168201604052602081101561062a57600080fd5b81019080805164010000000081111561064257600080fd5b8201602081018481111561065557600080fd5b815164010000000081118282018710171561066f57600080fd5b5090955050505050505b92915050565b60008115156106c1576000848152602081905260409020805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0385161790556106ff565b81600114156106ff576000848152600160205260409020805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0385161790555b509192915050565b600081151561072e5750600082815260208190526040902054600160a060020a0316610679565b81600114156106795750600082815260016020526040902054600160a060020a031661067956fea165627a7a72305820fceb94592802218ad32161e5adf4c7a9eebf7c19b5fc06321bddda978ba28f580029";

    public static final String FUNC_WRITESC = "writeSC";

    public static final String FUNC_READSC = "readSC";

    public static final String FUNC_LINK = "link";

    public static final String FUNC_GETSC = "getSC";

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

    public RemoteCall<TransactionReceipt> link(Uint256 _propertyID, Address addrDB, Uint256 permissionType) {
        final Function function = new Function(
                FUNC_LINK, 
                Arrays.<Type>asList(_propertyID, addrDB, permissionType), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Address> getSC(Uint256 _propertyID, Uint256 permissionType) {
        final Function function = new Function(FUNC_GETSC, 
                Arrays.<Type>asList(_propertyID, permissionType), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
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
