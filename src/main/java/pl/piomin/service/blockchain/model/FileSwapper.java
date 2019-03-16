package pl.piomin.service.blockchain.model;

import java.util.Map;

/**
 * @author: HuShili
 * @date: 2019/3/15
 * @description: none
 */
public class FileSwapper {
    private String fileName;
    private Map<String, String> content;

    public FileSwapper() {}

    public Map<String, String> getContent() {
        return content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void addContent(String id, String data) {
        content.put(id, data);
    }

    public String getContent(String id) {
        return content.get(id);
    }

    public int count() {
        return content.keySet().size();
    }

    public boolean contains(String id) {
        return content.containsKey(id);
    }
}
