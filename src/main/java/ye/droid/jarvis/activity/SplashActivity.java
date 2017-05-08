package ye.droid.jarvis.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import ye.droid.jarvis.BuildConfig;
import ye.droid.jarvis.R;
import ye.droid.jarvis.utils.DialogFactory;
import ye.droid.jarvis.utils.DisplayUtils;
import ye.droid.jarvis.utils.JHttpUtils;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = SplashActivity.class.getSimpleName();

    private String mUpdateAddress = "http://192.168.191.2:8080/jarvis/jarvis.json";
    private TextView tv_splash_version_name;
    private TextView tv_server_message;

    private int mLocalVersionCode; //本地版本号

    private Handler handler;

    private long splash_dau = 3000;
    private long startTime;
    private long endTime;
    private long dua;


    private int REQUEST_PERMISSION_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //开启splash界面计时器
        startTime = System.currentTimeMillis();

        handler = new Handler();

        //初始化UI
        initUI();
    }

    public void test(View view) {
        String disInfo = "";
        int flag = 0;
        List<String> list = DisplayUtils.getDisInfo(this);
        for (String info : list) {
            disInfo += info;
            flag++;
            if (flag != 6)
                disInfo += "---";
        }
        Toast.makeText(this, "屏幕信息：\r\n" + disInfo, Toast.LENGTH_LONG).show();
    }

    /**
     * 初始化UI，寻找控件
     */
    private void initUI() {
        //1. 寻找控件并赋值
        tv_splash_version_name = (TextView) findViewById(R.id.tv_splash_version_name);
        String versionName = getVersionName();
        tv_splash_version_name.setText("当前版本:" + versionName);

        tv_server_message = (TextView) findViewById(R.id.tv_server_message);
        //1.5 检查APP权限
        checkPermission();
        //2. 检查软件更新
        checkUpdate();
    }

    /**
     * 获取当前软件版本名称
     *
     * @return
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @return 返回0获取失败，否则就获取成功
     */
    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 检查并申请权限
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        } else {
            Log.i(TAG, "读SD卡权限已经授予！");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "权限已经获取..." + requestCode);
    }

    /**
     * 检查软件更新
     */
    private void checkUpdate() {
        new Thread() {
            @Override
            public void run() {

                String jsonzContent = JHttpUtils.getHttpStream(SplashActivity.this, mUpdateAddress);
                //1. 获取更新信息失败，3s后延时进入主界面
                if (jsonzContent == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_server_message.setTextColor(Color.YELLOW);
                            tv_server_message.setText("维护服务器离线.即将进入主界面...");
                            enterHome(splash_dau); //entrthome1
                        }
                    });
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(jsonzContent);
                    final String versionName = jsonObject.getString("versionName");
                    String versionCode = jsonObject.getString("versionCode");
                    final String versionDesc = jsonObject.getString("versionDesc");
                    final String downloadURL = jsonObject.getString("downloadURL");

                    mLocalVersionCode = getVersionCode();
                    if (mLocalVersionCode < Integer.parseInt(versionCode))
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv_version_desc = new TextView(SplashActivity.this);
                                tv_version_desc.setPadding(60, tv_version_desc.getPaddingTop(), tv_version_desc.getPaddingRight(), tv_version_desc.getPaddingBottom());
                                tv_version_desc.setTextSize(18);
                                tv_version_desc.setText(versionDesc);

                                Dialog updateDialog = DialogFactory.generateDialog(SplashActivity.this, R.drawable.app_update, "更新", "检测到可用的更新！新版本名称：" + versionName, tv_version_desc, "更新", "忽略", new DialogInterface.OnClickListener() {
                                    //开始更新点击事件
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //使用xutils框架下载软件安装包，并进行安装
                                        //1. 判断sd卡是否挂载可用
                                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                            //2. 获取下载路径
                                            String path = Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator + "jarvis.apk";
                                            HttpUtils httpUtils = new HttpUtils();

                                            httpUtils.download(downloadURL, path, new RequestCallBack<File>() {
                                                @Override
                                                public void onSuccess(ResponseInfo<File> responseInfo) {
                                                    File apkFile = responseInfo.result;
                                                    Log.i(TAG, "onSuccess..." + apkFile.getAbsolutePath() + "..." + apkFile.length() + "..." + Environment.getExternalStorageDirectory() + "..." + BuildConfig.APPLICATION_ID);
                                                    installApk(apkFile);
                                                }

                                                @Override
                                                public void onFailure(HttpException e, String s) {
                                                    Log.i(TAG, "onFailure... 下载失败，进入主界面！");
                                                    endTime = System.currentTimeMillis();
                                                    dua = endTime - startTime;
                                                    if (dua < splash_dau) {
                                                        enterHome(splash_dau - dua);
                                                    } else {
                                                        enterHome(splash_dau);
                                                    }
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getApplicationContext(), "下载更新包失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onStart() {
                                                    super.onStart();
                                                    Log.i(TAG, "onStart");
                                                }

                                                @Override
                                                public void onLoading(long total, long current, boolean isUploading) {
                                                    super.onLoading(total, current, isUploading);
                                                    Log.i(TAG, total + "...onLoading..." + current);
                                                }
                                            });
                                        }
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    //3. 忽略更新点击事件,3s后延时进入主界面
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        endTime = System.currentTimeMillis();
                                        dua = endTime - startTime;
                                        if (dua < splash_dau) {
                                            enterHome(splash_dau - dua);
                                        } else {
                                            enterHome(splash_dau);
                                        }
                                    }
                                }, new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        dialog.dismiss();
                                        endTime = System.currentTimeMillis();
                                        dua = endTime - startTime;
                                        if (dua < splash_dau) {
                                            enterHome(splash_dau - dua);
                                        } else {
                                            enterHome(splash_dau);
                                        }
                                    }
                                });
                                updateDialog.show();
                            }
                        });
                    else {
                        //2. 已经是最新版本了，3s后直接延时进入主界面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                enterHome(splash_dau);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 延时进入主界面
     */
    private void enterHome(long delayed) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, delayed);
    }

    /**
     * 安装APK
     *
     * @param file 待安装软件
     */
    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "ye.droid.jarvis.fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100)
            enterHome(1000);
    }

}
