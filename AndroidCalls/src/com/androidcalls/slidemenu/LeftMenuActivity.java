package com.androidcalls.slidemenu;

import org.json.JSONObject;

import com.androidcall.model.UserModel;
import com.androidcalls.Constants;
import com.androidcalls.R;
import com.androidcalls.Utility;
import com.androidcalls.signin.SignInActivity;
import com.androidcalls.widgets.CircleImageView;
import com.androidquery.AQuery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LeftMenuActivity extends Activity {

	CircleImageView imageViewCircle;
	TextView txtUserName;
	AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_left_menu);

		imageViewCircle = (CircleImageView) findViewById(R.id.imageViewUser);
		txtUserName = (TextView) findViewById(R.id.txtUserName);
		aq = new AQuery(LeftMenuActivity.this);

		if (Utility.getBooleanPreferences(LeftMenuActivity.this, "skiplogin")) {
			((RelativeLayout) findViewById(R.id.layoutProfile))
					.setVisibility(View.GONE);
			((RelativeLayout) findViewById(R.id.layoutLogout))
					.setVisibility(View.GONE);
		} else {
			setProfile();
		}

		((RelativeLayout) findViewById(R.id.layoutDashboard))
				.setBackgroundColor(getResources().getColor(R.color.bg_green));

		((RelativeLayout) findViewById(R.id.layoutDashboard))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						((RelativeLayout) findViewById(R.id.layoutDashboard))
								.setBackgroundColor(getResources().getColor(
										R.color.bg_green));
						((RelativeLayout) findViewById(R.id.layoutReports))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layouteditsms))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutexit))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutLogout))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));

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
						// TODO Auto-generated method stub

						((RelativeLayout) findViewById(R.id.layoutDashboard))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutReports))
								.setBackgroundColor(getResources().getColor(
										R.color.bg_green));
						((RelativeLayout) findViewById(R.id.layouteditsms))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutexit))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutLogout))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));

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
						// TODO Auto-generated method stub

						((RelativeLayout) findViewById(R.id.layoutDashboard))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutReports))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layouteditsms))
								.setBackgroundColor(getResources().getColor(
										R.color.bg_green));
						((RelativeLayout) findViewById(R.id.layoutexit))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutLogout))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));

						Intent in = new Intent(Utility.SLIDEMENURBROADCAST);
						in.putExtra("request", Constants.EDIT_SMS);
						LocalBroadcastManager
								.getInstance(LeftMenuActivity.this)
								.sendBroadcast(in);
					}
				});
		((RelativeLayout) findViewById(R.id.layoutexit))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						((RelativeLayout) findViewById(R.id.layoutDashboard))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutReports))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layouteditsms))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutexit))
								.setBackgroundColor(getResources().getColor(
										R.color.bg_green));
						((RelativeLayout) findViewById(R.id.layoutLogout))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));

						finish();
					}
				});

		((RelativeLayout) findViewById(R.id.layoutLogout))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						((RelativeLayout) findViewById(R.id.layoutDashboard))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutReports))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layouteditsms))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutexit))
								.setBackgroundColor(getResources().getColor(
										R.color.transparent));
						((RelativeLayout) findViewById(R.id.layoutLogout))
								.setBackgroundColor(getResources().getColor(
										R.color.bg_green));

						if (Utility.getBooleanPreferences(
								LeftMenuActivity.this, "islogin")) {
							Intent intent = new Intent(LeftMenuActivity.this,
									SignInActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							UserModel.getInstance().logout();
							finish();
						} else {
							finish();
						}
					}
				});

	}

	private void setProfile() {
		try {
			JSONObject object = new JSONObject(Utility.getStringPreferences(
					LeftMenuActivity.this, "userdata"));

			aq.id(imageViewCircle).image(object.getString("profile_image"),
					true, true);

			txtUserName.setText(object.getString("name"));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
