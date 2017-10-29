package my.edu.unikl.icdsystemfirebase;

/**
 * Created by MirolHusni95 on 10/9/2017.
 */

public class AdminEvent {

    private String uid,evName,evDesc,evDate;

    public AdminEvent() {
    }

    public AdminEvent(String uid, String evName, String evDesc, String evDate) {
        this.uid = uid;   // Primary key and key
        this.evName = evName;
        this.evDesc = evDesc;
        this.evDate = evDate;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEvName() {
        return evName;
    }

    public void setEvName(String evName) {
        this.evName = evName;
    }

    public String getEvDesc() {
        return evDesc;
    }

    public void setEvDesc(String evDesc) {
        this.evDesc = evDesc;
    }

    public String getEvDate() {
        return evDate;
    }

    public void setEvDate(String evDate) {
        this.evDate = evDate;
    }
}
