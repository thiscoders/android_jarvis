package ye.droid.jarvis.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ye.droid.jarvis.R;

/**
 * SharedPreferences工具类，方便的使用SharedPreferences获取各种数据
 * Created by ye on 2017/5/9.
 */

public class SharedPreferencesUtils {
    private static SharedPreferences sharedPreferences;

    /**
     * 保存boolean到共享参数
     *
     * @param context 上下文对象
     * @param key     键
     * @param value   值
     * @return 保存成功标记
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(context.getString(R.string.SharedPreferences_File1), Context.MODE_PRIVATE);
        return sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 从共享参数中获取boolean值
     *
     * @param context  上下文对象
     * @param key      键
     * @param defValue 默认返回值
     * @return 所获取的结果值
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(context.getString(R.string.SharedPreferences_File1), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defValue);
    }


    /**
     * 保存string到共享参数
     *
     * @param context 上下文对象
     * @param key     键
     * @param value   值
     * @return 保存成功标记
     */
    public static boolean putString(Context context, String key, String value) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(context.getString(R.string.SharedPreferences_File1), Context.MODE_PRIVATE);
        return sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 从共享参数中获取string值
     *
     * @param context  上下文对象
     * @param key      键
     * @param defValue 默认返回值
     * @return 所获取的结果值
     */
    public static String getString(Context context, String key, String defValue) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(context.getString(R.string.SharedPreferences_File1), Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defValue);
    }
}
