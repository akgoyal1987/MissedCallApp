package com.androidcalls.db;

import java.util.ArrayList;

import com.androidcall.model.CallingModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "calling_db";

	private final static String CALL_TABLE = "call_table";
	private final static String _ID = "_id";
	private final static String PHONE_NUMBER = "phone_number";
	private final static String RECEIVED_TIMESTAMP = "date_time";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String create_call_table = "CREATE TABLE " + CALL_TABLE + "(" + _ID
				+ " integer primary key autoincrement, " + PHONE_NUMBER
				+ " varchar," + RECEIVED_TIMESTAMP + " varchar)";
		db.execSQL(create_call_table);

	}

	// insert missed call data to DB
	public long insertCallingData(CallingModel model) {

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PHONE_NUMBER, model.phone_number);
		values.put(RECEIVED_TIMESTAMP, model.received_timestamp);
		long id = db.insert(CALL_TABLE, null, values);
		return id;

	}

	// get missed call data from DB
	public ArrayList<CallingModel> getCallingRecords() {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<CallingModel> list = new ArrayList<CallingModel>();
		Cursor cursor = db.query(CALL_TABLE, new String[] { _ID, PHONE_NUMBER,
				RECEIVED_TIMESTAMP }, null, null, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					CallingModel model = new CallingModel();
					model.phone_number = cursor.getString(cursor
							.getColumnIndex(PHONE_NUMBER));
					model.received_timestamp = cursor.getString(cursor
							.getColumnIndex(RECEIVED_TIMESTAMP));
					list.add(model);

				} while (cursor.moveToNext());

			}

		}

		return list;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
