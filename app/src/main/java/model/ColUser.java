package model;

import android.os.Parcel;
import android.os.Parcelable;

public class ColUser implements Parcelable {

	private Integer idUser;
	private String userName;
	private String namaLengkap;
	private String telp;
	private String email;

	public ColUser() {
	};

	protected ColUser(Parcel in) {
		idUser = in.readInt();
		userName = in.readString();
		namaLengkap = in.readString();
		telp = in.readString();
		email = in.readString();
	}

	public static final Creator<ColUser> CREATOR = new Creator<ColUser>() {
		@Override
		public ColUser createFromParcel(Parcel in) {
			return new ColUser(in);
		}

		@Override
		public ColUser[] newArray(int size) {
			return new ColUser[size];
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