package com.example.chris.nearbykos2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ErorrBug extends Activity {
    private TextView tvError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erorr_bug);

        tvError     = (TextView)findViewById(R.id.TvErorr);
        tvError.setText(getIntent().getStringExtra("error"));

    }
}
