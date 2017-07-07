package cf.castellon.turistorre.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class Fiestas implements Parcelable{
    private String uidFiestas;
    private String titulo;
    private String descripcion;
    private Map<String, DiaFiestaMeta> diasFiestas;
    public static final Parcelable.Creator<Fiestas> CREATOR = new Parcelable.Creator<Fiestas>() {
        @Override
        public Fiestas createFromParcel(Parcel in) {
            return new Fiestas(in);
        }

        @Override
        public Fiestas[] newArray(int size) {
            return new Fiestas[size];
        }
    };

    public Fiestas(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        uidFiestas = in.readString();
        titulo = in.readString();
        descripcion = in.readString();
        in.readMap(diasFiestas,DiaFiestaMeta.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uidFiestas);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeMap(diasFiestas);
    }


    public Fiestas(String uidFiestas, String titulo, String descripcion, Map<String, DiaFiestaMeta> diasFiestas) {
        this.uidFiestas = uidFiestas;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.diasFiestas = diasFiestas;
    }


    public Fiestas() {
    }


    public String getUidFiestas() {
        return uidFiestas;
    }

    public void setUidFiestas(String uidFiestas) {
        this.uidFiestas = uidFiestas;
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

    public Map<String, DiaFiestaMeta> getDiasFiestas() {
        return diasFiestas;
    }

    public void setDiasFiestas(Map<String, DiaFiestaMeta> diasFiestas) {
        this.diasFiestas = diasFiestas;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        Fiestas fiestas = (Fiestas)obj;
        return uidFiestas.equals(fiestas.getUidFiestas());
    }


}
