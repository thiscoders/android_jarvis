package ye.droid.jarvis.utils;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ye on 2017/5/7.
 */

public class StreamUtils {
    private final static String TAG = StreamUtils.class.getSimpleName();

    /**
     * 将inputstream转化为string
     *
     * @param inputStream 输入流
     * @return 字符串
     * @throws IOException
     */
    public static String stream2String(InputStream inputStream) throws IOException {
        String res = "";
        int len;
        byte[] buffer = new byte[1024 * 500];
        while ((len = inputStream.read(buffer)) != -1) {
            res += new String(buffer, 0, len);
        }
        inputStream.close();
        return res;
    }

    /**
     * 将inputstream转换成文件
     *
     * @param inputStream 流对象
     * @param destFile    本地保存文件
     */
    public static File stream2File(InputStream inputStream, File destFile) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(destFile);
            int len;
            byte[] bytes = new byte[1024 * 1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return destFile;
    }
}
