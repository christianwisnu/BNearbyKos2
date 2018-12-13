package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by christian on 13/06/17.
 */

public class ColHomeDetail implements Parcelable {

    private Integer id_kos;
    private Integer id_cust;
    private Integer id_sewaStatus;
    private String namaKos;
    private String alamat;
    private String gambar;
    private String gambar2;
    private String gambar3;
    private String gambar4;
    private String gambar5;
    private Integer lebar;
    private Integer panjang;
    private String statusListrik;
    private Integer jmlhKamar;
    private String fasilitas;
    private double latitude;
    private double longtitude;
    private Integer harga;
    private String namaCust;
    private String tlpCust;
    private String emailCust;
    private String kodeKota;
    private String namaKota;
    private Integer sisa;
    private double rating;
    private Integer countUser;
    private ArrayList<ColListImge> listImage;

    public Integer getId_kos() {
        return id_kos;
    }

    public void setId_kos(Integer id_kos) {
        this.id_kos = id_kos;
    }

    public Integer getId_sewaStatus() {
        return id_sewaStatus;
    }

    public void setId_sewaStatus(Integer id_sewaStatus) {
        this.id_sewaStatus = id_sewaStatus;
    }

    public String getNamaKos() {
        return namaKos;
    }

    public void setNamaKos(String namaKos) {
        this.namaKos = namaKos;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public Integer getLebar() {
        return lebar;
    }

    public void setLebar(Integer lebar) {
        this.lebar = lebar;
    }

    public Integer getPanjang() {
        return panjang;
    }

    public void setPanjang(Integer panjang) {
        this.panjang = panjang;
    }

    public String getStatusListrik() {
        return statusListrik;
    }

    public void setStatusListrik(String statusListrik) {
        this.statusListrik = statusListrik;
    }

    public Integer getJmlhKamar() {
        return jmlhKamar;
    }

    public void setJmlhKamar(Integer jmlhKamar) {
        this.jmlhKamar = jmlhKamar;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public ArrayList<ColListImge> getListImage() {
        return listImage;
    }

    public void setListImage(ArrayList<ColListImge> listImage) {
        this.listImage = listImage;
    }

    public Integer getId_cust() {
        return id_cust;
    }

    public void setId_cust(Integer id_cust) {
        this.id_cust = id_cust;
    }

    public ColHomeDetail(){}

    private ColHomeDetail(Parcel in) {
        id_kos = in.readInt();
        id_sewaStatus = in.readInt();
        namaKos = in.readString();
        alamat = in.readString();
        gambar = in.readString();
        lebar = in.readInt();
        panjang = in.readInt();
        statusListrik = in.readString();
        jmlhKamar = in.readInt();
        fasilitas = in.readString();
        latitude = in.readDouble();
        longtitude = in.readDouble();
        harga = in.readInt();
        id_cust = in.readInt();
        namaCust = in.readString();
        tlpCust = in.readString();
        kodeKota = in.readString();
        namaKota = in.readString();
        sisa = in.readInt();
        listImage = in.readArrayList(ColListImge.class.getClassLoader());
    }

    public static final Creator<ColHomeDetail> CREATOR = new Creator<ColHomeDetail>() {
        @Override
        public ColHomeDetail createFromParcel(Parcel in) {
            return new ColHomeDetail(in);
        }

        @Override
        public ColHomeDetail[] newArray(int size) {
            return new ColHomeDetail[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_kos);
        dest.writeInt(id_sewaStatus);
        dest.writeString(namaKos);
        dest.writeString(alamat);
        dest.writeString(gambar);
        dest.writeInt(lebar);
        dest.writeInt(panjang);
        dest.writeString(statusListrik);
        dest.writeInt(jmlhKamar);
        dest.writeString(fasilitas);
        dest.writeDouble(latitude);
        dest.writeDouble(longtitude);
        dest.writeInt(harga);
        dest.writeInt(id_cust);
        dest.writeString(namaCust);
        dest.writeString(tlpCust);
        dest.writeString(kodeKota);
        dest.writeString(namaKota);
        dest.writeInt(sisa);
    }

    public String getNamaCust() {
        return namaCust;
    }

    public void setNamaCust(String namaCust) {
        this.namaCust = namaCust;
    }

    public String getTlpCust() {
        return tlpCust;
    }

    public void setTlpCust(String tlpCust) {
        this.tlpCust = tlpCust;
    }

    public String getEmailCust() {
        return emailCust;
    }

    public void setEmailCust(String emailCust) {
        this.emailCust = emailCust;
    }

    public String getGambar2() {
        return gambar2;
    }

    public void setGambar2(String gambar2) {
        this.gambar2 = gambar2;
    }

    public String getGambar3() {
        return gambar3;
    }

    public void setGambar3(String gambar3) {
        this.gambar3 = gambar3;
    }

    public String getGambar4() {
        return gambar4;
    }

    public void setGambar4(String gambar4) {
        this.gambar4 = gambar4;
    }

    public String getGambar5() {
        return gambar5;
    }

    public void setGambar5(String gambar5) {
        this.gambar5 = gambar5;
    }

    public String getKodeKota() {
        return kodeKota;
    }

    public void setKodeKota(String kodeKota) {
        this.kodeKota = kodeKota;
    }

    public String getNamaKota() {
        return namaKota;
    }

    public void setNamaKota(String namaKota) {
        this.namaKota = namaKota;
    }

    public Integer getSisa() {
        return sisa;
    }

    public void setSisa(Integer sisa) {
        this.sisa = sisa;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Integer getCountUser() {
        return countUser;
    }

    public void setCountUser(Integer countUser) {
        this.countUser = countUser;
    }
}
