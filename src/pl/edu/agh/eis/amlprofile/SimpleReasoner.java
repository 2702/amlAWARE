package pl.edu.agh.eis.amlprofile;

import android.content.Context;
import pl.edu.agh.eis.datasource.Profile;

public class SimpleReasoner implements AMLReasoner {

	private ProfileManager profileManager;
	private Profile currentProfile = null;
	private Context context;
	
	
	public SimpleReasoner(ProfileManager profileManager, Context context) {
		this.profileManager = profileManager;
		this.context = context;
	}

	@Override
	public void processSSID(String SSID) {
		Profile newProfile = getProfileBySSID(SSID);
		if (newProfile != null && !newProfile.equals(currentProfile)) {
			((StartActivity) context).setCurrentProfile(newProfile);
		}
	}

	private Profile getProfileBySSID(String SSID) {
		for (Profile profile : profileManager.fetchAllProfiles())
		{
			if (profile.getSSID().equals(SSID)) {
				return profile;
			}
		}
		return null;
	}

}
