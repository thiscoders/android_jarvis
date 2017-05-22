package ye.droid.jarvis.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import ye.droid.jarvis.beans.UpDateBean;

/**
 * Created by ye on 2017/5/22.
 */

public class AppUpdateUtils {
    private final static String TAG = AppUpdateUtils.class.getSimpleName();
    //更新相关
    private static String mUpdateAddress = ConstantValues.UPDATE_ADDRESS; //更新地址
    private static int mLocalVersionCode; //本地版本号

    /**
     * 获取更新版本的信息，并且封装成一个javabean返回
     *
     * @param context 上下文
     * @return 更新包详细信息的JavaBean
     */
    public static UpDateBean haveNewVersion(Context context) {
        String content = DownloadUtils.getHttpString(mUpdateAddress);
        UpDateBean upDateBean;
        try {
            //解析服务器返回的Json字符串
            JSONObject jsonObject = new JSONObject(content);
            String versionName = jsonObject.getString("versionName"); //获取新版本名称
            String versionCode = jsonObject.getString("versionCode"); //获取新版本编号
            String versionDesc = jsonObject.getString("versionDesc"); //获取新版本描述
            String downloadURL = jsonObject.getString("downloadURL");//获取新版本下载的地址

            upDateBean = new UpDateBean(versionName, versionCode, versionDesc, downloadURL, "");

            mLocalVersionCode = getVersionCode(context);
            if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                upDateBean.setVersionInfo(ConstantValues.HAVE_UPDATE);
            } else {
                upDateBean.setVersionInfo(ConstantValues.NOT_UPDATE);
            }
            return upDateBean;
        } catch (JSONException e) {
            Log.i(TAG, "服务器Json出错..." + e.toString());
            upDateBean = new UpDateBean();
            upDateBean.setVersionInfo(ConstantValues.ERROR_UPDATE);
            return upDateBean;
        }
    }

    /**
     * @param upDateBean 封装了新版本信息的JavaBean
     * @return 下载得文件
     */
    public static File downNewVersionApp(UpDateBean upDateBean) {
        File file = null;
        //1. 判断sd卡是否挂载可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //2. 获取下载路径
            String path = Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator + "jarvis.apk";
            file = DownloadUtils.downloadHttpFile(upDateBean.getDownloadURL(), path);//核心方法
        } else {
            Log.i(TAG, "SD卡未挂载！");
        }
        return file;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本机App版本号
     *
     * @return 返回0获取失败，否则就获取成功
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
