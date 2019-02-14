package pl.piomin.service.blockchain.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
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
    private static final String BINARY = "608060405234801561001057600080fd5b506105c0806100206000396000f3fe608060405234801561001057600080fd5b5060043610610053576000357c0100000000000000000000000000000000000000000000000000000000900480632f779e3b146100585780639f64158214610223575b600080fd5b6101a86004803603604081101561006e57600080fd5b810190808035906020019064010000000081111561008b57600080fd5b82018360208201111561009d57600080fd5b803590602001918460018302840111640100000000831117156100bf57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561012257600080fd5b82018360208201111561013457600080fd5b8035906020019184600183028401116401000000008311171561015657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610357565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101e85780820151818401526020810190506101cd565b50505050905090810190601f1680156102155780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102dc6004803603602081101561023957600080fd5b810190808035906020019064010000000081111561025657600080fd5b82018360208201111561026857600080fd5b8035906020019184600183028401116401000000008311171561028a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506103e2565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561031c578082015181840152602081019050610301565b50505050905090810190601f1680156103495780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6060816001846040518082805190602001908083835b602083101515610392578051825260208201915060208101905060208303925061036d565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902090805190602001906103d89291906104ef565b5082905092915050565b60606001826040518082805190602001908083835b60208310151561041c57805182526020820191506020810190506020830392506103f7565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104e35780601f106104b8576101008083540402835291602001916104e3565b820191906000526020600020905b8154815290600101906020018083116104c657829003601f168201915b50505050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061053057805160ff191683800117855561055e565b8280016001018555821561055e579182015b8281111561055d578251825591602001919060010190610542565b5b50905061056b919061056f565b5090565b61059191905b8082111561058d576000816000905550600101610575565b5090565b9056fea165627a7a72305820c1af5b40c5889288acfc1d762eabc3dd13e11b2c3f8cdf37d396adad08c848550029";

    public static final String FUNC_WRITEDB = "writeDB";

    public static final String FUNC_READDB = "readDB";

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

    public RemoteCall<TransactionReceipt> writeDB(Utf8String _id, Utf8String _data) {
        final Function function = new Function(
                FUNC_WRITEDB, 
                Arrays.<Type>asList(_id, _data), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Utf8String> readDB(Utf8String _id) {
        final Function function = new Function(FUNC_READDB, 
                Arrays.<Type>asList(_id), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
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

    @Deprecated
    public static RemoteCall<Data_sol_Data> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Data_sol_Data.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Data_sol_Data> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Data_sol_Data.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Data_sol_Data> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Data_sol_Data.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
