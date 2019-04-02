package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.contract.Role_sol_Role;
import pl.piomin.service.blockchain.contract.System_sol_System;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    public static final String[] accounts = new String[]{"0x6a2fb5e3bf37f0c3d90db4713f7ad4a3b2c24111", "0xdb2bbab1d9eca60c937aa9c995664f86b3da2934", "0xcdfea5a11062fab4cf4c2fda88e32fc6f7753145",
            "0x329b81e0a2af215c7e41b32251ae4d6ff1a83e3e", "0x370287edd5a5e7c4b0f5e305b00fe95fc702ce47", "0x40b00de2e7b694b494022eef90e874f5e553f996", "0x49e2170e0b1188f2151ac35287c743ee60ea1f6a"};

    private final Web3j web3j;
    private Credentials current = null;

    public UserService(Web3j web3j) {
        this.web3j = web3j;
    }

    public Credentials getCurrent() {
        return current;
    }

    public String[] resetContract(String sysAddr) throws Exception {

        //recreate RCs
        String[] roleAddrs = new String[RoleType.Types.size()];

        for (int i = 0; i < 6; i++) {
            if (signIn(accounts[i + 1], "Innov@teD@ily1")) {
                Role_sol_Role role = Role_sol_Role.deploy(web3j, current, GAS_PRICE, GAS_LIMIT).send();
                LOGGER.info("Role Contract " + i + " deployed: " + role.getContractAddress() + ". Role Name: " + RoleType.Types.get(i));
                roleAddrs[i] = role.getContractAddress();

                if (signIn(accounts[0],"Innov@teD@ily1" )) {
                    System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                    TransactionReceipt transactionReceipt = system.setRcIndex(new Utf8String(RoleType.Types.get(i)), new Address(roleAddrs[i])).send();
                    LOGGER.info("Transaction succeed: " + transactionReceipt.getTransactionHash());

                    //register users
                    transactionReceipt = system.register(new Address(accounts[i + 1]), new Utf8String(RoleType.Types.get(i))).send();
                    LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                }
            }
        }

        return roleAddrs;
    }

    public boolean resetPermission(String[] roleAddrs, String[] dataAddrs) throws Exception {
        //link RCs and SCs
        //Producer-Producer
        if (signIn(accounts[1], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Producer")], "Producer_Operator", new Address(dataAddrs[PropertyType.getID("Producer_Operator")]));
            setOwned(roleAddrs[RoleType.getID("Producer")], "Producer_Operator", new Address(dataAddrs[PropertyType.getID("Producer_Operator")]));

            setManaged(roleAddrs[RoleType.getID("Producer")], "Producer_ProduceDate", new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]));
            setOwned(roleAddrs[RoleType.getID("Producer")], "Producer_ProduceDate", new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]));

            setManaged(roleAddrs[RoleType.getID("Producer")], "Producer_OutDate", new Address(dataAddrs[PropertyType.getID("Producer_OutDate")]));
            setOwned(roleAddrs[RoleType.getID("Producer")], "Producer_OutDate", new Address(dataAddrs[PropertyType.getID("Producer_OutDate")]));

            setManaged(roleAddrs[RoleType.getID("Producer")], "Producer_GuaranteeDate", new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]));
            setOwned(roleAddrs[RoleType.getID("Producer")], "Producer_GuaranteeDate", new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]));

            setManaged(roleAddrs[RoleType.getID("Producer")], "Producer_TestResult", new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]));

            setManaged(roleAddrs[RoleType.getID("Producer")], "Producer_BasePrice", new Address(dataAddrs[PropertyType.getID("Producer_BasePrice")]));
            setOwned(roleAddrs[RoleType.getID("Producer")], "Producer_BasePrice", new Address(dataAddrs[PropertyType.getID("Producer_BasePrice")]));

            setManaged(roleAddrs[RoleType.getID("Producer")], "Producer_SellPrice", new Address(dataAddrs[PropertyType.getID("Producer_SellPrice")]));
            setOwned(roleAddrs[RoleType.getID("Producer")], "Producer_SellPrice", new Address(dataAddrs[PropertyType.getID("Producer_SellPrice")]));

            setManaged(roleAddrs[RoleType.getID("Producer")], "Producer_Amount", new Address(dataAddrs[PropertyType.getID("Producer_Amount")]));
            setOwned(roleAddrs[RoleType.getID("Producer")], "Producer_Amount", new Address(dataAddrs[PropertyType.getID("Producer_Amount")]));
        }
        //Storager-Producer
        if (signIn(accounts[2], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Storager")], "Producer_Operator", new Address(dataAddrs[PropertyType.getID("Producer_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Storager")], "Producer_ProduceDate", new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]));
            setManaged(roleAddrs[RoleType.getID("Storager")], "Producer_OutDate", new Address(dataAddrs[PropertyType.getID("Producer_OutDate")]));
            setManaged(roleAddrs[RoleType.getID("Storager")], "Producer_GuaranteeDate", new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]));
            setManaged(roleAddrs[RoleType.getID("Storager")], "Producer_TestResult", new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]));
        }
        //Transporter-Producer
        if (signIn(accounts[3], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Producer_Operator", new Address(dataAddrs[PropertyType.getID("Producer_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Producer_ProduceDate", new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]));
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Producer_GuaranteeDate", new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]));
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Producer_TestResult", new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]));
        }
        //Seller-Producer
        if (signIn(accounts[4], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Seller")], "Producer_Operator", new Address(dataAddrs[PropertyType.getID("Producer_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Seller")], "Producer_ProduceDate", new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]));
            setManaged(roleAddrs[RoleType.getID("Seller")], "Producer_GuaranteeDate", new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]));
            setManaged(roleAddrs[RoleType.getID("Seller")], "Producer_TestResult", new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]));
            setManaged(roleAddrs[RoleType.getID("Seller")], "Producer_SellPrice", new Address(dataAddrs[PropertyType.getID("Producer_SellPrice")]));
        }
        //Buyer-Producer
        if (signIn(accounts[5], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Producer_Operator", new Address(dataAddrs[PropertyType.getID("Producer_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Producer_ProduceDate", new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]));
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Producer_GuaranteeDate", new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]));
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Producer_TestResult", new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]));
        }
        //Government-Producer
        if (signIn(accounts[6], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Government")], "Producer_Operator", new Address(dataAddrs[PropertyType.getID("Producer_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Producer_ProduceDate", new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Producer_OutDate", new Address(dataAddrs[PropertyType.getID("Producer_OutDate")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Producer_GuaranteeDate", new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Producer_TestResult", new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]));
            setOwned(roleAddrs[RoleType.getID("Government")], "Producer_TestResult", new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Producer_BasePrice", new Address(dataAddrs[PropertyType.getID("Producer_BasePrice")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Producer_SellPrice", new Address(dataAddrs[PropertyType.getID("Producer_SellPrice")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Producer_Amount", new Address(dataAddrs[PropertyType.getID("Producer_Amount")]));
        }
        //Producer-Storager
        if (signIn(accounts[1], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Producer")], "Storager_Operator", new Address(dataAddrs[PropertyType.getID("Storager_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Storager_InTime", new Address(dataAddrs[PropertyType.getID("Storager_InTime")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Storager_OutTime", new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Storager_Price", new Address(dataAddrs[PropertyType.getID("Storager_Price")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Storager_Duration", new Address(dataAddrs[PropertyType.getID("Storager_Duration")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Storager_Amount", new Address(dataAddrs[PropertyType.getID("Storager_Amount")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Storager_Company", new Address(dataAddrs[PropertyType.getID("Storager_Company")]));
        }
        //Storager-Storager
        if (signIn(accounts[2], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Storager")], "Storager_Operator", new Address(dataAddrs[PropertyType.getID("Storager_Operator")]));
            setOwned(roleAddrs[RoleType.getID("Storager")], "Storager_Operator", new Address(dataAddrs[PropertyType.getID("Storager_Operator")]));

            setManaged(roleAddrs[RoleType.getID("Storager")], "Storager_InTime", new Address(dataAddrs[PropertyType.getID("Storager_InTime")]));
            setOwned(roleAddrs[RoleType.getID("Storager")], "Storager_InTime", new Address(dataAddrs[PropertyType.getID("Storager_InTime")]));

            setManaged(roleAddrs[RoleType.getID("Storager")], "Storager_OutTime", new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]));
            setOwned(roleAddrs[RoleType.getID("Storager")], "Storager_OutTime", new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]));

            setManaged(roleAddrs[RoleType.getID("Storager")], "Storager_Price", new Address(dataAddrs[PropertyType.getID("Storager_Price")]));
            setOwned(roleAddrs[RoleType.getID("Storager")], "Storager_Price", new Address(dataAddrs[PropertyType.getID("Storager_Price")]));

            setManaged(roleAddrs[RoleType.getID("Storager")], "Storager_Duration", new Address(dataAddrs[PropertyType.getID("Storager_Duration")]));
            setOwned(roleAddrs[RoleType.getID("Storager")], "Storager_Duration", new Address(dataAddrs[PropertyType.getID("Storager_Duration")]));

            setManaged(roleAddrs[RoleType.getID("Storager")], "Storager_Amount", new Address(dataAddrs[PropertyType.getID("Storager_Amount")]));
            setOwned(roleAddrs[RoleType.getID("Storager")], "Storager_Amount", new Address(dataAddrs[PropertyType.getID("Storager_Amount")]));

            setManaged(roleAddrs[RoleType.getID("Storager")], "Storager_Company", new Address(dataAddrs[PropertyType.getID("Storager_Company")]));
            setOwned(roleAddrs[RoleType.getID("Storager")], "Storager_Company", new Address(dataAddrs[PropertyType.getID("Storager_Company")]));
        }
        //Transporter-Storager
        if (signIn(accounts[3], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Storager_Operator", new Address(dataAddrs[PropertyType.getID("Storager_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Storager_OutTime", new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]));
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Storager_Company", new Address(dataAddrs[PropertyType.getID("Storager_Company")]));
        }
        //Seller-Storager
        if (signIn(accounts[4], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Seller")], "Storager_Operator", new Address(dataAddrs[PropertyType.getID("Storager_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Seller")], "Storager_OutTime", new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]));
            setManaged(roleAddrs[RoleType.getID("Seller")], "Storager_Company", new Address(dataAddrs[PropertyType.getID("Storager_Company")]));
        }
        //Buyer-Storager
        if (signIn(accounts[5], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Storager_Operator", new Address(dataAddrs[PropertyType.getID("Storager_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Storager_Company", new Address(dataAddrs[PropertyType.getID("Storager_Company")]));
        }
        //Government-Storager
        if (signIn(accounts[6], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Government")], "Storager_Operator", new Address(dataAddrs[PropertyType.getID("Storager_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Storager_InTime", new Address(dataAddrs[PropertyType.getID("Storager_InTime")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Storager_OutTime", new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Storager_Price", new Address(dataAddrs[PropertyType.getID("Storager_Price")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Storager_Duration", new Address(dataAddrs[PropertyType.getID("Storager_Duration")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Storager_Amount", new Address(dataAddrs[PropertyType.getID("Storager_Amount")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Storager_Company", new Address(dataAddrs[PropertyType.getID("Storager_Company")]));
        }
        //Producer-Transporter
        if (signIn(accounts[1], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Producer")], "Transporter_Operator", new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Transporter_OutTime", new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Transporter_Price", new Address(dataAddrs[PropertyType.getID("Transporter_Price")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Transporter_Amount", new Address(dataAddrs[PropertyType.getID("Transporter_Amount")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Transporter_Distance", new Address(dataAddrs[PropertyType.getID("Transporter_Distance")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Transporter_Company", new Address(dataAddrs[PropertyType.getID("Transporter_Company")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Transporter_From", new Address(dataAddrs[PropertyType.getID("Transporter_From")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Transporter_To", new Address(dataAddrs[PropertyType.getID("Transporter_To")]));
        }
        //Storager-Transporter
        if (signIn(accounts[2], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Storager")], "Transporter_Operator", new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Storager")], "Transporter_OutTime", new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]));
            setManaged(roleAddrs[RoleType.getID("Storager")], "Transporter_Company", new Address(dataAddrs[PropertyType.getID("Transporter_Company")]));
        }
        //Transporter-Transporter
        if (signIn(accounts[3], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Transporter_Operator", new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]));
            setOwned(roleAddrs[RoleType.getID("Transporter")], "Transporter_Operator", new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]));

            setManaged(roleAddrs[RoleType.getID("Transporter")], "Transporter_OutTime", new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]));
            setOwned(roleAddrs[RoleType.getID("Transporter")], "Transporter_OutTime", new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]));

            setManaged(roleAddrs[RoleType.getID("Transporter")], "Transporter_Price", new Address(dataAddrs[PropertyType.getID("Transporter_Price")]));
            setOwned(roleAddrs[RoleType.getID("Transporter")], "Transporter_Price", new Address(dataAddrs[PropertyType.getID("Transporter_Price")]));

            setManaged(roleAddrs[RoleType.getID("Transporter")], "Transporter_Amount", new Address(dataAddrs[PropertyType.getID("Transporter_Amount")]));
            setOwned(roleAddrs[RoleType.getID("Transporter")], "Transporter_Amount", new Address(dataAddrs[PropertyType.getID("Transporter_Amount")]));

            setManaged(roleAddrs[RoleType.getID("Transporter")], "Transporter_Distance", new Address(dataAddrs[PropertyType.getID("Transporter_Distance")]));
            setOwned(roleAddrs[RoleType.getID("Transporter")], "Transporter_Distance", new Address(dataAddrs[PropertyType.getID("Transporter_Distance")]));

            setManaged(roleAddrs[RoleType.getID("Transporter")], "Transporter_Company", new Address(dataAddrs[PropertyType.getID("Transporter_Company")]));
            setOwned(roleAddrs[RoleType.getID("Transporter")], "Transporter_Company", new Address(dataAddrs[PropertyType.getID("Transporter_Company")]));

            setManaged(roleAddrs[RoleType.getID("Transporter")], "Transporter_From", new Address(dataAddrs[PropertyType.getID("Transporter_From")]));
            setOwned(roleAddrs[RoleType.getID("Transporter")], "Transporter_From", new Address(dataAddrs[PropertyType.getID("Transporter_From")]));

            setManaged(roleAddrs[RoleType.getID("Transporter")], "Transporter_To", new Address(dataAddrs[PropertyType.getID("Transporter_To")]));
            setOwned(roleAddrs[RoleType.getID("Transporter")], "Transporter_To", new Address(dataAddrs[PropertyType.getID("Transporter_To")]));
        }
        //Seller-Transporter
        if (signIn(accounts[4], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Seller")], "Transporter_Operator", new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Seller")], "Transporter_OutTime", new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]));
            setManaged(roleAddrs[RoleType.getID("Seller")], "Transporter_Company", new Address(dataAddrs[PropertyType.getID("Transporter_Company")]));
        }
        //Buyer-Transporter
        if (signIn(accounts[5], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Transporter_Operator", new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Transporter_Company", new Address(dataAddrs[PropertyType.getID("Transporter_Company")]));
        }
        //Government-Transporter
        if (signIn(accounts[6], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Government")], "Transporter_Operator", new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Transporter_OutTime", new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Transporter_Price", new Address(dataAddrs[PropertyType.getID("Transporter_Price")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Transporter_Amount", new Address(dataAddrs[PropertyType.getID("Transporter_Amount")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Transporter_Distance", new Address(dataAddrs[PropertyType.getID("Transporter_Distance")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Transporter_Company", new Address(dataAddrs[PropertyType.getID("Transporter_Company")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Transporter_From", new Address(dataAddrs[PropertyType.getID("Transporter_From")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Transporter_To", new Address(dataAddrs[PropertyType.getID("Transporter_To")]));
        }
        //Producer-Seller
        if (signIn(accounts[1], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Producer")], "Seller_Operator", new Address(dataAddrs[PropertyType.getID("Seller_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Seller_InTime", new Address(dataAddrs[PropertyType.getID("Seller_InTime")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Seller_InPrice", new Address(dataAddrs[PropertyType.getID("Seller_InPrice")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Seller_Amount", new Address(dataAddrs[PropertyType.getID("Seller_Amount")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Seller_Company", new Address(dataAddrs[PropertyType.getID("Seller_Company")]));
            setManaged(roleAddrs[RoleType.getID("Producer")], "Seller_OutPrice", new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]));
        }
        //Storager-Seller
        if (signIn(accounts[2], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Storager")], "Seller_Operator", new Address(dataAddrs[PropertyType.getID("Seller_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Storager")], "Seller_OutPrice", new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]));
        }
        //Transporter-Seller
        if (signIn(accounts[3], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Seller_Operator", new Address(dataAddrs[PropertyType.getID("Seller_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Seller_Company", new Address(dataAddrs[PropertyType.getID("Seller_Company")]));
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Seller_OutPrice", new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]));
        }
        //Seller-Seller
        if (signIn(accounts[4], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Seller")], "Seller_Operator", new Address(dataAddrs[PropertyType.getID("Seller_Operator")]));
            setOwned(roleAddrs[RoleType.getID("Seller")], "Seller_Operator", new Address(dataAddrs[PropertyType.getID("Seller_Operator")]));

            setManaged(roleAddrs[RoleType.getID("Seller")], "Seller_InTime", new Address(dataAddrs[PropertyType.getID("Seller_InTime")]));
            setOwned(roleAddrs[RoleType.getID("Seller")], "Seller_InTime", new Address(dataAddrs[PropertyType.getID("Seller_InTime")]));

            setManaged(roleAddrs[RoleType.getID("Seller")], "Seller_InPrice", new Address(dataAddrs[PropertyType.getID("Seller_InPrice")]));
            setOwned(roleAddrs[RoleType.getID("Seller")], "Seller_InPrice", new Address(dataAddrs[PropertyType.getID("Seller_InPrice")]));

            setManaged(roleAddrs[RoleType.getID("Seller")], "Seller_Amount", new Address(dataAddrs[PropertyType.getID("Seller_Amount")]));
            setOwned(roleAddrs[RoleType.getID("Seller")], "Seller_Amount", new Address(dataAddrs[PropertyType.getID("Seller_Amount")]));

            setManaged(roleAddrs[RoleType.getID("Seller")], "Seller_Company", new Address(dataAddrs[PropertyType.getID("Seller_Company")]));
            setOwned(roleAddrs[RoleType.getID("Seller")], "Seller_Company", new Address(dataAddrs[PropertyType.getID("Seller_Company")]));

            setManaged(roleAddrs[RoleType.getID("Seller")], "Seller_OutPrice", new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]));
            setOwned(roleAddrs[RoleType.getID("Seller")], "Seller_OutPrice", new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]));
        }
        //Buyer-Seller
        if (signIn(accounts[5], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Seller_Operator", new Address(dataAddrs[PropertyType.getID("Seller_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Seller_Company", new Address(dataAddrs[PropertyType.getID("Seller_Company")]));
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Seller_OutPrice", new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]));
        }
        //Government-Seller
        if (signIn(accounts[6], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Government")], "Seller_Operator", new Address(dataAddrs[PropertyType.getID("Seller_Operator")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Seller_InTime", new Address(dataAddrs[PropertyType.getID("Seller_InTime")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Seller_InPrice", new Address(dataAddrs[PropertyType.getID("Seller_InPrice")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Seller_Amount", new Address(dataAddrs[PropertyType.getID("Seller_Amount")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Seller_Company", new Address(dataAddrs[PropertyType.getID("Seller_Company")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Seller_OutPrice", new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]));
        }
        //Producer-Buyer
        if (signIn(accounts[1], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Producer")], "Buyer_Price", new Address(dataAddrs[PropertyType.getID("Buyer_Price")]));
        }
        //Storager-Buyer
        if (signIn(accounts[2], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Storager")], "Buyer_Price", new Address(dataAddrs[PropertyType.getID("Buyer_Price")]));
        }
        //Transporter-Buyer
        if (signIn(accounts[3], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Transporter")], "Buyer_Price", new Address(dataAddrs[PropertyType.getID("Buyer_Price")]));
        }
        //Seller-Buyer
        if (signIn(accounts[4], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Seller")], "Buyer_Time", new Address(dataAddrs[PropertyType.getID("Buyer_Time")]));
            setManaged(roleAddrs[RoleType.getID("Seller")], "Buyer_Amount", new Address(dataAddrs[PropertyType.getID("Buyer_Amount")]));
            setManaged(roleAddrs[RoleType.getID("Seller")], "Buyer_Price", new Address(dataAddrs[PropertyType.getID("Buyer_Price")]));
        }
        //Government-Buyer
        if (signIn(accounts[6], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Government")], "Buyer_Name", new Address(dataAddrs[PropertyType.getID("Buyer_Name")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Buyer_Mobile", new Address(dataAddrs[PropertyType.getID("Buyer_Mobile")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Buyer_Time", new Address(dataAddrs[PropertyType.getID("Buyer_Time")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Buyer_Amount", new Address(dataAddrs[PropertyType.getID("Buyer_Amount")]));
            setManaged(roleAddrs[RoleType.getID("Government")], "Buyer_Price", new Address(dataAddrs[PropertyType.getID("Buyer_Price")]));
        }
        //Buyer-Buyer
        if (signIn(accounts[5], "Innov@teD@ily1")) {
            setManaged(roleAddrs[RoleType.getID("Buyer")], "Buyer_Name", new Address(dataAddrs[PropertyType.getID("Buyer_Name")]));
            setOwned(roleAddrs[RoleType.getID("Buyer")], "Buyer_Name", new Address(dataAddrs[PropertyType.getID("Buyer_Name")]));

            setManaged(roleAddrs[RoleType.getID("Buyer")], "Buyer_Mobile", new Address(dataAddrs[PropertyType.getID("Buyer_Mobile")]));
            setOwned(roleAddrs[RoleType.getID("Buyer")], "Buyer_Mobile", new Address(dataAddrs[PropertyType.getID("Buyer_Mobile")]));

            setManaged(roleAddrs[RoleType.getID("Buyer")], "Buyer_Time", new Address(dataAddrs[PropertyType.getID("Buyer_Time")]));
            setOwned(roleAddrs[RoleType.getID("Buyer")], "Buyer_Time", new Address(dataAddrs[PropertyType.getID("Buyer_Time")]));

            setManaged(roleAddrs[RoleType.getID("Buyer")], "Buyer_Amount", new Address(dataAddrs[PropertyType.getID("Buyer_Amount")]));
            setOwned(roleAddrs[RoleType.getID("Buyer")], "Buyer_Amount", new Address(dataAddrs[PropertyType.getID("Buyer_Amount")]));

            setManaged(roleAddrs[RoleType.getID("Buyer")], "Buyer_Price", new Address(dataAddrs[PropertyType.getID("Buyer_Price")]));
            setOwned(roleAddrs[RoleType.getID("Buyer")], "Buyer_Price", new Address(dataAddrs[PropertyType.getID("Buyer_Price")]));
        }

        return true;
    }

    public boolean signIn(String address, String password) throws IOException, CipherException {
        Resource resource = new ClassPathResource(address);
        File file = resource.getFile();
        current = WalletUtils.loadCredentials(
                password,
                file.getAbsolutePath());
        return true;
    }

    @Deprecated
    public String signUp(String password) throws Exception {
        String fileName = WalletUtils.generateNewWalletFile(password, new ClassPathResource("").getFile());
        Resource resource = new ClassPathResource(fileName);
        current = WalletUtils.loadCredentials(
                password,
                resource.getFile().getAbsolutePath());
        String newFile = resource.getFile().getParent() + "/" + current.getAddress();
        if (!resource.getFile().renameTo(new File(newFile))) {
            LOGGER.error("Fail to rename the wallet file.");
        }

        LOGGER.info("Initial ether sent. Transaction hash: " + initTransfer(current.getAddress(), 100000000));
        return current.getAddress();
    }

    @Deprecated
    private String initTransfer(String toAddr, int value) throws Exception {
        Resource resource = new ClassPathResource("0x6a2fb5e3bf37f0c3d90db4713f7ad4a3b2c24111");
        File file = resource.getFile();
        Credentials minerAccount = WalletUtils.loadCredentials(
                "Innov@teD@ily1",
                file.getAbsolutePath());
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(minerAccount.getAddress(), DefaultBlockParameterName.LATEST).send();
        RawTransaction transaction = RawTransaction.createEtherTransaction(transactionCount.getTransactionCount(), GAS_PRICE, GAS_LIMIT, toAddr, BigInteger.valueOf(value));

        byte[] signedMessage = TransactionEncoder.signMessage(transaction, minerAccount);
        String hexValue = Numeric.toHexString(signedMessage);
        return web3j.ethSendRawTransaction(hexValue).send().getTransactionHash();
    }

    public String addRole() throws Exception {
        Role_sol_Role rc = Role_sol_Role.deploy(web3j, current, GAS_PRICE, GAS_LIMIT).send();
        LOGGER.info("Role Contract deployed: " + rc.getContractAddress());
        return rc.getContractAddress();
    }

    public TransactionReceipt setOwned(String rcAddr, String name, Address scAddr) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = rc.setOwned(new Utf8String(name), scAddr).send();

        LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public RemoteCall<TransactionReceipt> setOwnedAsync(String rcAddr, String name, Address scAddr) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
        return rc.setOwned(new Utf8String(name), scAddr);
    }

    private TransactionReceipt setManaged(String rcAddr, String name, Address scAddr) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = rc.setManaged(new Utf8String(name), scAddr).send();

        LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public RemoteCall<TransactionReceipt> setManagedAsync(String rcAddr, String name, Address scAddr) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
        return rc.setManaged(new Utf8String(name), scAddr);
    }

    public String getOwned(String rcAddr, String name) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
        String scAddr = rc.getOwned(new Utf8String(name)).send().getValue();

        LOGGER.info("Read succeed: " + scAddr);
        return scAddr;
    }

    public Map<String, String> getOwnedAll(String rcAddr) throws Exception {
        Map<String, String> result = new HashMap<>();
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);

        for (String property : PropertyType.Types) {
            try {
                String scAddr = rc.getOwned(new Utf8String(property)).send().getValue();
                if (!scAddr.equals("0x0000000000000000000000000000000000000000")) {
                    result.putIfAbsent(property, scAddr);
                }
            } catch (NullPointerException e) {
                LOGGER.error(e.getMessage());
            }
        }

        return result;
    }

    public String getManaged(String rcAddr, String name) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
        String scAddr = rc.getManaged(new Utf8String(name)).send().getValue();

        LOGGER.info("Read succeed: " + scAddr);
        return scAddr;
    }

    public Map<String, String> getManagedAll(String rcAddr) throws Exception {
        Map<String, String> result = new HashMap<>();
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);

        for (String property : PropertyType.Types) {
            try {
                String scAddr = rc.getManaged(new Utf8String(property)).send().getValue();
                if (!scAddr.equals("0x0000000000000000000000000000000000000000")) {
                    result.putIfAbsent(property, scAddr);
                }
            } catch (NullPointerException e) {
                LOGGER.error(e.getMessage());
            }
        }

        return result;
    }
}
