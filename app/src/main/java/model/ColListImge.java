package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by christian on 19/06/17.
 */

public class ColListImge implements Parcelable {

    private Integer idKos;
    private Integer line;
    private String detailPathGambar;

    public Integer getIdKos() {
        return idKos;
    }

    public void setIdKos(Integer idKos) {
        this.idKos = idKos;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getDetailPathGambar() {
        return detailPathGambar;
    }

    public void setDetailPathGambar(String detailPathGambar) {
        this.detailPathGambar = detailPathGambar;
    }

    public ColListImge(){}

    protected ColListImge(Parcel in) {
        idKos = in.readInt();
        line = in.readInt();
        detailPathGambar = in.readString();
    }

    public static final Creator<ColListImge> CREATOR = new Creator<ColListImge>() {
        @Override
        public ColListImge createFromParcel(Parcel in) {
            return new ColListImge(in);
        }

        @Override
        public ColListImge[] newArray(int size) {
            return new ColListImge[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idKos);
        dest.writeInt(line);
        dest.writeString(detailPathGambar);
    }
}
