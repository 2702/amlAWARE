package pl.edu.agh.eis.datasource;

import java.util.ArrayList;

public class Profiles {
	protected ArrayList<Profile> profiles;
	
	public Profiles(){
		profiles = new ArrayList<Profile>();
	}
	
	public ArrayList<Profile> getAllProfiles(){
		return profiles;
	}
	
	public ArrayList<Profile> getAllActive(){
		ArrayList<Profile> active = new ArrayList<Profile>();
		
		for(Profile profile : profiles) {
//			if(profile.isActive) active.add(profile);
		}
		
		return active;
	}
	
}
