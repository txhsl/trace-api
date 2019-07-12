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
        "Farm PIC", "Breed", "Grown Date", "Weigt", "Quarantine PIC", "Quarantine Result", "Farm Name", "Farm Licence",

        "Abattoir PIC", "Slaughter Date", "Quality Inspector1", "Check Result1", "Abattoir Name", "Abattoir Licence",

        "Packaging PIC", "Packaging Date", "Quality Inspector2", "Quality Result2", "Company Name", "Packaging Licence",

        "Warehousing PIC", "Entry Date", "Leaving Date", "Amount1", "Storage Company", "Storage Licence",

        "Logistics PIC", "Start Time", "Reach Time", "Amount2", "Transport Distance", "Logistics Company", "Logistics Licence", "Transport From", "Transport To",

        "Sourcing PIC", "Sourcing Date", "Processor PIC", "Production Date", "Quality Inspector3", "Quality Result3", "Processor Name", "Production License",

        "Distribution PIC", "Load Time", "Delivery Time", "Amount3", "Distribution Distance", "Distribution Company", "Distribution Licence", "Distribution From", "Distribution To",

        "Purchase PIC", "Purchase Date", "Amount4", "Retailer Company", "Retailer Licence", "Price")
    );

    public static int getID(String propertyName) {
        return Types.indexOf(propertyName);
    }
}
