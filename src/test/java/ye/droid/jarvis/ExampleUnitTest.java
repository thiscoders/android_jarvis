package ye.droid.jarvis;

import org.junit.Test;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;

import ye.droid.jarvis.utils.MD5Utils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testFloatRound() {
        float num = (float) 89.25322354455;
        BigDecimal bigDecimal = new BigDecimal(num);
        float res = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        System.out.println(res);
    }

    @Test
    public void testMD5() {
        System.out.println(MD5Utils.encodeMD5("123") + "..." + MD5Utils.encodeMD5("123").length());
        System.out.println(MD5Utils.encodeMD5("123") + "..." + MD5Utils.encodeMD5("123").length());
    }
}