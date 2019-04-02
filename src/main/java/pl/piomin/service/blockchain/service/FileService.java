package pl.piomin.service.blockchain.service;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.piomin.service.blockchain.model.FileSwapper;
import pl.piomin.service.blockchain.model.TaskSwapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: HuShili
 * @date: 2019/3/15
 * @description: none
 */

@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    private static final String IPFS_URL = "/ip4/127.0.0.1/tcp/5001";

    private final IPFS ipfs;
    private final File folder;

    private Map<String, FileSwapper> cache = new HashMap<>();

    public FileService() {
        ipfs = new IPFS(IPFS_URL);
        folder = new File("data");

        try {
            LOGGER.info("IPFS connected, version: " + ipfs.version());
        } catch (IOException e){
            LOGGER.error("Connection failed. " + e.getMessage());
        }

        if (!folder.exists()) {
           folder.mkdir();
           LOGGER.info("Data folder created, path: " + folder.getAbsolutePath());
        }
    }

    public TaskSwapper record(String propertyName, String fileName, String id, String value) throws IOException {
        FileSwapper file;
        TaskSwapper task = null;
        if (cache.containsKey(propertyName)) {
             file = cache.get(propertyName);
        }
        else {
            file = new FileSwapper();
            file.setFileName(fileName);
        }

        if (!file.getFileName().equals(fileName)) {
            cache.remove(propertyName);
            task = new TaskSwapper(file.getFileName(), upload(output(file)));
            file = new FileSwapper();
            file.setFileName(fileName);
        }
        file.addContent(id, value);
        cache.put(propertyName, file);

        return task;
    }

    public String query(String propertyName, String id) {
        if (cache.containsKey(propertyName) && cache.get(propertyName).contains(id)) {
            return cache.get(propertyName).getContent(id);
        }
        return null;
    }


    public static String query(FileSwapper data, String id) {
        if (data.contains(id)) {
            return data.getContent(id);
        }
        return null;
    }

    private String output(FileSwapper data) throws IOException {
        File target = new File(folder.getPath() + '\\' + data.getFileName());
        if(target.exists()){
            LOGGER.info("File already existed, path: " + target.getAbsolutePath());
            return target.getPath();
        }
        if (target.createNewFile()) {
            LOGGER.info("File created: " + target.getAbsolutePath());
        }
        FileOutputStream outStream = new FileOutputStream(target);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
        objectOutputStream.writeObject(data);

        outStream.close();
        return target.getAbsolutePath();
    }

    public FileSwapper input(File file) throws IOException,ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        LOGGER.info("File loaded: " + file.getPath());
        return (FileSwapper) objectInputStream.readObject();
    }

    private String upload(String path) throws IOException {
        File target = new File(path);
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(target);
        MerkleNode addResult = ipfs.add(file).get(0);
        String hash = addResult.hash.toString();

        //Rename the file
        if(target.renameTo(new File(folder.getPath() + '\\' + hash))) {
            LOGGER.info("File uploaded, hash: " + hash);
            return hash;
        }
        else {
            LOGGER.error("File upload failed, already existed, path: " + path);
            return hash;
        }
    }

    public File download(String hash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(hash);
        byte[] data = ipfs.cat(filePointer);
        if(data != null){
            File file = new File(folder.getPath() + '\\' +hash);
            if(file.exists()){
                LOGGER.info("File already existed, path: " + file.getPath());
                return file;
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data, 0, data.length);
            fos.flush();
            fos.close();
            LOGGER.info("File downloaded, hash: " + hash + ", path: " + file.getPath());
            return file;
        }
        else {
            LOGGER.error("File not exist, hash: " + hash);
            return null;
        }
    }

    public boolean checkFile(String fileName, String expectedName) {
        if (fileName.equals(expectedName)) {
            LOGGER.info("File checked, name: " + fileName);
            return true;
        }
        else {
            LOGGER.error("File check failed, name: " + fileName + ", expected: " + expectedName);
            return false;
        }
    }
}
