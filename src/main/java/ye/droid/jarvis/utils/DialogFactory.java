package ye.droid.jarvis.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;


/**
 * Created by ye on 2017/5/7.
 */

public class DialogFactory {

    /**
     * 生成对话框
     *
     * @param context       上下文对象
     * @param icon          弹框图标
     * @param title         弹框标题
     * @param content       弹框内容
     * @param view          弹框显示控件(可选)
     * @param positiveValue 确定按钮上的值
     * @param negativeValue 取消按钮上的值(可选)
     * @param positive      确定按钮触发的事件
     * @param negative      取消按钮触发的事件(可选)
     * @return 设置完成的对话框，待返回
     */
    public static AlertDialog generateDialog(Context context, int icon, String title, String content, View view, String positiveValue, String negativeValue, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative, DialogInterface.OnCancelListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setIcon(icon);
        if (view != null)
            builder.setView(view);
        //添加响应事件
        builder.setPositiveButton(positiveValue, positive);
        if (negative != null)
            builder.setNegativeButton(negativeValue, negative);
        if (cancelListener != null)
            builder.setOnCancelListener(cancelListener);
        return builder.create();
    }
}


