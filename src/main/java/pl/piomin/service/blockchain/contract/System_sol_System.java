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
    private static final String BINARY = "608060405234801561001057600080fd5b5060048054600160a060020a03191633179055610c29806100326000396000f3fe6080604052600436106100a3576000357c010000000000000000000000000000000000000000000000000000000090048063590e1ae311610076578063590e1ae3146103b8578063817c8966146103cd5780639a169e8514610400578063df714961146104b3578063e0ef1007146104e6576100a3565b806323b872dd146101135780632ffaf65d1461016857806332434a2e146102425780634b77bead14610305575b60003410156100fc576040805160e560020a62461bcd02815260206004820152601660248201527f57726f6e6720616d6f756e74206f6620746f6b656e2e00000000000000000000604482015290519081900360640190fd5b336000908152600560205260409020805434019055005b34801561011f57600080fd5b506101566004803603606081101561013657600080fd5b50600160a060020a038135811691602081013590911690604001356105af565b60408051918252519081900360200190f35b34801561017457600080fd5b506102266004803603604081101561018b57600080fd5b8101906020810181356401000000008111156101a657600080fd5b8201836020820111156101b857600080fd5b803590602001918460018302840111640100000000831117156101da57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955050509035600160a060020a031691506106a89050565b60408051600160a060020a039092168252519081900360200190f35b34801561024e57600080fd5b506102266004803603604081101561026557600080fd5b600160a060020a03823516919081019060408101602082013564010000000081111561029057600080fd5b8201836020820111156102a257600080fd5b803590602001918460018302840111640100000000831117156102c457600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506107f5945050505050565b34801561031157600080fd5b506102266004803603602081101561032857600080fd5b81019060208101813564010000000081111561034357600080fd5b82018360208201111561035557600080fd5b8035906020019184600183028401116401000000008311171561037757600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506108ef945050505050565b3480156103c457600080fd5b50610156610960565b3480156103d957600080fd5b50610156600480360360208110156103f057600080fd5b5035600160a060020a0316610a0a565b34801561040c57600080fd5b506102266004803603602081101561042357600080fd5b81019060208101813564010000000081111561043e57600080fd5b82018360208201111561045057600080fd5b8035906020019184600183028401116401000000008311171561047257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610a25945050505050565b3480156104bf57600080fd5b50610226600480360360208110156104d657600080fd5b5035600160a060020a0316610a58565b3480156104f257600080fd5b506102266004803603606081101561050957600080fd5b81019060208101813564010000000081111561052457600080fd5b82018360208201111561053657600080fd5b8035906020019184600183028401116401000000008311171561055857600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955050600160a060020a038335811694506020909301359092169150610a769050565b3360009081526003602052604081205460ff161515600114610609576040805160e560020a62461bcd0281526020600482015260126024820152600080516020610bde833981519152604482015290519081900360640190fd5b600160a060020a03841660009081526005602052604090205482111561065c57600160a060020a03808516600090815260056020526040808220805490839055928616825290208054909101905561068a565b600160a060020a03808516600090815260056020526040808220805486900390559185168152208054830190555b505050600160a060020a031660009081526005602052604090205490565b600454600090600160a060020a031633146106fb576040805160e560020a62461bcd0281526020600482015260126024820152600080516020610bde833981519152604482015290519081900360640190fd5b816002846040518082805190602001908083835b6020831061072e5780518252601f19909201916020918201910161070f565b51815160209384036101000a60001901801990921691161790529201948552506040519384900381018420805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0396909616959095179094555050845160029286929182918401908083835b602083106107b75780518252601f199092019160209182019101610798565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a031695945050505050565b600454600090600160a060020a03163314610848576040805160e560020a62461bcd0281526020600482015260126024820152600080516020610bde833981519152604482015290519081900360640190fd5b6001826040518082805190602001908083835b6020831061087a5780518252601f19909201916020918201910161085b565b51815160001960209485036101000a0190811690199091161790529201948552506040805194859003820190942054600160a060020a03978816600090815291829052939020805473ffffffffffffffffffffffffffffffffffffffff19169387169390931792839055505090921692915050565b60006001826040518082805190602001908083835b602083106109235780518252601f199092019160209182019101610904565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a0316949350505050565b336000908152600560205260408120548181116109c7576040805160e560020a62461bcd02815260206004820152600e60248201527f4e6f20746f6b656e206c6566742e000000000000000000000000000000000000604482015290519081900360640190fd5b604051339082156108fc029083906000818181858888f193505050501580156109f4573d6000803e3d6000fd5b5033600090815260056020526040812055905090565b600160a060020a031660009081526005602052604090205490565b6000600282604051808280519060200190808383602083106109235780518252601f199092019160209182019101610904565b600160a060020a039081166000908152602081905260409020541690565b600454600090600160a060020a03163314610ac9576040805160e560020a62461bcd0281526020600482015260126024820152600080516020610bde833981519152604482015290519081900360640190fd5b826001856040518082805190602001908083835b60208310610afc5780518252601f199092019160209182019101610add565b51815160209384036101000a6000190180199092169116179052920194855250604080519485900382018520805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0397881617905594871660009081526003825294909420805460ff1916600190811790915588519094899493508392508401908083835b60208310610b9e5780518252601f199092019160209182019101610b7f565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a0316969550505050505056fe5065726d697373696f6e2064656e6965642e0000000000000000000000000000a165627a7a7230582061630e888904650cb2e2cf2b12329c28b93237b5ffd2010416bc3deea5bb8de90029";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_SETSCINDEX = "setScIndex";

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_GETRCINDEX = "getRcIndex";

    public static final String FUNC_REFUND = "refund";

    public static final String FUNC_GETLEVEL = "getLevel";

    public static final String FUNC_GETSCINDEX = "getScIndex";

    public static final String FUNC_GETRC = "getRC";

    public static final String FUNC_SETRCINDEX = "setRcIndex";

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

    public RemoteCall<TransactionReceipt> transferFrom(Address _from, Address _to, Uint256 amount) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(_from, _to, amount), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public RemoteCall<TransactionReceipt> refund() {
        final Function function = new Function(
                FUNC_REFUND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Uint256> getLevel(Address _user) {
        final Function function = new Function(FUNC_GETLEVEL, 
                Arrays.<Type>asList(_user), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
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

    public RemoteCall<TransactionReceipt> setRcIndex(Utf8String _name, Address _rcAddr, Address _rcManager) {
        final Function function = new Function(
                FUNC_SETRCINDEX, 
                Arrays.<Type>asList(_name, _rcAddr, _rcManager), 
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
