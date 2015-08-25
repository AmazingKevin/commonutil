package cn.ibona.commonutil.utils;

import java.security.MessageDigest;

/**
 * Created by Kevin on 2015/8/25.
 */
public class Md5Util {

    ////////////// ////////////// //////////////  ////////////// ////////////// //////////////  ////////////// ////////////// //////////////

//todo md5加密
    /**
     * md5加密
     * @param string
     * @return
     * @throws Exception
     */
    public static   String encode(String string) throws Exception {
        byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
