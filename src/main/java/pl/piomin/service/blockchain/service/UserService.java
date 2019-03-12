package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;
import pl.piomin.service.blockchain.PermissionType;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.contract.Data_sol_Data;
import pl.piomin.service.blockchain.contract.Role_sol_Role;
import pl.piomin.service.blockchain.contract.System_sol_System;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

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

    private final Web3j web3j;
    private Credentials current = null;

    public UserService(Web3j web3j) {
        this.web3j = web3j;
    }

    public Credentials getCurrent() {
        return current;
    }

    public boolean reset(String sysAddr) throws Exception {

        //recreate RCs
        String[] roleAddrs = new String[RoleType.Types.size()];
        String[] accounts = new String[]{"0x6a2fb5e3bf37f0c3d90db4713f7ad4a3b2c24111", "0x40b00de2e7b694b494022eef90e874f5e553f996", "0x49e2170e0b1188f2151ac35287c743ee60ea1f6a",
                "0x135b8fb39d0f06ea1f2466f7e9f39d3136431480", "0x329b81e0a2af215c7e41b32251ae4d6ff1a83e3e", "0x370287edd5a5e7c4b0f5e305b00fe95fc702ce47"};
        for (int i = 0; i < 6; i++) {
            if (signIn(accounts[i], "Innov@teD@ily1")) {
                Role_sol_Role role = Role_sol_Role.deploy(web3j, current, GAS_PRICE, GAS_LIMIT).send();
                LOGGER.info("Role Contract " + i + " deployed: " + role.getContractAddress() + ". Role Name: " + RoleType.Types.get(i));
                roleAddrs[i] = role.getContractAddress();

                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.register(new Address(roleAddrs[i])).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.getTransactionHash());
            }
        }

        //recreate SCs
        String[] dataAddrs = new String[PropertyType.Types.size()];
        for (int j = 0; j < PropertyType.Types.size(); j++) {
            if (j == 0) {
                signIn(accounts[0], "Innov@teD@ily1");
            }
            if (j == 8) {
                signIn(accounts[1], "Innov@teD@ily1");
            }
            if (j == 15) {
                signIn(accounts[2], "Innov@teD@ily1");
            }
            if (j == 23) {
                signIn(accounts[3], "Innov@teD@ily1");
            }
            if (j == 29) {
                signIn(accounts[4], "Innov@teD@ily1");
            }

            Data_sol_Data data = Data_sol_Data.deploy(web3j, current, GAS_PRICE, GAS_LIMIT).send();
            dataAddrs[j] = data.getContractAddress();
            LOGGER.info("Data Contract " + j + " deployed: " + data.getContractAddress() + ".Property Name: " + PropertyType.Types.get(j));
        }

        //link RCs and SCs
        //Producer-Producer
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_Operator"), new Address(dataAddrs[PropertyType.getID("Producer_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_Operator"), new Address(dataAddrs[PropertyType.getID("Producer_Operator")]), false);

        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_ProduceDate"), new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_ProduceDate"), new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]), false);

        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_OutDate"), new Address(dataAddrs[PropertyType.getID("Producer_OutDate")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_OutDate"), new Address(dataAddrs[PropertyType.getID("Producer_OutDate")]), false);

        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_GuaranteeDate"), new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_GuaranteeDate"), new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]), false);

        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_TestResult"), new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]), true);

        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_BasePrice"), new Address(dataAddrs[PropertyType.getID("Producer_BasePrice")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_BasePrice"), new Address(dataAddrs[PropertyType.getID("Producer_BasePrice")]), false);

        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_SellPrice"), new Address(dataAddrs[PropertyType.getID("Producer_SellPrice")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_SellPrice"), new Address(dataAddrs[PropertyType.getID("Producer_SellPrice")]), false);

        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_Amount"), new Address(dataAddrs[PropertyType.getID("Producer_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Producer_Amount"), new Address(dataAddrs[PropertyType.getID("Producer_Amount")]), false);

        //Storager-Producer
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Producer_Operator"), new Address(dataAddrs[PropertyType.getID("Producer_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Producer_ProduceDate"), new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Producer_OutDate"), new Address(dataAddrs[PropertyType.getID("Producer_OutDate")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Producer_GuaranteeDate"), new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Producer_TestResult"), new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]), true);

        //Transporter-Producer
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Producer_Operator"), new Address(dataAddrs[PropertyType.getID("Producer_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Producer_ProduceDate"), new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Producer_GuaranteeDate"), new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Producer_TestResult"), new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]), true);

        //Seller-Producer
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Producer_Operator"), new Address(dataAddrs[PropertyType.getID("Producer_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Producer_ProduceDate"), new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Producer_GuaranteeDate"), new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Producer_TestResult"), new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Producer_SellPrice"), new Address(dataAddrs[PropertyType.getID("Producer_SellPrice")]), true);

        //Buyer-Producer
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Producer_Operator"), new Address(dataAddrs[PropertyType.getID("Producer_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Producer_ProduceDate"), new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Producer_GuaranteeDate"), new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Producer_TestResult"), new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]), true);

        //Government-Producer
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Producer_Operator"), new Address(dataAddrs[PropertyType.getID("Producer_Operator")]), true);

        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Producer_ProduceDate"), new Address(dataAddrs[PropertyType.getID("Producer_ProduceDate")]), true);

        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Producer_OutDate"), new Address(dataAddrs[PropertyType.getID("Producer_OutDate")]), true);

        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Producer_GuaranteeDate"), new Address(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")]), true);

        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Producer_TestResult"), new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Producer_TestResult"), new Address(dataAddrs[PropertyType.getID("Producer_TestResult")]), false);

        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Producer_BasePrice"), new Address(dataAddrs[PropertyType.getID("Producer_BasePrice")]), true);

        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Producer_SellPrice"), new Address(dataAddrs[PropertyType.getID("Producer_SellPrice")]), true);

        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Producer_Amount"), new Address(dataAddrs[PropertyType.getID("Producer_Amount")]), true);


        //Producer-Storager
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Storager_Operator"), new Address(dataAddrs[PropertyType.getID("Storager_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Storager_InTime"), new Address(dataAddrs[PropertyType.getID("Storager_InTime")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Storager_OutTime"), new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Storager_Price"), new Address(dataAddrs[PropertyType.getID("Storager_Price")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Storager_Duration"), new Address(dataAddrs[PropertyType.getID("Storager_Duration")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Storager_Amount"), new Address(dataAddrs[PropertyType.getID("Storager_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Storager_Company"), new Address(dataAddrs[PropertyType.getID("Storager_Company")]), true);

        //Storager-Storager
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_Operator"), new Address(dataAddrs[PropertyType.getID("Storager_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_Operator"), new Address(dataAddrs[PropertyType.getID("Storager_Operator")]), false);

        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_InTime"), new Address(dataAddrs[PropertyType.getID("Storager_InTime")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_InTime"), new Address(dataAddrs[PropertyType.getID("Storager_InTime")]), false);

        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_OutTime"), new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_OutTime"), new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]), false);

        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_OutTime"), new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_OutTime"), new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]), false);

        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_Duration"), new Address(dataAddrs[PropertyType.getID("Storager_Duration")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_Duration"), new Address(dataAddrs[PropertyType.getID("Storager_Duration")]), false);

        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_Amount"), new Address(dataAddrs[PropertyType.getID("Storager_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_Amount"), new Address(dataAddrs[PropertyType.getID("Storager_Amount")]), false);

        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_Company"), new Address(dataAddrs[PropertyType.getID("Storager_Company")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Storager_Company"), new Address(dataAddrs[PropertyType.getID("Storager_Company")]), false);

        //Transporter-Storager
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Storager_Operator"), new Address(dataAddrs[PropertyType.getID("Storager_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Storager_OutTime"), new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Storager_Company"), new Address(dataAddrs[PropertyType.getID("Storager_Company")]), true);

        //Seller-Storager
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Storager_Operator"), new Address(dataAddrs[PropertyType.getID("Storager_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Storager_OutTime"), new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Storager_Company"), new Address(dataAddrs[PropertyType.getID("Storager_Company")]), true);

        //Buyer-Storager
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Storager_Operator"), new Address(dataAddrs[PropertyType.getID("Storager_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Storager_Company"), new Address(dataAddrs[PropertyType.getID("Storager_Company")]), true);

        //Government-Storager
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Storager_Operator"), new Address(dataAddrs[PropertyType.getID("Storager_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Storager_InTime"), new Address(dataAddrs[PropertyType.getID("Storager_InTime")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Storager_OutTime"), new Address(dataAddrs[PropertyType.getID("Storager_OutTime")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Storager_Price"), new Address(dataAddrs[PropertyType.getID("Storager_Price")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Storager_Duration"), new Address(dataAddrs[PropertyType.getID("Storager_Duration")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Storager_Amount"), new Address(dataAddrs[PropertyType.getID("Storager_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Storager_Company"), new Address(dataAddrs[PropertyType.getID("Storager_Company")]), true);


        //Producer-Transporter
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Transporter_Operator"), new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Transporter_OutTime"), new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Transporter_Price"), new Address(dataAddrs[PropertyType.getID("Transporter_Price")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Transporter_Amount"), new Address(dataAddrs[PropertyType.getID("Transporter_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Transporter_Distance"), new Address(dataAddrs[PropertyType.getID("Transporter_Distance")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Transporter_Company"), new Address(dataAddrs[PropertyType.getID("Transporter_Company")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Transporter_From"), new Address(dataAddrs[PropertyType.getID("Transporter_From")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Transporter_To"), new Address(dataAddrs[PropertyType.getID("Transporter_To")]), true);

        //Storager-Transporter
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Transporter_Operator"), new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Transporter_OutTime"), new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Transporter_Company"), new Address(dataAddrs[PropertyType.getID("Transporter_Company")]), true);

        //Transporter-Transporter
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_Operator"), new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_Operator"), new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]), false);

        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_OutTime"), new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_OutTime"), new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]), false);

        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_Price"), new Address(dataAddrs[PropertyType.getID("Transporter_Price")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_Price"), new Address(dataAddrs[PropertyType.getID("Transporter_Price")]), false);

        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_Amount"), new Address(dataAddrs[PropertyType.getID("Transporter_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_Amount"), new Address(dataAddrs[PropertyType.getID("Transporter_Amount")]), false);

        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_Distance"), new Address(dataAddrs[PropertyType.getID("Transporter_Distance")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_Distance"), new Address(dataAddrs[PropertyType.getID("Transporter_Distance")]), false);

        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_Company"), new Address(dataAddrs[PropertyType.getID("Transporter_Company")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_Company"), new Address(dataAddrs[PropertyType.getID("Transporter_Company")]), false);

        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_From"), new Address(dataAddrs[PropertyType.getID("Transporter_From")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_From"), new Address(dataAddrs[PropertyType.getID("Transporter_From")]), false);

        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_To"), new Address(dataAddrs[PropertyType.getID("Transporter_To")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Transporter_To"), new Address(dataAddrs[PropertyType.getID("Transporter_To")]), false);

        //Seller-Transporter
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Transporter_Operator"), new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Transporter_OutTime"), new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Transporter_Company"), new Address(dataAddrs[PropertyType.getID("Transporter_Company")]), true);

        //Buyer-Transporter
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Transporter_Operator"), new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Transporter_Company"), new Address(dataAddrs[PropertyType.getID("Transporter_Company")]), true);

        //Government-Transporter
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Transporter_Operator"), new Address(dataAddrs[PropertyType.getID("Transporter_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Transporter_OutTime"), new Address(dataAddrs[PropertyType.getID("Transporter_OutTime")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Transporter_Price"), new Address(dataAddrs[PropertyType.getID("Transporter_Price")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Transporter_Amount"), new Address(dataAddrs[PropertyType.getID("Transporter_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Transporter_Distance"), new Address(dataAddrs[PropertyType.getID("Transporter_Distance")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Transporter_Company"), new Address(dataAddrs[PropertyType.getID("Transporter_Company")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Transporter_From"), new Address(dataAddrs[PropertyType.getID("Transporter_From")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Transporter_To"), new Address(dataAddrs[PropertyType.getID("Transporter_To")]), true);


        //Producer-Seller
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Seller_Operator"), new Address(dataAddrs[PropertyType.getID("Seller_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Seller_InTime"), new Address(dataAddrs[PropertyType.getID("Seller_InTime")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Seller_InPrice"), new Address(dataAddrs[PropertyType.getID("Seller_InPrice")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Seller_Amount"), new Address(dataAddrs[PropertyType.getID("Seller_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Seller_Company"), new Address(dataAddrs[PropertyType.getID("Seller_Company")]), true);
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Seller_OutPrice"), new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]), true);

        //Storager-Seller
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Seller_Operator"), new Address(dataAddrs[PropertyType.getID("Seller_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Seller_OutPrice"), new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]), true);

        //Transporter-Seller
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Seller_Operator"), new Address(dataAddrs[PropertyType.getID("Seller_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Seller_Company"), new Address(dataAddrs[PropertyType.getID("Seller_Company")]), true);
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Seller_OutPrice"), new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]), true);

        //Seller-Seller
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_Operator"), new Address(dataAddrs[PropertyType.getID("Seller_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_Operator"), new Address(dataAddrs[PropertyType.getID("Seller_Operator")]), false);

        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_InTime"), new Address(dataAddrs[PropertyType.getID("Seller_InTime")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_InTime"), new Address(dataAddrs[PropertyType.getID("Seller_InTime")]), false);

        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_InPrice"), new Address(dataAddrs[PropertyType.getID("Seller_InPrice")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_InPrice"), new Address(dataAddrs[PropertyType.getID("Seller_InPrice")]), false);

        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_Amount"), new Address(dataAddrs[PropertyType.getID("Seller_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_Amount"), new Address(dataAddrs[PropertyType.getID("Seller_Amount")]), false);

        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_Company"), new Address(dataAddrs[PropertyType.getID("Seller_Company")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_Company"), new Address(dataAddrs[PropertyType.getID("Seller_Company")]), false);

        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_OutPrice"), new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Seller_OutPrice"), new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]), false);

        //Buyer-Seller
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Seller_Operator"), new Address(dataAddrs[PropertyType.getID("Seller_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Seller_Company"), new Address(dataAddrs[PropertyType.getID("Seller_Company")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Seller_OutPrice"), new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]), true);

        //Government-Seller
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Seller_Operator"), new Address(dataAddrs[PropertyType.getID("Seller_Operator")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Seller_InTime"), new Address(dataAddrs[PropertyType.getID("Seller_InTime")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Seller_InPrice"), new Address(dataAddrs[PropertyType.getID("Seller_InPrice")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Seller_Amount"), new Address(dataAddrs[PropertyType.getID("Seller_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Seller_Company"), new Address(dataAddrs[PropertyType.getID("Seller_Company")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Seller_OutPrice"), new Address(dataAddrs[PropertyType.getID("Seller_OutPrice")]), true);


        //Producer-Buyer
        setSC(roleAddrs[RoleType.getID("Producer")], PropertyType.getID("Buyer_Price"), new Address(dataAddrs[PropertyType.getID("Buyer_Price")]), true);

        //Storager-Buyer
        setSC(roleAddrs[RoleType.getID("Storager")], PropertyType.getID("Buyer_Price"), new Address(dataAddrs[PropertyType.getID("Buyer_Price")]), true);

        //Transporter-Buyer
        setSC(roleAddrs[RoleType.getID("Transporter")], PropertyType.getID("Buyer_Price"), new Address(dataAddrs[PropertyType.getID("Buyer_Price")]), true);

        //Seller-Buyer
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Buyer_Time"), new Address(dataAddrs[PropertyType.getID("Buyer_Time")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Buyer_Amount"), new Address(dataAddrs[PropertyType.getID("Buyer_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Seller")], PropertyType.getID("Buyer_Price"), new Address(dataAddrs[PropertyType.getID("Buyer_Price")]), true);

        //Government-Buyer
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Buyer_Name"), new Address(dataAddrs[PropertyType.getID("Buyer_Name")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Buyer_Mobile"), new Address(dataAddrs[PropertyType.getID("Buyer_Mobile")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Buyer_Time"), new Address(dataAddrs[PropertyType.getID("Buyer_Time")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Buyer_Amount"), new Address(dataAddrs[PropertyType.getID("Buyer_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Government")], PropertyType.getID("Buyer_Price"), new Address(dataAddrs[PropertyType.getID("Buyer_Price")]), true);

        //Buyer-Buyer
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Buyer_Name"), new Address(dataAddrs[PropertyType.getID("Buyer_Name")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Buyer_Name"), new Address(dataAddrs[PropertyType.getID("Buyer_Name")]), false);

        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Buyer_Mobile"), new Address(dataAddrs[PropertyType.getID("Buyer_Mobile")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Buyer_Mobile"), new Address(dataAddrs[PropertyType.getID("Buyer_Mobile")]), false);

        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Buyer_Time"), new Address(dataAddrs[PropertyType.getID("Buyer_Time")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Buyer_Time"), new Address(dataAddrs[PropertyType.getID("Buyer_Time")]), false);

        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Buyer_Amount"), new Address(dataAddrs[PropertyType.getID("Buyer_Amount")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Buyer_Amount"), new Address(dataAddrs[PropertyType.getID("Buyer_Amount")]), false);

        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Buyer_Price"), new Address(dataAddrs[PropertyType.getID("Buyer_Price")]), true);
        setSC(roleAddrs[RoleType.getID("Buyer")], PropertyType.getID("Buyer_Price"), new Address(dataAddrs[PropertyType.getID("Buyer_Price")]), false);

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
            LOGGER.error("Fail to rename the wallet file");
        }

        LOGGER.info("Initial ether sent. Transaction hash: " + initTransfer(current.getAddress(), 100000000));
        return current.getAddress();
    }

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

    public TransactionReceipt setSC(String rcAddr, int id, Address scAddr, boolean isRead) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt
                = rc.link(new Uint256(id), scAddr, new Uint256(isRead ? PermissionType.Types.Read.ordinal() : PermissionType.Types.Write.ordinal())).send();

        LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public Address getSC(String rcAddr, int id, boolean isRead) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
        String scAddr = rc.getSC(new Uint256(id), new Uint256(isRead ? PermissionType.Types.Read.ordinal() : PermissionType.Types.Write.ordinal())).send().getValue();

        LOGGER.info("Read succeed: " + scAddr);
        return new Address(scAddr);
    }
}
