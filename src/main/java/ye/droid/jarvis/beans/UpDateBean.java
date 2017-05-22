package ye.droid.jarvis.beans;

/**
 * Created by ye on 2017/5/22.
 */

public class UpDateBean {

    private String versionName;
    private String versionCode;
    private String versionDesc;
    private String downloadURL;
    private String versionInfo;


    public UpDateBean() {
    }

    public UpDateBean(String versionName, String versionCode, String versionDesc, String downloadURL, String versionInfo) {
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.versionDesc = versionDesc;
        this.downloadURL = downloadURL;
        this.versionInfo = versionInfo;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    @Override
    public String toString() {
        return "UpDateBean{" +
                "versionName='" + versionName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", versionDesc='" + versionDesc + '\'' +
                ", downloadURL='" + downloadURL + '\'' +
                ", versionInfo='" + versionInfo + '\'' +
                '}';
    }
}
