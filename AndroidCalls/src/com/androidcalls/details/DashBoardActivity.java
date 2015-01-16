package com.androidcalls.details;

import com.androidcalls.CallDetectService;
import com.androidcalls.R;
import com.androidcalls.Utility;
import com.androidcalls.slidemenu.SlideMenuActivityGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DashBoardActivity extends Activity {

	private boolean detectEnabled;

	private TextView textViewDetectState;
	private Button buttonToggleDetect;
	private Button buttonExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);

		((ImageButton) findViewById(R.id.buttonDetectToggle))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						setDetectEnabled(!detectEnabled);
						((ImageButton) findViewById(R.id.buttonDetectToggle))
								.setSelected(detectEnabled);

					}
				});
		((ImageButton) findViewById(R.id.btn_menu))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						SlideMenuActivityGroup.getInstance().openLeft();
					}
				});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		detectEnabled = Utility.getBooleanPreferences(DashBoardActivity.this,
				"isBlocking");

		if (detectEnabled) {
			((ImageButton) findViewById(R.id.buttonDetectToggle))
					.setSelected(true);
			Intent intent = new Intent(this, CallDetectService.class);
			startService(intent);
		} else {
			((ImageButton) findViewById(R.id.buttonDetectToggle))
					.setSelected(false);
			Intent intent = new Intent(this, CallDetectService.class);
			stopService(intent);
		}

		((TextView) findViewById(R.id.text_count)).setText(""
				+ Utility.getIntegerPreferences(DashBoardActivity.this,
						"call_count"));
		((TextView) findViewById(R.id.txtSendMessage)).setText("sent sms: "
				+ Utility.getIntegerPreferences(DashBoardActivity.this,
						"sms_count"));

	}

	private void setDetectEnabled(boolean enable) {
		detectEnabled = enable;

		Intent intent = new Intent(this, CallDetectService.class);
		if (enable) {
			// start detect service
			startService(intent);

			Utility.setBooleanPreferences(DashBoardActivity.this, "isBlocking",
					true);
		} else {
			// stop detect service
			stopService(intent);

			Utility.setBooleanPreferences(DashBoardActivity.this, "isBlocking",
					false);
		}
	}

}
