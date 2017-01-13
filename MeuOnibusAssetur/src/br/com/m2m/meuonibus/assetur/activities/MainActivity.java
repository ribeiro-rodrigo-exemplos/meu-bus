package br.com.m2m.meuonibus.assetur.activities;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import br.com.hhw.startapp.activities.MenuStartAppActivity;
import br.com.how.hhwslidemenu.HHWMenuItem;
import br.com.how.hhwslidemenu.HHWSlideMenu;
import br.com.m2m.meuonibus.assetur.R;
import br.com.m2m.meuonibus.assetur.fragments.FaleConoscoFragment;
import br.com.m2m.meuonibus.assetur.fragments.ListaLinhasFragment;
import br.com.m2m.meuonibus.assetur.fragments.NoticiasFragment;
import br.com.m2m.meuonibuscommons.adapters.PlaceAutocompleteAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MainActivity extends MenuStartAppActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	
	public GoogleApiClient mGoogleApiClient;
	public PlaceAutocompleteAdapter mAdapter;
	protected static final String TAG = "API Places >>>>> ";
	private static final LatLngBounds BOUNDS_GREATER_BRAZIL = new LatLngBounds(
			new LatLng(-14.2392976, -53.1805017), new LatLng(-13.2392976,
					-52.1805017));

	protected void createMenu() {

		if (menuItems == null) {

			
			ArrayList<HHWMenuItem> menuItems = new ArrayList<HHWMenuItem>();
						
			menuItems.add(new HHWMenuItem(getString(R.string.inicio), new ListaLinhasFragment(), getResources().getDrawable(R.drawable.mn_item_inicio)));
			menuItems.add(new HHWMenuItem(getString(R.string.noticias), new NoticiasFragment(), getResources().getDrawable(R.drawable.mn_item_noticias)));
			menuItems.add(new HHWMenuItem(getString(R.string.fale_conosco), new FaleConoscoFragment(), getResources().getDrawable(R.drawable.mn_item_faleconosco)));
			menuItems.add(new HHWMenuItem(getString(R.string.como_funciona), TutorialActivity.class, getResources().getDrawable(R.drawable.mn_item_comofunciona)));

			slideMenu = new HHWSlideMenu(this, menuItems);
		} else {
			slideMenu = new HHWSlideMenu(this, menuItems);
		}
	}
	
	protected void onCreateStartApp() {
		setCustomActionBar();
		if (mGoogleApiClient == null) {
			rebuildGoogleApiClient();
			mAdapter = new PlaceAutocompleteAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, BOUNDS_GREATER_BRAZIL,
					null);
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

		mAdapter.setGoogleApiClient(null);		
	}
	
	/**
	 * Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using
	 * AutoManage functionality. This automatically sets up the API client to
	 * handle Activity lifecycle events.
	 */
	protected synchronized void rebuildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, 0 /* clientId */, this)
				.addConnectionCallbacks(this).addApi(Places.GEO_DATA_API)
				.build();
	}
	
	public void setAdapterWithGoogleApiClient() {
		mAdapter.setGoogleApiClient(mGoogleApiClient);

	}
}
