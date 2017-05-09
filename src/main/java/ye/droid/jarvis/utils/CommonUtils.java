package ye.droid.jarvis.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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
import ye.droid.jarvis.activity.SplashActivity;

/**
 * Created by ye on 2017/5/9.
 */

public class CommonUtils {
    private final String TAG = CommonUtils.class.getSimpleName();

    public static void startUpdate(Context context, String mUpdateAddress) {

    }

}
