package my.edu.unikl.icdsystemfirebase.Firebase;

/**
 * Created by Mirolhusni95 on 18-Oct-17.
 */

public class UserInformation {

    private String uid, uEmail, uName, uPhone, uPhotoUrl;

    public UserInformation(){

    }

    public UserInformation(String uid, String uName, String uEmail, String uPhone, String uPhotoUrl){
        this.uid = uid;
        this.uName = uName;
        this.uEmail = uEmail;
        this.uPhone = uPhone;
        this.uPhotoUrl = uPhotoUrl;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getuPhotoUrl() {
        return uPhotoUrl;
    }

    public void setuPhotoUrl(String uPhotoUrl) {
        this.uPhotoUrl = uPhotoUrl;
    }
}
