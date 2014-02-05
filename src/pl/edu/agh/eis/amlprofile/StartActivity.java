package pl.edu.agh.eis.amlprofile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.edu.agh.eis.datasource.Profile;
import pl.edu.agh.eis.datasource.ProfileType;
import pl.edu.agh.eis.listview.MyProfileArrayAdapter;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	private AMLReasoner reasoner;

	private AudioManager audioManager;
	
	public ProfileManager getProfileManager() {
		return profileManager;
	}

	public void setProfileManager(ProfileManager profileManager) {
		this.profileManager = profileManager;
	}

	private ProfileManager profileManager;
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String ssid = getCurrentSsid(context);
			if (ssid != null) {
				Log.w("AML", ssid);
				StartActivity.this.setSSID(ssid);
			} else {
				Log.w("AML", "null");
			}
		}
		
	};

	private Profile currentProfile;
	
	public static final int NO_OF_TABS = 3;

	public void setSSID(String SSID) {
		if (reasoner != null)
		{
			reasoner.processSSID(SSID);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("ACTION_AWARE_WIFI_SCAN_ENDED");
		this.registerReceiver(this.broadcastReceiver, intentFilter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(this.broadcastReceiver);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		
		initializeContextFunctionality();
		
		setContentView(R.layout.activity_start);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

	}

	private void initializeContextFunctionality() {
		profileManager = new ProfileManager(this);
		reasoner = new SimpleReasoner(profileManager, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		List<Fragment> fragments = new ArrayList<Fragment>();
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments.add(new StatusSectionFragment());
			fragments.add(new ProfilesSectionFragment());
			fragments.add(new MapSectionFragment());
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			// returns NO_OF_TABS
			return NO_OF_TABS;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_start).toUpperCase(l);
			case 1:
				return getString(R.string.title_profiles).toUpperCase(l);
			case 2:
				return getString(R.string.title_map).toUpperCase(l);
			}
			return null;
		}
		
		public void reloadProfilesFragment(Profile currentProfile) {
			ProfilesSectionFragment fragment = (ProfilesSectionFragment) fragments.get(1);
			fragment.reloadProfiles(currentProfile);
		}
	}

	/**
	 * A status fragment representing a section of the app responsible for displaying 
	 * currently active profile.
	 */
	public static class StatusSectionFragment extends Fragment {

		public StatusSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_status_activity,
					container, false);

			return rootView;
		}
	}
	
	/**
	 * A status fragment representing a section of the app responsible for displaying 
	 * currently active profile.
	 */
	public static class ProfilesSectionFragment extends ListFragment {

		public ProfilesSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_profiles_activity,
					container, false);
			reloadProfiles(null);
			return rootView;
		}

		private void reloadProfiles(Profile currentProfile) {
			final StartActivity startActivity = (StartActivity) getActivity();
			MyProfileArrayAdapter adapter = new MyProfileArrayAdapter(getActivity(),
					startActivity.getProfileManager().fetchAllProfiles(), currentProfile);
			setListAdapter(adapter);
		}
	}
	
	
	/**
	 * A status fragment representing a section of the app responsible for displaying 
	 * currently active profile.
	 */
	public static class MapSectionFragment extends Fragment {

		public MapSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_map_activity,
					container, false);
	
			return rootView;
		}
	}
	
	public void reloadProfiles() {
		mSectionsPagerAdapter.reloadProfilesFragment(currentProfile);
	}
	
	public void addProfileHandle(View v){
		showEditForm();
	}

	private void showEditForm() {
		final Dialog dialog = new Dialog(StartActivity.this);
		dialog.setContentView(R.layout.profile_form);
		dialog.setTitle("Profile Details");

		final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.profileFormType);
		
		final EditText nameEdit = (EditText) dialog.findViewById(R.id.profileFormName);
		final EditText ssidEdit = (EditText) dialog.findViewById(R.id.profileFormSSID);
		
		String currentSsid = getCurrentSsid(StartActivity.this);
		ssidEdit.setText(currentSsid != null ? currentSsid : "");
		
		Button okButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ProfileType type = null;
				switch(rg.getCheckedRadioButtonId()) {
				case R.id.profileSilent:
					type = ProfileType.SILENT;
					break;
				case R.id.profileVibration:
					type = ProfileType.VIBRATION;
					break;
				case R.id.profileLoud:
					type = ProfileType.LOUD;
					break;
				}
				profileManager.saveProfile(new Profile(
					nameEdit.getText().toString(), 
					ssidEdit.getText().toString(), 
					type
				));
				reloadProfiles();
				dialog.dismiss();
			}
		});

		Button cancelButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	private String getCurrentSsid(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo.isConnected()) {
			final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			if (connectionInfo != null)
			{
				String ssid = connectionInfo.getSSID();
				if (ssid != null && !ssid.equals("")) {
					return ssid.substring(1, ssid.length()-1);
				}
			}
		}
		return null;
	}

	private void activateProfile(Profile profile) {
		if (!profile.equals(currentProfile)) {
			Toast.makeText(this, "Activating profile " + profile.getName(), Toast.LENGTH_LONG).show();
			this.currentProfile = profile;
			audioManager.setRingerMode(profile.getRingerMode());
			reloadProfiles();
		}
	}

	public void setCurrentProfile(Profile currentProfile) {
		activateProfile(currentProfile);
	}	
	
		
}
