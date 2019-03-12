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
        "Producer_Operator",
        "Producer_ProduceDate",
        "Producer_OutDate",
        "Producer_GuaranteeDate",
        "Producer_TestResult",
        "Producer_BasePrice",
        "Producer_SellPrice",
        "Producer_Amount",
        "Storager_Operator",
        "Storager_InTime",
        "Storager_OutTime",
        "Storager_Price",
        "Storager_Duration",
        "Storager_Amount",
        "Storager_Company",
        "Transporter_Operator",
        "Transporter_OutTime",
        "Transporter_Price",
        "Transporter_Amount",
        "Transporter_Distance",
        "Transporter_Company",
        "Transporter_From",
        "Transporter_To",
        "Seller_Operator",
        "Seller_InTime",
        "Seller_InPrice",
        "Seller_Amount",
        "Seller_Company",
        "Seller_OutPrice",
        "Buyer_Name",
        "Buyer_Mobile",
        "Buyer_Time",
        "Buyer_Amount",
        "Buyer_Price")
    );

    public static int getID(String propertyName) {
        return Types.indexOf(propertyName);
    }
}
