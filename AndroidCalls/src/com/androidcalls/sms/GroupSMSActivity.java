package com.androidcalls.sms;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.androidcall.model.CallingModel;
import com.androidcalls.R;
import com.androidcalls.Utility;
import com.androidcalls.db.DBHelper;

public class GroupSMSActivity extends Activity {

	ArrayList<CallingModel> listModel;

	EditText editSMS;

	TextView txtSendMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupsms);

		DBHelper db = new DBHelper(GroupSMSActivity.this);
		listModel = db.getCallingRecords();

		editSMS = (EditText) findViewById(R.id.editSms);
		txtSendMessage = (TextView) findViewById(R.id.txtSendMessage);

		txtSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String message = editSMS.getText().toString();
				if (message == null || message.trim().equals("")) {
					Utility.showAlert(GroupSMSActivity.this, "Alert",
							"Message cannot be blank");
					return;
				}

				String toNumbers = "";
				for (CallingModel model : listModel) {

					String data = model.phone_number;
					if ("".equals(toNumbers))
						toNumbers = data;
					else
						toNumbers = toNumbers + ";" + data;
				}
				Log.d("Numbers", toNumbers);
				Uri sendSmsTo = Uri.parse("smsto:" + toNumbers);

				Intent intent = new Intent(
						android.content.Intent.ACTION_SENDTO, sendSmsTo);
				intent.putExtra("sms_body", message);
				startActivity(intent);
				finish();

			}
		});

	}
}
