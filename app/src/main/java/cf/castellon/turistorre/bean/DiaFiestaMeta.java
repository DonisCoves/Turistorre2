package cf.castellon.turistorre.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DiaFiestaMeta implements Parcelable{
    public String uidDiaFiesta;
    public String tituloDiaFiesta;
    public static final Parcelable.Creator<DiaFiestaMeta> CREATOR = new Parcelable.Creator<DiaFiestaMeta>() {
        @Override
        public DiaFiestaMeta createFromParcel(Parcel in) {
            return new DiaFiestaMeta(in);
        }

        @Override
        public DiaFiestaMeta[] newArray(int size) {
            return new DiaFiestaMeta[size];
        }
    };

    public DiaFiestaMeta(String uidDiaFiesta, String tituloDiaFiesta) {
        this.uidDiaFiesta = uidDiaFiesta;
        this.tituloDiaFiesta = tituloDiaFiesta;
    }

    public DiaFiestaMeta(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        uidDiaFiesta = in.readString();
        tituloDiaFiesta = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uidDiaFiesta);
        dest.writeString(tituloDiaFiesta);
    }

    public String getTituloDiaFiesta() {
        return tituloDiaFiesta;
    }

    public void setTituloDiaFiesta(String tituloDiaFiesta) {
        this.tituloDiaFiesta = tituloDiaFiesta;
    }

    public DiaFiestaMeta() {
    }

    public String getUidDiaFiesta() {
        return uidDiaFiesta;
    }

    public void setUidDiaFiesta(String uidDiaFiesta) {
        this.uidDiaFiesta = uidDiaFiesta;
    }
}
