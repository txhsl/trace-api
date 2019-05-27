package pl.piomin.service.blockchain.model;

/**
 * @author: HuShili
 * @date: 2019/5/27
 * @description: none
 */

public class Report {
    private String reporter;
    private String target;
    private int amount;
    private String to;

    public Report() {}

    public String getTarget() {
        return target;
    }

    public int getAmount() {
        return amount;
    }

    public String getReporter() {
        return reporter;
    }

    public String getTo() {
        return to;
    }
}
