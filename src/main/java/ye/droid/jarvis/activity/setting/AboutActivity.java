package ye.droid.jarvis.activity.setting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ye.droid.jarvis.R;
import ye.droid.jarvis.activity.PerfectActivity;
import ye.droid.jarvis.beans.UpDateBean;
import ye.droid.jarvis.utils.AppUpdateUtils;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.DialogFactory;
import ye.droid.jarvis.utils.ServiceUtils;

public class AboutActivity extends PerfectActivity {
    private final String TAG = AboutActivity.class.getSimpleName();

    private TextView tv_splash_version_name;
    private TextView tv_service2;

    private final int MENU_CHECK_UPDATE = 0;
    private final int MENU_CHECK_SERVICE = 1;
    private final int MENU_OPEN_SOURCE = 2;

    private Handler handler;
    private int delayMillis = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initUI(); //初始化UI
        initData(); //初始化数据
    }

    private void initUI() {
        tv_splash_version_name = (TextView) findViewById(R.id.tv_about_version_name);
        tv_service2 = (TextView) findViewById(R.id.tv_service2);
    }

    private void initData() {
        handler = new Handler();
        tv_splash_version_name.setText("当前版本：" + AppUpdateUtils.getVersionName(this));
        tv_service2.setText("-_-\r\n");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preAnim();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_CHECK_UPDATE, 0, "检查更新");
        menu.add(1, MENU_CHECK_SERVICE, 1, "检查服务");
        menu.add(2, MENU_OPEN_SOURCE, 2, "打开源码");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_CHECK_UPDATE:
                checkAppUpdate();
                break;
            case MENU_CHECK_SERVICE:
                checkService2();
                break;
            case MENU_OPEN_SOURCE:
                openGithub();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkService2() {
        String res = "";
        boolean smsListenerServiceRunning = ServiceUtils.serviceIsRunning(this, "ye.droid.jarvis.service.burglars.SmsListenerService", false);
        if (smsListenerServiceRunning) {
            res += "短信监听运行中...\r\n";
        }
        boolean locationChangeServiceRunning = ServiceUtils.serviceIsRunning(this, "ye.droid.jarvis.service.burglars.LocationChangeService", false);
        if (locationChangeServiceRunning) {
            res += "位置监听运行中...\r\n";
        }
        boolean phoneAddressChangeServiceRunning = ServiceUtils.serviceIsRunning(this, "ye.droid.jarvis.service.SuspendFrameService", false);
        if (phoneAddressChangeServiceRunning) {
            res += "来电归属地悬浮框运行中...\r\n";
        }
        tv_service2.setText(res);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_service2.setText("-_-\r\n");
            }
        }, delayMillis);
    }

    /**
     * 浏览器中查看代码
     */
    public void openGithub() {
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
