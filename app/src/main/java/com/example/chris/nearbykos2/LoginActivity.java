package com.example.chris.nearbykos2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import control.AppController;
import control.Link;
import control.Utils;
import model.ColCustomer;
import model.ColUser;
import service.BaseApiService;
import session.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private BaseApiService mApiService;
    private ArrayList<ColUser> columnlist = new ArrayList<ColUser>();
    private ArrayList<ColCustomer> columnlistCust = new ArrayList<ColCustomer>();
    private SessionManager session;

    @BindView(R.id.bLoginNew)Button btnLogin;
    @BindView(R.id.btnLoginClearUser)Button btnClearUser;
    @BindView(R.id.input_layout_login_userid)TextInputLayout inputLayoutUser;
    @BindView(R.id.input_layout_login_sandi)TextInputLayout inputLayoutPasw;
    @BindView(R.id.eLoginUserID)EditText eUserID;
    @BindView(R.id.eLoginSandi)EditText ePassword;
    @BindView(R.id.txtLoginSignUp)TextView txtSignUp;
    @BindView(R.id.spLoginNew)Spinner spStatusUser;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mApiService         = Link.getAPIService();
        ButterKnife.bind(this);
        session		= new SessionManager(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    @OnTextChanged(value = R.id.eLoginUserID, callback = OnTextChanged.Callback.TEXT_CHANGED)
    protected void txtChangePass(){
        btnClearUser.setVisibility(View.VISIBLE);
    }

    @OnTextChanged(value = R.id.eLoginSandi, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void afterTxtChangePass(Editable editable){
        validatePasw(editable.length());
    }

    @OnTextChanged(value = R.id.eLoginUserID, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void afterTxtChangeUser(Editable editable){
        validateUser(eUserID);
    }

    @OnClick(R.id.txtLoginSignUp)
    protected void signUp(){
        startActivityForResult(new Intent(LoginActivity.this, Register.class),3);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @OnClick(R.id.btnLoginClearUser)
    protected void clearUser(){
        eUserID.setText("");
        btnClearUser.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.bLoginNew)
    protected void loginOther(){
        if(validateUser(eUserID) && validatePasw(ePassword.length())){
            if(String.valueOf(spStatusUser.getSelectedItem()).equals("User")){
                SingUp(Link.FilePHP + "loginUser.php");
            }else{
                SingUp(Link.FilePHP + "loginCust.php");
            }
        }
    }

    private void SingUp(String save){
        pDialog.setMessage("Login ...");
        showDialog();
        StringRequest simpan = new StringRequest(Request.Method.POST, save,
                new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonrespon = new JSONObject(response);
                            int Sucsess = jsonrespon.getInt("success");
                            hideDialog();
                            if (Sucsess==1){
                                JSONArray JsonArray = jsonrespon.getJSONArray("Melbu");
                                String status=null;
                                if(String.valueOf(spStatusUser.getSelectedItem()).equals("User")){
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

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivityForResult(i, 5000);
                                overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this,
                                        "Gagal Coba Lagi", Toast.LENGTH_LONG)
                                        .show();
                                hideDialog();
                            }
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this,
                                    e.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                            hideDialog();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("passwd", ePassword.getText().toString().trim());
                params.put("username", eUserID.getText().toString().trim());
                return params;
            }
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().invalidate(save, true);
        AppController.getInstance().addToRequestQueue(simpan);
    }

    private boolean validateUser(EditText edittext) {
        boolean value;
        if (eUserID.getText().toString().isEmpty()){
            value=false;
            requestFocus(eUserID);
            inputLayoutUser.setError(getString(R.string.err_msg_user));
        } else{
            value=true;
            inputLayoutUser.setError(null);
        }
        return value;
    }

    private boolean validatePasw(int length) {
        boolean value = true;
        int minValue = 5;
        if (ePassword.getText().toString().trim().isEmpty()) {
            value=false;
            requestFocus(ePassword);
            inputLayoutPasw.setError(getString(R.string.err_msg_sandi));
        } else if (length > inputLayoutPasw.getCounterMaxLength()) {
            value=false;
            inputLayoutPasw.setError("Max character password length is " + inputLayoutPasw.getCounterMaxLength());
        } else if (length < minValue) {
            value=false;
            inputLayoutPasw.setError("Min character password length is 5" );
        } else{
            value=true;
            inputLayoutPasw.setError(null);}
        return value;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        Utils.freeMemory();
        super.onDestroy();
        Utils.trimCache(this);
    }
}