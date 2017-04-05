package cf.castellon.turistorre.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Raco implements Parcelable{
    public String url;
    public String titulo;
    public String descripcion;
    public static final Creator<Raco> CREATOR = new Creator<Raco>() {
        @Override
        public Raco createFromParcel(Parcel in) {
            return new Raco(in);
        }

        @Override
        public Raco[] newArray(int size) {
            return new Raco[size];
        }
    };

    public Raco() {};

    public Raco(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        url = in.readString();
        titulo = in.readString();
        descripcion = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(titulo);
        dest.writeString(descripcion);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
