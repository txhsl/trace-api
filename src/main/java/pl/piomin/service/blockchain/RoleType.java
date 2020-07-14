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
        "生产方",
        "加工方",
        "物流方",
        "销售方",
        "消费者",
        "监管部门")
    );

    public static int getID(String roleName) {
        return Types.indexOf(roleName);
    }
}
