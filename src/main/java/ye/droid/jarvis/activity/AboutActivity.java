package ye.droid.jarvis.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ye.droid.jarvis.R;
import ye.droid.jarvis.beans.UpDateBean;
import ye.droid.jarvis.utils.AppUpdateUtils;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.DialogFactory;

public class AboutActivity extends AppCompatActivity {
    private final String TAG = AboutActivity.class.getSimpleName();

    private TextView tv_splash_version_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initUI(); //初始化UI
        initData(); //初始化数据
    }

    private void initUI() {
        tv_splash_version_name = (TextView) findViewById(R.id.tv_about_version_name);
    }

    private void initData() {
        tv_splash_version_name.setText("当前版本：" + AppUpdateUtils.getVersionName(this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    /**
     * 浏览器中查看代码
     *
     * @param view
     */
    public void openGithub(View view) {
        //意图
        Intent intent = new Intent();
        //意图的行为，隐式意图
        intent.setAction(Intent.ACTION_VIEW);
        //意图的数据
        intent.setData(Uri.parse(getString(R.string.app_github_address)));
        //启动
        this.startActivity(intent);
    }

    /**
     * 检查软件更新
     *
     * @param view
     */
    public void checkUpDate(View view) {
        Toast.makeText(this, "检查更新中，请稍后...", Toast.LENGTH_SHORT).show();
        checkAppUpdate();
    }


    /**
     * 检查App更新
     */
    private void checkAppUpdate() {
        new Thread() {
            @Override
            public void run() {
                final UpDateBean upDateBean = AppUpdateUtils.haveNewVersion(AboutActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (upDateBean.getVersionInfo()) {
                            case ConstantValues.HAVE_UPDATE:
                                //弹出对话框，用户选择是否更新
                                Dialog dialog = DialogFactory.generateDialog(AboutActivity.this,
                                        R.drawable.app_update,
                                        upDateBean.getVersionInfo(),
                                        upDateBean.getVersionDesc(),
                                        null,
                                        "更新",
                                        "忽略",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                new Thread() {
                                                    @Override
                                                    public void run() {
                                                        //APK
                                                        File apk = AppUpdateUtils.downNewVersionApp(upDateBean);
                                                        if (apk == null) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(getApplicationContext(), ConstantValues.ERROR_UPDATE, Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            return;
                                                        }
                                                        installApk(apk);
                                                    }
                                                }.start();
                                            }
                                        }, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(getApplicationContext(), "忽略更新...", Toast.LENGTH_SHORT).show();
                                            }
                                        }, new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "取消更新...", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                dialog.show();
                                break;
                            case ConstantValues.NOT_UPDATE:
                                //软件已经是最新版本
                                Toast.makeText(AboutActivity.this, upDateBean.getVersionInfo(), Toast.LENGTH_SHORT).show();
                                break;
                            case ConstantValues.ERROR_UPDATE:
                                //服务器异常
                                Toast.makeText(AboutActivity.this, upDateBean.getVersionInfo(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * 安装APK
     *
     * @param file 待安装软件
     */
    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW); //开启安装软件界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //android n 及其以上需要独立设置意图过滤器
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "ye.droid.jarvis.fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else { // android n 一下不需要单独设置意图过滤器
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        //开启安装app界面
        startActivityForResult(intent, ConstantValues.CANCEL_INSTALL_UPDATE);
    }
}
