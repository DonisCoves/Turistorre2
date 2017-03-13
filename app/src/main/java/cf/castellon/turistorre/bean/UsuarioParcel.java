package cf.castellon.turistorre.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UsuarioParcel implements Parcelable{
    public String uidUser;
    public String nombre;
    public String avatar;
    public String grupo;
    public static final Parcelable.Creator<UsuarioParcel> CREATOR = new Parcelable.Creator<UsuarioParcel>() {
        @Override
        public UsuarioParcel createFromParcel(Parcel in) {
            return new UsuarioParcel(in);
        }

        @Override
        public UsuarioParcel[] newArray(int size) {
            return new UsuarioParcel[size];
        }
    };

    public UsuarioParcel() {}

    public UsuarioParcel(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        uidUser = in.readString();
        nombre = in.readString();
        avatar = in.readString();
        grupo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uidUser);
        dest.writeString(nombre);
        dest.writeString(avatar);
        dest.writeString(grupo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public UsuarioParcel(String nombre, String avatar, String uidUser, String grupo) {
        this.nombre = nombre;
        this.avatar = avatar;
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
}