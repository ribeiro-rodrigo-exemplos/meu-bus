package br.com.m2m.meuonibus.activities;

import android.os.Bundle;
import android.util.Log;
import br.com.m2m.meuonibuscommons.activities.base.BaseWithTitleActivity;
import br.com.m2m.meuonibuscommons.adapters.PlaceAutocompleteAdapter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

public class PontosRecargaActivity extends BaseWithTitleActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	
	public GoogleApiClient mGoogleApiClient;
	public PlaceAutocompleteAdapter mAdapter;
	protected static final String TAG = "API Places >>>>> ";

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
//		mGoogleApiClient = new GoogleApiClient.Builder(this)
//				.enableAutoManage(this, 0 /* clientId */, this)
//				.addConnectionCallbacks(this).addApi(Places.GEO_DATA_API)
//				.build();
	}
	
	public void setAdapterWithGoogleApiClient() {
		mAdapter.setGoogleApiClient(mGoogleApiClient);

	}
}
