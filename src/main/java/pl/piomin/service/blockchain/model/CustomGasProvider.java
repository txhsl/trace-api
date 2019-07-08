package pl.piomin.service.blockchain.model;

import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

public class CustomGasProvider extends DefaultGasProvider {

    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(6000000);

}
