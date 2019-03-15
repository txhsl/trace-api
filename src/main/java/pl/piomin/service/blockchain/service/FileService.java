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

    public FileService() {
        ipfs = new IPFS(IPFS_URL);
    }

    public String write(FileSwapper data, String name) throws IOException {
        File target = new File(name);
        FileOutputStream outStream = new FileOutputStream(target);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
        objectOutputStream.writeObject(data);

        outStream.close();
        return target.getPath();
    }

    public FileSwapper read(String path) throws IOException,ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return (FileSwapper) objectInputStream.readObject();
    }

    public String upload(String path) throws IOException {
        File target = new File(path);
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(target);
        MerkleNode addResult = ipfs.add(file).get(0);
        String hash = addResult.hash.toString();

        //Rename the file
        target.renameTo(new File(target.getParentFile().getAbsolutePath() + "/" + hash));
        return hash;
    }

    public void download(String hash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(hash);
        byte[] data = ipfs.cat(filePointer);
        if(data != null){
            File file  = new File(hash);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data, 0, data.length);
            fos.flush();
            fos.close();
        }
    }
}
