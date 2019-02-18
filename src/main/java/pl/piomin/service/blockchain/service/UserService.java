package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.contract.Data_sol_Data;
import pl.piomin.service.blockchain.contract.Role_sol_Role;
import pl.piomin.service.blockchain.contract.System_sol_System;
import pl.piomin.service.blockchain.model.User;

import java.io.File;
import java.io.IOException;

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
        String[] roleAddrs = new String[RoleType.Types.values().length];
        String[] accounts = new String[]{"0x6a2fb5e3bf37f0c3d90db4713f7ad4a3b2c24111", "0x40b00de2e7b694b494022eef90e874f5e553f996", "0x49e2170e0b1188f2151ac35287c743ee60ea1f6a",
                "0x135b8fb39d0f06ea1f2466f7e9f39d3136431480", "0x329b81e0a2af215c7e41b32251ae4d6ff1a83e3e", "0x370287edd5a5e7c4b0f5e305b00fe95fc702ce47"};
        for (int i = 0; i < 6; i++) {
            if (signIn(new User(accounts[i], "Innov@teD@ily1"))) {
                Role_sol_Role role = Role_sol_Role.deploy(web3j, current, GAS_PRICE, GAS_LIMIT).send();
                LOGGER.info("Role Contract " + i + " deployed: " + role.getContractAddress() + ". Role Name: " + RoleType.Types.values()[i].name());
                roleAddrs[i] = role.getContractAddress();

                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.register(new Address(roleAddrs[i])).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.getTransactionHash());
            }
        }

        //recreate SCs
        String[] dataAddrs = new String[PropertyType.Types.values().length];
        for (int j = 0; j < PropertyType.Types.values().length; j++) {
            if (j == 0) {
                signIn(new User(accounts[0], "Innov@teD@ily1"));
            }
            if (j == 8) {
                signIn(new User(accounts[1], "Innov@teD@ily1"));
            }
            if (j == 15) {
                signIn(new User(accounts[2], "Innov@teD@ily1"));
            }
            if (j == 23) {
                signIn(new User(accounts[3], "Innov@teD@ily1"));
            }
            if (j == 29) {
                signIn(new User(accounts[4], "Innov@teD@ily1"));
            }

            Data_sol_Data data = Data_sol_Data.deploy(web3j, current, GAS_PRICE, GAS_LIMIT).send();
            dataAddrs[j] = data.getContractAddress();
            LOGGER.info("Data Contract " + j + " deployed: " + data.getContractAddress() + ".Property Name: " + PropertyType.Types.values()[j].name());
        }

        //link RCs and SCs
        //Producer-Producer
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_Operator.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_ProduceDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_ProduceDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_ProduceDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_ProduceDate.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_OutDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_OutDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_OutDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_OutDate.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_GuaranteeDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_GuaranteeDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_GuaranteeDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_GuaranteeDate.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_TestResult.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_TestResult.ordinal()]), true);

        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_BasePrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_BasePrice.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_BasePrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_BasePrice.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_SellPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_SellPrice.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_SellPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_SellPrice.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Producer_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_Amount.ordinal()]), false);

        //Storager-Producer
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Producer_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Producer_ProduceDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_ProduceDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Producer_OutDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_OutDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Producer_GuaranteeDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_GuaranteeDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Producer_TestResult.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_TestResult.ordinal()]), true);

        //Transporter-Producer
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Producer_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Producer_ProduceDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_ProduceDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Producer_GuaranteeDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_GuaranteeDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Producer_TestResult.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_TestResult.ordinal()]), true);

        //Seller-Producer
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Producer_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Producer_ProduceDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_ProduceDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Producer_GuaranteeDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_GuaranteeDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Producer_TestResult.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_TestResult.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Producer_SellPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_SellPrice.ordinal()]), true);

        //Buyer-Producer
        setSC(roleAddrs[RoleType.Types.Buyer.ordinal()], PropertyType.Types.Producer_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Buyer.ordinal()], PropertyType.Types.Producer_ProduceDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_ProduceDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Buyer.ordinal()], PropertyType.Types.Producer_GuaranteeDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_GuaranteeDate.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Buyer.ordinal()], PropertyType.Types.Producer_TestResult.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_TestResult.ordinal()]), true);

        //Government-Producer
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Producer_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_Operator.ordinal()]), true);

        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Producer_ProduceDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_ProduceDate.ordinal()]), true);

        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Producer_OutDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_OutDate.ordinal()]), true);

        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Producer_GuaranteeDate.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_GuaranteeDate.ordinal()]), true);

        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Producer_TestResult.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_TestResult.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Producer_TestResult.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_TestResult.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Producer_BasePrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_BasePrice.ordinal()]), true);

        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Producer_SellPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_SellPrice.ordinal()]), true);

        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Producer_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Producer_Amount.ordinal()]), true);


        //Producer-Storager
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Storager_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Storager_InTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_InTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Storager_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_OutTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Storager_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Price.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Storager_Duration.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Duration.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Storager_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Storager_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Company.ordinal()]), true);

        //Storager-Storager
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Operator.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_InTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_InTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_InTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_InTime.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_OutTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_OutTime.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_OutTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_OutTime.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_Duration.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Duration.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_Duration.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Duration.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Amount.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Company.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Storager_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Company.ordinal()]), false);

        //Transporter-Storager
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Storager_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Storager_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_OutTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Storager_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Company.ordinal()]), true);

        //Seller-Storager
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Storager_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Storager_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_OutTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Storager_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Company.ordinal()]), true);

        //Buyer-Storager
        setSC(roleAddrs[RoleType.Types.Buyer.ordinal()], PropertyType.Types.Storager_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Buyer.ordinal()], PropertyType.Types.Storager_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Company.ordinal()]), true);

        //Government-Storager
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Storager_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Storager_InTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_InTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Storager_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_OutTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Storager_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Price.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Storager_Duration.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Duration.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Storager_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Storager_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Storager_Company.ordinal()]), true);


        //Producer-Transporter
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Transporter_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Transporter_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_OutTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Transporter_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Price.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Transporter_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Transporter_Distance.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Distance.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Transporter_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Company.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Transporter_From.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_From.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Transporter_To.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_To.ordinal()]), true);

        //Storager-Transporter
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Transporter_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Transporter_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_OutTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Transporter_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Company.ordinal()]), true);

        //Producer-Transporter
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Operator.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_OutTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_OutTime.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Price.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Price.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Amount.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_Distance.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Distance.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_Distance.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Distance.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Company.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Company.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_From.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_From.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_From.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_From.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_To.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_To.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Transporter_To.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_To.ordinal()]), false);

        //Seller-Transporter
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Transporter_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Transporter_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_OutTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Transporter_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Company.ordinal()]), true);

        //Buyer-Transporter
        setSC(roleAddrs[RoleType.Types.Buyer.ordinal()], PropertyType.Types.Transporter_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Buyer.ordinal()], PropertyType.Types.Transporter_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Company.ordinal()]), true);

        //Government-Transporter
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Transporter_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Transporter_OutTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_OutTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Transporter_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Price.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Transporter_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Transporter_Distance.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Distance.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Transporter_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_Company.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Transporter_From.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_From.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Transporter_To.ordinal(), new Address(dataAddrs[PropertyType.Types.Transporter_To.ordinal()]), true);


        //Producer-Seller
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Seller_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Seller_InTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_InTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Seller_InPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_InPrice.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Seller_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Seller_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Company.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Seller_OutPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_OutPrice.ordinal()]), true);

        //Storager-Seller
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Seller_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Seller_OutPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_OutPrice.ordinal()]), true);

        //Transporter-Seller
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Seller_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Seller_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Company.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Seller_OutPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_OutPrice.ordinal()]), true);

        //Seller-Seller
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Operator.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_InTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_InTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_InTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_InTime.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_InPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_InPrice.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_InPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_InPrice.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Amount.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Company.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Company.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_OutPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_OutPrice.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Seller_OutPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_OutPrice.ordinal()]), false);

        //Buyer-Seller
        setSC(roleAddrs[RoleType.Types.Buyer.ordinal()], PropertyType.Types.Seller_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Buyer.ordinal()], PropertyType.Types.Seller_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Company.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Buyer.ordinal()], PropertyType.Types.Seller_OutPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_OutPrice.ordinal()]), true);

        //Government-Seller
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Seller_Operator.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Operator.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Seller_InTime.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_InTime.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Seller_InPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_InPrice.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Seller_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Seller_Company.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_Company.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Seller_OutPrice.ordinal(), new Address(dataAddrs[PropertyType.Types.Seller_OutPrice.ordinal()]), true);


        //Producer-Buyer
        setSC(roleAddrs[RoleType.Types.Producer.ordinal()], PropertyType.Types.Buyer_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Price.ordinal()]), true);

        //Storager-Buyer
        setSC(roleAddrs[RoleType.Types.Storager.ordinal()], PropertyType.Types.Buyer_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Price.ordinal()]), true);

        //Transporter-Buyer
        setSC(roleAddrs[RoleType.Types.Transporter.ordinal()], PropertyType.Types.Buyer_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Price.ordinal()]), true);

        //Seller-Buyer
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Buyer_Time.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Time.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Buyer_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Seller.ordinal()], PropertyType.Types.Buyer_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Price.ordinal()]), true);

        //Seller-Buyer
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Name.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Name.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Mobile.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Mobile.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Time.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Time.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Price.ordinal()]), true);

        //Seller-Buyer
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Name.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Name.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Name.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Name.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Mobile.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Mobile.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Mobile.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Mobile.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Time.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Time.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Time.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Time.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Amount.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Amount.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Amount.ordinal()]), false);

        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Price.ordinal()]), true);
        setSC(roleAddrs[RoleType.Types.Government.ordinal()], PropertyType.Types.Buyer_Price.ordinal(), new Address(dataAddrs[PropertyType.Types.Buyer_Price.ordinal()]), false);

        return true;
    }

    public boolean signIn(User user) throws IOException, CipherException {
        Resource resource = new ClassPathResource(user.getAddress());
        File file = resource.getFile();
        current = WalletUtils.loadCredentials(
                user.getPassword(),
                file.getAbsolutePath());
        return true;
    }

    public TransactionReceipt setSC(String rcAddr, int id, Address scAddr, boolean isRead) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt
                = rc.link(new Uint256(id), scAddr, new Bool(isRead)).send();

        LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public Address getSC(String rcAddr, int id, boolean isRead) throws Exception {
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
        String scAddr = rc.getSC(new Uint256(id), new Bool(isRead)).send().getValue();

        LOGGER.info("Read succeed: " + scAddr);
        return new Address(scAddr);
    }
}
