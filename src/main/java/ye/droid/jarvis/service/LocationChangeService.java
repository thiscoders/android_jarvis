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
        String best = locationManager.getBestProvider(criteria, true);

        //神奇的代码
        // TODO: 2017/5/26 弄懂这个注解
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(best, 0, 0, new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();//经度
                double latitude = location.getLatitude();//纬度

                LocationBean bean = new LocationBean();
                bean.setLongitude(longitude);
                bean.setLatitude(latitude);

                Log.i(TAG, "location is ..." + bean.toString());
                String safePhone = SharedPreferencesUtils.getString(getApplicationContext(), ConstantValues.CONTACT_PHONEV2, "");//获取安全联系人的电话号码
                BurglarsSmsUtils.sendSms(safePhone, bean.toString());
                Toast.makeText(getApplicationContext(), "location is ..." + bean.toString(), Toast.LENGTH_SHORT).show();
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
