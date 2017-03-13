package cf.castellon.turistorre.bean;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;


@IgnoreExtraProperties
public class Evento {
    private String descripcion;
    private String hora_inicial;
    private String titulo;
    private String uidEvento;
    Map<String, Imagen> imagenes;

    public Evento() {}

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

}
