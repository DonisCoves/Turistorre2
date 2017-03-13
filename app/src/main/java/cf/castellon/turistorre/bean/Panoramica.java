package cf.castellon.turistorre.bean;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Panoramica {
    public String uidUser;
    public String uidImg;
    public String uriStrPre;
    public String uriStr;
    public String titulo;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Panoramica() {};

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
