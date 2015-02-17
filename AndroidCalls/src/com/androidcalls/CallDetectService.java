package com.androidcalls;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Call detect service. This service is needed, because MainActivity can lost
 * it's focus, and calls will not be detected.
 * 
 * @author Aseem Sharma.
 *
 */
public class CallDetectService extends Service {
	private CallHelper callHelper;	
	public CallDetectService() {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		callHelper = new CallHelper(this);
		callHelper.start();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		callHelper.stop();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// not supporting binding
		return null;
	}
}
