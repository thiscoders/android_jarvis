package ye.droid.jarvis.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ye on 2017/5/7.
 */

public class JHttpUtils {
    private static final String TAG = JHttpUtils.class.getSimpleName();

    /**
     * 将网络内容加载到内存中
     *
     * @param context  上下文对象
     * @param httpPath 网络地址
     * @return 获取的字符串
     */
    public static String getHttpStream(Context context, String httpPath) {
        return getHttpStream(context, httpPath, null);
    }

    /**
     * 将网络内容保存到本地
     *
     * @param context   上下文对象
     * @param httpPath  网络地址
     * @param localPath 本地保存地址
     * @return
     */
    public static String getHttpStream(final Context context, final String httpPath, final String localPath) {
        try {
            //1. 创建URL对象
            URL url = new URL(httpPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //2. 配置URL属性
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            //3. 获取服务器响应
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                if (localPath != null) {
                    //将获取的内容保存到本地
                    File deatFile = context.getExternalFilesDir("down");
                    //将流保存为文件
                    StreamUtils.stream2File(inputStream, deatFile);
                    return null;
                }
                String content = StreamUtils.stream2String(inputStream);
                return content;
            } else {
                Log.i(TAG, "获取网络内容失败，响应码是" + responseCode);
            }

        } catch (MalformedURLException e) {
            Log.w(TAG, "服务器URL格式错误，" + e.toString());
        } catch (IOException e) {
            Log.w(TAG, "服务器离线，" + e.toString());
        }
        return null;
    }
}
