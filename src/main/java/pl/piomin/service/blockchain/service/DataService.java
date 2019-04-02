package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.contract.Data_sol_Data;
import pl.piomin.service.blockchain.contract.System_sol_System;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
@Service
public class DataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);

    private final Web3j web3j;

    public DataService(Web3j web3j) {
        this.web3j = web3j;
    }

    public String[] resetContract(String[] roleAddrs, String sysAddr) throws Exception {
        //recreate SCs
        Credentials current = null;
        String[] accounts = UserService.accounts;

        String[] dataAddrs = new String[PropertyType.Types.size()];
        for (int j = 0; j < PropertyType.Types.size(); j++) {
            if (j == 0) {
                current = signIn(accounts[1], "Innov@teD@ily1");
            }
            if (j == 7) {
                current = signIn(accounts[2], "Innov@teD@ily1");
            }
            if (j == 14) {
                current = signIn(accounts[3], "Innov@teD@ily1");
            }
            if (j == 22) {
                current = signIn(accounts[4], "Innov@teD@ily1");
            }
            if (j == 28) {
                current = signIn(accounts[5], "Innov@teD@ily1");
            }
            if (j == 33) {
                current = signIn(accounts[6], "Innov@teD@ily1");
            }

            Data_sol_Data data = Data_sol_Data.deploy(web3j, current, GAS_PRICE, GAS_LIMIT).send();
            dataAddrs[j] = data.getContractAddress();
            LOGGER.info("Data Contract " + j + " deployed: " + data.getContractAddress() + ". Property Name: " + PropertyType.Types.get(j) + ". Owner: " + current.getAddress());
        }

        current = signIn(accounts[0],"Innov@teD@ily1");
        for (int i = 0; i < PropertyType.Types.size(); i++) {
            System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
            TransactionReceipt transactionReceipt = system.setScIndex(new Utf8String(PropertyType.Types.get(i)), new Address(dataAddrs[i])).send();
            LOGGER.info("Transaction succeed: " + transactionReceipt.getTransactionHash());
        }
        return dataAddrs;
    }

    public boolean resetPermission(String[] roleAddrs, String[] dataAddrs) throws Exception {
        String[] accounts = UserService.accounts;
        Credentials current = signIn(accounts[1], "Innov@teD@ily1");
        //Producer-Producer
        addReader(dataAddrs[PropertyType.getID("Producer_Operator")], current, roleAddrs[RoleType.getID("Producer")]);
        setWriter(dataAddrs[PropertyType.getID("Producer_Operator")], current, roleAddrs[RoleType.getID("Producer")]);

        addReader(dataAddrs[PropertyType.getID("Producer_ProduceDate")], current, roleAddrs[RoleType.getID("Producer")]);
        setWriter(dataAddrs[PropertyType.getID("Producer_ProduceDate")], current, roleAddrs[RoleType.getID("Producer")]);

        addReader(dataAddrs[PropertyType.getID("Producer_OutDate")], current, roleAddrs[RoleType.getID("Producer")]);
        setWriter(dataAddrs[PropertyType.getID("Producer_OutDate")], current, roleAddrs[RoleType.getID("Producer")]);

        addReader(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")], current, roleAddrs[RoleType.getID("Producer")]);
        setWriter(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")], current, roleAddrs[RoleType.getID("Producer")]);

        addReader(dataAddrs[PropertyType.getID("Producer_BasePrice")], current, roleAddrs[RoleType.getID("Producer")]);
        setWriter(dataAddrs[PropertyType.getID("Producer_BasePrice")], current, roleAddrs[RoleType.getID("Producer")]);

        addReader(dataAddrs[PropertyType.getID("Producer_SellPrice")], current, roleAddrs[RoleType.getID("Producer")]);
        setWriter(dataAddrs[PropertyType.getID("Producer_SellPrice")], current, roleAddrs[RoleType.getID("Producer")]);

        addReader(dataAddrs[PropertyType.getID("Producer_Amount")], current, roleAddrs[RoleType.getID("Producer")]);
        setWriter(dataAddrs[PropertyType.getID("Producer_Amount")], current, roleAddrs[RoleType.getID("Producer")]);

        //Storager-Producer
        addReader(dataAddrs[PropertyType.getID("Producer_Operator")], current, roleAddrs[RoleType.getID("Storager")]);
        addReader(dataAddrs[PropertyType.getID("Producer_ProduceDate")], current, roleAddrs[RoleType.getID("Storager")]);
        addReader(dataAddrs[PropertyType.getID("Producer_OutDate")], current, roleAddrs[RoleType.getID("Storager")]);
        addReader(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")], current, roleAddrs[RoleType.getID("Storager")]);

        //Transporter-Producer
        addReader(dataAddrs[PropertyType.getID("Producer_Operator")], current, roleAddrs[RoleType.getID("Transporter")]);
        addReader(dataAddrs[PropertyType.getID("Producer_ProduceDate")], current, roleAddrs[RoleType.getID("Transporter")]);
        addReader(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")], current, roleAddrs[RoleType.getID("Transporter")]);

        //Seller-Producer
        addReader(dataAddrs[PropertyType.getID("Producer_Operator")], current, roleAddrs[RoleType.getID("Seller")]);
        addReader(dataAddrs[PropertyType.getID("Producer_ProduceDate")], current, roleAddrs[RoleType.getID("Seller")]);
        addReader(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")], current, roleAddrs[RoleType.getID("Seller")]);
        addReader(dataAddrs[PropertyType.getID("Producer_SellPrice")], current, roleAddrs[RoleType.getID("Seller")]);

        //Buyer-Producer
        addReader(dataAddrs[PropertyType.getID("Producer_Operator")], current, roleAddrs[RoleType.getID("Buyer")]);
        addReader(dataAddrs[PropertyType.getID("Producer_ProduceDate")], current, roleAddrs[RoleType.getID("Buyer")]);
        addReader(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")], current, roleAddrs[RoleType.getID("Buyer")]);

        //Government-Producer
        addReader(dataAddrs[PropertyType.getID("Producer_Operator")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Producer_ProduceDate")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Producer_OutDate")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Producer_GuaranteeDate")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Producer_BasePrice")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Producer_SellPrice")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Producer_Amount")], current, roleAddrs[RoleType.getID("Government")]);

        //Producer-Storager
        current = signIn(accounts[2], "Innov@teD@ily1");
        addReader(dataAddrs[PropertyType.getID("Storager_Operator")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Storager_InTime")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Storager_OutTime")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Storager_Price")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Storager_Duration")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Storager_Amount")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Storager_Company")], current, roleAddrs[RoleType.getID("Producer")]);

        //Storager-Storager
        addReader(dataAddrs[PropertyType.getID("Storager_Operator")], current, roleAddrs[RoleType.getID("Storager")]);
        setWriter(dataAddrs[PropertyType.getID("Storager_Operator")], current, roleAddrs[RoleType.getID("Storager")]);

        addReader(dataAddrs[PropertyType.getID("Storager_InTime")], current, roleAddrs[RoleType.getID("Storager")]);
        setWriter(dataAddrs[PropertyType.getID("Storager_InTime")], current, roleAddrs[RoleType.getID("Storager")]);

        addReader(dataAddrs[PropertyType.getID("Storager_OutTime")], current, roleAddrs[RoleType.getID("Storager")]);
        setWriter(dataAddrs[PropertyType.getID("Storager_OutTime")], current, roleAddrs[RoleType.getID("Storager")]);

        addReader(dataAddrs[PropertyType.getID("Storager_Price")], current, roleAddrs[RoleType.getID("Storager")]);
        setWriter(dataAddrs[PropertyType.getID("Storager_Price")], current, roleAddrs[RoleType.getID("Storager")]);

        addReader(dataAddrs[PropertyType.getID("Storager_Duration")], current, roleAddrs[RoleType.getID("Storager")]);
        setWriter(dataAddrs[PropertyType.getID("Storager_Duration")], current, roleAddrs[RoleType.getID("Storager")]);

        addReader(dataAddrs[PropertyType.getID("Storager_Amount")], current, roleAddrs[RoleType.getID("Storager")]);
        setWriter(dataAddrs[PropertyType.getID("Storager_Amount")], current, roleAddrs[RoleType.getID("Storager")]);

        addReader(dataAddrs[PropertyType.getID("Storager_Company")], current, roleAddrs[RoleType.getID("Storager")]);
        setWriter(dataAddrs[PropertyType.getID("Storager_Company")], current, roleAddrs[RoleType.getID("Storager")]);

        //Transporter-Storager
        addReader(dataAddrs[PropertyType.getID("Storager_Operator")], current, roleAddrs[RoleType.getID("Transporter")]);
        addReader(dataAddrs[PropertyType.getID("Storager_OutTime")], current, roleAddrs[RoleType.getID("Transporter")]);
        addReader(dataAddrs[PropertyType.getID("Storager_Company")], current, roleAddrs[RoleType.getID("Transporter")]);

        //Seller-Storager
        addReader(dataAddrs[PropertyType.getID("Storager_Operator")], current, roleAddrs[RoleType.getID("Seller")]);
        addReader(dataAddrs[PropertyType.getID("Storager_OutTime")], current, roleAddrs[RoleType.getID("Seller")]);
        addReader(dataAddrs[PropertyType.getID("Storager_Company")], current, roleAddrs[RoleType.getID("Seller")]);

        //Buyer-Storager
        addReader(dataAddrs[PropertyType.getID("Storager_Operator")], current, roleAddrs[RoleType.getID("Buyer")]);
        addReader(dataAddrs[PropertyType.getID("Storager_Company")], current, roleAddrs[RoleType.getID("Buyer")]);

        //Government-Storager
        addReader(dataAddrs[PropertyType.getID("Storager_Operator")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Storager_InTime")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Storager_OutTime")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Storager_Price")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Storager_Duration")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Storager_Amount")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Storager_Company")], current, roleAddrs[RoleType.getID("Government")]);

        //Producer-Transporter
        current = signIn(accounts[3], "Innov@teD@ily1");
        addReader(dataAddrs[PropertyType.getID("Transporter_Operator")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_OutTime")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_Price")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_Amount")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_Distance")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_Company")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_From")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_To")], current, roleAddrs[RoleType.getID("Producer")]);

        //Storager-Transporter
        addReader(dataAddrs[PropertyType.getID("Transporter_Operator")], current, roleAddrs[RoleType.getID("Storager")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_OutTime")], current, roleAddrs[RoleType.getID("Storager")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_Company")], current, roleAddrs[RoleType.getID("Storager")]);

        //Transporter-Transporter
        addReader(dataAddrs[PropertyType.getID("Transporter_Operator")], current, roleAddrs[RoleType.getID("Transporter")]);
        setWriter(dataAddrs[PropertyType.getID("Transporter_Operator")], current, roleAddrs[RoleType.getID("Transporter")]);

        addReader(dataAddrs[PropertyType.getID("Transporter_OutTime")], current, roleAddrs[RoleType.getID("Transporter")]);
        setWriter(dataAddrs[PropertyType.getID("Transporter_OutTime")], current, roleAddrs[RoleType.getID("Transporter")]);

        addReader(dataAddrs[PropertyType.getID("Transporter_Price")], current, roleAddrs[RoleType.getID("Transporter")]);
        setWriter(dataAddrs[PropertyType.getID("Transporter_Price")], current, roleAddrs[RoleType.getID("Transporter")]);

        addReader(dataAddrs[PropertyType.getID("Transporter_Amount")], current, roleAddrs[RoleType.getID("Transporter")]);
        setWriter(dataAddrs[PropertyType.getID("Transporter_Amount")], current, roleAddrs[RoleType.getID("Transporter")]);

        addReader(dataAddrs[PropertyType.getID("Transporter_Distance")], current, roleAddrs[RoleType.getID("Transporter")]);
        setWriter(dataAddrs[PropertyType.getID("Transporter_Distance")], current, roleAddrs[RoleType.getID("Transporter")]);

        addReader(dataAddrs[PropertyType.getID("Transporter_Company")], current, roleAddrs[RoleType.getID("Transporter")]);
        setWriter(dataAddrs[PropertyType.getID("Transporter_Company")], current, roleAddrs[RoleType.getID("Transporter")]);

        addReader(dataAddrs[PropertyType.getID("Transporter_From")], current, roleAddrs[RoleType.getID("Transporter")]);
        setWriter(dataAddrs[PropertyType.getID("Transporter_From")], current, roleAddrs[RoleType.getID("Transporter")]);

        addReader(dataAddrs[PropertyType.getID("Transporter_To")], current, roleAddrs[RoleType.getID("Transporter")]);
        setWriter(dataAddrs[PropertyType.getID("Transporter_To")], current, roleAddrs[RoleType.getID("Transporter")]);

        //Seller-Transporter
        addReader(dataAddrs[PropertyType.getID("Transporter_Operator")], current, roleAddrs[RoleType.getID("Seller")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_OutTime")], current, roleAddrs[RoleType.getID("Seller")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_Company")], current, roleAddrs[RoleType.getID("Seller")]);

        //Buyer-Transporter
        addReader(dataAddrs[PropertyType.getID("Transporter_Operator")], current, roleAddrs[RoleType.getID("Buyer")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_Company")], current, roleAddrs[RoleType.getID("Buyer")]);

        //Government-Transporter
        addReader(dataAddrs[PropertyType.getID("Transporter_Operator")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_OutTime")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_Price")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_Amount")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_Distance")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_Company")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_From")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Transporter_To")], current, roleAddrs[RoleType.getID("Government")]);

        //Producer-Seller
        current = signIn(accounts[4], "Innov@teD@ily1");
        addReader(dataAddrs[PropertyType.getID("Seller_Operator")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Seller_InTime")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Seller_InPrice")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Seller_Amount")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Seller_Company")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Seller_OutPrice")], current, roleAddrs[RoleType.getID("Producer")]);

        //Storager-Seller
        addReader(dataAddrs[PropertyType.getID("Seller_Operator")], current, roleAddrs[RoleType.getID("Storager")]);
        addReader(dataAddrs[PropertyType.getID("Seller_OutPrice")], current, roleAddrs[RoleType.getID("Storager")]);

        //Transporter-Seller
        addReader(dataAddrs[PropertyType.getID("Seller_Operator")], current, roleAddrs[RoleType.getID("Transporter")]);
        addReader(dataAddrs[PropertyType.getID("Seller_Company")], current, roleAddrs[RoleType.getID("Transporter")]);
        addReader(dataAddrs[PropertyType.getID("Seller_OutPrice")], current, roleAddrs[RoleType.getID("Transporter")]);

        //Seller-Seller
        addReader(dataAddrs[PropertyType.getID("Seller_Operator")], current, roleAddrs[RoleType.getID("Seller")]);
        setWriter(dataAddrs[PropertyType.getID("Seller_Operator")], current, roleAddrs[RoleType.getID("Seller")]);

        addReader(dataAddrs[PropertyType.getID("Seller_InTime")], current, roleAddrs[RoleType.getID("Seller")]);
        setWriter(dataAddrs[PropertyType.getID("Seller_InTime")], current, roleAddrs[RoleType.getID("Seller")]);

        addReader(dataAddrs[PropertyType.getID("Seller_InPrice")], current, roleAddrs[RoleType.getID("Seller")]);
        setWriter(dataAddrs[PropertyType.getID("Seller_InPrice")], current, roleAddrs[RoleType.getID("Seller")]);

        addReader(dataAddrs[PropertyType.getID("Seller_Amount")], current, roleAddrs[RoleType.getID("Seller")]);
        setWriter(dataAddrs[PropertyType.getID("Seller_Amount")], current, roleAddrs[RoleType.getID("Seller")]);

        addReader(dataAddrs[PropertyType.getID("Seller_Company")], current, roleAddrs[RoleType.getID("Seller")]);
        setWriter(dataAddrs[PropertyType.getID("Seller_Company")], current, roleAddrs[RoleType.getID("Seller")]);

        addReader(dataAddrs[PropertyType.getID("Seller_OutPrice")], current, roleAddrs[RoleType.getID("Seller")]);
        setWriter(dataAddrs[PropertyType.getID("Seller_OutPrice")], current, roleAddrs[RoleType.getID("Seller")]);

        //Buyer-Seller
        addReader(dataAddrs[PropertyType.getID("Seller_Operator")], current, roleAddrs[RoleType.getID("Buyer")]);
        addReader(dataAddrs[PropertyType.getID("Seller_Company")], current, roleAddrs[RoleType.getID("Buyer")]);
        addReader(dataAddrs[PropertyType.getID("Seller_OutPrice")], current, roleAddrs[RoleType.getID("Buyer")]);

        //Government-Seller
        addReader(dataAddrs[PropertyType.getID("Seller_Operator")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Seller_InTime")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Seller_InPrice")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Seller_Amount")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Seller_Company")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Seller_OutPrice")], current, roleAddrs[RoleType.getID("Government")]);

        //Producer-Buyer
        current = signIn(accounts[5], "Innov@teD@ily1");
        addReader(dataAddrs[PropertyType.getID("Buyer_Price")], current, roleAddrs[RoleType.getID("Producer")]);

        //Storager-Buyer
        addReader(dataAddrs[PropertyType.getID("Buyer_Price")], current, roleAddrs[RoleType.getID("Storager")]);

        //Transporter-Buyer
        addReader(dataAddrs[PropertyType.getID("Buyer_Price")], current, roleAddrs[RoleType.getID("Transporter")]);

        //Seller-Buyer
        addReader(dataAddrs[PropertyType.getID("Buyer_Time")], current, roleAddrs[RoleType.getID("Seller")]);
        addReader(dataAddrs[PropertyType.getID("Buyer_Amount")], current, roleAddrs[RoleType.getID("Seller")]);
        addReader(dataAddrs[PropertyType.getID("Buyer_Price")], current, roleAddrs[RoleType.getID("Seller")]);

        //Government-Buyer
        addReader(dataAddrs[PropertyType.getID("Buyer_Name")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Buyer_Mobile")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Buyer_Time")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Buyer_Amount")], current, roleAddrs[RoleType.getID("Government")]);
        addReader(dataAddrs[PropertyType.getID("Buyer_Price")], current, roleAddrs[RoleType.getID("Government")]);

        //Buyer-Buyer
        addReader(dataAddrs[PropertyType.getID("Buyer_Name")], current, roleAddrs[RoleType.getID("Buyer")]);
        setWriter(dataAddrs[PropertyType.getID("Buyer_Name")], current, roleAddrs[RoleType.getID("Buyer")]);

        addReader(dataAddrs[PropertyType.getID("Buyer_Mobile")], current, roleAddrs[RoleType.getID("Buyer")]);
        setWriter(dataAddrs[PropertyType.getID("Buyer_Mobile")], current, roleAddrs[RoleType.getID("Buyer")]);

        addReader(dataAddrs[PropertyType.getID("Buyer_Time")], current, roleAddrs[RoleType.getID("Buyer")]);
        setWriter(dataAddrs[PropertyType.getID("Buyer_Time")], current, roleAddrs[RoleType.getID("Buyer")]);

        addReader(dataAddrs[PropertyType.getID("Buyer_Amount")], current, roleAddrs[RoleType.getID("Buyer")]);
        setWriter(dataAddrs[PropertyType.getID("Buyer_Amount")], current, roleAddrs[RoleType.getID("Buyer")]);

        addReader(dataAddrs[PropertyType.getID("Buyer_Price")], current, roleAddrs[RoleType.getID("Buyer")]);
        setWriter(dataAddrs[PropertyType.getID("Buyer_Price")], current, roleAddrs[RoleType.getID("Buyer")]);

        //Government
        current = signIn(accounts[6], "Innov@teD@ily1");
        addReader(dataAddrs[PropertyType.getID("Producer_TestResult")], current, roleAddrs[RoleType.getID("Producer")]);
        addReader(dataAddrs[PropertyType.getID("Producer_TestResult")], current, roleAddrs[RoleType.getID("Storager")]);
        addReader(dataAddrs[PropertyType.getID("Producer_TestResult")], current, roleAddrs[RoleType.getID("Transporter")]);
        addReader(dataAddrs[PropertyType.getID("Producer_TestResult")], current, roleAddrs[RoleType.getID("Seller")]);
        addReader(dataAddrs[PropertyType.getID("Producer_TestResult")], current, roleAddrs[RoleType.getID("Buyer")]);
        addReader(dataAddrs[PropertyType.getID("Producer_TestResult")], current, roleAddrs[RoleType.getID("Government")]);
        setWriter(dataAddrs[PropertyType.getID("Producer_TestResult")], current, roleAddrs[RoleType.getID("Government")]);

        return true;
    }

    public String addProperty(Credentials credentials) throws Exception {
        Data_sol_Data sc = Data_sol_Data.deploy(web3j, credentials, GAS_PRICE, GAS_LIMIT).send();
        LOGGER.info("Data Contract deployed: " + sc.getContractAddress());
        return sc.getContractAddress();
    }

    public String getFileNum(String addr, String id, Credentials credentials) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        String result = sc.getFileNum(new Utf8String(id)).send().getValue();
        LOGGER.info("File number calculated: " + result +", data id: " + id);
        return result;
    }


    public TransactionReceipt addReader(String addr, Credentials credentials, String roleAddr) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = sc.addReader(new Address(roleAddr)).send();

        LOGGER.info("Reader added: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public CompletableFuture<TransactionReceipt> addReaderAsync(String addr, Credentials credentials, String roleAddr) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        CompletableFuture<TransactionReceipt> futrue = sc.addReader(new Address(roleAddr)).sendAsync();

        LOGGER.info("Reader added: " + futrue.toString());
        return futrue;
    }

    public boolean checkReader(String addr, Address rcAddr, Credentials credentials) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        return sc.checkReader(rcAddr).send().getValue();
    }

    public TransactionReceipt setWriter(String addr, Credentials credentials, String roleAddr) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = sc.setWriter(new Address(roleAddr)).send();

        LOGGER.info("Writer setted: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public CompletableFuture<TransactionReceipt> setWriterAsync(String addr, Credentials credentials, String roleAddr) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        CompletableFuture<TransactionReceipt> futrue = sc.setWriter(new Address(roleAddr)).sendAsync();

        LOGGER.info("Reader added: " + futrue.toString());
        return futrue;
    }

    public boolean checkWriter(String addr, Address rcAddr, Credentials credentials) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        return sc.checkWriter(rcAddr).send().getValue();
    }

    public String getOwner(String addr, Credentials credentials) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        return sc.getOwner().send().getValue();
    }

    public TransactionReceipt write(String addr, Credentials credentials, String fileNo, String hash) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = sc.writeDB(new Utf8String(fileNo), new Utf8String(hash)).send();

        LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public CompletableFuture<TransactionReceipt> writeAsync(String addr, Credentials credentials, String fileNo, String hash) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        CompletableFuture<TransactionReceipt> future = sc.writeDB(new Utf8String(fileNo), new Utf8String(hash)).sendAsync();

        LOGGER.info("Transaction sent: " + future.toString());
        return future;
    }

    public String read(String addr, Credentials credentials, String id) throws Exception {
        Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        String hash = sc.readDB(new Utf8String(id)).send().getValue();
        LOGGER.info("Read succeed: " + hash);
        return hash;
    }

    private Credentials signIn(String address, String password) throws IOException, CipherException {
        Resource resource = new ClassPathResource(address);
        File file = resource.getFile();
        return  WalletUtils.loadCredentials(
                password,
                file.getAbsolutePath());
    }
}
