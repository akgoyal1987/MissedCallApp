package com.androidcalls.reports;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.androidcall.model.CallingModel;
import com.androidcalls.R;
import com.androidcalls.db.DBHelper;
import com.androidcalls.slidemenu.SlideMenuActivityGroup;
import com.androidcalls.sms.GroupSMSActivity;

public class ReportActivity extends Activity {
	static Context mContext;
	ArrayList<CallingModel> listModel;
	ListView listCalling;
	ReportListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reports);
		mContext = this;
		listModel = new ArrayList<CallingModel>();

		((ImageButton) findViewById(R.id.btn_menu))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						SlideMenuActivityGroup.getInstance().openLeft();
					}
				});

		((ImageButton) findViewById(R.id.btnMessages))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (listModel.size() > 0) {

							Intent intent = new Intent(ReportActivity.this,
									GroupSMSActivity.class);
							intent.putExtra("list_calling", listModel);
							startActivity(intent);

						}

					}
				});

	}

	public static ReportActivity getInstance() {
		return (ReportActivity) mContext;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DBHelper db = new DBHelper(mContext);
		listModel = db.getCallingRecords();
		listCalling = (ListView) findViewById(R.id.listReports);
		if (listModel != null) {
			adapter = new ReportListAdapter(mContext, R.layout.cell_report,
					listModel);
			listCalling.setAdapter(adapter);
		}

	}

}
