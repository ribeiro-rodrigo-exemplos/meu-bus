package br.com.m2m.meuonibus.vitoria.fragments;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.hhw.startapp.helpers.ServiceRestClientHelper;
import br.com.m2m.meuonibus.vitoria.MeuOnibusApplication;
import br.com.m2m.meuonibus.vitoria.R;
import br.com.m2m.meuonibus.vitoria.activities.FiltroRecargaActivity;
import br.com.m2m.meuonibus.vitoria.activities.MainActivity;
import br.com.m2m.meuonibus.vitoria.handlers.PontoRecargaHandler;
import br.com.m2m.meuonibus.vitoria.models.PontoRecarga;
import br.com.m2m.meuonibus.vitoria.models.ws.PontosRecargaWS;
import br.com.m2m.meuonibuscommons.activities.helpers.AppCustomDialogHelper;
import br.com.m2m.meuonibuscommons.adapters.PlaceAutocompleteAdapter;
import br.com.m2m.meuonibuscommons.models.AjustesOpcaoRaioBusca;
import br.com.m2m.meuonibuscommons.models.Coordenada;
import br.com.m2m.meuonibuscommons.models.db.LocalizacaoDAO;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PontosRecargaFragment extends Fragment {

	// places
	private AutoCompleteTextView mAutocompleteView;
	private RelativeLayout containerSearch;;
	private static final LatLngBounds BOUNDS_GREATER_BRAZIL = new LatLngBounds(
			new LatLng(-14.2392976, -53.1805017), new LatLng(-13.2392976,
					-52.1805017));
	InputMethodManager imm;
	LatLng lastPlace;

	private MainActivity ownerActivity;

	MapView mMapView;
	private GoogleMap googleMap;
	int statusGooglePlayServices;

	Marker markerMe;
	Circle radius;
	private ArrayList<Marker> pinsPontos = new ArrayList<Marker>();

	Location userLocation;
	Location lastLocation;
	
	private double currentLat;
	private double currentLong;

	boolean isToShowTraffic = true;

	LocalizacaoDAO localDao;
	Coordenada localDB;

	private ArrayList<PontoRecarga> pontos = new ArrayList<PontoRecarga>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		verifyGPS();

		localDao = new LocalizacaoDAO(ownerActivity);
		localDao.open();
		localDB = localDao.selectUltimoPonto();
		
		currentLat = currentLong = 0;

		statusGooglePlayServices = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());

		View v = inflater.inflate(R.layout.fragment_home, container, false);
		mMapView = (MapView) v.findViewById(R.id.mapView_home);
		mMapView.onCreate(savedInstanceState);

		// keyboard
		imm = (InputMethodManager) ownerActivity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		setAutoComplete(v);

		pinsPontos = new ArrayList<Marker>();

		if (statusGooglePlayServices == ConnectionResult.SUCCESS) {
			initMap();
			if(pinsPontos.size() == 0) {
				googleMap
						.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
							@Override
							public void onMyLocationChange(Location location) {
								userLocation = location;
								showMapInLocation(userLocation);
							}
						});
				googleMap.setMyLocationEnabled(true);
			}

		} else {
			ownerActivity.mAdapter.setGoogleApiClient(null);
		}
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_search_filter, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_search) {
			showOrHideSearch();
		}
		if (id == R.id.action_filter) {
			startActivity(new Intent(getActivity(), FiltroRecargaActivity.class));
		}
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ownerActivity.setAdapterWithGoogleApiClient();

		if (mMapView != null)
			mMapView.onResume();
		lastLocation = null;
		if (googleMap != null)
			googleMap.setMyLocationEnabled(true);
		if (currentLat != 0 && currentLong != 0)
			loadPontos(currentLat, currentLong, true);
	}

	@Override
	public void onPause() {
		super.onPause();
		closeSearch();

		containerSearch.setVisibility(View.GONE);
		ownerActivity.mAdapter.setGoogleApiClient(null);
		if (mMapView != null)
			mMapView.onPause();
		lastLocation = null;
		if (googleMap != null)
			googleMap.setMyLocationEnabled(false);
		
		ServiceRestClientHelper.cancelRequests(ownerActivity);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		saveLocalization();
		mMapView.onDestroy();
		lastLocation = null;
		if (googleMap != null)
			googleMap.setMyLocationEnabled(false);
		ServiceRestClientHelper.cancelRequests(ownerActivity);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
		lastLocation = null;
		if (googleMap != null)
			googleMap.setMyLocationEnabled(false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		ownerActivity = (MainActivity) activity;
	}
	
	private void closeSearch() {
		mAutocompleteView.clearFocus();
		containerSearch.setVisibility(View.GONE);
		closeKeyBoard();
	}

	private void closeKeyBoard() {
		if (imm != null) {
			imm.hideSoftInputFromWindow(mAutocompleteView.getWindowToken(), 0);
		}
	}

	private void showKeyBoard() {
		if (imm != null) {
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
		}
	}

	private void showOrHideSearch() {
		if (containerSearch.getVisibility() == View.VISIBLE) {
			closeSearch();
		} else {
			containerSearch.setVisibility(View.VISIBLE);
			mAutocompleteView.requestFocus();
			showKeyBoard();
		}
	}

	private void showLatlongInMap(LatLng pos, float zoom) {
		if (mMapView != null) {
			googleMap = mMapView.getMap();
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(pos).zoom(zoom).build();
			googleMap.moveCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			googleMap.setIndoorEnabled(true);
		}
	}

	private void showMapInLocation(Location locationMap) {
		if (mMapView != null) {
			if (lastLocation == null) {
				lastLocation = locationMap;
				googleMap = mMapView.getMap();
				if (lastPlace == null) {
					lastPlace = new LatLng(locationMap.getLatitude(),
							locationMap.getLongitude());
				}
				
				loadPontos(lastPlace.latitude,lastPlace.longitude, true);

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(lastPlace).zoom(15).build();
				googleMap.moveCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));

				googleMap.setTrafficEnabled(isToShowTraffic);
				setMarkerMe(locationMap);

			} else {
				if (locationMap.distanceTo(lastLocation) >= 7) {
					lastPlace = null;
					lastLocation = locationMap;
					setMarkerMe(locationMap);
				}
			}
		}
	}

	private void initMap() {
		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
			if (localDB != null) {
				Log.d("nome >>>>> ", localDB.idBD + " - " + localDB.nome
						+ " / " + localDB.latitude + " / " + localDB.longitude);
				showLatlongInMap(
						new LatLng(localDB.latitude, localDB.longitude), 15);
				loadPontos(localDB.latitude, localDB.longitude, true);
			} else {
				Log.d("exibindo >>>>> ", "ZOOM Brasil no mapa");
				showLatlongInMap(new LatLng(-14.2392976, -53.1805017), 4);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setMarkerMe(Location locationMap) {
		if (markerMe != null) {
			markerMe.remove();
		}
		MarkerOptions me = new MarkerOptions().position(
				new LatLng(locationMap.getLatitude(), locationMap
						.getLongitude()))
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.map_pessoa));
		markerMe = googleMap.addMarker(me);
	}
	
	private void setRadius(double lat,  double lng) {
		if (radius != null) {
			radius.remove();
		}
		String raio = AjustesOpcaoRaioBusca.getSavedRaio(ownerActivity);
		CircleOptions circleOptions = new CircleOptions().center(new LatLng(lat, lng)).radius(Double.parseDouble(raio)); // In meters
		circleOptions.strokeColor(Color.rgb(126, 179, 170));
		radius = googleMap.addCircle(circleOptions);
	}

	private void loadPinPontos() {
		pinsPontos.clear();
		
		if (pinsPontos.size() > 0) {
			for (Marker marker : pinsPontos) {
				marker.remove();
			}
		}

		for (PontoRecarga ponto : pontos) {
			MarkerOptions marker = new MarkerOptions().position(new LatLng(
					ponto.latLong.latitude, ponto.latLong.longitude));
			marker.title(ponto.nome);

			if (ponto.loja != null && !ponto.loja.equalsIgnoreCase("")) {
				marker.snippet(ponto.loja + '%' + ponto.endereco);
			} else {
				marker.snippet(ponto.endereco);
			}
			
			if(ponto.nome.equalsIgnoreCase(MeuOnibusApplication.getContext().getString(R.string.recarga))) {
				marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pino_vermelho));
			} else if(ponto.nome.equalsIgnoreCase(MeuOnibusApplication.getContext().getString(R.string.venda_credito))) {
				marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pino_verde));
			}
			
			pinsPontos.add(googleMap.addMarker(marker));
		}
		
		googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
            @SuppressLint("InflateParams") @Override
            public View getInfoWindow(Marker marker) {
            	View v = getLayoutInflater(getArguments()).inflate(R.layout.recarga_info_window, null);
                TextView tipo = (TextView) v.findViewById(R.id.textView1);
                TextView endereco = (TextView) v.findViewById(R.id.textView2);
                tipo.setText(marker.getTitle());
                if(marker.getTitle().equalsIgnoreCase(MeuOnibusApplication.getContext().getString(R.string.recarga))) {
                	tipo.setTextColor(getResources().getColor(R.color.pontoRecarga));
    			} else if(marker.getTitle().equalsIgnoreCase(MeuOnibusApplication.getContext().getString(R.string.venda_credito))) {
    				tipo.setTextColor(getResources().getColor(R.color.pontoAtualizacao));
    			}
                
                String snippet = marker.getSnippet();
                String[] parts = snippet.split("%");
                if(parts.length > 1) {
	                String formatted = "<b>" + parts[0] + "</b><br />" + parts[1];
	                endereco.setText(Html.fromHtml(formatted));
                } else {
                	endereco.setText(snippet);
                }
                
                return v;
            }
 
            @Override
            public View getInfoContents(Marker marker) {
            	return null;
            }
        });
		
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				if (!marker.isInfoWindowShown()) {
					marker.setInfoWindowAnchor((float) 3.2, (float) 0.3);
	                marker.showInfoWindow();
				} else {
	                marker.hideInfoWindow();
				}
				return true;
			}
		});
	}

	private void loadPontos(Double lat, Double lng, final boolean isFromUserLocation) {
		currentLat = lat;
		currentLong = lng;
		String sLat = String.valueOf(lat);
		String sLng = String.valueOf(lng);
		
		googleMap.clear();
		pinsPontos.clear();
		
		String raio = AjustesOpcaoRaioBusca.getSavedRaio(ownerActivity);
		
		setRadius(lat,lng);

		try {
			PontosRecargaWS.getPontos(this, sLat, sLng, raio, new PontoRecargaHandler() {
				@Override
				public void setPontosRecarga(ArrayList<PontoRecarga> pontosHandler) {
					pontos = pontosHandler;
					loadPinPontos();
				}

				@Override
				public void setErro(Throwable e) {
					try {
						String msg = getString(R.string.nao_foi_possivel_localizar_pontos_na_sua_localizacao);
						if (!isFromUserLocation) {
							msg = getString(R.string.nao_foi_possivel_localizar_pontos);
						}
						Toast.makeText(ownerActivity.getApplicationContext(), msg,
								Toast.LENGTH_SHORT).show();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveLocalization() {
		if (userLocation != null) {
			Coordenada local = new Coordenada();
			local.nome = "meulocal";
			local.latitude = userLocation.getLatitude();
			local.longitude = userLocation.getLongitude();
			localDao.insertLocalizacao(local);
		}
	}

	private void saveLocalization(LatLng latLng) {
		Coordenada local = new Coordenada();
		local.nome = "meulocal";
		local.latitude = latLng.latitude;
		local.longitude = latLng.longitude;
		localDao.insertLocalizacao(local);
	}
	
	private void verifyGPS() {
		try {
			LocationManager locationManager = (LocationManager) ownerActivity. getSystemService(Context.LOCATION_SERVICE);

		        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
		            Log.i("About GPS", "GPS is Enabled in your devide");
		        } else {
		        	String text = getString(R.string.mensagem_ligue_gps);
		    		AppCustomDialogHelper.showSimpleGenericDialog(ownerActivity, text);
		        }
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void setAutoComplete(View v) {
		containerSearch = (RelativeLayout) v
				.findViewById(R.id.autocomplete_container);
		// Retrieve the AutoCompleteTextView that will display Place
		// suggestions.
		mAutocompleteView = (AutoCompleteTextView) v
				.findViewById(R.id.autocomplete_places);

		// Register a listener that receives callbacks when a suggestion has
		// been selected
		mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

		// Retrieve the TextView that will display details of the selected
		// place.
		// mPlaceDetailsText = (TextView) v.findViewById(R.id.place_details);

		// Set up the adapter that will retrieve suggestions from the Places Geo
		// Data API that cover
		// the entire world.
		ownerActivity.mAdapter = new PlaceAutocompleteAdapter(ownerActivity,
				android.R.layout.simple_list_item_1, BOUNDS_GREATER_BRAZIL,
				null);
		mAutocompleteView.setAdapter(ownerActivity.mAdapter);

		// Set up the 'clear text' button that clears the text in the
		// autocomplete view
		Button clearButton = (Button) v
				.findViewById(R.id.autocomplete_button_clear);
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAutocompleteView.setText("");
			}
		});
	}

	private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			/*
			 * Retrieve the place ID of the selected item from the Adapter. The
			 * adapter stores each Place suggestion in a PlaceAutocomplete
			 * object from which we read the place ID.
			 */
			final PlaceAutocompleteAdapter.PlaceAutocomplete item = ownerActivity.mAdapter
					.getItem(position);
			final String placeId = String.valueOf(item.placeId);
			// Log.i(TAG, "Autocomplete item selected: " + item.description);

			/*
			 * Issue a request to the Places Geo Data API to retrieve a Place
			 * object with additional details about the place.
			 */
			PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
					.getPlaceById(ownerActivity.mGoogleApiClient, placeId);
			placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

			// Toast.makeText(ownerActivity,
			// "Clicked: " + item.description, Toast.LENGTH_SHORT).show();

			// Log.i(TAG, "Called getPlaceById to get Place details for "
			// + item.placeId);
		}
	};

	/**
	 * Callback for results from a Places Geo Data API query that shows the
	 * first place result in the details view on screen.
	 */
	private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
		@Override
		public void onResult(PlaceBuffer places) {
			if (!places.getStatus().isSuccess()) {
				return;
			}
			showOrHideSearch();
			// Get the Place object from the buffer.
			final Place place = places.get(0);
			lastPlace = place.getLatLng();
			showLatlongInMap(place.getLatLng(), 15);
			mAutocompleteView.setText("");
			loadPontos(place.getLatLng().latitude,place.getLatLng().longitude, false);
			saveLocalization(place.getLatLng());
		}
	};
}