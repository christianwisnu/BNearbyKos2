package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by christian on 04/01/18.
 */

public class ColTransaksi implements Parcelable {

    private Integer idBooking;
    private Integer idPenyewa;
    private String namaPenyewa;
    private Integer idKos;
    private String telpPenyewa;
    private String tglMasuk;
    private String tglKeluar;
    private String status;

    public Integer getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(Integer idBooking) {
        this.idBooking = idBooking;
    }

    public Integer getIdPenyewa() {
        return idPenyewa;
    }

    public void setIdPenyewa(Integer idPenyewa) {
        this.idPenyewa = idPenyewa;
    }

    public String getNamaPenyewa() {
        return namaPenyewa;
    }

    public void setNamaPenyewa(String namaPenyewa) {
        this.namaPenyewa = namaPenyewa;
    }

    public Integer getIdKos() {
        return idKos;
    }

    public void setIdKos(Integer idKos) {
        this.idKos = idKos;
    }

    public String getTelpPenyewa() {
        return telpPenyewa;
    }

    public void setTelpPenyewa(String telpPenyewa) {
        this.telpPenyewa = telpPenyewa;
    }

    public String getTglMasuk() {
        return tglMasuk;
    }

    public void setTglMasuk(String tglMasuk) {
        this.tglMasuk = tglMasuk;
    }

    public String getTglKeluar() {
        return tglKeluar;
    }

    public void setTglKeluar(String tglKeluar) {
        this.tglKeluar = tglKeluar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ColTransaksi() {
    };

    protected ColTransaksi(Parcel in) {
        idBooking = in.readInt();
        idPenyewa = in.readInt();
        namaPenyewa = in.readString();
        idKos = in.readInt();
        telpPenyewa = in.readString();
        tglMasuk = in.readString();
        tglKeluar = in.readString();
        status = in.readString();
    }

    public static final Creator<ColTransaksi> CREATOR = new Creator<ColTransaksi>() {
        @Override
        public ColTransaksi createFromParcel(Parcel in) {
            return new ColTransaksi(in);
        }

        @Override
        public ColTransaksi[] newArray(int size) {
            return new ColTransaksi[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idBooking);
        dest.writeInt(idPenyewa);
        dest.writeString(namaPenyewa);
        dest.writeInt(idKos);
        dest.writeString(telpPenyewa);
        dest.writeString(tglMasuk);
        dest.writeString(tglKeluar);
        dest.writeString(status);
    }
}
