package session;

import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.chris.nearbykos2.LoginActivity;


public class SessionManager {
	// Shared Preferences
	private SharedPreferences pref;

	// Editor for Shared preferences
	private Editor editor;

	// Context
	private Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "nearby.xml";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	private boolean login;

	public static final String sid ="id";
	public static final String logku ="login";
	public static final String sUserName ="username";
	public static final String sNamaLengkap ="nama";
	public static final String sTelp ="telp";
	public static final String sEmail ="email";
	public static final String sStatusLogin = "status";
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(Integer id, String userName,String namaLgkp,
								   String tlp, String email, String status, boolean loginku){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		editor.putBoolean(logku,loginku);
		editor.putInt(sid, id);
		editor.putString(sUserName, userName);
		editor.putString(sNamaLengkap, namaLgkp);
		editor.putString(sTelp, tlp);
		editor.putString(sEmail, email);
		editor.putString(sStatusLogin, status);
		editor.commit();
	}


	/**
	 * Get stored session data
	 * */
	public HashMap<String, Object> getUserDetails(){
		HashMap<String, Object> user = new HashMap<String, Object>();
		user.put(sid, pref.getInt(sid, 0));
		user.put(sUserName, pref.getString(sUserName, null));
		user.put(sNamaLengkap, pref.getString(sNamaLengkap, null));
		user.put(sTelp, pref.getString(sTelp, null));
		user.put(sEmail, pref.getString(sEmail, null));
		user.put(sStatusLogin, pref.getString(sStatusLogin, null));
		user.put(logku, pref.getBoolean(logku, false));
		return user;
	}

	/**
	 * Clear session details
	 * */
	/*public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, SignUp.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
	}*/

	public void logoutUser(){
		setLogin(false);
		createLoginSession(0, null, null, null, null,
				null, false);
		Intent i = new Intent(_context, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

}