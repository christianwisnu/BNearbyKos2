package com.example.chris.nearbykos2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import control.AppController;
import control.CheckValidation;
import control.Link;

/**
 * Created by christian on 19/01/18.
 */

public class AddRekeningCust extends Activity {

    private TextInputLayout inputLayoutNoRek;
    private EditText edNoRek, edNamaBank;
    private Button btnBank, btnSave;
    private String idCust, kodeBank, namaBank, noRek;
    private Integer status, line;
    private ImageView imgBack;
    private ProgressDialog dialog;
    private String saveRek = "saveRekCust.php";
    private String editRek = "editRekCust.php";
    private String cekLine = "getMaxLineRek.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrekeningcust);
        Bundle i = getIntent().getExtras();
        if (i != null){
            try {
                status = i.getInt("Status");
                idCust = i.getString("idCust");
                line = i.getInt("line");
                kodeBank = i.getString("kodeBank");
                namaBank = i.getString("namaBank");
                noRek = i.getString("noRek");
            } catch (Exception e) {}
        }
        inputLayoutNoRek = (TextInputLayout)findViewById(R.id.input_layout_rekening);

        btnBank = (Button)findViewById(R.id.bAddRekBank);
        imgBack = (ImageView)findViewById(R.id.ImbAddRekeningBack);
        edNoRek = (EditText)findViewById(R.id.eAddRekNoRek);
        edNamaBank = (EditText)findViewById(R.id.eAddRekNamaBank);
        btnSave = (Button) findViewById(R.id.bSaveAddRekCust);

        edNoRek.addTextChangedListener(new MyTextWatcher(edNoRek));

        if(status==2){
            btnBank.setVisibility(View.INVISIBLE);
            edNoRek.setText(noRek);
            edNamaBank.setText(namaBank);
        }else {
            btnBank.setVisibility(View.VISIBLE);
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edNoRek.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckValidation.hasText(edNoRek, getResources().getString(R.string.err_msg_norek));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(validation()){
                    dialog = new ProgressDialog(AddRekeningCust.this);
                    dialog.setCancelable(true);
                    dialog.setMessage("Loading ...\nPlease Wait!");
                    dialog.show();
                    if (status==1){
                        getMaxNumber(Link.FilePHP+cekLine+"?kodeCust="+idCust+"&kodeBank="+kodeBank);
                    }else if (status==2){
                        editData(Link.FilePHP+editRek);
                    }
                }
            }
        });

        btnBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddRekeningCust.this, SearchBank.class);
                startActivityForResult(i,7);
            }
        });
    }

    private void getMaxNumber(String Url){
        JsonObjectRequest jsonget = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d("Respone", response.toString());
                        try {
                            int sucses= response.getInt("success");
                            Log.i("Status", String.valueOf(sucses));
                            if (sucses==1){
                                JSONArray JsonArray = response.getJSONArray("uploade");
                                JSONObject object = JsonArray.getJSONObject(0);
                                Integer lineku;
                                if(object.getString("count").equals("null"))
                                    lineku=1;
                                else{
                                    Integer maxNo = object.getInt("count");
                                    lineku = maxNo+1;
                                }
                                saveRek(Link.FilePHP+saveRek, lineku);
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "Data tidak dapat diproses!", Toast.LENGTH_LONG)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getMessage();
                    }
                }){
                    @Override
                    public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                        java.util.Map<String,String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/json");
                        return params;
                    }
                };
        AppController.getInstance().getRequestQueue().getCache().invalidate(Url, true);
        AppController.getInstance().addToRequestQueue(jsonget);
    }

    private void saveRek(String save, final Integer lineku){
        StringRequest register = new StringRequest(Request.Method.POST, save, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                VolleyLog.d("Respone", response.toString());
                try {
                    JSONObject jsonrespon = new JSONObject(response);
                    int Sucsess = jsonrespon.getInt("success");
                    Log.i("Suceses", String.valueOf(Sucsess));
                    if (Sucsess > 0 ){
                        Toast.makeText(AddRekeningCust.this,
                                "Data berhasil disimpan", Toast.LENGTH_LONG)
                                .show();
                        finish();

                    }else{
                        Toast.makeText(AddRekeningCust.this,
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
                //VolleyLog.d(TAG, error.toString());
            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("kodeCust", idCust);
                params.put("kodeBank", kodeBank);
                params.put("line", String.valueOf(lineku));
                params.put("noRek", edNoRek.getText().toString());
                return params;
            }
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                java.util.Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(register);
    }

    private void editData(String save){
        StringRequest simpan = new StringRequest(Request.Method.POST, save,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        //VolleyLog.d(TAG, response.toString());
                        try {
                            JSONObject jsonrespon = new JSONObject(response);
                            int Sucsess = jsonrespon.getInt("success");
                            if (Sucsess >0 ){
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "Edit Rekening gagal!", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("kodeCust", idCust);
                params.put("kodeBank", kodeBank);
                params.put("line", String.valueOf(line));
                params.put("noRek", edNoRek.getText().toString());
                return params;
            }
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                java.util.Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(simpan);
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
                case R.id.input_layout_rekening:
                    validateRek();
                    break;
            }
        }
    }

    private boolean validateRek() {
        if (edNoRek.getText().toString().trim().isEmpty()) {
            inputLayoutNoRek.setError(getString(R.string.err_msg_norek));
            requestFocus(edNoRek);
            return false;
        } else {
            inputLayoutNoRek.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validation() {
        boolean val = true;
        if (!CheckValidation.hasText(edNoRek, getResources().getString(R.string.err_msg_norek)))
            val = false;
        if (!CheckValidation.hasText(edNamaBank, getResources().getString(R.string.err_msg_namabank)))
            val = false;
        /*if(edNamaBank.getText().toString()==null || edNamaBank.getText().toString().trim().equals("")){
            Toast.makeText(AddRekeningCust.this,
                    "Bank harus di isi", Toast.LENGTH_LONG)
                    .show();
            val = false;
        }*/
        return val;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {//bank
            if(resultCode == RESULT_OK) {
                kodeBank = data.getStringExtra("kodeBank");
                namaBank = data.getStringExtra("namaBank");
                edNamaBank.setText(namaBank);
            }
        }
    }
}
