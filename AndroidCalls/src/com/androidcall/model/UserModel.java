package com.androidcall.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.androidcalls.Utility;
import com.androidcalls.signin.SignInActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class UserModel implements ConnectionCallbacks,
		OnConnectionFailedListener {
	// Some App constants
	public static int CAMERA_FILTER_NONE = 1;
	public static int CAMERA_FILTER_YELLOW = 2;
	public static int CAMERA_FILTER_GREEN = 3;
	public static int CAMERA_FILTER_PINK = 4;

	private static UserModel instance;
	public static Context applicationContext;
	private static SharedPreferences sharedPreferences;
	public static String AppServiceURL;
	// add variables to use for user
	public static boolean isUserLoggedIn;
	private JSONObject objUser;

	private ProgressDialog dialog;

	int UserID;
	String name, socialid, username, imgpath, gender, token, email;

	// Other Activity's Callback
	public static SignInActivity loginactivity;

	// gmail login stuffs
	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;
	/* Client used to interact with Google APIs. */
	private static GoogleApiClient mGoogleApiClient;

	// facebook login stuffs
	private static final List<String> PERMISSIONS = Arrays.asList("basic_info",
			"email", "gender", "user_location");

	public static AQuery aq;

	public static void initInstance(Context _applicationContext) {
		if (instance == null) {
			// Create the instance
			instance = new UserModel();
			applicationContext = _applicationContext;
			sharedPreferences = applicationContext.getSharedPreferences(
					"Hookd", Context.MODE_PRIVATE);
			AppServiceURL = "http://app.maptrax.in/phoneservice/customerservice.svc/";
			aq = new AQuery(applicationContext);
		}
	}

	public static UserModel getInstance() {
		// Return the instance
		return instance;
	}

	// always call this mehtod to load last loggedin user info
	public void LoadUserInfo() {
		isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false);
		// if user is loggedIn then we have the user data
		if (isUserLoggedIn) {
			String strUInfo = sharedPreferences.getString("userInfo", null);
			if (strUInfo != null) {
				try {
					objUser = new JSONObject(strUInfo);
					UserID = objUser.getJSONObject("user").getInt("userid");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// save the user in local device history
	public void save() {

		Utility.setBooleanPreferences(applicationContext, "islogin", true);
		Utility.setBooleanPreferences(applicationContext, "skiplogin", false);
		Utility.setStringPreferences(applicationContext, "userdata",
				objUser.toString());

	}

	public void logout() {

		if (mGoogleApiClient != null) {
			if (mGoogleApiClient.isConnected()) {
				Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				mGoogleApiClient.disconnect();
			}
		}
		isUserLoggedIn = false;
		Utility.setBooleanPreferences(applicationContext, "islogin", false);
		Utility.setBooleanPreferences(applicationContext, "skiplogin", false);
		Utility.setStringPreferences(applicationContext, "userdata", null);
	}

	// Social Third party logins of Gmail
	public void Sociallogin(final String provider) {
		if (provider.equalsIgnoreCase("gmail")) {
			mGoogleApiClient = new GoogleApiClient.Builder(applicationContext)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).addApi(Plus.API, null)
					.addScope(Plus.SCOPE_PLUS_LOGIN)
					.addScope(Plus.SCOPE_PLUS_PROFILE).build();

			mGoogleApiClient.connect();
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		// after getting result from gmail get signin api
		if (arg0 != null) {
			try {
				if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
					Person currentPerson = Plus.PeopleApi
							.getCurrentPerson(mGoogleApiClient);

					socialid = currentPerson.getId();
					name = currentPerson.getDisplayName();
					email = Plus.AccountApi.getAccountName(mGoogleApiClient);
					gender = (currentPerson.getGender() == Person.Gender.MALE) ? "male"
							: "female";
					username = currentPerson.getDisplayName();
					// by default the profile url gives 50x50 px image only
					// we can replace the value with whatever dimension we want
					// by
					// replacing sz=X
					String personPhotoUrl = currentPerson.getImage().getUrl();
					imgpath = personPhotoUrl.substring(0,
							personPhotoUrl.length() - 2)
							+ "320";

				} else {
					System.out.println("user denided");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		System.out.println("Gmail onConnectionFailed");
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		mGoogleApiClient.connect();
		System.out.println("gmail onConnectionSuspended");
	}

	public static void setGoogleClient(GoogleApiClient client) {
		mGoogleApiClient = client;
	}

	// Getter Google Client
	public static GoogleApiClient getGoogleClient() {
		return mGoogleApiClient;
	}

	public void onGoogleClientConnected() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);

				socialid = currentPerson.getId();
				name = currentPerson.getDisplayName();
				email = Plus.AccountApi.getAccountName(mGoogleApiClient);
				gender = (currentPerson.getGender() == Person.Gender.MALE) ? "male"
						: "female";
				username = currentPerson.getDisplayName();
				// by default the profile url gives 50x50 px image only
				// we can replace the value with whatever dimension we want by
				// replacing sz=X
				String personPhotoUrl = currentPerson.getImage().getUrl();
				imgpath = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2) + "320";
				objUser = new JSONObject();
				objUser.put("name", name);
				objUser.put("email", email);
				objUser.put("gender", gender);
				objUser.put("username", username);
				objUser.put("profile_image", imgpath);

				System.out.println(objUser.toString() + "<<<");

				login(email, "gmail");
			} else {
				System.out.println("user denided");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login(String email, String provider) {
		// AQuery aq = new AQuery(applicationContext);
		String url = AppServiceURL;

		url = AppServiceURL + "authenticatemobileuser/" + email;

		aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				if (json != null) {

					{
						try {
							if (json.getString("ErrorCode").contains("1")) {
								isUserLoggedIn = true;

								// save user info
								save();

								loginactivity.CompleteLoginHandler(
										isUserLoggedIn, "");

							} else {
								isUserLoggedIn = false;

							}
							loginactivity.CompleteLoginHandler(isUserLoggedIn,
									json.getString("ErrorMessage"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});

	}

	// return the user info
	public JSONObject GetUserInfo() {
		return this.objUser;
	}

	public void showProgressHUD(Context context, String title, String message) {
		if (title == null)
			title = "";
		if (message == null)
			message = "";
		dialog = ProgressDialog.show(context, title, message);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);

	}

	public void hideProgressHud() {
		if (dialog != null)
			dialog.cancel();
	}

}
