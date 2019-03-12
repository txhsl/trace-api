package pl.piomin.service.blockchain.model;

import java.util.Map;

/**
 * @author: HuShili
 * @date: 2019/2/13
 * @description: none
 */
public class DataMultipleSwapper {

    private String[] ids;

    private String[] propertyNames;

    private Map<String, Map<String, String>> data;

    public DataMultipleSwapper() {

    }

    public Map<String, Map<String, String>>getData() {
        return data;
    }

    public String[] getIds() {
        return ids;
    }

    public String[] getPropertyNames() {
        return propertyNames;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public void setPropertyNames(String[] propertyNames) {
        this.propertyNames = propertyNames;
    }
}
