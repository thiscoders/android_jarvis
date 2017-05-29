package ye.droid.jarvis.beans;

/**
 * Created by ye on 2017/5/24.
 */

public class LocationBean {

    private double longitude;
    private double latitude;
    public double altitude;

    public LocationBean() {
    }

    public LocationBean(double longitude, double latitude, double altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "LocationBean{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                '}';
    }
}
