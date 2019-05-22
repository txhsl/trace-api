package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
//@Component
//@EnableScheduling
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    public static final String[] accounts = new String[]{"0x6a2fb5e3bf37f0c3d90db4713f7ad4a3b2c24111", "0x38a5d4e63bbac1af0eba0d99ef927359ab8d7293", "0x40b00de2e7b694b494022eef90e874f5e553f996",
            "0x49e2170e0b1188f2151ac35287c743ee60ea1f6a", "0x86dec6586bfa1dfe303eafbefee843919b543fd3", "0x135b8fb39d0f06ea1f2466f7e9f39d3136431480", "0x329b81e0a2af215c7e41b32251ae4d6ff1a83e3e",
            "0x370287edd5a5e7c4b0f5e305b00fe95fc702ce47", "0x5386787c9ef76a235d27f000170abeede038a3db", "0xb41717679a04696a2aaac280d9d45ddd3760ff47", "0xcdfea5a11062fab4cf4c2fda88e32fc6f7753145"};

    private final Web3j web3j;
    private final int REQUEST_LIMIT = 10;
    private Credentials current = null;
    //private ArrayList<RemoteCall<TransactionReceipt>> resetList = new ArrayList<>();

    public UserService(Web3j web3j) {
        this.web3j = web3j;
    }

    //@Scheduled(fixedDelay = 3000)
    //private void checkCompleted() {
    //    if (resetList.size() > 0) {
    //        resetList.get(0).sendAsync();
    //        LOGGER.info("A tx sent");
    //        resetList.remove(0);
    //    }
    //}

    public Credentials getCurrent() {
        return current;
    }

    public String[] resetContract(String sysAddr) throws Exception {

        //recreate RCs
        String[] roleAddrs = new String[RoleType.Types.size()];

        for (int i = 0; i < 10; i++) {
            if (signIn(accounts[i + 1], "Innov@teD@ily1")) {
                Role_sol_Role role = Role_sol_Role.deploy(web3j, current, GAS_PRICE, GAS_LIMIT).send();
                LOGGER.info("Role Contract " + i + " deployed: " + role.getContractAddress() + ". Role Name: " + RoleType.Types.get(i));
                roleAddrs[i] = role.getContractAddress();

                if (signIn(accounts[0],"Innov@teD@ily1" )) {
                    System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                    TransactionReceipt transactionReceipt = system.setRcIndex(new Utf8String(RoleType.Types.get(i)), new Address(roleAddrs[i])).send();
                    LOGGER.info("Transaction succeed: " + transactionReceipt.toString());

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
        if (signIn(accounts[1], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("畜牧方")], "畜牧负责人", new Address(dataAddrs[PropertyType.getID("畜牧负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("畜牧方")], "畜牧负责人", new Address(dataAddrs[PropertyType.getID("畜牧负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("畜牧方")], "品种", new Address(dataAddrs[PropertyType.getID("品种")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("畜牧方")], "品种", new Address(dataAddrs[PropertyType.getID("品种")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("畜牧方")], "出栏日期", new Address(dataAddrs[PropertyType.getID("出栏日期")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("畜牧方")], "出栏日期", new Address(dataAddrs[PropertyType.getID("出栏日期")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("畜牧方")], "出栏重量", new Address(dataAddrs[PropertyType.getID("出栏重量")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("畜牧方")], "出栏重量", new Address(dataAddrs[PropertyType.getID("出栏重量")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("畜牧方")], "畜牧检验负责人", new Address(dataAddrs[PropertyType.getID("畜牧检验负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("畜牧方")], "畜牧检验负责人", new Address(dataAddrs[PropertyType.getID("畜牧检验负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("畜牧方")], "畜牧检验结果", new Address(dataAddrs[PropertyType.getID("畜牧检验结果")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("畜牧方")], "畜牧检验结果", new Address(dataAddrs[PropertyType.getID("畜牧检验结果")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("畜牧方")], "畜牧公司", new Address(dataAddrs[PropertyType.getID("畜牧公司")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("畜牧方")], "畜牧公司", new Address(dataAddrs[PropertyType.getID("畜牧公司")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("畜牧方")], "畜牧许可证", new Address(dataAddrs[PropertyType.getID("畜牧许可证")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("畜牧方")], "畜牧许可证", new Address(dataAddrs[PropertyType.getID("畜牧许可证")])).sendAsync();
            
        }
        if (signIn(accounts[2], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("屠宰方")], "畜牧检验结果", new Address(dataAddrs[PropertyType.getID("畜牧检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("屠宰方")], "畜牧公司", new Address(dataAddrs[PropertyType.getID("畜牧公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("屠宰方")], "畜牧许可证", new Address(dataAddrs[PropertyType.getID("畜牧许可证")])).sendAsync();
            
        }
        if (signIn(accounts[3], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "畜牧检验结果", new Address(dataAddrs[PropertyType.getID("畜牧检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "畜牧公司", new Address(dataAddrs[PropertyType.getID("畜牧公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "畜牧许可证", new Address(dataAddrs[PropertyType.getID("畜牧许可证")])).sendAsync();
            
        }
        if (signIn(accounts[4], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "畜牧检验结果", new Address(dataAddrs[PropertyType.getID("畜牧检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "畜牧公司", new Address(dataAddrs[PropertyType.getID("畜牧公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "畜牧许可证", new Address(dataAddrs[PropertyType.getID("畜牧许可证")])).sendAsync();
            
        }
        if (signIn(accounts[5], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "畜牧检验结果", new Address(dataAddrs[PropertyType.getID("畜牧检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "畜牧公司", new Address(dataAddrs[PropertyType.getID("畜牧公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "畜牧许可证", new Address(dataAddrs[PropertyType.getID("畜牧许可证")])).sendAsync();
            
        }
        if (signIn(accounts[6], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "畜牧检验结果", new Address(dataAddrs[PropertyType.getID("畜牧检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "畜牧公司", new Address(dataAddrs[PropertyType.getID("畜牧公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "畜牧许可证", new Address(dataAddrs[PropertyType.getID("畜牧许可证")])).sendAsync();
            
        }
        if (signIn(accounts[7], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "畜牧检验结果", new Address(dataAddrs[PropertyType.getID("畜牧检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "畜牧公司", new Address(dataAddrs[PropertyType.getID("畜牧公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "畜牧许可证", new Address(dataAddrs[PropertyType.getID("畜牧许可证")])).sendAsync();
            
        }
        if (signIn(accounts[8], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "畜牧检验结果", new Address(dataAddrs[PropertyType.getID("畜牧检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "畜牧公司", new Address(dataAddrs[PropertyType.getID("畜牧公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "畜牧许可证", new Address(dataAddrs[PropertyType.getID("畜牧许可证")])).sendAsync();
            
        }
        if (signIn(accounts[9], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "品种", new Address(dataAddrs[PropertyType.getID("品种")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "出栏日期", new Address(dataAddrs[PropertyType.getID("出栏日期")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "出栏重量", new Address(dataAddrs[PropertyType.getID("出栏重量")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "畜牧检验结果", new Address(dataAddrs[PropertyType.getID("畜牧检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "畜牧公司", new Address(dataAddrs[PropertyType.getID("畜牧公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "畜牧许可证", new Address(dataAddrs[PropertyType.getID("畜牧许可证")])).sendAsync();
            
        }
        if (signIn(accounts[10], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "畜牧负责人", new Address(dataAddrs[PropertyType.getID("畜牧负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "畜牧检验负责人", new Address(dataAddrs[PropertyType.getID("畜牧检验负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "畜牧检验结果", new Address(dataAddrs[PropertyType.getID("畜牧检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "畜牧公司", new Address(dataAddrs[PropertyType.getID("畜牧公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "畜牧许可证", new Address(dataAddrs[PropertyType.getID("畜牧许可证")])).sendAsync();
            
        }


        if (signIn(accounts[2], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰负责人", new Address(dataAddrs[PropertyType.getID("屠宰负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰负责人", new Address(dataAddrs[PropertyType.getID("屠宰负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰日期", new Address(dataAddrs[PropertyType.getID("屠宰日期")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰日期", new Address(dataAddrs[PropertyType.getID("屠宰日期")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰检验负责人", new Address(dataAddrs[PropertyType.getID("屠宰检验负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰检验负责人", new Address(dataAddrs[PropertyType.getID("屠宰检验负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰检验结果", new Address(dataAddrs[PropertyType.getID("屠宰检验结果")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰检验结果", new Address(dataAddrs[PropertyType.getID("屠宰检验结果")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰公司", new Address(dataAddrs[PropertyType.getID("屠宰公司")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰公司", new Address(dataAddrs[PropertyType.getID("屠宰公司")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰许可证", new Address(dataAddrs[PropertyType.getID("屠宰许可证")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("屠宰方")], "屠宰许可证", new Address(dataAddrs[PropertyType.getID("屠宰许可证")])).sendAsync();
            
        }
        if (signIn(accounts[3], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "屠宰检验结果", new Address(dataAddrs[PropertyType.getID("屠宰检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "屠宰公司", new Address(dataAddrs[PropertyType.getID("屠宰公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "屠宰许可证", new Address(dataAddrs[PropertyType.getID("屠宰许可证")])).sendAsync();
            
        }
        if (signIn(accounts[4], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "屠宰检验结果", new Address(dataAddrs[PropertyType.getID("屠宰检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "屠宰公司", new Address(dataAddrs[PropertyType.getID("屠宰公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "屠宰许可证", new Address(dataAddrs[PropertyType.getID("屠宰许可证")])).sendAsync();;
            
        }
        if (signIn(accounts[5], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "屠宰检验结果", new Address(dataAddrs[PropertyType.getID("屠宰检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "屠宰公司", new Address(dataAddrs[PropertyType.getID("屠宰公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "屠宰许可证", new Address(dataAddrs[PropertyType.getID("屠宰许可证")])).sendAsync();
            
        }
        if (signIn(accounts[6], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "屠宰检验结果", new Address(dataAddrs[PropertyType.getID("屠宰检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "屠宰公司", new Address(dataAddrs[PropertyType.getID("屠宰公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "屠宰许可证", new Address(dataAddrs[PropertyType.getID("屠宰许可证")])).sendAsync();
            
        }
        if (signIn(accounts[7], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "屠宰检验结果", new Address(dataAddrs[PropertyType.getID("屠宰检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "屠宰公司", new Address(dataAddrs[PropertyType.getID("屠宰公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "屠宰许可证", new Address(dataAddrs[PropertyType.getID("屠宰许可证")])).sendAsync();
            
        }
        if (signIn(accounts[8], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "屠宰检验结果", new Address(dataAddrs[PropertyType.getID("屠宰检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "屠宰公司", new Address(dataAddrs[PropertyType.getID("屠宰公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "屠宰许可证", new Address(dataAddrs[PropertyType.getID("屠宰许可证")])).sendAsync();
            
        }
        if (signIn(accounts[9], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "屠宰日期", new Address(dataAddrs[PropertyType.getID("屠宰日期")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "屠宰检验结果", new Address(dataAddrs[PropertyType.getID("屠宰检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "屠宰公司", new Address(dataAddrs[PropertyType.getID("屠宰公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "屠宰许可证", new Address(dataAddrs[PropertyType.getID("屠宰许可证")])).sendAsync();
            
        }
        if (signIn(accounts[10], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "屠宰负责人", new Address(dataAddrs[PropertyType.getID("屠宰负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "屠宰检验负责人", new Address(dataAddrs[PropertyType.getID("屠宰检验负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "屠宰检验结果", new Address(dataAddrs[PropertyType.getID("屠宰检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "屠宰公司", new Address(dataAddrs[PropertyType.getID("屠宰公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "屠宰许可证", new Address(dataAddrs[PropertyType.getID("屠宰许可证")])).sendAsync();
            
        }


        if (signIn(accounts[3], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "包装负责人", new Address(dataAddrs[PropertyType.getID("包装负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("包装方")], "包装负责人", new Address(dataAddrs[PropertyType.getID("包装负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "包装日期", new Address(dataAddrs[PropertyType.getID("包装日期")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("包装方")], "包装日期", new Address(dataAddrs[PropertyType.getID("包装日期")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "包装检验负责人", new Address(dataAddrs[PropertyType.getID("包装检验负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("包装方")], "包装检验负责人", new Address(dataAddrs[PropertyType.getID("包装检验负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "包装检验结果", new Address(dataAddrs[PropertyType.getID("包装检验结果")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("包装方")], "包装检验结果", new Address(dataAddrs[PropertyType.getID("包装检验结果")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "包装公司", new Address(dataAddrs[PropertyType.getID("包装公司")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("包装方")], "包装公司", new Address(dataAddrs[PropertyType.getID("包装公司")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("包装方")], "包装许可证", new Address(dataAddrs[PropertyType.getID("包装许可证")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("包装方")], "包装许可证", new Address(dataAddrs[PropertyType.getID("包装许可证")])).sendAsync();
            
        }
        if (signIn(accounts[4], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "包装检验结果", new Address(dataAddrs[PropertyType.getID("包装检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "包装公司", new Address(dataAddrs[PropertyType.getID("包装公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "包装许可证", new Address(dataAddrs[PropertyType.getID("包装许可证")])).sendAsync();
            
        }
        if (signIn(accounts[5], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "包装检验结果", new Address(dataAddrs[PropertyType.getID("包装检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "包装公司", new Address(dataAddrs[PropertyType.getID("包装公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "包装许可证", new Address(dataAddrs[PropertyType.getID("包装许可证")])).sendAsync();
            
        }
        if (signIn(accounts[6], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "包装检验结果", new Address(dataAddrs[PropertyType.getID("包装检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "包装公司", new Address(dataAddrs[PropertyType.getID("包装公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "包装许可证", new Address(dataAddrs[PropertyType.getID("包装许可证")])).sendAsync();
            
        }
        if (signIn(accounts[7], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "包装检验结果", new Address(dataAddrs[PropertyType.getID("包装检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "包装公司", new Address(dataAddrs[PropertyType.getID("包装公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "包装许可证", new Address(dataAddrs[PropertyType.getID("包装许可证")])).sendAsync();
            
        }
        if (signIn(accounts[8], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "包装检验结果", new Address(dataAddrs[PropertyType.getID("包装检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "包装公司", new Address(dataAddrs[PropertyType.getID("包装公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "包装许可证", new Address(dataAddrs[PropertyType.getID("包装许可证")])).sendAsync();
            
        }
        if (signIn(accounts[9], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "包装日期", new Address(dataAddrs[PropertyType.getID("包装日期")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "包装检验结果", new Address(dataAddrs[PropertyType.getID("包装检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "包装公司", new Address(dataAddrs[PropertyType.getID("包装公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "包装许可证", new Address(dataAddrs[PropertyType.getID("包装许可证")])).sendAsync();
            
        }
        if (signIn(accounts[10], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "包装负责人", new Address(dataAddrs[PropertyType.getID("包装负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "包装检验负责人", new Address(dataAddrs[PropertyType.getID("包装检验负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "包装检验结果", new Address(dataAddrs[PropertyType.getID("包装检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "包装公司", new Address(dataAddrs[PropertyType.getID("包装公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "包装许可证", new Address(dataAddrs[PropertyType.getID("包装许可证")])).sendAsync();
            
        }


        if (signIn(accounts[4], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "仓储负责人", new Address(dataAddrs[PropertyType.getID("仓储负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("仓储方")], "仓储负责人", new Address(dataAddrs[PropertyType.getID("仓储负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "入仓时间", new Address(dataAddrs[PropertyType.getID("入仓时间")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("仓储方")], "入仓时间", new Address(dataAddrs[PropertyType.getID("入仓时间")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "出仓时间", new Address(dataAddrs[PropertyType.getID("出仓时间")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("仓储方")], "出仓时间", new Address(dataAddrs[PropertyType.getID("出仓时间")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "仓储量", new Address(dataAddrs[PropertyType.getID("仓储量")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("仓储方")], "仓储量", new Address(dataAddrs[PropertyType.getID("仓储量")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "仓储公司", new Address(dataAddrs[PropertyType.getID("仓储公司")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("仓储方")], "仓储公司", new Address(dataAddrs[PropertyType.getID("仓储公司")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("仓储方")], "仓储许可证", new Address(dataAddrs[PropertyType.getID("仓储许可证")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("仓储方")], "仓储许可证", new Address(dataAddrs[PropertyType.getID("仓储许可证")])).sendAsync();
            
        }
        if (signIn(accounts[5], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "仓储量", new Address(dataAddrs[PropertyType.getID("仓储量")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "仓储公司", new Address(dataAddrs[PropertyType.getID("仓储公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "仓储许可证", new Address(dataAddrs[PropertyType.getID("仓储许可证")])).sendAsync();
            
        }
        if (signIn(accounts[6], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "仓储量", new Address(dataAddrs[PropertyType.getID("仓储量")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "仓储公司", new Address(dataAddrs[PropertyType.getID("仓储公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "仓储许可证", new Address(dataAddrs[PropertyType.getID("仓储许可证")])).sendAsync();
            
        }
        if (signIn(accounts[7], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "仓储量", new Address(dataAddrs[PropertyType.getID("仓储量")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "仓储公司", new Address(dataAddrs[PropertyType.getID("仓储公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "仓储许可证", new Address(dataAddrs[PropertyType.getID("仓储许可证")])).sendAsync();
            
        }
        if (signIn(accounts[8], "Innov@teD@ily1")) {

            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "仓储量", new Address(dataAddrs[PropertyType.getID("仓储量")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "仓储公司", new Address(dataAddrs[PropertyType.getID("仓储公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "仓储许可证", new Address(dataAddrs[PropertyType.getID("仓储许可证")])).sendAsync();
            
        }
        if (signIn(accounts[9], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "入仓时间", new Address(dataAddrs[PropertyType.getID("入仓时间")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "出仓时间", new Address(dataAddrs[PropertyType.getID("出仓时间")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "仓储量", new Address(dataAddrs[PropertyType.getID("仓储量")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "仓储量", new Address(dataAddrs[PropertyType.getID("仓储量")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "仓储公司", new Address(dataAddrs[PropertyType.getID("仓储公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "仓储许可证", new Address(dataAddrs[PropertyType.getID("仓储许可证")])).sendAsync();
            
        }
        if (signIn(accounts[10], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "仓储负责人", new Address(dataAddrs[PropertyType.getID("仓储负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "仓储量", new Address(dataAddrs[PropertyType.getID("仓储量")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "仓储公司", new Address(dataAddrs[PropertyType.getID("仓储公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "仓储许可证", new Address(dataAddrs[PropertyType.getID("仓储许可证")])).sendAsync();
            
        }


        if (signIn(accounts[5], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "物流负责人", new Address(dataAddrs[PropertyType.getID("物流负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("物流方")], "物流负责人", new Address(dataAddrs[PropertyType.getID("物流负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "起运时间", new Address(dataAddrs[PropertyType.getID("起运时间")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("物流方")], "起运时间", new Address(dataAddrs[PropertyType.getID("起运时间")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "到货时间", new Address(dataAddrs[PropertyType.getID("到货时间")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("物流方")], "到货时间", new Address(dataAddrs[PropertyType.getID("到货时间")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "运输量", new Address(dataAddrs[PropertyType.getID("运输量")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("物流方")], "运输量", new Address(dataAddrs[PropertyType.getID("运输量")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "运输距离", new Address(dataAddrs[PropertyType.getID("运输距离")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("物流方")], "运输距离", new Address(dataAddrs[PropertyType.getID("运输距离")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "物流公司", new Address(dataAddrs[PropertyType.getID("物流公司")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("物流方")], "物流公司", new Address(dataAddrs[PropertyType.getID("物流公司")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "物流许可证", new Address(dataAddrs[PropertyType.getID("物流许可证")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("物流方")], "物流许可证", new Address(dataAddrs[PropertyType.getID("物流许可证")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "起始地", new Address(dataAddrs[PropertyType.getID("起始地")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("物流方")], "起始地", new Address(dataAddrs[PropertyType.getID("起始地")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("物流方")], "目的地", new Address(dataAddrs[PropertyType.getID("目的地")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("物流方")], "目的地", new Address(dataAddrs[PropertyType.getID("目的地")])).sendAsync();
            
        }
        if (signIn(accounts[6], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "物流公司", new Address(dataAddrs[PropertyType.getID("物流公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "物流许可证", new Address(dataAddrs[PropertyType.getID("物流许可证")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "起始地", new Address(dataAddrs[PropertyType.getID("起始地")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "目的地", new Address(dataAddrs[PropertyType.getID("目的地")])).sendAsync();
            
        }
        if (signIn(accounts[7], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "物流公司", new Address(dataAddrs[PropertyType.getID("物流公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "物流许可证", new Address(dataAddrs[PropertyType.getID("物流许可证")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "起始地", new Address(dataAddrs[PropertyType.getID("起始地")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "目的地", new Address(dataAddrs[PropertyType.getID("目的地")])).sendAsync();
            
        }
        if (signIn(accounts[8], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "物流公司", new Address(dataAddrs[PropertyType.getID("物流公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "物流许可证", new Address(dataAddrs[PropertyType.getID("物流许可证")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "起始地", new Address(dataAddrs[PropertyType.getID("起始地")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "目的地", new Address(dataAddrs[PropertyType.getID("目的地")])).sendAsync();

            }
        if (signIn(accounts[9], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "起运时间", new Address(dataAddrs[PropertyType.getID("起运时间")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "到货时间", new Address(dataAddrs[PropertyType.getID("到货时间")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "运输量", new Address(dataAddrs[PropertyType.getID("运输量")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "运输距离", new Address(dataAddrs[PropertyType.getID("运输距离")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "物流公司", new Address(dataAddrs[PropertyType.getID("物流公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "物流许可证", new Address(dataAddrs[PropertyType.getID("物流许可证")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "起始地", new Address(dataAddrs[PropertyType.getID("起始地")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "目的地", new Address(dataAddrs[PropertyType.getID("目的地")])).sendAsync();
            
        }
        if (signIn(accounts[10], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "物流负责人", new Address(dataAddrs[PropertyType.getID("物流负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "物流公司", new Address(dataAddrs[PropertyType.getID("物流公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "物流许可证", new Address(dataAddrs[PropertyType.getID("物流许可证")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "起始地", new Address(dataAddrs[PropertyType.getID("起始地")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "目的地", new Address(dataAddrs[PropertyType.getID("目的地")])).sendAsync();
            
        }


        if (signIn(accounts[6], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "加工进货负责人", new Address(dataAddrs[PropertyType.getID("加工进货负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级加工")], "加工进货负责人", new Address(dataAddrs[PropertyType.getID("加工进货负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "加工进货日期", new Address(dataAddrs[PropertyType.getID("加工进货日期")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级加工")], "加工进货日期", new Address(dataAddrs[PropertyType.getID("加工进货日期")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "加工负责人", new Address(dataAddrs[PropertyType.getID("加工负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级加工")], "加工负责人", new Address(dataAddrs[PropertyType.getID("加工负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "加工日期", new Address(dataAddrs[PropertyType.getID("加工日期")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级加工")], "加工日期", new Address(dataAddrs[PropertyType.getID("加工日期")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "加工检验负责人", new Address(dataAddrs[PropertyType.getID("加工检验负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级加工")], "加工检验负责人", new Address(dataAddrs[PropertyType.getID("加工检验负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "加工检验结果", new Address(dataAddrs[PropertyType.getID("加工检验结果")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级加工")], "加工检验结果", new Address(dataAddrs[PropertyType.getID("加工检验结果")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "加工公司", new Address(dataAddrs[PropertyType.getID("加工公司")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级加工")], "加工公司", new Address(dataAddrs[PropertyType.getID("加工公司")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级加工")], "加工许可证", new Address(dataAddrs[PropertyType.getID("加工许可证")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级加工")], "加工许可证", new Address(dataAddrs[PropertyType.getID("加工许可证")])).sendAsync();
            
        }
        if (signIn(accounts[7], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "加工检验结果", new Address(dataAddrs[PropertyType.getID("加工检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "加工公司", new Address(dataAddrs[PropertyType.getID("加工公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "加工许可证", new Address(dataAddrs[PropertyType.getID("加工许可证")])).sendAsync();
            
        }
        if (signIn(accounts[8], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "加工检验结果", new Address(dataAddrs[PropertyType.getID("加工检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "加工公司", new Address(dataAddrs[PropertyType.getID("加工公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "加工许可证", new Address(dataAddrs[PropertyType.getID("加工许可证")])).sendAsync();
            
        }
        if (signIn(accounts[9], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "加工进货日期", new Address(dataAddrs[PropertyType.getID("加工进货日期")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "加工日期", new Address(dataAddrs[PropertyType.getID("加工日期")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "加工检验结果", new Address(dataAddrs[PropertyType.getID("加工检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "加工公司", new Address(dataAddrs[PropertyType.getID("加工公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "加工许可证", new Address(dataAddrs[PropertyType.getID("加工许可证")])).sendAsync();
            
        }
        if (signIn(accounts[10], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "加工进货负责人", new Address(dataAddrs[PropertyType.getID("加工进货负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "加工负责人", new Address(dataAddrs[PropertyType.getID("加工负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "加工检验负责人", new Address(dataAddrs[PropertyType.getID("加工检验负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "加工检验结果", new Address(dataAddrs[PropertyType.getID("加工检验结果")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "加工公司", new Address(dataAddrs[PropertyType.getID("加工公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "加工许可证", new Address(dataAddrs[PropertyType.getID("加工许可证")])).sendAsync();
            
        }


        if (signIn(accounts[7], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "二级物流负责人", new Address(dataAddrs[PropertyType.getID("二级物流负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级物流")], "二级物流负责人", new Address(dataAddrs[PropertyType.getID("二级物流负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "二级起运时间", new Address(dataAddrs[PropertyType.getID("二级起运时间")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级物流")], "二级起运时间", new Address(dataAddrs[PropertyType.getID("二级起运时间")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "二级到货时间", new Address(dataAddrs[PropertyType.getID("二级到货时间")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级物流")], "二级到货时间", new Address(dataAddrs[PropertyType.getID("二级到货时间")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "二级运输量", new Address(dataAddrs[PropertyType.getID("二级运输量")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级物流")], "二级运输量", new Address(dataAddrs[PropertyType.getID("二级运输量")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "二级运输距离", new Address(dataAddrs[PropertyType.getID("二级运输距离")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级物流")], "二级运输距离", new Address(dataAddrs[PropertyType.getID("二级运输距离")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "二级物流公司", new Address(dataAddrs[PropertyType.getID("二级物流公司")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级物流")], "二级物流公司", new Address(dataAddrs[PropertyType.getID("二级物流公司")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "二级物流许可证", new Address(dataAddrs[PropertyType.getID("二级物流许可证")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级物流")], "二级物流许可证", new Address(dataAddrs[PropertyType.getID("二级物流许可证")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "二级起始地", new Address(dataAddrs[PropertyType.getID("二级起始地")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级物流")], "二级起始地", new Address(dataAddrs[PropertyType.getID("二级起始地")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("二级物流")], "二级目的地", new Address(dataAddrs[PropertyType.getID("二级目的地")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("二级物流")], "二级目的地", new Address(dataAddrs[PropertyType.getID("二级目的地")])).sendAsync();
            
        }
        if (signIn(accounts[8], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "二级物流公司", new Address(dataAddrs[PropertyType.getID("二级物流公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "二级物流许可证", new Address(dataAddrs[PropertyType.getID("二级物流许可证")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "二级起始地", new Address(dataAddrs[PropertyType.getID("二级起始地")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "二级目的地", new Address(dataAddrs[PropertyType.getID("二级目的地")])).sendAsync();
            
        }
        if (signIn(accounts[9], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "二级起运时间", new Address(dataAddrs[PropertyType.getID("二级起运时间")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "二级到货时间", new Address(dataAddrs[PropertyType.getID("二级到货时间")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "二级运输量", new Address(dataAddrs[PropertyType.getID("二级运输量")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "二级运输距离", new Address(dataAddrs[PropertyType.getID("二级运输距离")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "二级物流公司", new Address(dataAddrs[PropertyType.getID("二级物流公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "二级物流许可证", new Address(dataAddrs[PropertyType.getID("二级物流许可证")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "二级起始地", new Address(dataAddrs[PropertyType.getID("二级起始地")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "二级目的地", new Address(dataAddrs[PropertyType.getID("二级目的地")])).sendAsync();
        }
        if (signIn(accounts[10], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "二级物流负责人", new Address(dataAddrs[PropertyType.getID("二级物流负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "二级物流公司", new Address(dataAddrs[PropertyType.getID("二级物流公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "二级物流许可证", new Address(dataAddrs[PropertyType.getID("二级物流许可证")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "二级起始地", new Address(dataAddrs[PropertyType.getID("二级起始地")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "二级目的地", new Address(dataAddrs[PropertyType.getID("二级目的地")])).sendAsync();
            
        }


        if (signIn(accounts[8], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "零售进货负责人", new Address(dataAddrs[PropertyType.getID("零售进货负责人")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("零售方")], "零售进货负责人", new Address(dataAddrs[PropertyType.getID("零售进货负责人")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "零售进货时间", new Address(dataAddrs[PropertyType.getID("零售进货时间")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("零售方")], "零售进货时间", new Address(dataAddrs[PropertyType.getID("零售进货时间")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "进货量", new Address(dataAddrs[PropertyType.getID("进货量")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("零售方")], "进货量", new Address(dataAddrs[PropertyType.getID("进货量")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "零售公司", new Address(dataAddrs[PropertyType.getID("零售公司")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("零售方")], "零售公司", new Address(dataAddrs[PropertyType.getID("零售公司")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "零售许可证", new Address(dataAddrs[PropertyType.getID("零售许可证")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("零售方")], "零售许可证", new Address(dataAddrs[PropertyType.getID("零售许可证")])).sendAsync();
            
            setManagedAsync(roleAddrs[RoleType.getID("零售方")], "零售价", new Address(dataAddrs[PropertyType.getID("零售价")])).sendAsync();
            setOwnedAsync(roleAddrs[RoleType.getID("零售方")], "零售价", new Address(dataAddrs[PropertyType.getID("零售价")])).sendAsync();
            
        }
        if (signIn(accounts[9], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "零售进货时间", new Address(dataAddrs[PropertyType.getID("零售进货时间")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "进货量", new Address(dataAddrs[PropertyType.getID("进货量")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "零售公司", new Address(dataAddrs[PropertyType.getID("零售公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "零售许可证", new Address(dataAddrs[PropertyType.getID("零售许可证")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("消费者")], "零售价", new Address(dataAddrs[PropertyType.getID("零售价")])).sendAsync();
            
        }
        if (signIn(accounts[10], "Innov@teD@ily1")) {
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "零售进货负责人", new Address(dataAddrs[PropertyType.getID("零售进货负责人")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "零售公司", new Address(dataAddrs[PropertyType.getID("零售公司")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "零售许可证", new Address(dataAddrs[PropertyType.getID("零售许可证")])).sendAsync();
            setManagedAsync(roleAddrs[RoleType.getID("监管部门")], "零售价", new Address(dataAddrs[PropertyType.getID("零售价")])).sendAsync();
            
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
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = rc.setOwned(new Utf8String(name), scAddr).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public RemoteCall<TransactionReceipt> setOwnedAsync(String rcAddr, String name, Address scAddr) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                RemoteCall<TransactionReceipt> tx = rc.setOwned(new Utf8String(name), scAddr);
                //resetList.add(tx);
                return tx;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    private TransactionReceipt setManaged(String rcAddr, String name, Address scAddr) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = rc.setManaged(new Utf8String(name), scAddr).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public RemoteCall<TransactionReceipt> setManagedAsync(String rcAddr, String name, Address scAddr) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                RemoteCall<TransactionReceipt> tx = rc.setManaged(new Utf8String(name), scAddr);
                //resetList.add(tx);
                return tx;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public String getOwned(String rcAddr, String name) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                String scAddr = rc.getOwned(new Utf8String(name)).send().getValue();

                LOGGER.info("Read succeed: " + scAddr);
                return scAddr;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public Map<String, String> getOwnedAll(String rcAddr) throws Exception {
        Map<String, String> result = new HashMap<>();
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);

        for (String property : PropertyType.Types) {
            boolean flag = false;
            int count = 0;

            while(!flag && count < REQUEST_LIMIT) {
                try {
                    String scAddr = rc.getOwned(new Utf8String(property)).send().getValue();
                    if (!scAddr.equals("0x0000000000000000000000000000000000000000")) {
                        result.putIfAbsent(property, scAddr);
                    }
                    flag = true;
                } catch (NullPointerException e) {
                    LOGGER.error(e.toString());
                    count++;
                }
            }
        }
        return result;
    }

    public String getManaged(String rcAddr, String name) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                String scAddr = rc.getManaged(new Utf8String(name)).send().getValue();

                LOGGER.info("Read succeed: " + scAddr);
                return scAddr;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public Map<String, String> getManagedAll(String rcAddr) throws Exception {
        Map<String, String> result = new HashMap<>();
        Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);

        for (String property : PropertyType.Types) {
            boolean flag = false;
            int count = 0;

            while(!flag && count < REQUEST_LIMIT) {
                try {
                    String scAddr = rc.getManaged(new Utf8String(property)).send().getValue();
                    if (!scAddr.equals("0x0000000000000000000000000000000000000000")) {
                        result.putIfAbsent(property, scAddr);
                    }
                    flag = true;
                } catch (NullPointerException e) {
                    LOGGER.error(e.toString());
                    count++;
                }
            }
        }

        return result;
    }
}
