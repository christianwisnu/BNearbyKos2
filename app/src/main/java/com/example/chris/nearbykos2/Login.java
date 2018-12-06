package com.example.chris.nearbykos2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import control.AppController;
import control.Link;
import model.ColCustomer;
import model.ColUser;
import session.SessionManager;

public class Login extends android.support.v7.app.AppCompatActivity{

	private String LoginCust				="loginCust.php";
	private String LoginUser				="loginUser.php";
	private EditText eNama,ePassword;
	private Button bLogin;
	private String TAG = Login.class.getName();
	private SessionManager session;
	private ArrayList<ColUser> columnlist = new ArrayList<ColUser>();
	private ArrayList<ColCustomer> columnlistCust = new ArrayList<ColCustomer>();
	private ProgressDialog dialog;
	private TextInputLayout inputLayoutName, inputLayoutPasw;
	private Spinner spLogin;
	private TextView txtRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

		session		= new SessionManager(this);
		bLogin		= (Button)findViewById(R.id.bLoginLogin);
		inputLayoutName = (TextInputLayout)findViewById(R.id.input_layout_loginusername);
		inputLayoutPasw = (TextInputLayout)findViewById(R.id.input_layout_loginpasw);
		eNama		= (EditText)findViewById(R.id.eLoginUserName);
		ePassword	= (EditText)findViewById(R.id.eLoginPassword);
		spLogin = (Spinner) findViewById(R.id.spLoginLogin);
		txtRegister = (TextView) findViewById(R.id.txtRegister);
        Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        animFadeIn.setDuration(1000);
        animFadeIn.setStartOffset(1000);
        animFadeIn.setFillAfter(true);

        bLogin.setAnimation(animFadeIn);

		eNama.addTextChangedListener(new MyTextWatcher(eNama));
		ePassword.addTextChangedListener(new MyTextWatcher(ePassword));

		bLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.setMessage("CHECK ID ANDA ...");
				if(String.valueOf(spLogin.getSelectedItem()).equals("User")){
					SingUp(Link.FilePHP + LoginUser);
					Log.i("LINK",Link.FilePHP + LoginUser);
				}else{
					SingUp(Link.FilePHP + LoginCust);
					Log.i("LINK",Link.FilePHP + LoginCust);
				}
			}
		});

		txtRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Login.this, Register.class));
			}
		});
	}

	private class MyTextWatcher implements TextWatcher {

		private View view;

		private MyTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void afterTextChanged(Editable editable) {
			switch (view.getId()) {
				case R.id.input_layout_loginusername:
					validateUserName();
					break;
				case R.id.input_layout_loginpasw:
					validatePasw();
					break;
			}
		}
	}

	private boolean validateUserName() {
		if (eNama.getText().toString().trim().isEmpty()) {
			inputLayoutName.setError(getString(R.string.err_msg_user_name));
			requestFocus(eNama);
			return false;
		} else {
			inputLayoutName.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validatePasw() {
		if (ePassword.getText().toString().trim().isEmpty()) {
			inputLayoutPasw.setError(getString(R.string.err_msg_pasw));
			requestFocus(ePassword);
			return false;
		} else {
			inputLayoutPasw.setErrorEnabled(false);
		}
		return true;
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	private void SingUp(String save){
		dialog.show();
		StringRequest simpan = new StringRequest(Request.Method.POST, save,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						VolleyLog.d(TAG, response.toString());
						try {
							JSONObject jsonrespon = new JSONObject(response);
							int Sucsess = jsonrespon.getInt("success");
							Log.i("Status", String.valueOf(Sucsess));
							dialog.dismiss();
							if (Sucsess==1){
								JSONArray JsonArray = jsonrespon.getJSONArray("Melbu");
								String status=null;
								if(String.valueOf(spLogin.getSelectedItem()).equals("User")){
									status="USER";
									for (int i = 0; i < JsonArray.length(); i++) {
										JSONObject objectt = JsonArray.getJSONObject(i);
										ColUser colums 	= new ColUser();
										colums.setIdUser(objectt.getInt("i_iduser"));
										colums.setUserName(objectt.getString("c_username"));
										colums.setNamaLengkap(objectt.getString("vc_namalengkap"));
										colums.setTelp(objectt.getString("c_telp"));
										colums.setEmail(objectt.getString("c_email"));
										columnlist.add(colums);
									}
									session.setLogin(true);
									session.createLoginSession(columnlist.get(0).getIdUser(), columnlist.get(0).getUserName(),
											columnlist.get(0).getNamaLengkap(),
											columnlist.get(0).getTelp(), columnlist.get(0).getEmail(), status, true);
								}else{
									status="CUST";
									for (int i = 0; i < JsonArray.length(); i++) {
										JSONObject objectt = JsonArray.getJSONObject(i);
										ColCustomer colums 	= new ColCustomer();
										colums.setIdUser(objectt.getInt("i_idcust"));
										colums.setUserName(objectt.getString("c_username"));
										colums.setNamaLengkap(objectt.getString("vc_namalengkap"));
										colums.setTelp(objectt.getString("c_telp"));
										colums.setEmail(objectt.getString("c_email"));
										columnlistCust.add(colums);
									}
									session.setLogin(true);
									session.createLoginSession(columnlistCust.get(0).getIdUser(), columnlistCust.get(0).getUserName(),
											columnlistCust.get(0).getNamaLengkap(),
											columnlistCust.get(0).getTelp(), columnlistCust.get(0).getEmail(), status, true);
								}

								Intent i = new Intent(Login.this, MainActivity.class);
								startActivityForResult(i, 5000);
								overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
								finish();
							}else{
								Toast.makeText(Login.this,
										"Gagal Coba Lagi", Toast.LENGTH_LONG)
										.show();
								dialog.dismiss();
							}
						} catch (Exception e) {
							Toast.makeText(Login.this,
									e.getMessage(), Toast.LENGTH_LONG)
									.show();
							dialog.dismiss();
						}
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				VolleyLog.d(TAG, error.toString());
				dialog.dismiss();
			}
		}){
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("passwd", ePassword.getText().toString().trim());
				params.put("username",  eNama.getText().toString().trim());
				return params;
			}
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String,String> params = new HashMap<String, String>();
				params.put("Content-Type","application/x-www-form-urlencoded");
				return params;
			}
		};
		AppController.getInstance().getRequestQueue().getCache().invalidate(save, true);
		AppController.getInstance().addToRequestQueue(simpan);
	}
}