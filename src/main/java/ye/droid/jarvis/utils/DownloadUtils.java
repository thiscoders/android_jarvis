package ye.droid.jarvis.utils;

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

public class DownloadUtils {
    private static final String TAG = DownloadUtils.class.getSimpleName();

    /**
     * 将网络内容转化成String返回
     * @param httpPath 网络地址
     * @return
     */
    public static String getHttpString(String httpPath) {
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
                String content = StreamUtils.stream2String(inputStream);
                return content;
            } else {
                return "JSON服务器响应码错误..." + responseCode;
            }
        } catch (MalformedURLException e) {
            Log.w(TAG, "JSON服务器URL格式错误..." + e.toString());
        } catch (IOException e) {
            Log.w(TAG, "JSON服务器飞到火星上去了..." + e.toString());
        }
        return "";
    }

    /**
     * 将网络文件下载到本地，并且返回
     *
     * @param urlPath   远程URL地址
     * @param localPath 本地保存地址
     * @return 保存成功的文件
     */
    public static File downloadHttpFile(String urlPath, String localPath) {
        File file = null;
        try {
            //1. 创建URL对象
            URL url = new URL(urlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //2. 配置URL属性
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            //3. 获取服务器响应
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                file = StreamUtils.stream2File(inputStream, new File(localPath));
            } else {
                Log.w(TAG, "APK服务器响应码码错误..." + responseCode);
            }
        } catch (MalformedURLException e) {
            Log.w(TAG, "APK服务器URL格式错误..." + e.toString());
        } catch (IOException e) {
            Log.w(TAG, "APK服务器飞到火星上去了..." + e.toString());
        }
        return file;
    }
}
