package com.androidcalls.slidemenu;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidcall.model.UserModel;
import com.androidcalls.Constants;
import com.androidcalls.R;
import com.androidcalls.Utility;
import com.androidcalls.signin.SignInActivity;
import com.androidcalls.widgets.CircleImageView;
import com.androidquery.AQuery;

public class LeftMenuActivity extends Activity {

	CircleImageView imageViewCircle;
	TextView txtUserName, txtUserEmail;
	AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_left_menu);

		imageViewCircle = (CircleImageView) findViewById(R.id.imageViewUser);
		txtUserName = (TextView) findViewById(R.id.txtUserName);
		txtUserEmail = (TextView) findViewById(R.id.txtUserEmail);
		aq = new AQuery(LeftMenuActivity.this);

		if (Utility.getBooleanPreferences(LeftMenuActivity.this, "skiplogin")) {
			((RelativeLayout) findViewById(R.id.layoutProfile))
					.setVisibility(View.GONE);
		} else {
			setProfile();
		}

		((RelativeLayout) findViewById(R.id.layoutDashboard))
				.setBackgroundColor(getResources().getColor(R.color.active_tab));

		((RelativeLayout) findViewById(R.id.layoutDashboard))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						descelectAllTabs();
						((RelativeLayout) findViewById(R.id.layoutDashboard))
								.setBackgroundColor(getResources().getColor(
										R.color.active_tab));

						Intent in = new Intent(Utility.SLIDEMENURBROADCAST);
						in.putExtra("request", Constants.DASHBOARD);
						LocalBroadcastManager
								.getInstance(LeftMenuActivity.this)
								.sendBroadcast(in);
					}
				});
		((RelativeLayout) findViewById(R.id.layoutReports))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						descelectAllTabs();
						((RelativeLayout) findViewById(R.id.layoutReports))
								.setBackgroundColor(getResources().getColor(
										R.color.active_tab));

						Intent in = new Intent(Utility.SLIDEMENURBROADCAST);
						in.putExtra("request", Constants.REPORTS);
						LocalBroadcastManager
								.getInstance(LeftMenuActivity.this)
								.sendBroadcast(in);
					}
				});

		((RelativeLayout) findViewById(R.id.layouteditsms))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						descelectAllTabs();
						((RelativeLayout) findViewById(R.id.layouteditsms))
								.setBackgroundColor(getResources().getColor(
										R.color.active_tab));

						Intent in = new Intent(Utility.SLIDEMENURBROADCAST);
						in.putExtra("request", Constants.EDIT_SMS);
						LocalBroadcastManager
								.getInstance(LeftMenuActivity.this)
								.sendBroadcast(in);
					}
				});
		((RelativeLayout) findViewById(R.id.layoutprofile))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						descelectAllTabs();
						((RelativeLayout) findViewById(R.id.layoutprofile))
								.setBackgroundColor(getResources().getColor(
										R.color.active_tab));
					}
				});
		((RelativeLayout) findViewById(R.id.layoutsettings))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						descelectAllTabs();
						((RelativeLayout) findViewById(R.id.layoutsettings))
								.setBackgroundColor(getResources().getColor(
										R.color.active_tab));
					}
				});
		((RelativeLayout) findViewById(R.id.layoutlogout))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (Utility.getBooleanPreferences(
								LeftMenuActivity.this, "skiplogin"))
							finish();
						else {
							// Destroy Session or Logout and take him to login
							// activity
							Intent intent = new Intent(LeftMenuActivity.this,
									SignInActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							UserModel.getInstance().logout();
							finish();

						}
					}
				});

	}

	public void descelectAllTabs() {
		((RelativeLayout) findViewById(R.id.layoutDashboard))
				.setBackgroundColor(getResources()
						.getColor(R.color.transparent));
		((RelativeLayout) findViewById(R.id.layoutReports))
				.setBackgroundColor(getResources()
						.getColor(R.color.transparent));
		((RelativeLayout) findViewById(R.id.layouteditsms))
				.setBackgroundColor(getResources()
						.getColor(R.color.transparent));
		((RelativeLayout) findViewById(R.id.layoutprofile))
				.setBackgroundColor(getResources()
						.getColor(R.color.transparent));
		((RelativeLayout) findViewById(R.id.layoutsettings))
				.setBackgroundColor(getResources()
						.getColor(R.color.transparent));
		((RelativeLayout) findViewById(R.id.layoutlogout))
				.setBackgroundColor(getResources()
						.getColor(R.color.transparent));
	}

	private void setProfile() {
		try {
			JSONObject object = new JSONObject(Utility.getStringPreferences(
					LeftMenuActivity.this, "userdata"));

			aq.id(imageViewCircle).image(object.getString("profile_image"),
					true, true);

			txtUserName.setText(object.getString("name"));
			txtUserEmail.setText(object.getString("email"));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
