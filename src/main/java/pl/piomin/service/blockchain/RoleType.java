package pl.piomin.service.blockchain;

/**
 * @author: HuShili
 * @date: 2019/2/18
 * @description: none
 */
public class RoleType {

    public enum Types {
        Producer,
        Storager,
        Transporter,
        Seller,
        Buyer,
        Government
    }

    public static int getID(String roleName) {
        return Types.valueOf(roleName).ordinal();
    }
}
