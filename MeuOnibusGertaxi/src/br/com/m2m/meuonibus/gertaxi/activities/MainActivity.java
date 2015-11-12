package br.com.m2m.meuonibus.gertaxi.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import br.com.hhw.startapp.activities.MenuStartAppActivity;
import br.com.hhw.startapp.helpers.SharedPreferencesHelper;
import br.com.how.hhwslidemenu.HHWMenuItem;
import br.com.how.hhwslidemenu.HHWSlideMenu;
import br.com.m2m.meuonibus.gertaxi.R;
import br.com.m2m.meuonibus.gertaxi.fragments.AjustesFragment;
import br.com.m2m.meuonibus.gertaxi.fragments.FaleConoscoFragment;
import br.com.m2m.meuonibus.gertaxi.fragments.HomeFragment;
import br.com.m2m.meuonibus.gertaxi.fragments.MeusPontosFragment;
import br.com.m2m.meuonibus.gertaxi.fragments.NoticiasFragment;
import br.com.m2m.meuonibuscommons.adapters.PlaceAutocompleteAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

public class MainActivity extends MenuStartAppActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	
	public GoogleApiClient mGoogleApiClient;
	public PlaceAutocompleteAdapter mAdapter;
	protected static final String TAG = "API Places >>>>> ";

	protected void createMenu() {

		if (menuItems == null) {

			
			ArrayList<HHWMenuItem> menuItems = new ArrayList<HHWMenuItem>();
						
			menuItems.add(new HHWMenuItem(getString(R.string.inicio), new HomeFragment(), getResources().getDrawable(R.drawable.mn_item_inicio)));
//			menuItems.add(new HHWMenuItem(getString(R.string.planejador), fragment
//					.newInstance("Planejador de viagens"), getResources().getDrawable(R.drawable.mn_item_planejador)));
			menuItems.add(new HHWMenuItem(getString(R.string.favoritos), new MeusPontosFragment(), getResources().getDrawable(R.drawable.mn_item_meuspontos)));
			menuItems.add(new HHWMenuItem(getString(R.string.noticias), new NoticiasFragment(), getResources().getDrawable(R.drawable.mn_item_noticias)));
			menuItems.add(new HHWMenuItem(getString(R.string.fale_conosco), new FaleConoscoFragment(), getResources().getDrawable(R.drawable.mn_item_faleconosco)));
			menuItems.add(new HHWMenuItem(getString(R.string.ajustes), new AjustesFragment(), getResources().getDrawable(R.drawable.mn_item_ajustes)));
			menuItems.add(new HHWMenuItem(getString(R.string.como_funciona), TutorialActivity.class, getResources().getDrawable(R.drawable.mn_item_comofunciona)));

			slideMenu = new HHWSlideMenu(this, menuItems);
		} else {
			slideMenu = new HHWSlideMenu(this, menuItems);
		}
	}
	
	protected void onCreateStartApp() {
		if (SharedPreferencesHelper.getInstance(this).hasToShowTutorial()) {
			SharedPreferencesHelper.getInstance(this).setHasToShowTutorial(
					false);
			startActivity(new Intent(this, TutorialActivity.class));
		}
		setCustomActionBar();
		if (mGoogleApiClient == null) {
			rebuildGoogleApiClient();
		}
	}
	
	private void setCustomActionBar() {
		ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_custom_with_logo);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5c9b91")));
	}


	@Override
	public void onConnected(Bundle bundle) {
		// Successfully connected to the API client. Pass it to the adapter to
		// enable API access.
		mAdapter.setGoogleApiClient(mGoogleApiClient);
		Log.i(TAG, "GoogleApiClient connected.");
	}

	@Override
	public void onConnectionSuspended(int i) {
		// Connection to the API client has been suspended. Disable API access
		// in the client.
		mAdapter.setGoogleApiClient(null);
		Log.e(TAG, "GoogleApiClient connection suspended.");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
				+ connectionResult.getErrorCode());

		// TODO(Developer): Check error code and notify the user of error state
		// and resolution.
//		Toast.makeText(
//				this,
//				"Could not connect to Google API Client: Error "
//						+ connectionResult.getErrorCode(), Toast.LENGTH_SHORT)
//				.show();

		// Disable API access in the adapter because the client was not
		// initialised correctly.
		mAdapter.setGoogleApiClient(null);		
	}
	
	/**
	 * Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using
	 * AutoManage functionality. This automatically sets up the API client to
	 * handle Activity lifecycle events.
	 */
	protected synchronized void rebuildGoogleApiClient() {
		// When we build the GoogleApiClient we specify where connected and
		// connection failed
		// callbacks should be returned, which Google APIs our app uses and
		// which OAuth 2.0
		// scopes our app requests.
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, 0 /* clientId */, this)
				.addConnectionCallbacks(this).addApi(Places.GEO_DATA_API)
				.build();
	}
	
	public void setAdapterWithGoogleApiClient() {
		mAdapter.setGoogleApiClient(mGoogleApiClient);

	}
}
