package cf.castellon.turistorre.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ImagenParcel implements Parcelable{
    public String uidUser;
    public String uidImg;
    public String uriStrPre;
    public String uriStr;
    public static final Parcelable.Creator<ImagenParcel> CREATOR = new Parcelable.Creator<ImagenParcel>() {
        @Override
        public ImagenParcel createFromParcel(Parcel in) {
            return new ImagenParcel(in);
        }

        @Override
        public ImagenParcel[] newArray(int size) {
            return new ImagenParcel[size];
        }
    };

    public ImagenParcel() {};

    public ImagenParcel(Parcel in) {
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
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uidUser);
        dest.writeString(uidImg);
        dest.writeString(uriStrPre);
        dest.writeString(uriStr);
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


}
