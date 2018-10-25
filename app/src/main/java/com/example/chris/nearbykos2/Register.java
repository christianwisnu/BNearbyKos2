package com.example.chris.nearbykos2;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import control.AppController;
import control.Link;
import model.ColUser;
import session.SessionManager;


public class Register extends Fragment {

	private String registerCust = "addCust.php";
	private String registerUser = "addUser.php";
	private EditText eNama, eEmail, ePassword, eusername, eTelp;
	private ProgressDialog dialog;
	private Button bSingup;
	private String TAG = SignUp.class.getName();
	private ArrayList<ColUser> columnlist = new ArrayList<ColUser>();
	private SessionManager session;
	private String simage="none.jpg";
    private Spinner spjurusan;
	private TextInputLayout inputLayoutName, inputLayoutTelp, inputLayoutEmail,
			inputLayoutUserName, inputLayoutPasw;
	private String tipeLogin,slasid;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
    	session		= new SessionManager(getActivity());
    	View vwregister	= inflater.inflate(R.layout.register,container,false);
		inputLayoutName = (TextInputLayout)vwregister.findViewById(R.id.input_layout_nama);
		inputLayoutTelp = (TextInputLayout)vwregister.findViewById(R.id.input_layout_telp);
		inputLayoutEmail = (TextInputLayout)vwregister.findViewById(R.id.input_layout_email);
		inputLayoutUserName = (TextInputLayout)vwregister.findViewById(R.id.input_layout_username);
		inputLayoutPasw = (TextInputLayout)vwregister.findViewById(R.id.input_layout_pasw);

		eNama		= (EditText)vwregister.findViewById(R.id.input_nama);
		eTelp		= (EditText)vwregister.findViewById(R.id.input_telp);
		eEmail		= (EditText)vwregister.findViewById(R.id.input_email);
		eusername	= (EditText)vwregister.findViewById(R.id.input_username);
		ePassword	= (EditText)vwregister.findViewById(R.id.input_pasw);
		spjurusan = (Spinner) vwregister.findViewById(R.id.spRegisterLogin);
		bSingup		= (Button)vwregister.findViewById(R.id.bRegisterSave);

        Animation animFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        animFadeIn.setDuration(1000);
        animFadeIn.setStartOffset(1000);
        animFadeIn.setFillAfter(true);

        bSingup.setAnimation(animFadeIn);

		eNama.addTextChangedListener(new MyTextWatcher(eNama));
        ePassword.addTextChangedListener(new MyTextWatcher(ePassword));
        eusername.addTextChangedListener(new MyTextWatcher(eusername));

		bSingup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog = new ProgressDialog(getActivity());
				dialog.setCancelable(true);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.setMessage("Register ...");
				tipeLogin = String.valueOf(spjurusan.getSelectedItem());
				if(tipeLogin.equals("User")){
					Register(Link.FilePHP+registerUser);
				}else{
					Register(Link.FilePHP+registerCust);
				}
			}
		});
		

		return vwregister;
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
				case R.id.input_nama:
					validateName();
					break;
				case R.id.input_username:
					validateUserName();
					break;
				case R.id.input_pasw:
					validatePasw();
					break;
			}
		}
	}

	private boolean validateName() {
		if (eNama.getText().toString().trim().isEmpty()) {
			inputLayoutName.setError(getString(R.string.err_msg_name));
			requestFocus(eNama);
			return false;
		} else {
			inputLayoutName.setErrorEnabled(false);
		}
		return true;
	}

	private boolean validateUserName() {
		if (eusername.getText().toString().trim().isEmpty()) {
			inputLayoutUserName.setError(getString(R.string.err_msg_user_name));
			requestFocus(eusername);
			return false;
		} else {
			inputLayoutUserName.setErrorEnabled(false);
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
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	private void Register(String save){
		dialog.show();
		StringRequest register = new StringRequest(Method.POST, save, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				VolleyLog.d("Respone", response.toString());
				try {
					JSONObject jsonrespon = new JSONObject(response);
					int Sucsess = jsonrespon.getInt("success");
					slasid		= String.valueOf(Sucsess);
					Log.i("Suceses", String.valueOf(Sucsess));
					if (Sucsess > 0 ){
						String status=null;
						if(tipeLogin.equals("User")){
							status="USER";
						}else{
							status="CUST";
						}
                        /*session.createLoginSession(0, eusername.getText().toString(),
                                eNama.getText().toString(), eTelp.getText().toString(),
								eEmail.getText().toString(), status);
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivityForResult(i, 5000);
                        getActivity().overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
                        getActivity().finish();*/
						refresh();
						dialog.cancel();
						Toast.makeText(getActivity(),
								"Register berhasil. \nMasuk melalui menu LOGIN!", Toast.LENGTH_LONG)
								.show();
					}else{
						Toast.makeText(getActivity(),
								"Gagal Coba Lagi", Toast.LENGTH_LONG)
								.show();
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, error.toString());
			}
		}){
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Nama", eusername.getText().toString().trim());
				params.put("Pasw", ePassword.getText().toString().trim());
				params.put("NamaLengkap", eNama.getText().toString());
				params.put("telp", eTelp.getText().toString()==null?"":eTelp.getText().toString());
				params.put("email", eEmail.getText().toString()==null?"":eEmail.getText().toString());
				return params;
			}
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String,String> params = new HashMap<String, String>();
				params.put("Content-Type","application/x-www-form-urlencoded");
				return params;
			}
		};
		AppController.getInstance().addToRequestQueue(register);
	}

	private void refresh(){
		eNama.setText("");
		eusername.setText("");
		eTelp.setText("");
		eEmail.setText("");
		ePassword.setText("");
		eusername.setFocusable(true);
	}
}