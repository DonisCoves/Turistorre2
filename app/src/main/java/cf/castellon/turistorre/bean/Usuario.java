package cf.castellon.turistorre.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Usuario implements Parcelable{
    public String uidUser;
    public String nombre;
    public String email;
    public String avatar;
    public String grupo;
    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public Usuario() {}

    public Usuario(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        uidUser = in.readString();
        nombre = in.readString();
        email = in.readString();
        avatar = in.readString();
        grupo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uidUser);
        dest.writeString(nombre);
        dest.writeString(email);
        dest.writeString(avatar);
        dest.writeString(grupo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Usuario(String nombre, String avatar, String email, String uidUser, String grupo) {
        this.nombre = nombre;
        this.avatar = avatar;
        this.email = email;
        this.uidUser = uidUser;
        this.grupo = grupo;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uidUser", uidUser);
        result.put("nombre", nombre);
        result.put("nombre", email);
        result.put("avatar", avatar);
        result.put("grupo", grupo);
        return result;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        Usuario user;
        boolean iguales;

        user = (Usuario)obj;
        iguales = uidUser.equals(user.getUidUser());
        if (iguales && !user.getGrupo().equals(grupo))  //Actalizamos cambios de grupos
            user.setGrupo(grupo);
        return iguales;
    }
}