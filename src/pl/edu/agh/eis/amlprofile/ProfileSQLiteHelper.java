package pl.edu.agh.eis.amlprofile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileSQLiteHelper extends SQLiteOpenHelper {

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SSID = "ssid";
	public static final String COLUMN_TYPE = "type";
	private static final String DATABASE_NAME = "commments.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE = "profiles";
	private static final String DATABASE_CREATE = "create table " + TABLE + "("
			+ COLUMN_NAME + " text not null, " + COLUMN_SSID
			+ " text not null," + COLUMN_TYPE + " integer);";
	
	public ProfileSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Log.w(ProfileSQLiteHelper.class.getName(),
		// "Upgrading database from version " + oldVersion + " to "
		// + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(db);
	}

}
