package cf.castellon.turistorre.bean;

/**
 * Created by pccc on 07/05/2016.
 */
public class Proveedor {
    String nombre;
    String avatarUrl;

    public Proveedor() {}

    public Proveedor(String nombre, String avatarUrl) {
        this.nombre = nombre;
        this.avatarUrl = avatarUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAvatar() {
        return avatarUrl;
    }

    public void setAvatar(String avatar) {
        this.avatarUrl = avatarUrl;
    }
}
