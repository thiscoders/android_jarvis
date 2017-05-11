package ye.droid.jarvis.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import ye.droid.jarvis.BuildConfig;
import ye.droid.jarvis.R;
import ye.droid.jarvis.activity.HomeActivity;
import ye.droid.jarvis.activity.SplashActivity;

/**
 * Created by ye on 2017/5/9.
 */

public class CommonUtils {
    private final String TAG = CommonUtils.class.getSimpleName();

    public static void startUpdate(Context context, String mUpdateAddress) {

    }


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
