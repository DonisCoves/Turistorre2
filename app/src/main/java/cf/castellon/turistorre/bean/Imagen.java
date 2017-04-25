package cf.castellon.turistorre.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Imagen implements Parcelable{
    public String uidUser;
    public String uidImg;
    public String uriStrPre;
    public String uriStr;
    public String titulo;
    public String descripcion;
    public String fecha;
    public static final Parcelable.Creator<Imagen> CREATOR = new Parcelable.Creator<Imagen>() {
        @Override
        public Imagen createFromParcel(Parcel in) {
            return new Imagen(in);
        }

        @Override
        public Imagen[] newArray(int size) {
            return new Imagen[size];
        }
    };

    public Imagen() {};

    public Imagen(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        uidUser = in.readString();
        uidImg = in.readString();
        uriStrPre = in.readString();
        uriStr = in.readString();
        titulo = in.readString();
        descripcion = in.readString();
        fecha = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uidUser);
        dest.writeString(uidImg);
        dest.writeString(uriStrPre);
        dest.writeString(uriStr);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeString(fecha);
    }

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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        Imagen img = (Imagen)obj;
        return uidImg.equals(img.getUidImg());
    }
}
