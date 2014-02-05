package pl.edu.agh.eis.amlprofile;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pl.edu.agh.eis.datasource.Profile;
import pl.edu.agh.eis.datasource.ProfileType;

/**
* - przechowuje listê profili
* - umo¿liwia ustawienie wybranego profilu (np wycisza telefon)
*/
public class ProfileManager {

	Profile activeProfile;
	List<Profile> profiles = new ArrayList<Profile>();
	private ProfileSQLiteHelper dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = { 
			ProfileSQLiteHelper.COLUMN_NAME,
			ProfileSQLiteHelper.COLUMN_SSID,
			ProfileSQLiteHelper.COLUMN_TYPE
	};
	
	public ProfileManager(Context context) {
		dbHelper = new ProfileSQLiteHelper(context);
		database = dbHelper.getWritableDatabase();
	}
	
	public Profile getActiveProfile() {
		return activeProfile;
	}

	public List<Profile> fetchAllProfiles() {
		Cursor cursor = database.query(
				ProfileSQLiteHelper.TABLE,
		        allColumns , null, null, null, null, null);
		profiles.clear();
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	        profiles.add(new Profile(
	        		cursor.getString(0),
	        		cursor.getString(1),
	        		ProfileType.fromOrdinal(cursor.getInt(2))
	        ));
	        cursor.moveToNext();
	    }
		return profiles;
	}
	
	public void saveProfile(Profile profile) {
	    ContentValues values = new ContentValues();
	    values.put(ProfileSQLiteHelper.COLUMN_NAME, profile.getName());
	    values.put(ProfileSQLiteHelper.COLUMN_SSID, profile.getSSID());
	    values.put(ProfileSQLiteHelper.COLUMN_TYPE, profile.getType().ordinal());
	    long insertId = database.insert(ProfileSQLiteHelper.TABLE, null, values);
	    profiles.add(profile);
	}
	
	public void deleteProfile(Profile p) {
		profiles.remove(profiles.indexOf(p));
		database.delete(ProfileSQLiteHelper.TABLE, ProfileSQLiteHelper.COLUMN_NAME + " = \"" + p.getName() + '"', null);
	}
	
}