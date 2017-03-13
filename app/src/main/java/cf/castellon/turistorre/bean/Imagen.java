package cf.castellon.turistorre.bean;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Imagen {
    public String uidUser;
    public String uidImg;
    public String uriStrPre;
    public String uriStr;

    public Imagen() {};

    public String getUidImg() {
        return uidImg;
    }

    public void setUidImg(String uidImg) {
        this.uidImg = uidImg;
    }

    public String getUriStrPre() {
        return uriStrPre;
    }

    public String getUriStr() {
        return uriStr;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public void setUriStrPre(String uriStrPre) {
        this.uriStrPre = uriStrPre;
    }

    public void setUriStr(String uriStr) {
        this.uriStr = uriStr;
    }
}
