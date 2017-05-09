package ye.droid.jarvis.utils;

import java.math.BigDecimal;

/**
 * Created by ye on 2017/5/9.
 */

public class JMathUtils {

    /**
     * 将float保留n位小数返回
     * @param num 源float数
     * @param limit 处理后的数
     * @return
     */
    public static float handleFloat(float num, int limit) {
        BigDecimal bigDecimal = new BigDecimal(num);
        float res = bigDecimal.setScale(limit, BigDecimal.ROUND_HALF_UP).floatValue();
        return res;
    }
}
