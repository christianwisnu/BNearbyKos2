package com.example.chris.nearbykos2;

import session.SessionManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import com.astuetz.PagerSlidingTabStrip;

public class SignUp extends FragmentActivity{
		
	private PagerSlidingTabStrip pgssignup;
	private ViewPager vprsignup;
	private MyPagerAdapter adapter;
	private SessionManager session;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		session		= new SessionManager(SignUp.this);
		
		pgssignup		= (PagerSlidingTabStrip)findViewById(R.id.PgsSignup);
		vprsignup		= (ViewPager)findViewById(R.id.VprSignup);
		
		adapter	= new MyPagerAdapter(getSupportFragmentManager());
		vprsignup.setAdapter(adapter);
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		vprsignup.setPageMargin(pageMargin);
		pgssignup.setViewPager(vprsignup);		
		pgssignup.setTextColor(Color.parseColor("#FFFFFF"));
		
	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Signup", "Login"};

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			 return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public  Fragment getItem(int position) {
			//return SuperAwesomeCardFragment.newInstance(position);			
			switch (position) {
			case 0:				
				return new Register();						
			case 1:				
				return new Login();			
			}
			return null;

		}
		
	}
	
}