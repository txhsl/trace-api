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
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.contract.Data_sol_Data;
import pl.piomin.service.blockchain.contract.Role_sol_Role;
import pl.piomin.service.blockchain.contract.System_sol_System;
import pl.piomin.service.blockchain.model.Message;
import pl.piomin.service.blockchain.model.TaskSwapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static pl.piomin.service.blockchain.model.CustomGasProvider.GAS_LIMIT;
import static pl.piomin.service.blockchain.model.CustomGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
@Service
public class UserService {

    private class Info {
        private String property;
        private String target;
        private boolean isRead;
        private String sender;

        private Info(String property, String target, boolean isRead, String sender) {
            this.property = property;
            this.target = target;
            this.isRead = isRead;
            this.sender = sender;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    public static final String[] accounts = new String[]{"0x6a2fb5e3bf37f0c3d90db4713f7ad4a3b2c24111", "0x38a5d4e63bbac1af0eba0d99ef927359ab8d7293", "0x40b00de2e7b694b494022eef90e874f5e553f996",
            "0x49e2170e0b1188f2151ac35287c743ee60ea1f6a", "0x86dec6586bfa1dfe303eafbefee843919b543fd3", "0x135b8fb39d0f06ea1f2466f7e9f39d3136431480", "0x329b81e0a2af215c7e41b32251ae4d6ff1a83e3e",
            "0x370287edd5a5e7c4b0f5e305b00fe95fc702ce47", "0x5386787c9ef76a235d27f000170abeede038a3db", "0xb41717679a04696a2aaac280d9d45ddd3760ff47", "0xcdfea5a11062fab4cf4c2fda88e32fc6f7753145"};

    private final Web3j web3j;
    private final int REQUEST_LIMIT = 10;
    private Credentials current = null;
    private Map<Info, RemoteCall<TransactionReceipt>> resetList = new HashMap<>();

    public UserService(Web3j web3j) {
        this.web3j = web3j;
    }

    private void sendTx() throws InterruptedException {
        for (Info info : resetList.keySet()) {
            Thread.sleep(2000);

            TaskSwapper permissionTask = new TaskSwapper(info.property, Message.Type.Permission.toString(), info.sender);
            permissionTask.setFuture(resetList.get(info).sendAsync());
            BlockchainService.addPending(permissionTask);
            LOGGER.info("A tx sent. Total " + resetList.size());
        }
        resetList.clear();
    }

    public Credentials getCurrent() {
        return current;
    }

    public boolean reset(String sysAddr) throws Exception {
        //link RCs and SCs
        if (signIn(accounts[1], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr,"生产方", "生产公司");
            resetWriterAsync(sysAddr, "生产方", "生产公司");
            
            resetReaderAsync(sysAddr, "生产方", "生产负责人");
            resetWriterAsync(sysAddr, "生产方", "生产负责人");
            
            resetReaderAsync(sysAddr, "生产方", "生产日期");
            resetWriterAsync(sysAddr, "生产方", "生产日期");
            
            resetReaderAsync(sysAddr, "生产方", "重量");
            resetWriterAsync(sysAddr, "生产方", "重量");
            
            resetReaderAsync(sysAddr, "生产方", "生产批次");
            resetWriterAsync(sysAddr, "生产方", "生产批次");
            
            resetReaderAsync(sysAddr, "生产方", "生产检验结果");
            resetWriterAsync(sysAddr, "生产方", "生产检验结果");
            
            resetReaderAsync(sysAddr, "生产方", "生产许可证");

            resetReaderAsync(sysAddr, "加工方", "生产公司");
            resetReaderAsync(sysAddr, "加工方", "生产日期");
            resetReaderAsync(sysAddr, "加工方", "重量");
            resetReaderAsync(sysAddr, "加工方", "生产批次");
            resetReaderAsync(sysAddr, "加工方", "生产检验结果");
            resetReaderAsync(sysAddr, "加工方", "生产许可证");

            resetReaderAsync(sysAddr, "消费者", "生产公司");
            resetReaderAsync(sysAddr, "消费者", "生产日期");
            resetReaderAsync(sysAddr, "消费者", "重量");
            resetReaderAsync(sysAddr, "消费者", "生产批次");
            resetReaderAsync(sysAddr, "消费者", "生产检验结果");
            resetReaderAsync(sysAddr, "消费者", "生产许可证");

            resetReaderAsync(sysAddr, "监管部门", "生产公司");
            resetReaderAsync(sysAddr, "监管部门", "生产负责人");
            resetReaderAsync(sysAddr, "监管部门", "生产日期");
            resetReaderAsync(sysAddr, "监管部门", "重量");
            resetReaderAsync(sysAddr, "监管部门", "生产批次");
            resetReaderAsync(sysAddr, "监管部门", "生产检验结果");
            resetReaderAsync(sysAddr, "监管部门", "生产许可证");

            resetWriterAsync(sysAddr, "监管部门", "生产许可证");
        }


        if (signIn(accounts[2], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr, "加工方", "加工公司");
            resetWriterAsync(sysAddr, "加工方", "加工公司");
            
            resetReaderAsync(sysAddr, "加工方", "加工负责人");
            resetWriterAsync(sysAddr, "加工方", "加工负责人");
            
            resetReaderAsync(sysAddr, "加工方", "加工日期");
            resetWriterAsync(sysAddr, "加工方", "加工日期");
            
            resetReaderAsync(sysAddr, "加工方", "加工批次");
            resetWriterAsync(sysAddr, "加工方", "加工批次");
            
            resetReaderAsync(sysAddr, "加工方", "加工检验结果");
            resetWriterAsync(sysAddr, "加工方", "加工检验结果");
            
            resetReaderAsync(sysAddr, "加工方", "加工许可证");

            resetReaderAsync(sysAddr, "物流方", "加工公司");
            resetReaderAsync(sysAddr, "物流方", "加工日期");
            resetReaderAsync(sysAddr, "物流方", "加工批次");
            resetReaderAsync(sysAddr, "物流方", "加工检验结果");
            resetReaderAsync(sysAddr, "物流方", "加工许可证");

            resetReaderAsync(sysAddr, "消费者", "加工公司");
            resetReaderAsync(sysAddr, "消费者", "加工日期");
            resetReaderAsync(sysAddr, "消费者", "加工批次");
            resetReaderAsync(sysAddr, "消费者", "加工检验结果");
            resetReaderAsync(sysAddr, "消费者", "加工许可证");

            resetReaderAsync(sysAddr, "监管部门", "加工公司");
            resetReaderAsync(sysAddr, "监管部门", "加工负责人");
            resetReaderAsync(sysAddr, "监管部门", "加工日期");
            resetReaderAsync(sysAddr, "监管部门", "加工批次");
            resetReaderAsync(sysAddr, "监管部门", "加工检验结果");
            resetReaderAsync(sysAddr, "监管部门", "加工许可证");

            resetWriterAsync(sysAddr, "监管部门", "加工许可证");
        }


        if (signIn(accounts[3], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr, "物流方", "物流公司");
            resetWriterAsync(sysAddr, "物流方", "物流公司");
            
            resetReaderAsync(sysAddr, "物流方", "物流负责人");
            resetWriterAsync(sysAddr, "物流方", "物流负责人");
            
            resetReaderAsync(sysAddr, "物流方", "运输日期");
            resetWriterAsync(sysAddr, "物流方", "运输日期");
            
            resetReaderAsync(sysAddr, "物流方", "车辆编号");
            resetWriterAsync(sysAddr, "物流方", "车辆编号");
            
            resetReaderAsync(sysAddr, "物流方", "到货验收结果");
            resetWriterAsync(sysAddr, "物流方", "到货验收结果");
            
            resetReaderAsync(sysAddr, "物流方", "物流许可证");

            resetReaderAsync(sysAddr, "销售方", "物流公司");
            resetReaderAsync(sysAddr, "销售方", "运输日期");
            resetReaderAsync(sysAddr, "销售方", "车辆编号");
            resetReaderAsync(sysAddr, "销售方", "到货验收结果");
            resetReaderAsync(sysAddr, "销售方", "物流许可证");

            resetReaderAsync(sysAddr, "消费者", "物流公司");
            resetReaderAsync(sysAddr, "消费者", "运输日期");
            resetReaderAsync(sysAddr, "消费者", "车辆编号");
            resetReaderAsync(sysAddr, "消费者", "到货验收结果");
            resetReaderAsync(sysAddr, "消费者", "物流许可证");

            resetReaderAsync(sysAddr, "监管部门", "物流公司");
            resetReaderAsync(sysAddr, "监管部门", "物流负责人");
            resetReaderAsync(sysAddr, "监管部门", "运输日期");
            resetReaderAsync(sysAddr, "监管部门", "车辆编号");
            resetReaderAsync(sysAddr, "监管部门", "到货验收结果");
            resetReaderAsync(sysAddr, "监管部门", "物流许可证");

            resetWriterAsync(sysAddr, "监管部门", "物流许可证");
        }


        if (signIn(accounts[4], "Innov@teD@ily1")) {
            resetReaderAsync(sysAddr, "销售方", "销售公司");
            resetWriterAsync(sysAddr, "销售方", "销售公司");

            resetReaderAsync(sysAddr, "销售方", "销售负责人");
            resetWriterAsync(sysAddr, "销售方", "销售负责人");

            resetReaderAsync(sysAddr, "销售方", "进货日期");
            resetWriterAsync(sysAddr, "销售方", "进货日期");

            resetReaderAsync(sysAddr, "销售方", "标价");
            resetWriterAsync(sysAddr, "销售方", "标价");

            resetReaderAsync(sysAddr, "销售方", "销售许可证");

            resetReaderAsync(sysAddr, "消费者", "销售公司");
            resetReaderAsync(sysAddr, "消费者", "进货日期");
            resetReaderAsync(sysAddr, "消费者", "标价");
            resetReaderAsync(sysAddr, "消费者", "销售许可证");

            resetReaderAsync(sysAddr, "监管部门", "销售公司");
            resetReaderAsync(sysAddr, "监管部门", "销售负责人");
            resetReaderAsync(sysAddr, "监管部门", "进货日期");
            resetReaderAsync(sysAddr, "监管部门", "标价");
            resetReaderAsync(sysAddr, "监管部门", "销售许可证");

            resetWriterAsync(sysAddr, "监管部门", "销售许可证");
        }

        sendTx();
        return true;
    }

    public boolean signIn(String address, String password) throws IOException, CipherException {
        //Resource resource = new ClassPathResource(address);
        //File file = resource.getFile();
        //current = WalletUtils.loadCredentials(password, file.getAbsolutePath());
        current = WalletUtils.loadCredentials(password, address);
        return true;
    }

    public String getAdmin(String rcAddr) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                Role_sol_Role rc = Role_sol_Role.load(rcAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                String owner = rc.getAdmin().send().getValue();
                LOGGER.info("Read succeed: " + owner);
                return owner;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt assignWriter(String sysAddr, String roleName, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.assignWriter(new Utf8String(propertyName), new Utf8String(roleName)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    private void resetWriterAsync(String sysAddr, String roleName, String propertyName){
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                RemoteCall<TransactionReceipt> tx = system.assignWriter(new Utf8String(propertyName), new Utf8String(roleName));
                resetList.putIfAbsent(new Info(propertyName, roleName, false, current.getAddress()), tx);
                return;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    private void resetReaderAsync(String sysAddr, String roleName, String propertyName){
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                RemoteCall<TransactionReceipt> tx = system.assignReader(new Utf8String(propertyName), new Utf8String(roleName));
                resetList.putIfAbsent(new Info(propertyName, roleName, true, current.getAddress()), tx);
                return;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> assignWriterAsync(String sysAddr, String roleName, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                return system.assignWriter(new Utf8String(propertyName), new Utf8String(roleName)).sendAsync();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    private TransactionReceipt assignReader(String sysAddr, String roleName, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.assignReader(new Utf8String(propertyName), new Utf8String(roleName)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> assignReaderAsync(String sysAddr, String roleName, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                return system.assignReader(new Utf8String(propertyName), new Utf8String(roleName)).sendAsync();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public boolean checkWriter(String sysAddr, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                Boolean isWriter = system.checkWriter(new Utf8String(propertyName), new Address(current.getAddress())).send().getValue();

                LOGGER.info("Check writer succeed: " + isWriter);
                return isWriter;
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

    public boolean checkReader(String sysAddr, String propertyName) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                Boolean isReader = system.checkReader(new Utf8String(propertyName), new Address(current.getAddress())).send().getValue();

                LOGGER.info("Check reader succeed: " + isReader);
                return isReader;
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

    public Map<String, String> getAdministratedAll(String sysAddr, String userAddr) throws Exception {
        Map<String, String> result = new HashMap<>();
        System_sol_System system = System_sol_System.load(sysAddr, web3j, current, GAS_PRICE, GAS_LIMIT);

        for (String property : PropertyType.Types) {
            boolean flag = false;
            int count = 0;

            while(!flag && count < REQUEST_LIMIT) {
                try {
                    String scAddr = system.getSC(new Utf8String(property)).send().getValue();
                    Data_sol_Data sc = Data_sol_Data.load(scAddr, web3j, current, GAS_PRICE, GAS_LIMIT);
                    if (sc.getAdmin().send().getValue().equals(userAddr)) {
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
