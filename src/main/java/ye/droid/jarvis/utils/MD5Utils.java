package ye.droid.jarvis.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ye on 2017/5/11.
 */

public class MD5Utils {
    public static String encodeMD5(String str) {
        String res = null;
        if (str == null || str.equals("")) {
            return res;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes("UTF-8"));
            res = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() < 2)
                    temp = "0" + temp;
                res += temp;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }
}
