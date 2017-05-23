package ye.droid.jarvis.beans;

/**
 * Created by ye on 2017/5/23.
 */

public class SmsBean {


    private String _id = "";
    private String thread_id = "";
    private String smsAddress = "";
    private String smsBody = "";
    private String read = "";
    private int action = 0;// 1代表设置为已读，2表示删除短信

    public SmsBean() {
    }

    public SmsBean(String _id, String thread_id, String smsAddress, String smsBody, String read, int action) {
        this._id = _id;
        this.thread_id = thread_id;
        this.smsAddress = smsAddress;
        this.smsBody = smsBody;
        this.read = read;
        this.action = action;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getSmsAddress() {
        return smsAddress;
    }

    public void setSmsAddress(String smsAddress) {
        this.smsAddress = smsAddress;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "SmsBean{" +
                "_id='" + _id + '\'' +
                ", thread_id='" + thread_id + '\'' +
                ", smsAddress='" + smsAddress + '\'' +
                ", smsBody='" + smsBody + '\'' +
                ", read='" + read + '\'' +
                ", action=" + action +
                '}';
    }
}
