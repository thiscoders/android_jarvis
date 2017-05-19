package ye.droid.jarvis.beans;

/**
 * Created by ye on 2017/5/14.
 */

public class ContactBean {
    private String contactID; //raw_contacts 的 _id 字段
    // 我的电话，只有我的手机有这个属性，初步估计是QQ的关系
    private String contactPhone; //vnd.android.cursor.item/vnd.com.tencent.mobileqq.voicecall.profile
    private String contactName; //vnd.android.cursor.item/name
    private String contactPhoto; //vnd.android.cursor.item/photo
    private String contactNote; //vnd.android.cursor.item/note
    private String contactNickname; //vnd.android.cursor.item/nickname
    private String contactMisc; //vnd.com.google.cursor.item/contact_misc
    private String contactGroupMembership; //vnd.android.cursor.item/group_membership
    private String contactPhone_v2; //vnd.android.cursor.item/phone_v2

    public ContactBean() {
    }

    public ContactBean(String contactID, String contactPhone, String contactName, String contactPhoto, String contactNote, String contactNickname, String contactMisc, String contactGroupMembership, String contactPhone_v2) {
        this.contactID = contactID;
        this.contactPhone = contactPhone;
        this.contactName = contactName;
        this.contactPhoto = contactPhoto;
        this.contactNote = contactNote;
        this.contactNickname = contactNickname;
        this.contactMisc = contactMisc;
        this.contactGroupMembership = contactGroupMembership;
        this.contactPhone_v2 = contactPhone_v2;
    }

    //getting
    public String getContactPhone() {
        return contactPhone;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhoto() {
        return contactPhoto;
    }

    public String getContactNote() {
        return contactNote;
    }

    public String getContactNickname() {
        return contactNickname;
    }

    public String getContactMisc() {
        return contactMisc;
    }

    public String getContactGroupMembership() {
        return contactGroupMembership;
    }

    public String getContactPhone_v2() {
        return contactPhone_v2;
    }

    public String getContactID() {
        return contactID;
    }

    //setting
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactPhoto(String contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    public void setContactNote(String contactNote) {
        this.contactNote = contactNote;
    }

    public void setContactNickname(String contactNickname) {
        this.contactNickname = contactNickname;
    }

    public void setContactMisc(String contactMisc) {
        this.contactMisc = contactMisc;
    }

    public void setContactGroupMembership(String contactGroupMembership) {
        this.contactGroupMembership = contactGroupMembership;
    }

    public void setContactPhone_v2(String contactPhone_v2) {
        this.contactPhone_v2 = contactPhone_v2;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    @Override
    public String toString() {
        return "ContactBean{" +
                "contactID=" + contactID +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhoto='" + contactPhoto + '\'' +
                ", contactNote='" + contactNote + '\'' +
                ", contactNickname='" + contactNickname + '\'' +
                ", contactMisc='" + contactMisc + '\'' +
                ", contactGroupMembership='" + contactGroupMembership + '\'' +
                ", contactPhone_v2='" + contactPhone_v2 + '\'' +
                '}';
    }

    public String bean2Json() {
        //return "{\"ContactBean\":{\"contactID\":" + contactID + ", \"contactPhone\":\"" + contactPhone + "\", \"contactName\":\"" + contactName + "\", \"contactPhoto\":\"" + contactPhoto + "\", \"contactNote\":\"" + contactNote + "\", \"contactNickname\":\"" + contactNickname + "\", \"contactMisc\":\"" + contactMisc +\
        //"\", \"contactGroupMembership\":\"" + contactGroupMembership + "\", \"contactPhone_v2\":\"" + contactPhone_v2 + "\"}}";
        return "";
    }
}
