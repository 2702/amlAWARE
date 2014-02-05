package pl.edu.agh.eis.amlprofile;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

public class ContextObserver extends ContentObserver {

	public ContextObserver(Handler handler) {
		super(handler);
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
//		Cursor sensorData = getContentResolver().query(Sensor_Data.CONTENT_URI, tableColumns, whereCondition, whereArguments, orderBy);
	}
}
