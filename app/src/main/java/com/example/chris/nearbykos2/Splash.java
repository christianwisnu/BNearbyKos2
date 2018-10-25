package com.example.chris.nearbykos2;

import java.util.ArrayList;
import model.ColUser;
import session.SessionManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import control.Utils;

public class Splash extends Activity {
	private ImageView ImgTop;
	private TextView TvLoading;
	private ArrayList<ColUser>columnlist = new ArrayList<ColUser>();
	private SessionManager session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		session = new SessionManager(Splash.this);
		
		ImgTop		= (ImageView)findViewById(R.id.ImgSplashTop);
		TvLoading	= (TextView)findViewById(R.id.TvSplashTextLoading);

		Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        animFadeIn.setDuration(1000);
        animFadeIn.setStartOffset(1000);
        animFadeIn.setFillAfter(true);
		
        ImgTop.setAnimation(animFadeIn);
        TvLoading.setAnimation(animFadeIn);		
        
		//Utils.loadGifIntoImageView(ImgTop, R.drawable.icon, Splash.this);
		
		if(session.isLoggedIn()==false){
			Intent i =new Intent(getApplicationContext(), SignUp.class);
			startActivity(i);
			finish();
		}else if (session.isLoggedIn()==true){
			Intent i =new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);	
			finish();
		}
	}
}