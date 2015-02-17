package com.androidcalls;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.json.JSONObject;

import com.android.internal.telephony.ITelephony;
import com.androidcall.model.CallingModel;
import com.androidcalls.db.DBHelper;
import com.androidcalls.slidemenu.LeftMenuActivity;
import com.androidcalls.slidemenu.SlideMenuActivityGroup;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Helper class to detect incoming and outgoing calls.
 * 
 * @author Aseem Sharma.
 *
 */
public class CallHelper {

	/**
	 * Listener to detect incoming calls.
	 */
	private ITelephony telephonyService;

	private Context ctx;
	private TelephonyManager tm;
	private CallStateListener callStateListener;
	public static int count = 0;
	private OutgoingReceiver outgoingReceiver;
	CallingModel model;
	String message = "";
	static boolean isRunning = false;

	public CallHelper(Context ctx) {
		this.ctx = ctx;
		callStateListener = new CallStateListener();
		outgoingReceiver = new OutgoingReceiver();
		model = new CallingModel();
	}

	private class CallStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			if (!isRunning && state == TelephonyManager.CALL_STATE_RINGING) {
				isRunning = true;
				Toast.makeText(ctx, "Incoming: " + incomingNumber,
						Toast.LENGTH_LONG).show();
				TelephonyManager tmngr = (TelephonyManager) ctx
						.getSystemService(Context.TELEPHONY_SERVICE);
				try {
					Class c = Class.forName(tmngr.getClass().getName());
					Method m = c.getDeclaredMethod("getITelephony");
					m.setAccessible(true);
					telephonyService = (ITelephony) m.invoke(tmngr);

					final String phoneNumber = incomingNumber;
					if ((phoneNumber != null)
							&& Utility.getBooleanPreferences(ctx, "isBlocking")) {
						telephonyService.endCall();

						model.phone_number = phoneNumber;
						message = phoneNumber
								+ Utility.getStringPreferences(ctx, "send_sms");
						if (Utility.getBooleanPreferences(ctx, "skiplogin") == false) {
							AQuery aq = new AQuery(ctx);
							String url = "http://app.maptrax.in/phoneservice/callservice.svc/AddMissedCall";
							HashMap<String, String> map = new HashMap<String, String>();
							JSONObject input = new JSONObject();
							input.putOpt("MobileNumber", phoneNumber+"");
							input.putOpt("Email", "dhavan.rathore@gmail.com");
							JSONObject object = new JSONObject(Utility.getStringPreferences(ctx,"userdata"));
							map.put("MobileNumber", phoneNumber);
							map.put("Email",  object.getString("email"));
							aq.post(url, input, JSONObject.class,
								new AjaxCallback<JSONObject>() {
									@Override
									public void callback(String url,
											JSONObject object,
											AjaxStatus status) {
										// Object = {"TotalMissedCalls":0,"MissedCalls":[],"ErrorCode":"1","SuccessMessage":"Missed Call has been Added Successfully.","ErrorMessage":null}
										insertMessage();
									}
								});
						} else {
							insertMessage();

						}

					}
					isRunning = false;
				} catch (Exception e) {
					e.printStackTrace();
					isRunning = false;
				}
			}

		}

	}

	// Insert message to database and send message.
	private void insertMessage() {
		DBHelper db = new DBHelper(ctx);

		long id = db.insertCallingData(model);
		if (id != -1) {
			int count = Utility.getIntegerPreferences(ctx, "call_count");

			Utility.setIntegerPreferences(ctx, "call_count", count + 1);

			SlideMenuActivityGroup.getInstance().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {

						SmsManager smsManager = SmsManager.getDefault();
						smsManager.sendTextMessage(model.phone_number, null,
								message, null, null);

						int count_sms = Utility.getIntegerPreferences(ctx,
								"sms_count");

						Utility.setIntegerPreferences(ctx, "sms_count",
								count_sms + 1);
						

					} catch (Exception e) {
						Toast.makeText(ctx,
								"SMS faild, please try again later!",
								Toast.LENGTH_LONG).show();
						
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * Broadcast receiver to detect the outgoing calls.
	 */
	public class OutgoingReceiver extends BroadcastReceiver {
		public OutgoingReceiver() {
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

			// Toast.makeText(ctx, "Outgoing: " + number, Toast.LENGTH_LONG)
			// .show();
		}

	}

	/**
	 * Start calls detection.
	 */
	public void start() {
		tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_NEW_OUTGOING_CALL);
		ctx.registerReceiver(outgoingReceiver, intentFilter);
	}

	/**
	 * Stop calls detection.
	 */
	public void stop() {
		tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
		ctx.unregisterReceiver(outgoingReceiver);
	}

}
