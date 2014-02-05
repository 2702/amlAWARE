package pl.edu.agh.eis.listview;

import java.util.List;

import pl.edu.agh.eis.amlprofile.R;
import pl.edu.agh.eis.amlprofile.StartActivity;
import pl.edu.agh.eis.datasource.Profile;
import pl.edu.agh.eis.datasource.ProfileType;
import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileArrayAdapter extends ArrayAdapter<Profile> {
  private final Context context;
  private final List<Profile> profiles;
  Typeface titleFont;
  Typeface descriptionFont;
private Profile currentProfile;
  
  public static class ViewHolder{
	  /**
	   * Consider using ViewHolder pattern
	   */
  }
  
  public MyProfileArrayAdapter(Context context, List<Profile> profileList, Profile currentProfile) {
    super(context,R.layout.profile_row, profileList);
    this.context = context;
    this.profiles = profileList;
    this.currentProfile = currentProfile;
  }

  @Override
  public View getView(final int position, View rowView, ViewGroup parent) {    
    if (rowView == null) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	rowView = inflater.inflate(R.layout.profile_row, null);
    }
    
    titleFont = Typeface.createFromAsset(getContext().getAssets(), "ChampagneLimousinesBold.ttf");
    descriptionFont = Typeface.createFromAsset(getContext().getAssets(), "ChampagneLimousines.ttf");
    
    TextView profileTitle = (TextView) rowView.findViewById(R.id.profile_title);
    profileTitle.setTypeface(titleFont);
    profileTitle.setText(profiles.get(position).getName());
    
    ImageView icon = (ImageView) rowView.findViewById(R.id.profile_icon_image);
    icon.setColorFilter(new LightingColorFilter(Color.BLACK, Color.BLACK));
    
    
    
    final StartActivity activity = (StartActivity) context;
    final Profile profile = profiles.get(position);
    
    
    icon.setImageResource(getIconByType(profile.getType()));
    
    if (currentProfile != null && currentProfile.equals(profile)) {
    	rowView.setBackgroundColor(Color.LTGRAY);
    } else {
    	rowView.setBackgroundColor(Color.WHITE);
    }
    
    rowView.setOnLongClickListener(new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(getContext(), "deleting " + profile.getName(), Toast.LENGTH_LONG).show();
			activity.getProfileManager().deleteProfile(profile);
			activity.reloadProfiles();
			return true;
		}
	});
    
    return rowView;
  }

private int getIconByType(ProfileType type) {
	switch(type) {
	case SILENT:
		return android.R.drawable.ic_lock_silent_mode;
	case VIBRATION:
		return android.R.drawable.ic_lock_idle_alarm;
	default:
		return android.R.drawable.ic_lock_silent_mode_off;
	}
	
}
} 