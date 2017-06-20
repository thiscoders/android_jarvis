package ye.droid.jarvis;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 测试文件存储
 * Created by ye on 2017/5/26.
 */

public class FileStoreTest {

    @Test
    public void write() throws IOException {
        File dir = new File("D:\\Jarvis\\Haha");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File("D:\\Jarvis\\Haha\\location.log");

        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

        //yyyy-MM-dd HH:mm:ss E 年月日 时分秒 星期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < 100; i++) {
            writer.write(dateFormat.format(System.currentTimeMillis()) + "\t\t->\t" + (100 + i) + "\r\n");
        }
        writer.write("=======================================================================");
        writer.flush();
        writer.close();
    }
}
