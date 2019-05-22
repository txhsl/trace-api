package pl.piomin.service.blockchain;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author: HuShili
 * @date: 2019/2/18
 * @description: none
 */
public class RoleType {

    public static ArrayList<String> Types = new ArrayList<>(
        Arrays.asList(
        "畜牧方",
        "屠宰方",
        "包装方",
        "仓储方",
        "物流方",
        "二级加工",
        "二级物流",
        "零售方",
        "消费者",
        "监管部门")
    );

    public static int getID(String roleName) {
        return Types.indexOf(roleName);
    }
}
