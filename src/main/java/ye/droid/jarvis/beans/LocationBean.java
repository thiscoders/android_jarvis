package ye.droid.jarvis.beans;

/**
 * Created by ye on 2017/5/24.
 */

public class LocationBean {

    private double longitude;
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "LocationBean{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
