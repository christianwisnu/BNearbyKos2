package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chris on 25/06/2017.
 */

public class ColHomeBooking implements Parcelable {

    private Integer idBooking;
    private Integer idKos;
    private String namaKos;
    private String gambarKos;
    private String alamatKos;
    private Integer idCust;
    private String namaCust;
    private Integer idUser;
    private String namaUser;
    private Integer harga;
    private String CStat;
    private String StatusBooking;
    private String telpUser;
    private String tglSurvey;//tglCekIn
    private String fotoDP;
    private String statusDP;
    private String kodeBank;
    private String namaBank;
    private Integer line;
    private String noRek;

    public ColHomeBooking() {
    };

    protected ColHomeBooking(Parcel in) {
        idBooking = in.readInt();
        idKos = in.readInt();
        namaKos = in.readString();
        idCust = in.readInt();
        namaCust = in.readString();
        idUser = in.readInt();
        namaUser = in.readString();
        harga = in.readInt();
        CStat = in.readString();
        StatusBooking = in.readString();
        tglSurvey =in.readString();
        fotoDP = in.readString();
        statusDP = in.readString();
        kodeBank = in.readString();
        namaBank = in.readString();
        line = in.readInt();
        noRek = in.readString();
    }

    public static final Creator<ColHomeBooking> CREATOR = new Creator<ColHomeBooking>() {
        @Override
        public ColHomeBooking createFromParcel(Parcel in) {
            return new ColHomeBooking(in);
        }

        @Override
        public ColHomeBooking[] newArray(int size) {
            return new ColHomeBooking[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idBooking);
        dest.writeInt(idKos);
        dest.writeString(namaKos);
        dest.writeInt(idCust);
        dest.writeString(namaCust);
        dest.writeInt(idUser);
        dest.writeString(namaUser);
        dest.writeInt(harga);
        dest.writeString(CStat);
        dest.writeString(StatusBooking);
        dest.writeString(tglSurvey);
        dest.writeString(fotoDP);
        dest.writeString(statusDP);
        dest.writeString(kodeBank);
        dest.writeString(namaBank);
        dest.writeInt(line);
        dest.writeString(noRek);
    }

    public Integer getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(Integer idBooking) {
        this.idBooking = idBooking;
    }

    public Integer getIdKos() {
        return idKos;
    }

    public void setIdKos(Integer idKos) {
        this.idKos = idKos;
    }

    public String getNamaKos() {
        return namaKos;
    }

    public void setNamaKos(String namaKos) {
        this.namaKos = namaKos;
    }

    public Integer getIdCust() {
        return idCust;
    }

    public void setIdCust(Integer idCust) {
        this.idCust = idCust;
    }

    public String getNamaCust() {
        return namaCust;
    }

    public void setNamaCust(String namaCust) {
        this.namaCust = namaCust;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public String getCStat() {
        return CStat;
    }

    public void setCStat(String CStat) {
        this.CStat = CStat;
    }

    public String getStatusBooking() {
        return StatusBooking;
    }

    public void setStatusBooking(String statusBooking) {
        StatusBooking = statusBooking;
    }

    public String getGambarKos() {
        return gambarKos;
    }

    public void setGambarKos(String gambarKos) {
        this.gambarKos = gambarKos;
    }

    public String getAlamatKos() {
        return alamatKos;
    }

    public void setAlamatKos(String alamatKos) {
        this.alamatKos = alamatKos;
    }

    public String getTelpUser() {
        return telpUser;
    }

    public void setTelpUser(String telpUser) {
        this.telpUser = telpUser;
    }

    public String getTglSurvey() {
        return tglSurvey;
    }

    public void setTglSurvey(String tglSurvey) {
        this.tglSurvey = tglSurvey;
    }

    public String getFotoDP() {
        return fotoDP;
    }

    public void setFotoDP(String fotoDP) {
        this.fotoDP = fotoDP;
    }

    public String getStatusDP() {
        return statusDP;
    }

    public void setStatusDP(String statusDP) {
        this.statusDP = statusDP;
    }

    public String getKodeBank() {
        return kodeBank;
    }

    public void setKodeBank(String kodeBank) {
        this.kodeBank = kodeBank;
    }

    public String getNamaBank() {
        return namaBank;
    }

    public void setNamaBank(String namaBank) {
        this.namaBank = namaBank;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getNoRek() {
        return noRek;
    }

    public void setNoRek(String noRek) {
        this.noRek = noRek;
    }
}
