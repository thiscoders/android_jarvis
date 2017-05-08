package ye.droid.jarvis.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ye on 2017/5/8.
 */

public class DisplayUtils {
    private final static String TAG = DisplayUtils.class.getSimpleName();

    public static List<String> getDisInfo(Context context) {
        List<String> list = new ArrayList<String>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels; //屏幕宽度(像素)
        int height = displayMetrics.heightPixels;//屏幕高度(像素)
        float density = displayMetrics.density;//屏幕密度
        int densityDPI = displayMetrics.densityDpi; //屏幕密度dpi
        float screenWidth = width / density; //屏幕宽度(dp)
        float screenHeight = height / density;//屏幕高度(dp)

        list.add(width + "");
        list.add(height + "");
        list.add(density + "");
        list.add(densityDPI + "");
        list.add(screenWidth + "");
        list.add(screenHeight + "");


        Log.i(TAG, width + "..." + height + "---" + density + "---" + densityDPI + "---" + screenWidth + "..." + screenHeight);

        return list;
    }

}
