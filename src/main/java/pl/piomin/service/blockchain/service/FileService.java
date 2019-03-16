package pl.piomin.service.blockchain.service;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.piomin.service.blockchain.model.FileSwapper;

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
    private static final int CACHE_SIZE = 100;

    private final IPFS ipfs;

    private Map<String, FileSwapper> cache = new HashMap<>();

    public FileService() {
        ipfs = new IPFS(IPFS_URL);
    }

    public String record(String propertyName, String id, String value) throws IOException {
        FileSwapper file;
        if (cache.containsKey(propertyName)) {
             file = cache.get(propertyName);
        }
        else {
            file = new FileSwapper();
            file.setFileName(id);   //Name with the first record
        }
        file.addContent(id, value);
        if (file.count() >= CACHE_SIZE) {
            cache.remove(propertyName);
            return upload(output(file));
        }
        cache.put(propertyName, file);
        return null;
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

    public String output(FileSwapper data) throws IOException {
        File target = new File(data.getFileName());
        FileOutputStream outStream = new FileOutputStream(target);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
        objectOutputStream.writeObject(data);

        outStream.close();
        LOGGER.info("File created: " + target.getPath());
        return target.getPath();
    }

    public FileSwapper input(File file) throws IOException,ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        LOGGER.info("File loaded: " + file.getPath());
        return (FileSwapper) objectInputStream.readObject();
    }

    public String upload(String path) throws IOException {
        File target = new File(path);
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(target);
        MerkleNode addResult = ipfs.add(file).get(0);
        String hash = addResult.hash.toString();

        //Rename the file
        target.renameTo(new File(target.getParentFile().getAbsolutePath() + "/" + hash));
        LOGGER.info("File uploaded, hash: " + hash);
        return hash;
    }

    public File download(String hash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(hash);
        byte[] data = ipfs.cat(filePointer);
        if(data != null){
            File file = new File(hash);
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
}
