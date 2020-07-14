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
        "生产公司", "生产负责人", "生产日期", "重量", "生产批次", "生产检验结果", "生产许可证",

        "加工公司", "加工负责人", "加工日期", "加工批次", "加工检验结果", "加工许可证",

        "物流公司", "物流负责人", "运输日期", "车辆编号", "到货验收结果", "物流许可证",

        "销售公司", "销售负责人", "进货日期", "标价", "销售许可证")
    );

    public static int getID(String propertyName) {
        return Types.indexOf(propertyName);
    }
}
