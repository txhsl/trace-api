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
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.contract.Data_sol_Data;
import pl.piomin.service.blockchain.contract.System_sol_System;
import pl.piomin.service.blockchain.model.TaskSwapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private final int REQUEST_LIMIT = 10;
    private ArrayList<RemoteCall<TransactionReceipt>> resetList = new ArrayList<>();

    public DataService(Web3j web3j) {
        this.web3j = web3j;
    }

    private void sendTx() throws InterruptedException {
        while (resetList.size() > 0) {
            Thread.sleep(3000);
            TaskSwapper taskSwapper = new TaskSwapper("默认属性合约", "权限初始化", "");
            taskSwapper.setFuture(resetList.get(0).sendAsync());
            LOGGER.info("A tx sent. Left " + resetList.size());
            resetList.remove(0);
        }
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
            if (j == 8) {
                current = signIn(accounts[2], "Innov@teD@ily1");
            }
            if (j == 14) {
                current = signIn(accounts[3], "Innov@teD@ily1");
            }
            if (j == 20) {
                current = signIn(accounts[4], "Innov@teD@ily1");
            }
            if (j == 26) {
                current = signIn(accounts[5], "Innov@teD@ily1");
            }
            if (j == 35) {
                current = signIn(accounts[6], "Innov@teD@ily1");
            }
            if (j == 43) {
                current = signIn(accounts[7], "Innov@teD@ily1");
            }
            if (j == 52) {
                current = signIn(accounts[8], "Innov@teD@ily1");
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
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧负责人")], current, roleAddrs[RoleType.getID("畜牧方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("畜牧负责人")], current, roleAddrs[RoleType.getID("畜牧方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("品种")], current, roleAddrs[RoleType.getID("畜牧方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("品种")], current, roleAddrs[RoleType.getID("畜牧方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("出栏日期")], current, roleAddrs[RoleType.getID("畜牧方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("出栏日期")], current, roleAddrs[RoleType.getID("畜牧方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("出栏重量")], current, roleAddrs[RoleType.getID("畜牧方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("出栏重量")], current, roleAddrs[RoleType.getID("畜牧方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧检验负责人")], current, roleAddrs[RoleType.getID("畜牧方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("畜牧检验负责人")], current, roleAddrs[RoleType.getID("畜牧方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧检验结果")], current, roleAddrs[RoleType.getID("畜牧方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("畜牧检验结果")], current, roleAddrs[RoleType.getID("畜牧方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧公司")], current, roleAddrs[RoleType.getID("畜牧方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("畜牧公司")], current, roleAddrs[RoleType.getID("畜牧方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧许可证")], current, roleAddrs[RoleType.getID("畜牧方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("畜牧许可证")], current, roleAddrs[RoleType.getID("畜牧方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧检验结果")], current, roleAddrs[RoleType.getID("屠宰方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧公司")], current, roleAddrs[RoleType.getID("屠宰方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧许可证")], current, roleAddrs[RoleType.getID("屠宰方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧检验结果")], current, roleAddrs[RoleType.getID("包装方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧公司")], current, roleAddrs[RoleType.getID("包装方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧许可证")], current, roleAddrs[RoleType.getID("包装方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧检验结果")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧公司")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧许可证")], current, roleAddrs[RoleType.getID("仓储方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧检验结果")], current, roleAddrs[RoleType.getID("物流方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧公司")], current, roleAddrs[RoleType.getID("物流方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧许可证")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧检验结果")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧公司")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧许可证")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧检验结果")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧公司")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧许可证")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧检验结果")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧公司")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧许可证")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("品种")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("出栏日期")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("出栏重量")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧检验结果")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧公司")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧许可证")], current, roleAddrs[RoleType.getID("消费者")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧负责人")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧检验结果")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧公司")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("畜牧许可证")], current, roleAddrs[RoleType.getID("监管部门")]);
        

        current = signIn(accounts[2], "Innov@teD@ily1");
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰负责人")], current, roleAddrs[RoleType.getID("屠宰方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("屠宰负责人")], current, roleAddrs[RoleType.getID("屠宰方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰日期")], current, roleAddrs[RoleType.getID("屠宰方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("屠宰日期")], current, roleAddrs[RoleType.getID("屠宰方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰检验负责人")], current, roleAddrs[RoleType.getID("屠宰方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("屠宰检验负责人")], current, roleAddrs[RoleType.getID("屠宰方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰检验结果")], current, roleAddrs[RoleType.getID("屠宰方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("屠宰检验结果")], current, roleAddrs[RoleType.getID("屠宰方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰公司")], current, roleAddrs[RoleType.getID("屠宰方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("屠宰公司")], current, roleAddrs[RoleType.getID("屠宰方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰许可证")], current, roleAddrs[RoleType.getID("屠宰方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("屠宰许可证")], current, roleAddrs[RoleType.getID("屠宰方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰检验结果")], current, roleAddrs[RoleType.getID("包装方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰公司")], current, roleAddrs[RoleType.getID("包装方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰许可证")], current, roleAddrs[RoleType.getID("包装方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰检验结果")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰公司")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰许可证")], current, roleAddrs[RoleType.getID("仓储方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰检验结果")], current, roleAddrs[RoleType.getID("物流方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰公司")], current, roleAddrs[RoleType.getID("物流方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰许可证")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰检验结果")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰公司")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰许可证")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰检验结果")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰公司")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰许可证")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰检验结果")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰公司")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰许可证")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰日期")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰检验结果")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰公司")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰许可证")], current, roleAddrs[RoleType.getID("消费者")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰负责人")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰检验负责人")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰检验结果")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰公司")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("屠宰许可证")], current, roleAddrs[RoleType.getID("监管部门")]);
        

        current = signIn(accounts[3], "Innov@teD@ily1");
        resetReaderAsync(dataAddrs[PropertyType.getID("包装负责人")], current, roleAddrs[RoleType.getID("包装方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("包装负责人")], current, roleAddrs[RoleType.getID("包装方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装日期")], current, roleAddrs[RoleType.getID("包装方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("包装日期")], current, roleAddrs[RoleType.getID("包装方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装检验负责人")], current, roleAddrs[RoleType.getID("包装方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("包装检验负责人")], current, roleAddrs[RoleType.getID("包装方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装检验结果")], current, roleAddrs[RoleType.getID("包装方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("包装检验结果")], current, roleAddrs[RoleType.getID("包装方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装公司")], current, roleAddrs[RoleType.getID("包装方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("包装公司")], current, roleAddrs[RoleType.getID("包装方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装许可证")], current, roleAddrs[RoleType.getID("包装方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("包装许可证")], current, roleAddrs[RoleType.getID("包装方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装检验结果")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装公司")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装许可证")], current, roleAddrs[RoleType.getID("仓储方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装检验结果")], current, roleAddrs[RoleType.getID("物流方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装公司")], current, roleAddrs[RoleType.getID("物流方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装许可证")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装检验结果")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装公司")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装许可证")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装检验结果")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装公司")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装许可证")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装检验结果")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装公司")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装许可证")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装日期")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装检验结果")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装公司")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装许可证")], current, roleAddrs[RoleType.getID("消费者")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("包装负责人")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装检验负责人")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装检验结果")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装公司")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("包装许可证")], current, roleAddrs[RoleType.getID("监管部门")]);
        

        current = signIn(accounts[4], "Innov@teD@ily1");
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储负责人")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("仓储负责人")], current, roleAddrs[RoleType.getID("仓储方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("入仓时间")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("入仓时间")], current, roleAddrs[RoleType.getID("仓储方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("出仓时间")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("出仓时间")], current, roleAddrs[RoleType.getID("仓储方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储量")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("仓储量")], current, roleAddrs[RoleType.getID("仓储方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储公司")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("仓储公司")], current, roleAddrs[RoleType.getID("仓储方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储许可证")], current, roleAddrs[RoleType.getID("仓储方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("仓储许可证")], current, roleAddrs[RoleType.getID("仓储方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储公司")], current, roleAddrs[RoleType.getID("物流方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储许可证")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储公司")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储许可证")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储公司")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储许可证")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储公司")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储许可证")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("入仓时间")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("出仓时间")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储量")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储公司")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储许可证")], current, roleAddrs[RoleType.getID("消费者")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储负责人")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储公司")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("仓储许可证")], current, roleAddrs[RoleType.getID("监管部门")]);
        

        current = signIn(accounts[5], "Innov@teD@ily1");
        resetReaderAsync(dataAddrs[PropertyType.getID("物流负责人")], current, roleAddrs[RoleType.getID("物流方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("物流负责人")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("起运时间")], current, roleAddrs[RoleType.getID("物流方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("起运时间")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("到货时间")], current, roleAddrs[RoleType.getID("物流方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("到货时间")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("运输量")], current, roleAddrs[RoleType.getID("物流方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("运输量")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("运输距离")], current, roleAddrs[RoleType.getID("物流方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("运输距离")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("物流公司")], current, roleAddrs[RoleType.getID("物流方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("物流公司")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("物流许可证")], current, roleAddrs[RoleType.getID("物流方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("物流许可证")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("起始地")], current, roleAddrs[RoleType.getID("物流方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("起始地")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("目的地")], current, roleAddrs[RoleType.getID("物流方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("目的地")], current, roleAddrs[RoleType.getID("物流方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("物流公司")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("物流许可证")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("起始地")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("目的地")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("物流公司")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("物流许可证")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("起始地")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("目的地")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("物流公司")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("物流许可证")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("起始地")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("目的地")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("起运时间")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("到货时间")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("运输量")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("运输距离")], current, roleAddrs[RoleType.getID("消费者")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("物流公司")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("物流许可证")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("起始地")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("目的地")], current, roleAddrs[RoleType.getID("消费者")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("物流负责人")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("物流公司")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("物流许可证")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("起始地")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("目的地")], current, roleAddrs[RoleType.getID("监管部门")]);
        

        current = signIn(accounts[6], "Innov@teD@ily1");
        resetReaderAsync(dataAddrs[PropertyType.getID("加工进货负责人")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("加工进货负责人")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("加工进货日期")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("加工进货日期")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("加工负责人")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("加工负责人")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("加工日期")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("加工日期")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("加工检验负责人")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("加工检验负责人")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("加工检验结果")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("加工检验结果")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("加工公司")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("加工公司")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("加工许可证")], current, roleAddrs[RoleType.getID("二级加工")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("加工许可证")], current, roleAddrs[RoleType.getID("二级加工")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("加工检验结果")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("加工公司")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("加工许可证")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("加工检验结果")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("加工公司")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("加工许可证")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("加工检验结果")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("加工公司")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("加工许可证")], current, roleAddrs[RoleType.getID("消费者")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("加工检验结果")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("加工公司")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("加工许可证")], current, roleAddrs[RoleType.getID("监管部门")]);
        

        current = signIn(accounts[7], "Innov@teD@ily1");
        resetReaderAsync(dataAddrs[PropertyType.getID("二级物流负责人")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("二级物流负责人")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级起运时间")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("二级起运时间")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级到货时间")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("二级到货时间")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级运输量")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("二级运输量")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级运输距离")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("二级运输距离")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级物流公司")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("二级物流公司")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级物流许可证")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("二级物流许可证")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级起始地")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("二级起始地")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级目的地")], current, roleAddrs[RoleType.getID("二级物流")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("二级目的地")], current, roleAddrs[RoleType.getID("二级物流")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级物流公司")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级物流许可证")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级起始地")], current, roleAddrs[RoleType.getID("零售方")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级目的地")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级起运时间")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级到货时间")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级运输量")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级运输距离")], current, roleAddrs[RoleType.getID("消费者")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级物流公司")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级物流许可证")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级起始地")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级目的地")], current, roleAddrs[RoleType.getID("消费者")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("二级物流负责人")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级物流公司")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级物流许可证")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级起始地")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("二级目的地")], current, roleAddrs[RoleType.getID("监管部门")]);
        

        current = signIn(accounts[8], "Innov@teD@ily1");
        resetReaderAsync(dataAddrs[PropertyType.getID("零售进货负责人")], current, roleAddrs[RoleType.getID("零售方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("零售进货负责人")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("零售进货时间")], current, roleAddrs[RoleType.getID("零售方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("零售进货时间")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("进货量")], current, roleAddrs[RoleType.getID("零售方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("进货量")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("零售公司")], current, roleAddrs[RoleType.getID("零售方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("零售公司")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("零售许可证")], current, roleAddrs[RoleType.getID("零售方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("零售许可证")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("零售价")], current, roleAddrs[RoleType.getID("零售方")]);
        resetWriterAsync(dataAddrs[PropertyType.getID("零售价")], current, roleAddrs[RoleType.getID("零售方")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("零售进货时间")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("进货量")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("零售公司")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("零售许可证")], current, roleAddrs[RoleType.getID("消费者")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("零售价")], current, roleAddrs[RoleType.getID("消费者")]);
        
        resetReaderAsync(dataAddrs[PropertyType.getID("零售进货负责人")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("零售公司")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("零售许可证")], current, roleAddrs[RoleType.getID("监管部门")]);
        resetReaderAsync(dataAddrs[PropertyType.getID("零售价")], current, roleAddrs[RoleType.getID("监管部门")]);
        sendTx();
        return true;
    }

    public String addProperty(Credentials credentials) throws Exception {
        Data_sol_Data sc = Data_sol_Data.deploy(web3j, credentials, GAS_PRICE, GAS_LIMIT).send();
        LOGGER.info("Data Contract deployed: " + sc.getContractAddress());
        return sc.getContractAddress();
    }

    public String getFileNum(String addr, String id, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                String result = sc.getFileNum(new Utf8String(id)).send().getValue();
                LOGGER.info("File number calculated: " + result + ", data id: " + id);
                return result;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    private void resetReaderAsync(String addr, Credentials credentials, String roleAddr){
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                resetList.add(sc.addReader(new Address(roleAddr)));
                return;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    private void resetWriterAsync(String addr, Credentials credentials, String roleAddr){
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                resetList.add(sc.setWriter(new Address(roleAddr)));
                return;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }


    public TransactionReceipt addReader(String addr, Credentials credentials, String roleAddr) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = sc.addReader(new Address(roleAddr)).send();

                LOGGER.info("Reader added: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> addReaderAsync(String addr, Credentials credentials, String roleAddr) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                CompletableFuture<TransactionReceipt> futrue = sc.addReader(new Address(roleAddr)).sendAsync();

                LOGGER.info("Reader added: " + futrue.toString());
                return futrue;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public boolean checkReader(String addr, Address rcAddr, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                return sc.checkReader(rcAddr).send().getValue();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt setWriter(String addr, Credentials credentials, String roleAddr) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = sc.setWriter(new Address(roleAddr)).send();

                LOGGER.info("Writer setted: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> setWriterAsync(String addr, Credentials credentials, String roleAddr) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                CompletableFuture<TransactionReceipt> futrue = sc.setWriter(new Address(roleAddr)).sendAsync();

                LOGGER.info("Writer setted: " + futrue.toString());
                return futrue;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public boolean checkWriter(String addr, Address rcAddr, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                return sc.checkWriter(rcAddr).send().getValue();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public String getOwner(String addr, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                return sc.getOwner().send().getValue();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt write(String addr, Credentials credentials, String fileNo, String hash) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = sc.writeDB(new Utf8String(fileNo), new Utf8String(hash)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> writeAsync(String addr, Credentials credentials, String fileNo, String hash) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                CompletableFuture<TransactionReceipt> future = sc.writeDB(new Utf8String(fileNo), new Utf8String(hash)).sendAsync();

                LOGGER.info("Transaction sent: " + future.toString());
                return future;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public String read(String addr, Credentials credentials, String id) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Data_sol_Data sc = Data_sol_Data.load(addr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                String hash = sc.readDB(new Utf8String(id)).send().getValue();
                LOGGER.info("Read succeed: " + hash);
                return hash;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    private Credentials signIn(String address, String password) throws IOException, CipherException {
        Resource resource = new ClassPathResource(address);
        File file = resource.getFile();
        return  WalletUtils.loadCredentials(
                password,
                file.getAbsolutePath());
    }
}
