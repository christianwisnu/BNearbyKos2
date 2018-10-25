package com.example.chris.nearbykos2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import control.AppController;
import control.AsyncInvokeURLTask;
import control.CheckValidation;
import control.Link;
import control.Utils;

/**
 * Created by Chris on 18/01/2018.
 */

public class BayarDP extends Activity {

    private ProgressDialog dialog;
    private ImageView fotoDP;
    private EditText edRekening, edNamaBank, edKodeBank;
    private Integer idBooking, idCust;
    private String simage2="", encodedString, slashid;
    private Button btnSave;
    private String update = "bayarDP.php";
    private Spinner spRek;
    public static ArrayList<HashMap<String, String>> listJenis = new ArrayList<HashMap<String, String>>();
    public static String[] ARRJENIS=null;
    private String hasilspinjenis;
    private TextInputLayout inputLayoutNamaBank, inputLayoutNoRek;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proses_dp);
        Bundle i = getIntent().getExtras();
        if (i != null){
            try {
                idBooking = i.getInt("idBooking");
                idCust = i.getInt("idCust");
            } catch (Exception e) {}
        }
        fotoDP = (ImageView) findViewById(R.id.imgBuktiFotoDP);
        btnSave = (Button) findViewById(R.id.bProsesBukti);
        spRek = (Spinner)findViewById(R.id.spProsesDPListRek);
        edRekening = (EditText)findViewById(R.id.edProsesDPNoRek);
        edNamaBank = (EditText)findViewById(R.id.edProsesDPNamaBank);
        edKodeBank = (EditText)findViewById(R.id.edProsesDPKodeBank);
        inputLayoutNamaBank = (TextInputLayout)findViewById(R.id.input_layout_bayarDP_nama_bank);
        inputLayoutNoRek = (TextInputLayout)findViewById(R.id.input_layout_bayarDP_no_rek);

        edRekening.addTextChangedListener(new MyTextWatcher(edRekening));
        edNamaBank.addTextChangedListener(new MyTextWatcher(edNamaBank));

        fotoDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 2);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    if(simage2==null || simage2.trim().equals("")){
                        Toast.makeText(getApplicationContext(),
                                "Foto bukti transfer harus diisi!", Toast.LENGTH_SHORT)
                                .show();
                    }else{
                        dialog = new ProgressDialog(BayarDP.this);
                        dialog.setCancelable(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setMessage("Sedang diproses ...");
                        dialog.show();
                        editData(Link.FilePHP+update);
                    }
                }
            }
        });

        loadDatafromServer(spRek, edNamaBank, edKodeBank, edRekening, idCust);
    }

    private boolean validation() {
        boolean val = true;
        if (!CheckValidation.hasText(edNamaBank, getResources().getString(R.string.err_msg_namabank)))
            val = false;
        if (!CheckValidation.hasText(edRekening, getResources().getString(R.string.err_msg_norek)))
            val = false;
        return val;
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
                case R.id.input_layout_bayarDP_nama_bank:
                    validateNamaBank();
                    break;
                case R.id.input_layout_bayarDP_no_rek:
                    validateRek();
                    break;
            }
        }
    }

    private boolean validateNamaBank() {
        if (edNamaBank.getText().toString().trim().isEmpty()) {
            inputLayoutNamaBank.setError(getString(R.string.err_msg_namabank));
            return false;
        } else {
            inputLayoutNamaBank.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateRek() {
        if (edRekening.getText().toString().trim().isEmpty()) {
            inputLayoutNoRek.setError(getString(R.string.err_msg_norek));
            return false;
        } else {
            inputLayoutNoRek.setErrorEnabled(false);
        }
        return true;
    }

    public void loadDatafromServer(final Spinner jenis, final EditText namaBank,
                                   final EditText kodeBank, final EditText noRek, final Integer kodeCust) {
        try{
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                    1);
            nameValuePairs.add(new BasicNameValuePair("act", "queryspinner"));
            AsyncInvokeURLTask task = new AsyncInvokeURLTask(nameValuePairs,
                    new AsyncInvokeURLTask.OnPostExecuteListener() {

                        @Override
                        public void onPostExecute(String result) {
                            // TODO Auto-generated method stub
                            Log.d("TAG", "getdataspinner:" + result);
                            setDataSpinner(result, jenis, namaBank, kodeBank, noRek);
                        }
                    });

            task.applicationContext =BayarDP.this;
            task.mNoteItWebUrl = "/getRekCustSpinner.php?kodeCust="+String.valueOf(kodeCust);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDataSpinner(String result, Spinner jenis, final EditText namaBank,
                               final EditText kodeBank, final EditText noRek){
        try{
            JSONObject json = new JSONObject(result);
            if(json.getString("errorcode").equals("0")){
                Toast.makeText(BayarDP.this, "Pemilik Kos belum mempunyai rekening Bank!\nHubungi langsung pemilik kos.", Toast.LENGTH_LONG).show();
                finish();
            }else{
                listJenis = new ArrayList<HashMap<String, String>>();
                JSONArray datameja= json.getJSONArray("datakain");
                for (int i=0; i<datameja.length(); i++){
                    JSONObject jsonobj =datameja.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("i_idcust", jsonobj.getString("i_idcust"));
                    map.put("kode_bank", jsonobj.getString("kode_bank"));
                    map.put("nama_bank", jsonobj.getString("nama_bank"));
                    map.put("i_line", jsonobj.getString("i_line"));
                    map.put("vc_norek", jsonobj.getString("vc_norek"));
                    listJenis.add(map);
                }

                ARRJENIS = new String[listJenis.size()];
                for (int i = 0; i < listJenis.size(); i++) {
                    ARRJENIS[i]=listJenis.get(i).get("nama_bank")+"--"+listJenis.get(i).get("vc_norek");
                }
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }

        ArrayAdapter<String> aameja = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ARRJENIS);
        aameja.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenis.setAdapter(aameja);

        jenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                namaBank.setText(listJenis.get(pos).get("nama_bank"));
                kodeBank.setText(listJenis.get(pos).get("kode_bank"));
                noRek.setText(listJenis.get(pos).get("vc_norek"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == this.RESULT_OK && null != data) {//map
            simage2	= getNameImage().toString()+".jpg";
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Utils.GetCycleImage("file:///"+picturePath, fotoDP, this);
            String fileNameSegments[] = picturePath.split("/");
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            uploadingImage(Link.FileUpload, simage2);
        }
    }

    private String getNameImage() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HHmmss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void editData(String save){
        StringRequest simpan = new StringRequest(Request.Method.POST, save,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonrespon = new JSONObject(response);
                            int Sucsess = jsonrespon.getInt("success");
                            slashid		=String.valueOf(Sucsess);
                            if (Sucsess >0 ){
                                Toast.makeText(getApplicationContext(),
                                        "Proses berhasil!", Toast.LENGTH_SHORT)
                                        .show();
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "Pembayaran DP gagal!", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                //VolleyLog.d(TAG, error.toString());
            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("idBooking", String.valueOf(idBooking));
                params.put("foto", simage2);
                params.put("kodeBank", edKodeBank.getText().toString());
                params.put("noRek", edRekening.getText().toString());
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

    private void uploadingImage(String url, final String ImageName){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Upload ke server...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(BayarDP.this, "Gambar berhasil diupload!" , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        //Showing toast
                        Toast.makeText(BayarDP.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("image", encodedString);
                params.put("filename", ImageName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
