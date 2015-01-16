package com.androidcalls.sms;

import com.androidcalls.R;
import com.androidcalls.Utility;
import com.androidcalls.slidemenu.SlideMenuActivityGroup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditSMSActivity extends Activity {

	EditText edit_sms;

	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editsms);
		mContext = this;
		edit_sms = (EditText) findViewById(R.id.editSms);
		((ImageButton) findViewById(R.id.btn_menu))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						SlideMenuActivityGroup.getInstance().openLeft();
					}
				});
		((TextView) findViewById(R.id.txtSendMessage))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (edit_sms.getText().toString().length() == 0) {
							Utility.showAlert(EditSMSActivity.this, "Alert",
									"Please enter the sms to save it");
							return;
						}
						Utility.setStringPreferences(EditSMSActivity.this,
								"send_sms", edit_sms.getText().toString());
						Utility.showAlert(EditSMSActivity.this, "Success",
								"Message has been saved successfully");
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		edit_sms.setText(Utility.getStringPreferences(EditSMSActivity.this,
				"send_sms"));
	}
}
