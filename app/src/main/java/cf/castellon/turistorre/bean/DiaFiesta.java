package cf.castellon.turistorre.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class DiaFiesta implements Parcelable{
    private String uidDiaFiesta;
    private String titulo;
    private Map<String, Evento> eventos;
    public static final Parcelable.Creator<DiaFiesta> CREATOR = new Parcelable.Creator<DiaFiesta>() {
        @Override
        public DiaFiesta createFromParcel(Parcel in) {
            return new DiaFiesta(in);
        }

        @Override
        public DiaFiesta[] newArray(int size) {
            return new DiaFiesta[size];
        }
    };

    public DiaFiesta(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public DiaFiesta(String uidDiaFiesta, String titulo, Map<String, Evento> eventos) {
        this.uidDiaFiesta = uidDiaFiesta;
        this.titulo = titulo;
        this.eventos = eventos;
    }

    public DiaFiesta() {
    }

    private void readFromParcel(Parcel in) {
        uidDiaFiesta = in.readString();
        titulo= in.readString();
        in.readMap(eventos,Evento.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uidDiaFiesta);
        dest.writeString(titulo);
        dest.writeMap(eventos);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUidDiaFiesta() {
        return uidDiaFiesta;
    }

    public void setUidDiaFiesta(String uidDiaFiesta) {
        this.uidDiaFiesta = uidDiaFiesta;
    }

    public Map<String, Evento> getEventos() {
        return eventos;
    }

    public void setEventos(Map<String, Evento> eventos) {
        this.eventos = eventos;
    }

    public void addEvento(Evento evento){
        this.eventos.put(evento.getUidEvento(),evento);
    }
}
