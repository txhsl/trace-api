package pl.piomin.service.blockchain.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
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
public class System_sol_System extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060028054600160a060020a031916331790556105fb806100326000396000f3fe608060405234801561001057600080fd5b5060043610610068577c01000000000000000000000000000000000000000000000000000000006000350463df714961811461006d578063ea1bbe35146100af578063f2c298be14610155578063fd74aac6146101fb575b600080fd5b6100936004803603602081101561008357600080fd5b5035600160a060020a03166102ac565b60408051600160a060020a039092168252519081900360200190f35b610093600480360360208110156100c557600080fd5b8101906020810181356401000000008111156100e057600080fd5b8201836020820111156100f257600080fd5b8035906020019184600183028401116401000000008311171561011457600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506102ca945050505050565b6100936004803603602081101561016b57600080fd5b81019060208101813564010000000081111561018657600080fd5b82018360208201111561019857600080fd5b803590602001918460018302840111640100000000831117156101ba57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061033b945050505050565b6100936004803603604081101561021157600080fd5b81019060208101813564010000000081111561022c57600080fd5b82018360208201111561023e57600080fd5b8035906020019184600183028401116401000000008311171561026057600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955050509035600160a060020a031691506104599050565b600160a060020a039081166000908152602081905260409020541690565b60006001826040518082805190602001908083835b602083106102fe5780518252601f1990920191602091820191016102df565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a0316949350505050565b600254600090600160a060020a031633146103b757604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b6001826040518082805190602001908083835b602083106103e95780518252601f1990920191602091820191016103ca565b51815160001960209485036101000a019081169019909116179052920194855250604080519485900382019094205433600090815291829052939020805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a039485161790819055909216949350505050565b600254600090600160a060020a031633146104d557604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b816001846040518082805190602001908083835b602083106105085780518252601f1990920191602091820191016104e9565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381018420805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0396909616959095179094555050845160019286929182918401908083835b602083106105915780518252601f199092019160209182019101610572565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a03169594505050505056fea165627a7a723058204302f14478aef61c73d6a5fc1261427428fde141b0a56ba0553e9eab215451de0029";

    public static final String FUNC_GETRC = "getRC";

    public static final String FUNC_GETINDEX = "getIndex";

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_SETINDEX = "setIndex";

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

    public RemoteCall<Address> getRC(Address _user) {
        final Function function = new Function(FUNC_GETRC, 
                Arrays.<Type>asList(_user), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> getIndex(Utf8String _name) {
        final Function function = new Function(FUNC_GETINDEX, 
                Arrays.<Type>asList(_name), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> register(Utf8String _name) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(_name), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setIndex(Utf8String _name, Address _rcAddr) {
        final Function function = new Function(
                FUNC_SETINDEX, 
                Arrays.<Type>asList(_name, _rcAddr), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public static RemoteCall<System_sol_System> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(System_sol_System.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<System_sol_System> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(System_sol_System.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<System_sol_System> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(System_sol_System.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
