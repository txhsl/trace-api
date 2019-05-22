package pl.piomin.service.blockchain;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author: HuShili
 * @date: 2019/2/13
 * @description: none
 */
public class PropertyType {

    public static ArrayList<String> Types = new ArrayList<>(
        Arrays.asList(
        "畜牧负责人", "品种", "出栏日期", "出栏重量", "畜牧检验负责人", "畜牧检验结果", "畜牧公司", "畜牧许可证",

        "屠宰负责人", "屠宰日期", "屠宰检验负责人", "屠宰检验结果", "屠宰公司", "屠宰许可证",

        "包装负责人", "包装日期", "包装检验负责人", "包装检验结果", "包装公司", "包装许可证",

        "仓储负责人", "入仓时间", "出仓时间", "仓储量", "仓储公司", "仓储许可证",

        "物流负责人", "起运时间", "到货时间", "运输量", "运输距离", "物流公司", "物流许可证", "起始地", "目的地",

        "加工进货负责人", "加工进货日期", "加工负责人", "加工日期", "加工检验负责人", "加工检验结果", "加工公司", "加工许可证",

        "二级物流负责人", "二级起运时间", "二级到货时间", "二级运输量", "二级运输距离", "二级物流公司", "二级物流许可证", "二级起始地", "二级目的地",

        "零售进货负责人", "零售进货时间", "进货量", "零售公司", "零售许可证", "零售价")
    );

    public static int getID(String propertyName) {
        return Types.indexOf(propertyName);
    }
}
