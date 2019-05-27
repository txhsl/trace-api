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
public class Role_sol_Role extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060028054600160a060020a031916331790556106d9806100326000396000f3fe608060405234801561001057600080fd5b5060043610610073577c010000000000000000000000000000000000000000000000000000000060003504633c3817a381146100785780634933b2001461014557806358f9ab54146101eb578063893d20e814610291578063fd1e926314610299575b600080fd5b6101296004803603604081101561008e57600080fd5b8101906020810181356401000000008111156100a957600080fd5b8201836020820111156100bb57600080fd5b803590602001918460018302840111640100000000831117156100dd57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955050509035600160a060020a0316915061034a9050565b60408051600160a060020a039092168252519081900360200190f35b6101296004803603602081101561015b57600080fd5b81019060208101813564010000000081111561017657600080fd5b82018360208201111561018857600080fd5b803590602001918460018302840111640100000000831117156101aa57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506104c0945050505050565b6101296004803603602081101561020157600080fd5b81019060208101813564010000000081111561021c57600080fd5b82018360208201111561022e57600080fd5b8035906020019184600183028401116401000000008311171561025057600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610531945050505050565b610129610563565b610129600480360360408110156102af57600080fd5b8101906020810181356401000000008111156102ca57600080fd5b8201836020820111156102dc57600080fd5b803590602001918460018302840111640100000000831117156102fe57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955050509035600160a060020a031691506105729050565b600254600090600160a060020a031633146103c657604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b816001846040518082805190602001908083835b602083106103f95780518252601f1990920191602091820191016103da565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381018420805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0396909616959095179094555050845160019286929182918401908083835b602083106104825780518252601f199092019160209182019101610463565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a031695945050505050565b60006001826040518082805190602001908083835b602083106104f45780518252601f1990920191602091820191016104d5565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a0316949350505050565b60008082604051808280519060200190808383602083106104f45780518252601f1990920191602091820191016104d5565b600254600160a060020a031690565b600254600090600160a060020a031633146105ee57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b816000846040518082805190602001908083835b602083106106215780518252601f199092019160209182019101610602565b51815160001960209485036101000a0190811690199190911617905292019485525060405193849003810184208054600160a060020a039690961673ffffffffffffffffffffffffffffffffffffffff1990961695909517909455505084516000928692918291840190808383602083106104825780518252601f19909201916020918201910161046356fea165627a7a72305820627b23eab1a6f67f5837de390f8a2dcd974eee559e64317edfac1ee4080d0a710029";

    public static final String FUNC_SETMANAGED = "setManaged";

    public static final String FUNC_GETMANAGED = "getManaged";

    public static final String FUNC_GETOWNED = "getOwned";

    public static final String FUNC_GETOWNER = "getOwner";

    public static final String FUNC_SETOWNED = "setOwned";

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

    public RemoteCall<TransactionReceipt> setManaged(Utf8String _name, Address _scAddr) {
        final Function function = new Function(
                FUNC_SETMANAGED, 
                Arrays.<Type>asList(_name, _scAddr), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Address> getManaged(Utf8String _name) {
        final Function function = new Function(FUNC_GETMANAGED, 
                Arrays.<Type>asList(_name), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> getOwned(Utf8String _name) {
        final Function function = new Function(FUNC_GETOWNED, 
                Arrays.<Type>asList(_name), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> getOwner() {
        final Function function = new Function(FUNC_GETOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> setOwned(Utf8String _name, Address _scAddr) {
        final Function function = new Function(
                FUNC_SETOWNED, 
                Arrays.<Type>asList(_name, _scAddr), 
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

    public static RemoteCall<Role_sol_Role> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Role_sol_Role.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Role_sol_Role> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Role_sol_Role.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Role_sol_Role> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Role_sol_Role.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
