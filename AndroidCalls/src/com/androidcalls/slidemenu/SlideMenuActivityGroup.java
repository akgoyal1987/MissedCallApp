package com.androidcalls.slidemenu;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;

import com.androidcalls.Constants;
import com.androidcalls.R;
import com.androidcalls.Utility;
import com.androidcalls.details.DashBoardActivity;
import com.androidcalls.reports.ReportActivity;
import com.androidcalls.sms.EditSMSActivity;
import com.aretha.slidemenu.SlideMenu;
import com.aretha.slidemenu.SlideMenu.LayoutParams;
import com.aretha.slidemenu.SlideMenu.OnSlideStateChangeListener;

@SuppressWarnings("deprecation")
public class SlideMenuActivityGroup extends ActivityGroup implements
		OnSlideStateChangeListener {
	public static SlideMenu mSlideMenu;
	LocalActivityManager activityManager;
	static Context mContext;

	Intent intntClass;
	int current_activity = Constants.DASHBOARD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_slide_menu);
		mContext = this;
		intntClass = new Intent("requestReciever");
		getSlideMenu().setOnSlideStateChangeListener(this);
		if(Utility.getStringPreferences(mContext, "send_sms")==null){
			Utility.setStringPreferences(mContext, "send_sms",
					"Thank you for taking part in this survey");
		}
		Utility.setBooleanPreferences(mContext, "isLoggedIn", true);
		
	}

	public SlideMenu getSlideMenu() {

		return mSlideMenu;
	}

	public static SlideMenuActivityGroup getInstance() {
		return new SlideMenuActivityGroup();
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mSlideMenu = (SlideMenu) findViewById(R.id.slideMenu);
		mSlideMenu.setPrimaryShadowWidth(5);
		mSlideMenu.setSecondaryShadowWidth(5);
		mSlideMenu.setEdgeSlideEnable(true);
		mSlideMenu.setEdgetSlideWidth(20);

		activityManager = getLocalActivityManager();

		addIntialView();
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
				new IntentFilter(Utility.SLIDEMENURBROADCAST));

		LocalBroadcastManager.getInstance(this).registerReceiver(slideReceiver,
				new IntentFilter("slide"));

		LocalBroadcastManager.getInstance(this).registerReceiver(appLogout,
				new IntentFilter("applogout"));
	}

	private void addIntialView() {
		View primary = activityManager.startActivity("PrimaryActivity",
				new Intent(this, LeftMenuActivity.class)).getDecorView();
		mSlideMenu.addView(primary, new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				LayoutParams.ROLE_PRIMARY_MENU));

		View content = activityManager.startActivity("ContentActivity",
				new Intent(this, DashBoardActivity.class)).getDecorView();
		content.setFocusable(false);
		content.setFocusableInTouchMode(false);
		mSlideMenu.addView(content, new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				LayoutParams.ROLE_CONTENT));
	}

	private BroadcastReceiver slideReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String slide = intent.getStringExtra("slide");
			if (slide.equalsIgnoreCase("left")) {
				mSlideMenu.open(false, true);
				// LocalBroadcastManager.getInstance(mContext).sendBroadcast(new
				// Intent("animationleft"));

			} else {
				mSlideMenu.close(true);
			}
		}
	};

	public void openLeft() {
		mSlideMenu.open(false, true);
	}

	public void closeSlideMenu() {
		mSlideMenu.close(true);
	}

	public void onBackPressed() {
		super.onBackPressed();
		if(current_activity==Constants.DASHBOARD){
			Activity activity = activityManager.getActivity("ContentActivity");
			activity.finish();
		}else if(current_activity==Constants.EDIT_SMS){
//			Intent intent = new Intent();
//			replaceContentView(Constants.EDIT_SMS, intent);
		}else if(current_activity==Constants.REPORTS){
//			Intent intent = new Intent();
//			replaceContentView(Constants.REPORTS, intent);
		}
		
	};

	protected void onResume() {
		super.onResume();
		Activity activity = activityManager.getActivity("PrimaryActivity");
		if (activity != null)
			((LeftMenuActivity) activity).onResume();
	};

	public static void SwitchToRight() {
		mSlideMenu.open(false, true);
	}

	public void replaceContentView(int request, Intent intent) {
		try {
			switch (request) {
			case Constants.DASHBOARD: {

				View oldview = mSlideMenu.getChildAt(1);
				View content = activityManager.startActivity(
						"DashBoardActivity",
						new Intent(mContext, DashBoardActivity.class).putExtra(
								"query", intent.getStringExtra("query")))
						.getDecorView();
				if (oldview != content) {
					mSlideMenu.removeViewAt(1);
					mSlideMenu.addView(content, 1, new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							LayoutParams.ROLE_CONTENT));
				}

				// HashMap<String, String> map = (HashMap<String, String>)
				// intent
				// .getSerializableExtra("mapDataLeft");

				mSlideMenu.close(true);

				// L//ocalBroadcastManager.getInstance(mContext).sendBroadcast(
				// intntClass);

			}
				break;

			case Constants.REPORTS: {
				View oldview = mSlideMenu.getChildAt(1);
				View content = activityManager.startActivity("ReportActivity",
						new Intent(mContext, ReportActivity.class))
						.getDecorView();
				if (oldview != content) {
					mSlideMenu.removeViewAt(1);
					mSlideMenu.addView(content, 1, new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							LayoutParams.ROLE_CONTENT));
				}

				// HashMap<String, String> map = (HashMap<String, String>)
				// intent
				// .getSerializableExtra("mapDataLeft");

				mSlideMenu.close(true);

				// L//ocalBroadcastManager.getInstance(mContext).sendBroadcast(
				// intntClass);

			}
				break;

			case Constants.EDIT_SMS: {
				View oldview = mSlideMenu.getChildAt(1);
				View content = activityManager.startActivity("EditSMSActivity",
						new Intent(mContext, EditSMSActivity.class))
						.getDecorView();
				if (oldview != content) {
					mSlideMenu.removeViewAt(1);
					mSlideMenu.addView(content, 1, new LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							LayoutParams.ROLE_CONTENT));
				}

				// HashMap<String, String> map = (HashMap<String, String>)
				// intent
				// .getSerializableExtra("mapDataLeft");

				mSlideMenu.close(true);

				// L//ocalBroadcastManager.getInstance(mContext).sendBroadcast(
				// intntClass);

			}
				break;

			default:

				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			mSlideMenu.removeAllViews();
			addIntialView();

		}

	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			LocalBroadcastManager.getInstance(mContext).unregisterReceiver(
					receiver);

			int request = intent.getIntExtra("request", 1);

			replaceContentView(request, intent);

			LocalBroadcastManager.getInstance(mContext).registerReceiver(
					receiver, new IntentFilter(Utility.SLIDEMENURBROADCAST));

		}
	};

	private BroadcastReceiver appLogout = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Call logout method from here when called from any where else
		}
	};

	@Override
	public void onSlideStateChange(int slideState) {
		System.out
				.println("This is the slide state of silde menu" + slideState);
		if (slideState == SlideMenu.STATE_CLOSE) {

		}
		if (slideState == SlideMenu.STATE_OPEN_LEFT) {
			// MenuActivity.getInstance().addRangeSlider();

		}

	}

	@Override
	public void onSlideOffsetChange(float offsetPercent) {
		// TODO Auto-generated method stub

	}

}
