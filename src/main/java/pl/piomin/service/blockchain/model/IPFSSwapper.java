package pl.piomin.service.blockchain.model;

/**
 * @author: HuShili
 * @date: 2019/3/22
 * @description: none
 */
public class IPFSSwapper {
    private String fileName;
    private String fileHash;

    public IPFSSwapper(String fileName, String fileHash) {
        this.fileHash = fileHash;
        this.fileName = fileName;
    }

    public IPFSSwapper() {}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
}
