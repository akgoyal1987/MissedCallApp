package com.androidcalls.signin;

import com.androidcall.model.UserModel;
import com.androidcalls.R;
import com.androidcalls.Utility;
import com.androidcalls.slidemenu.SlideMenuActivityGroup;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener {

	private static final int RC_SIGN_IN = 0;
	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;

	private boolean mIntentInProgress, isGmailLoggedInAttemp;

	private boolean mSignInClicked;

	private ConnectionResult mConnectionResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signin);

		UserModel.loginactivity = this;
		isGmailLoggedInAttemp = false;
		((TextView) findViewById(R.id.btnGmailLogin))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						isGmailLoggedInAttemp = true;
						signInWithGplus();
					}
				});
		((TextView) findViewById(R.id.txtSkipLogin))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Utility.setBooleanPreferences(SignInActivity.this,
								"skiplogin", true);
						Intent intent = new Intent(SignInActivity.this,
								SlideMenuActivityGroup.class);
						startActivity(intent);
						finish();

					}
				});

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

	}

	public boolean isEmulator() {
		return Build.MANUFACTURER.equals("unknown");
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException("SenderId is Null");
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	public void CompleteLoginHandler(boolean status) {
		UserModel.getInstance().hideProgressHud();
		if (status) {
			// allow user to get in App and send to Home controller
			// Go to home page
			finish();
			if (isGmailLoggedInAttemp) {
				Intent intent = new Intent(getApplicationContext(),
						SlideMenuActivityGroup.class);
				startActivity(intent);
			}

		} else {
			System.out.println("User can not go to Home page ");
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}
	}

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {

		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		if (mConnectionResult != null && mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		} else if (mConnectionResult == null) {
			signOutFromGplus();
		}
	}

	/**
	 * Sign-out from google
	 * */
	private void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();

		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub

		if (mGoogleApiClient != null && !mGoogleApiClient.isConnecting()) {

			if (!UserModel.isUserLoggedIn && !isGmailLoggedInAttemp) {
				if (mGoogleApiClient != null) {
					if (mGoogleApiClient.isConnected()) {
						Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
						mGoogleApiClient.disconnect();
						mGoogleApiClient.connect();
					}
				}

			} else {

				UserModel.setGoogleClient(mGoogleApiClient);
				if (isGmailLoggedInAttemp) {
					UserModel.getInstance().showProgressHUD(
							SignInActivity.this, "Please wait...", "Logginin");
					UserModel.getInstance().onGoogleClientConnected();
				}
			}
			// if(connectionHint!=null)

		} else if (mConnectionResult == null) {
			signOutFromGplus();
		}
		// Get user's information
		// getProfileInformation();

	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		mGoogleApiClient.connect();
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;
			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();

			}
		}

	}

}
