package com.androidcalls;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.androidcall.model.CallingModel;
import com.androidcalls.db.DBHelper;
import com.androidcalls.slidemenu.SlideMenuActivityGroup;

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

	private OutgoingReceiver outgoingReceiver;

	public CallHelper(Context ctx) {
		this.ctx = ctx;

		callStateListener = new CallStateListener();
		outgoingReceiver = new OutgoingReceiver();
	}

	private class CallStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// called when someone is ringing to this phone

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
					Log.d("INCOMING", phoneNumber);
					if ((phoneNumber != null)
							&& Utility.getBooleanPreferences(ctx, "isBlocking")) {
						telephonyService.endCall();
						DBHelper db = new DBHelper(ctx);
						CallingModel model = new CallingModel();
						model.phone_number = phoneNumber;
						final String message = phoneNumber
								+ Utility.getStringPreferences(ctx, "send_sms");
						long id = db.insertCallingData(model);
						if (id != -1) {
							int count = Utility.getIntegerPreferences(ctx,
									"call_count");

							Utility.setIntegerPreferences(ctx, "call_count",
									count + 1);

							SlideMenuActivityGroup.getInstance().runOnUiThread(
									new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											try {

												// Uri smsUri = Uri.parse("sms:"
												// + phoneNumber);
												//
												// Intent sendIntent = new
												// Intent(
												// Intent.ACTION_VIEW,
												// smsUri);
												// sendIntent.putExtra("sms_body",
												// message);
												//
												// sendIntent.putExtra("address",
												// phoneNumber);
												// sendIntent
												// .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
												// sendIntent
												// .setType("vnd.android-dir/mms-sms");
												// ctx.startActivity(sendIntent);

												SmsManager smsManager = SmsManager
														.getDefault();
												smsManager.sendTextMessage(
														phoneNumber, null,
														message, null, null);

												int count_sms = Utility
														.getIntegerPreferences(
																ctx,
																"sms_count");

												Utility.setIntegerPreferences(
														ctx, "sms_count",
														count_sms + 1);

											} catch (Exception e) {
												Toast.makeText(
														ctx,
														"SMS faild, please try again later!",
														Toast.LENGTH_LONG)
														.show();

												e.printStackTrace();
											}
										}
									});

						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			}

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
