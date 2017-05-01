package cf.castellon.turistorre.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;


@IgnoreExtraProperties
public class Evento implements Parcelable{
    private String descripcion;
    private String hora_inicial;
    private String titulo;
    private String uidEvento;
    Map<String, Imagen> imagenes;
    public static final Parcelable.Creator<Evento> CREATOR = new Parcelable.Creator<Evento>() {
        @Override
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };

    public Evento() {}

    public Evento(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        descripcion = in.readString();
        hora_inicial = in.readString();
        titulo = in.readString();
        uidEvento = in.readString();
        in.readMap(imagenes, Imagen.class.getClassLoader());
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(descripcion);
        dest.writeString(hora_inicial);
        dest.writeString(titulo);
        dest.writeString(uidEvento);
        dest.writeMap(imagenes);
    }



    public Evento(String uidEvento, String titulo, String descripcion, String hora_inicial,
                  Map<String, Imagen> imagenes) {
        this.uidEvento = uidEvento;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.hora_inicial = hora_inicial;
        this.imagenes = imagenes;
    }



    public String getUidEvento() {
        return uidEvento;
    }

    public void setUidEvento(String uidEvento) {
        this.uidEvento = uidEvento;
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

    public String getHora_inicial() {
        return hora_inicial;
    }

    public void setHora_inicial(String hora_inicial) {
        this.hora_inicial = hora_inicial;
    }

     public Map<String, Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(Map<String, Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public void addImagen(Imagen imagen){
        this.imagenes.put(imagen.getUidImg(),imagen);
    }
}
