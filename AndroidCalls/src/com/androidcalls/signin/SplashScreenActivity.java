package com.androidcalls.signin;

import com.androidcall.model.UserModel;
import com.androidcalls.R;
import com.androidcalls.Utility;
import com.androidcalls.slidemenu.SlideMenuActivityGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashScreenActivity extends Activity {

	boolean isRunning = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);
		UserModel.initInstance(SplashScreenActivity.this);
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(500);

				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					while (isRunning) {

						if (Utility.getBooleanPreferences(
								SplashScreenActivity.this, "islogin")) {
							Intent intent = new Intent(
									SplashScreenActivity.this,
									SlideMenuActivityGroup.class);
							startActivity(intent);
							isRunning = false;
							finish();
						} else {
							Intent intent = new Intent(
									SplashScreenActivity.this,
									SignInActivity.class);
							startActivity(intent);
							isRunning = false;
							finish();
						}

					}

				}

			}
		}).start();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		isRunning = false;
	}

}
