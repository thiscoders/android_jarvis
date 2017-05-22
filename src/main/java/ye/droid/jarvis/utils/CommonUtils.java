package ye.droid.jarvis.utils;

import android.text.TextUtils;

/**
 * Created by ye on 2017/5/9.
 */

public class CommonUtils {
    private final String TAG = CommonUtils.class.getSimpleName();

    /**
     * 检测两个字符串是否相同
     *
     * @param first 第一个字符串
     * @param second 第二个字符串
     * @return 检测结果
     */
    public static int checkString(String first, String second) {
        int res = ConstantValues.STRING_DIFF;
        if (TextUtils.isEmpty(first) && TextUtils.isEmpty(second)) { //两个字符串均为null
            res = ConstantValues.STRING_NULL;
        } else if (!first.equals(second)) { //两个字符串不相等
            res = ConstantValues.STRING_DIFF;
        } else if (first.equals(second)) { //两个字符串相同
            res = ConstantValues.STRING_MATCH;
        }
        return res;
    }
}
