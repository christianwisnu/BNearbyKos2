package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by christian on 19/01/18.
 */

public class ColCustRekening implements Parcelable {

    private Integer kodeCust;
    private Integer line;
    private String kodeBank;
    private String namaBank;
    private String noRek;

    public ColCustRekening() {
    };

    protected ColCustRekening(Parcel in) {
        kodeCust = in.readInt();
        line = in.readInt();
        kodeBank = in.readString();
        noRek = in.readString();
        namaBank = in.readString();
    }

    public static final Creator<ColCustRekening> CREATOR = new Creator<ColCustRekening>() {
        @Override
        public ColCustRekening createFromParcel(Parcel in) {
            return new ColCustRekening(in);
        }

        @Override
        public ColCustRekening[] newArray(int size) {
            return new ColCustRekening[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(kodeCust);
        dest.writeInt(line);
        dest.writeString(kodeBank);
        dest.writeString(noRek);
        dest.writeString(namaBank);
    }

    public Integer getKodeCust() {
        return kodeCust;
    }

    public void setKodeCust(Integer kodeCust) {
        this.kodeCust = kodeCust;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getKodeBank() {
        return kodeBank;
    }

    public void setKodeBank(String kodeBank) {
        this.kodeBank = kodeBank;
    }

    public String getNoRek() {
        return noRek;
    }

    public void setNoRek(String noRek) {
        this.noRek = noRek;
    }

    public String getNamaBank() {
        return namaBank;
    }

    public void setNamaBank(String namaBank) {
        this.namaBank = namaBank;
    }
}
