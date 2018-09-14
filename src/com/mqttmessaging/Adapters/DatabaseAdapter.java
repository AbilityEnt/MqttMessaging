package com.mqttmessaging.Adapters;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.mqttmessaging.List.ConnectionList;

public class DatabaseAdapter {

	DataHelper helper;
	SQLiteDatabase db;

	public DatabaseAdapter(Context context) {
		helper = new DataHelper(context);
	}

	// put all values into connection table
	public long addConnection(String conName, String conId) {
		open();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DataHelper.CON_NAME, conName);
		contentValues.put(DataHelper.CON_DEV_ID, conId);
		long lid = db.insert(DataHelper.CONNECTION_TABLE, null, contentValues);
		close();
		return lid;
	}

	// Get's the list
	public ArrayList<ConnectionList> getConList() {
		open();
		Cursor c = db.query(DataHelper.CONNECTION_TABLE, null, null, null,
				null, null, null);
		if (c != null && c.moveToFirst()) {
			ArrayList<ConnectionList> connection = new ArrayList<ConnectionList>();
			while (!c.isAfterLast()) {
				String cname = c.getString(c
						.getColumnIndex(DataHelper.CON_NAME));
				String cid = c.getString(c
						.getColumnIndex(DataHelper.CON_DEV_ID));

				connection.add(new ConnectionList(cname, cid));

				c.moveToNext();
			}
			close();
			return connection;
		}
		close();
		return null;
	}

	// Gets the category Id from the category name
	public String getConIdByName(String conName) {
		open();
		String[] columns = { DataHelper.CON_ID };
		String[] selectionArgs = { conName };
		Cursor cursor = db.query(DataHelper.CONNECTION_TABLE, columns,
				DataHelper.CON_NAME + " =?", selectionArgs, null, null, null);
		StringBuffer buffer = new StringBuffer();
		while (cursor.moveToNext()) {
			int conIdIndex = cursor.getColumnIndex(DataHelper.CON_ID);

			String cid = cursor.getString(conIdIndex);

			buffer.append(cid);

		}
		close();
		return buffer.toString();
	}

	public long deleteConnection(String conId) {
		open();
		String[] whereArgs = { conId };
		int count = db.delete(DataHelper.CONNECTION_TABLE, DataHelper.CON_ID
				+ " = ?", whereArgs);
		close();
		return count;
	}

	static class DataHelper extends SQLiteOpenHelper {
		public DataHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;
		}

		// Strings
		private static final String DATABASE_NAME = "mqttdatabase";
		private static final int DATABASE_VERSION = 1;
		private static final String CONNECTION_TABLE = "CONNECTION";
		private static final String SENT_MESSAGE_TABLE = "SENTMESSAGE";
		private static final String RECEIVED_MESSAGE_TABLE = "RECEIVEDMESSAGE";

		// USER Strings
		private static final String CON_ID = "_id";
		private static final String CON_DEV_ID = "condevid";
		private static final String CON_NAME = "conname";

		// SENT MESSAGE Strings
		private static final String S_MES_ID = "_id";
		private static final String S_MES = "sentmessage";
		private static final String S_MES_DATE = "sentdate";

		// RECIEVED MESSAGE Strings
		private static final String R_MES_ID = "_id";
		private static final String R_MES_DEV = "fromdevice";
		private static final String R_MES = "receivedmessage";
		private static final String R_MES_DATE = "receiveddate";

		// Other needed var's
		private Context context;

		// Creation strings
		private static final String CREATE_CON_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ CONNECTION_TABLE
				+ " ("
				+ CON_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ CON_DEV_ID
				+ " VARCHAR(255), " + CON_NAME + " VARCHAR(255)); ";

		private static final String CREATE_S_MES_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ SENT_MESSAGE_TABLE
				+ " ("
				+ S_MES_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ S_MES
				+ " TEXT, "
				+ S_MES_DATE + " DEFAULT CURRENT_TIMESTAMP); ";

		private static final String CREATE_R_MES_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ RECEIVED_MESSAGE_TABLE
				+ " ("
				+ R_MES_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ R_MES_DEV
				+ " VARCHAR(255), "
				+ R_MES
				+ " TEXT, "
				+ R_MES_DATE
				+ " DEFAULT CURRENT_TIMESTAMP); ";

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(CREATE_CON_TABLE);
				db.execSQL(CREATE_S_MES_TABLE);
				db.execSQL(CREATE_R_MES_TABLE);
			} catch (Exception e) {
				Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Drop older table if existed
			db.execSQL("DROP TABLE IF EXISTS " + CONNECTION_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + SENT_MESSAGE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + RECEIVED_MESSAGE_TABLE);

			// Create tables again
			onCreate(db);
		}
	}

	public void open() {
		db = helper.getWritableDatabase();
	}

	public void close() {
		db.close();
	}
}
