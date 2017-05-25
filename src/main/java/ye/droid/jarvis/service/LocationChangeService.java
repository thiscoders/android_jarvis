package ye.droid.jarvis.service;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import ye.droid.jarvis.beans.LocationBean;
import ye.droid.jarvis.utils.BurglarsSmsUtils;
import ye.droid.jarvis.utils.ConstantValues;
import ye.droid.jarvis.utils.SharedPreferencesUtils;

/**
 * Created by ye on 2017/5/26.
 */

public class LocationChangeService extends Service {
    private String TAG = LocationChangeService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        final String best = locationManager.getBestProvider(criteria, true);

        // TODO: 2017/5/26 将位置信息保存到sdcard的日志中
        final File locLog = new File("/sdcard/Jarvis/Log/location.log");
        File dir = new File("/sdcard/Jarvis/Log");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!locLog.exists()) {
            try {
                locLog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //yyyy-MM-dd HH:mm:ss E 年月日 时分秒 星期
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        //神奇的代码
        // TODO: 2017/5/26 弄懂这个注解
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(best, 0, 0, new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();//经度
                double latitude = location.getLatitude();//纬度
                double altitude = location.getAltitude();//海拔

                LocationBean locationBean = new LocationBean();
                locationBean.setLongitude(longitude);
                locationBean.setLatitude(latitude);
                locationBean.setAltitude(altitude);

                //记录位置信息
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new FileWriter(locLog, true));
                    writer.write(dateFormat.format(System.currentTimeMillis()) + "\t\t" + locationBean.toString() + "\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                Log.i(TAG, "location is ..." + locationBean.toString());
                String safePhone = SharedPreferencesUtils.getString(getApplicationContext(), ConstantValues.CONTACT_PHONEV2, "");//获取安全联系人的电话号码
                BurglarsSmsUtils.sendSms(safePhone, locationBean.toString());
                Toast.makeText(getApplicationContext(), "location is ..." + locationBean.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });


    }
}
