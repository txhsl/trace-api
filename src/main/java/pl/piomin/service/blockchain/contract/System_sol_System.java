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
    private static final String BINARY = "608060405234801561001057600080fd5b5060038054600160a060020a031916331790556108eb806100326000396000f3fe608060405234801561001057600080fd5b506004361061007e577c010000000000000000000000000000000000000000000000000000000060003504632ffaf65d811461008357806332434a2e146101505780634b77bead1461020657806363edcf22146102ac5780639a169e851461035d578063df71496114610403575b600080fd5b6101346004803603604081101561009957600080fd5b8101906020810181356401000000008111156100b457600080fd5b8201836020820111156100c657600080fd5b803590602001918460018302840111640100000000831117156100e857600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955050509035600160a060020a031691506104299050565b60408051600160a060020a039092168252519081900360200190f35b6101346004803603604081101561016657600080fd5b600160a060020a03823516919081019060408101602082013564010000000081111561019157600080fd5b8201836020820111156101a357600080fd5b803590602001918460018302840111640100000000831117156101c557600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061059f945050505050565b6101346004803603602081101561021c57600080fd5b81019060208101813564010000000081111561023757600080fd5b82018360208201111561024957600080fd5b8035906020019184600183028401116401000000008311171561026b57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506106c2945050505050565b610134600480360360408110156102c257600080fd5b8101906020810181356401000000008111156102dd57600080fd5b8201836020820111156102ef57600080fd5b8035906020019184600183028401116401000000008311171561031157600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955050509035600160a060020a031691506107339050565b6101346004803603602081101561037357600080fd5b81019060208101813564010000000081111561038e57600080fd5b8201836020820111156103a057600080fd5b803590602001918460018302840111640100000000831117156103c257600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061086e945050505050565b6101346004803603602081101561041957600080fd5b5035600160a060020a03166108a1565b600354600090600160a060020a031633146104a557604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b816002846040518082805190602001908083835b602083106104d85780518252601f1990920191602091820191016104b9565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381018420805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0396909616959095179094555050845160029286929182918401908083835b602083106105615780518252601f199092019160209182019101610542565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a031695945050505050565b600354600090600160a060020a0316331461061b57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b6001826040518082805190602001908083835b6020831061064d5780518252601f19909201916020918201910161062e565b51815160001960209485036101000a0190811690199091161790529201948552506040805194859003820190942054600160a060020a03978816600090815291829052939020805473ffffffffffffffffffffffffffffffffffffffff19169387169390931792839055505090921692915050565b60006001826040518082805190602001908083835b602083106106f65780518252601f1990920191602091820191016106d7565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a0316949350505050565b600354600090600160a060020a031633146107af57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b816001846040518082805190602001908083835b602083106107e25780518252601f1990920191602091820191016107c3565b51815160001960209485036101000a0190811690199190911617905292019485525060405193849003810184208054600160a060020a039690961673ffffffffffffffffffffffffffffffffffffffff1990961695909517909455505084516001928692918291840190808383602083106105615780518252601f199092019160209182019101610542565b6000600282604051808280519060200190808383602083106106f65780518252601f1990920191602091820191016106d7565b600160a060020a03908116600090815260208190526040902054169056fea165627a7a72305820946fdc848b39259f48eb51dfe23046f193e6d912f6859e422462db55f8d8712d0029";

    public static final String FUNC_SETSCINDEX = "setScIndex";

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_GETRCINDEX = "getRcIndex";

    public static final String FUNC_SETRCINDEX = "setRcIndex";

    public static final String FUNC_GETSCINDEX = "getScIndex";

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

    public RemoteCall<TransactionReceipt> setScIndex(Utf8String _name, Address _scAddr) {
        final Function function = new Function(
                FUNC_SETSCINDEX, 
                Arrays.<Type>asList(_name, _scAddr), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> register(Address _user, Utf8String _roleName) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(_user, _roleName), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Address> getRcIndex(Utf8String _name) {
        final Function function = new Function(FUNC_GETRCINDEX, 
                Arrays.<Type>asList(_name), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> setRcIndex(Utf8String _name, Address _rcAddr) {
        final Function function = new Function(
                FUNC_SETRCINDEX, 
                Arrays.<Type>asList(_name, _rcAddr), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Address> getScIndex(Utf8String _name) {
        final Function function = new Function(FUNC_GETSCINDEX, 
                Arrays.<Type>asList(_name), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> getRC(Address _user) {
        final Function function = new Function(FUNC_GETRC, 
                Arrays.<Type>asList(_user), 
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
