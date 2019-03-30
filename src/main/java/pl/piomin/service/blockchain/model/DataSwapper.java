package pl.piomin.service.blockchain.model;

/**
 * @author: HuShili
 * @date: 2019/2/13
 * @description: none
 */
public class DataSwapper {

    private String id;
    private String propertyName;
    private String data;
    private String status;

    public DataSwapper() {

    }

    public DataSwapper(String id, String propertyName) {
        this(id, propertyName, null);
    }

    public DataSwapper(String id, String propertyName, String data) {
        this.id = id;
        this.propertyName = propertyName;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
