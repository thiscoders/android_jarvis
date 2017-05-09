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
    public final static int WIDTHPIXELS = 1;
    public final static int HEIGHTPIXELS = 2;
    public final static int DENSITY = 3;
    public final static int DENSITYDPI = 4;
    public final static int SCREENWIDTHDP = 5;
    public final static int SCREENHEIGHTDP = 6;

    /**
     * 获取屏幕的某一项参数
     *
     * @param context 上下文对象
     * @param which   获取的是哪一个参数
     * @return 返回的参数的具体值
     */
    public static float getDisInfo(Context context, int which) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels; //屏幕宽度(像素)
        int height = displayMetrics.heightPixels;//屏幕高度(像素)
        float density = displayMetrics.density;//屏幕密度
        int densityDPI = displayMetrics.densityDpi; //屏幕密度dpi
        float screenWidth = JMathUtils.handleFloat(width / density, 2); //屏幕宽度(dp)
        float screenHeight = JMathUtils.handleFloat(height / density, 2);//屏幕高度(dp)
        switch (which) {
            case WIDTHPIXELS://屏幕宽度(像素)
                return width;
            case HEIGHTPIXELS://屏幕高度(像素)
                return height;
            case DENSITY://屏幕密度
                return density;
            case DENSITYDPI: //屏幕密度dpi
                return densityDPI;
            case SCREENWIDTHDP: //屏幕宽度(dp)
                return screenWidth;
            case SCREENHEIGHTDP://屏幕高度(dp)
                return screenHeight;
        }
        return -1;
    }

    /**
     * 获取屏幕的所有参数，以list的方式返回
     *
     * @param context 上下文对象
     * @return 带屏幕参数的list
     */
    public static List<String> getDisInfo(Context context) {
        List<String> list = new ArrayList<String>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels; //屏幕宽度(像素)
        int height = displayMetrics.heightPixels;//屏幕高度(像素)
        float density = displayMetrics.density;//屏幕密度
        int densityDPI = displayMetrics.densityDpi; //屏幕密度dpi
        float screenWidth = JMathUtils.handleFloat(width / density, 2); //屏幕宽度(dp)
        float screenHeight = JMathUtils.handleFloat(height / density, 2);//屏幕高度(dp)
        //1080...1758---3.375---540---320.0...520.8889 显示比较大的时候
        //1080...1806---2.375---380---454.73685...760.4211 显示最小的时候
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
