package cf.castellon.turistorre.bean;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by pccc on 23/11/2016.
 */
@IgnoreExtraProperties
public class Bando {
    private String uidUser;
    private String titulo;
    private String descripcion;
    private String url;
    private String fecha;

    public Bando() {}

    public Bando(String uidUser, String titulo, String descripcion, String url, String fecha) {
        this.uidUser = uidUser;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.url = url;
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
