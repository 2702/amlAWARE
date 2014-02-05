package pl.edu.agh.eis.datasource;

import android.media.AudioManager;
import pl.edu.agh.eis.amlprofile.R;

/**
 * Class representing user profile.
 * TODO: Think how to represent conditions and Profile configuration
 *
 */
public class Profile {
	protected String name;
	private String SSID;
	private ProfileType type;
	
	public Profile(String name, String SSID, ProfileType type) {
		this.name = name;
		this.SSID = SSID;
		this.type = type;
//		switch(type) {
//		case SILENT:
//			R.drawable.ic_lock_silent_mode
//		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getSSID() {
		return SSID;
	}
	
	public ProfileType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profile other = (Profile) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int getRingerMode() {
		if (type == ProfileType.SILENT){
			return AudioManager.RINGER_MODE_SILENT;
		} else if (type == ProfileType.VIBRATION) {
			return AudioManager.RINGER_MODE_VIBRATE;
		}
		
		return AudioManager.RINGER_MODE_NORMAL;
	}

	

}
