package model;

import android.os.Parcel;
import android.os.Parcelable;

public class ColCustomer implements Parcelable {

	private Integer idUser;
	private String userName;
	private String namaLengkap;
	private String telp;
	private String email;

	public ColCustomer() {
	};

	protected ColCustomer(Parcel in) {
		idUser = in.readInt();
		userName = in.readString();
		namaLengkap = in.readString();
		telp = in.readString();
		email = in.readString();
	}

	public static final Creator<ColCustomer> CREATOR = new Creator<ColCustomer>() {
		@Override
		public ColCustomer createFromParcel(Parcel in) {
			return new ColCustomer(in);
		}

		@Override
		public ColCustomer[] newArray(int size) {
			return new ColCustomer[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(idUser);
		dest.writeString(userName);
		dest.writeString(namaLengkap);
		dest.writeString(telp);
		dest.writeString(email);
	}

	public Integer getIdUser() {return idUser;}
	public String getUserName() {return userName;}
	public String getNamaLengkap() {return namaLengkap;}
	public String getTelp() {return telp;}
	public String getEmail() {return email;}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setNamaLengkap(String namaLengkap) {
		this.namaLengkap = namaLengkap;
	}
	public void setTelp(String telp) {
		this.telp = telp;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}