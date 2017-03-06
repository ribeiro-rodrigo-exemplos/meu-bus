package br.com.m2m.meuonibus.astur.fragments;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import org.json.JSONArray;import org.json.JSONObject;import com.google.android.gms.common.ConnectionResult;import com.google.android.gms.common.GooglePlayServicesUtil;import com.google.android.gms.maps.CameraUpdateFactory;import com.google.android.gms.maps.GoogleMap;import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;import com.google.android.gms.maps.MapView;import com.google.android.gms.maps.MapsInitializer;import com.google.android.gms.maps.model.BitmapDescriptorFactory;import com.google.android.gms.maps.model.CameraPosition;import com.google.android.gms.maps.model.LatLng;import com.google.android.gms.maps.model.Marker;import com.google.android.gms.maps.model.MarkerOptions;import com.google.android.gms.maps.model.PolylineOptions;import android.annotation.SuppressLint;import android.app.Activity;import android.content.Intent;import android.graphics.Color;import android.location.Location;import android.os.Bundle;import android.support.annotation.Nullable;import android.support.v4.app.Fragment;import android.util.Log;import android.view.LayoutInflater;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.widget.TextView;import android.widget.Toast;import br.com.hhw.startapp.helpers.ServiceRestClientHelper;import br.com.m2m.meuonibus.astur.R;import br.com.m2m.meuonibus.astur.activities.HomeSearchActivity;import br.com.m2m.meuonibus.astur.activities.PrevisaoOnibusActivity;import br.com.m2m.meuonibus.astur.models.ws.BusMarker;import br.com.m2m.meuonibus.astur.models.ws.MeuOnibusWS;import br.com.m2m.meuonibus.astur.socket.SocketConnector;import br.com.m2m.meuonibus.astur.socket.SocketListener;import br.com.m2m.meuonibuscommons.handlers.PontoOnibusHandler;import br.com.m2m.meuonibuscommons.handlers.TrajetoHandler;import br.com.m2m.meuonibuscommons.models.Coordenada;import br.com.m2m.meuonibuscommons.models.LinhaOnibus;import br.com.m2m.meuonibuscommons.models.PontoOnibus;import br.com.m2m.meuonibuscommons.models.Trajeto;import br.com.m2m.meuonibuscommons.models.db.LocalizacaoDAO;@SuppressLint("NewApi")public class OnibusDetalheFragment extends Fragment {	private LinhaOnibus mLinhaOnibus;	private Trajeto mTrajeto;	private Activity ownerActivity;	private TextView nomeLinha;	MapView mMapView;	private GoogleMap googleMap;	int statusGooglePlayServices;	public static final String BUNDLE_ONIBUS_DETALHE_LINHA = OnibusDetalheFragment.class			.getName() + "#bundle_LINHA";		public static final String BUNDLE_ONIBUS_DETALHE_TRAJETO = OnibusDetalheFragment.class			.getName() + "#bundle_TRAJETO";	private List<Trajeto> trajetos = new ArrayList<Trajeto>();	private List<Coordenada> coordenadas = new ArrayList<Coordenada>();	private ArrayList<Marker> pinsPontos = new ArrayList<Marker>();	private List<PontoOnibus> mPontos = new ArrayList<PontoOnibus>();		private Marker markerMe;	private HashMap<String, BusMarker> busMarkersMap = new HashMap<String, BusMarker>();	private SocketConnector socket;		private Location userLocation;	public static OnibusDetalheFragment newInstance(LinhaOnibus linha, Trajeto trajeto) {		OnibusDetalheFragment fragment = new OnibusDetalheFragment();		Bundle bundle = new Bundle();		bundle.putSerializable(BUNDLE_ONIBUS_DETALHE_LINHA, linha);		bundle.putSerializable(BUNDLE_ONIBUS_DETALHE_TRAJETO, trajeto);		fragment.setArguments(bundle);		return fragment;	}	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {		setHasOptionsMenu(true);		statusGooglePlayServices = GooglePlayServicesUtil				.isGooglePlayServicesAvailable(getActivity());		View contentView = inflater.inflate(R.layout.fragment_onibus_detalhe,				container, false);		mMapView = (MapView) contentView				.findViewById(R.id.mapView_onibus_detalhe);		mMapView.onCreate(savedInstanceState);		try {			mLinhaOnibus = (LinhaOnibus) getArguments().getSerializable(BUNDLE_ONIBUS_DETALHE_LINHA);			mTrajeto = (Trajeto) getArguments().getSerializable(BUNDLE_ONIBUS_DETALHE_TRAJETO);			Log.d("LINHA >>>>", String.valueOf(mLinhaOnibus.idLinha));			Log.d("LINHA >>>>", mLinhaOnibus.idLinhaString);			nomeLinha.setText(mLinhaOnibus.name);		} catch (Exception e) {			e.printStackTrace();		}		if (statusGooglePlayServices == ConnectionResult.SUCCESS) {			initMap();			loadTrajeto();			googleMap	          .setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {	            @Override	            public void onMyLocationChange(Location location) {	            	userLocation = location;	            	setMarkerMe();	            }	          });			googleMap.setMyLocationEnabled(true);		}		return contentView;	}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		switch (item.getItemId()) {			case R.id.action_search:				Intent it = new Intent(ownerActivity.getBaseContext(), HomeSearchActivity.class);				it.putExtra("isNewFilter", true);				ownerActivity.startActivity(it);			break;		}		return super.onOptionsItemSelected(item);	}		@Override	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {		super.onCreateOptionsMenu(menu, inflater);		inflater.inflate(R.menu.menu_search, menu);	}	private void initMap() {		try {			MapsInitializer.initialize(getActivity().getApplicationContext());			showBrasil();			setMapMarkerListener();		} catch (Exception e) {			e.printStackTrace();		}	}	private void showBrasil() {		LocalizacaoDAO localDao = new LocalizacaoDAO(ownerActivity);		localDao.open();		Coordenada localDB = localDao.selectUltimoPonto();		if (mMapView != null) {			googleMap = mMapView.getMap();			if (localDB == null) {				Coordenada local = new Coordenada();				local.latitude = -14.2392976;				local.longitude = -53.1805017;				localDB = local;			}			CameraPosition cameraPosition = new CameraPosition.Builder()					.target(new LatLng(localDB.latitude, localDB.longitude))					.zoom(4).build();			googleMap.moveCamera(CameraUpdateFactory					.newCameraPosition(cameraPosition));			googleMap.setIndoorEnabled(true);		}	}	@Override	public void onAttach(Activity activity) {		super.onAttach(activity);		ownerActivity = activity;	}	private void loadTrajeto() {		if (mLinhaOnibus == null) {			return;		}		MeuOnibusWS.getTrajetos(mLinhaOnibus.idLinha, new TrajetoHandler() {			@Override			public void setTrajetos(ArrayList<Trajeto> trajetosHandler) {				trajetos = trajetosHandler;				try {					if (trajetos.size() > 0) {						for (Trajeto trajeto: trajetos) {							if (trajeto.idPonto == mTrajeto.idPonto) {								coordenadas = trajeto.coordenadas;								showTrajeto();								}						}					}				} catch (Exception e) {					e.printStackTrace();				}						}			@Override			public void setErro(Throwable e) {				try {					Toast.makeText(ownerActivity.getApplicationContext(),							getString(R.string.nao_foi_possivel_obter_trajeto),							Toast.LENGTH_SHORT).show();				} catch (Exception e2) {					e2.printStackTrace();				}			}		});	}	private void showTrajeto() {		zoomInPolyline();		setPolyline();		loadBusPoints();		loadBusInfo();	}	private void setPolyline() {		PolylineOptions options = new PolylineOptions();		options.width(5);		options.color(Color.rgb(255, 165, 0));		options.geodesic(true);		for (int i = 0; i < coordenadas.size(); i++) {			Coordenada point = coordenadas.get(i);			options.add(new LatLng(point.latitude, point.longitude));			googleMap.addPolyline(options);						if(i == 0) showLineStart(point);			if(i == coordenadas.size() - 1) showLineEnd(point);		}	}		private void showLineStart(Coordenada point) {		MarkerOptions start = new MarkerOptions().position(new LatLng(				point.latitude, point.longitude));		start.anchor(0.5f, 0.5f);		start.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_bolinha));		googleMap.addMarker(start);	}		private void showLineEnd(Coordenada point) {		MarkerOptions end = new MarkerOptions().position(new LatLng(				point.latitude, point.longitude));		end.anchor(0.5f, 0.5f);		end.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_bolinha));		googleMap.addMarker(end);	}	private void zoomInPolyline() {		int resultado = (int) Math.ceil(coordenadas.size() / 2);		Coordenada coordenada = coordenadas.get(resultado);		googleMap = mMapView.getMap();		CameraPosition cameraPosition = new CameraPosition.Builder()				.target(new LatLng(coordenada.latitude, coordenada.longitude))				.zoom(11.2f).build();		googleMap.moveCamera(CameraUpdateFactory				.newCameraPosition(cameraPosition));	}		private void setMarkerMe() {		if (markerMe != null) {			markerMe.remove();		}		MarkerOptions me = new MarkerOptions().position(				new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))				.icon(BitmapDescriptorFactory					.fromResource(R.drawable.map_pessoa));		markerMe = googleMap.addMarker(me);	}		private void loadBusInfo() {    	socket = new SocketConnector(getActivity().getApplicationContext());		socket.connect(new SocketListener() {			@Override			public JSONArray getSubscription() {				return getSubscriptions();			}			@Override			public void onSync(@Nullable JSONArray response) {				if (response == null || response.length() == 0) {					return;				}				try {					for (int i = 0; i < response.length(); i++) {						JSONObject data = response.getJSONObject(i);						JSONObject gps = data.getJSONObject("gps");						Double lat = Double.valueOf(gps.getString("latitude"));						Double lng = Double.valueOf(gps.getString("longitude"));						String idVeiculo = data.getString("idVeiculo");						BusMarker lastMarker = busMarkersMap.get(idVeiculo);						if (lastMarker != null && !isNewCoordinate(lat, lng, lastMarker)) {							continue;						}						if (lastMarker != null) {							RemoveBusMarker removeMarker = new RemoveBusMarker();							removeMarker.setBusMarker(lastMarker);							getActivity().runOnUiThread(removeMarker);						}						BusMarker busMarker = new BusMarker(lat, lng, idVeiculo, getLineName(data));						AddBusMarker addMarker = new AddBusMarker();						addMarker.setBusMarker(busMarker);						getActivity().runOnUiThread(addMarker);					}				} catch (Exception e) {					e.printStackTrace();				} catch (OutOfMemoryError ofm) {					ofm.printStackTrace();				}			}			@Override			public void onConnectError(Object error) {				Log.e("Error", "Socket connet error");			}			@Override			public void onEventError(Object error) {				Log.e("Error", "Socket event error");			}		});	}		private boolean isNewCoordinate(double newLatitude, double newLongitude, BusMarker lastMarker) {		return newLatitude != lastMarker.getLatitude() || newLongitude != lastMarker.getLongitude();	}	private String getLineName(JSONObject data) {		try {			String prefixo = data.getString("prefixoVeiculo");			JSONObject sinotico = data.getJSONObject("sinotico");			String nomeTrajeto = sinotico.getString("nomeTrajeto");						return String.format("%s \n%s", prefixo, nomeTrajeto);		} catch (Exception e) {			return "";		}	}	private JSONArray getSubscriptions() {		JSONArray jsonArray = new JSONArray();		jsonArray.put(String.format("%s:%s:*", mLinhaOnibus.idLinhaString, mTrajeto.idString));		return jsonArray;	}		private void setMapMarkerListener() {		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {			@Override			public boolean onMarkerClick(Marker marker) {				if (busMarkersMap.containsKey(marker.getSnippet())) {					marker.showInfoWindow();				} else {					showListaLinhas(marker.getSnippet());				}				return true;			}		});		googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {            @SuppressLint("InflateParams") @Override            public View getInfoWindow(Marker marker) {            	View v = getLayoutInflater(getArguments()).inflate(R.layout.info_window, null);                TextView nome = (TextView) v.findViewById(R.id.textView1);                nome.setText(marker.getTitle());                return v;            }            @Override            public View getInfoContents(Marker marker) {            	return null;            }        });	}		private void loadBusPoints() {		MeuOnibusWS.getPontos(String.valueOf(mTrajeto.idPonto), new PontoOnibusHandler() {						@Override			public void setPontosOnibus(ArrayList<PontoOnibus> pontosHandler) {				mPontos = pontosHandler;				loadPinPontos(pontosHandler);			}			@Override			public void setErro(Throwable e) {				System.out.print("Get bus points error!");			}		});	}		private void loadPinPontos(List<PontoOnibus> pontos) {		if (pinsPontos.size() > 0) {			for (Marker marker : pinsPontos) {				marker.remove();			}			pinsPontos.clear();		}		for (PontoOnibus ponto : pontos) {			MarkerOptions marker = new MarkerOptions().position(new LatLng(					ponto.latLong.latitude, ponto.latLong.longitude));			marker.title(ponto.nome);			marker.snippet(Integer.toString(ponto.idPonto));			marker.icon(BitmapDescriptorFactory					.fromResource(R.drawable.map_ponto));			pinsPontos.add(googleMap.addMarker(marker));		}	}		private void showListaLinhas(String idPonto) {		for (PontoOnibus ponto : mPontos) {			if (Integer.toString(ponto.idPonto).equals(idPonto)) {				Intent intent = new Intent(ownerActivity, PrevisaoOnibusActivity.class);				intent.putExtra(PrevisaoOnibusActivity.EXTRA_LISTA_LINHAS_PONTO, ponto);				intent.putExtra(PrevisaoOnibusActivity.EXTRA_LISTA_LINHAS_TRAJETO, mTrajeto);				intent.putExtra(PrevisaoOnibusActivity.EXTRA_LISTA_LINHAS, mLinhaOnibus);				ownerActivity.startActivity(intent);				break;			}		}	}	@Override	public void onResume() {		super.onResume();		mMapView.onResume();		if (googleMap != null) {		    googleMap.setMyLocationEnabled(true);		}	}	@Override	public void onPause() {		super.onPause();		mMapView.onPause();		if (googleMap != null) {			googleMap.setMyLocationEnabled(false);		}	}	@Override	public void onLowMemory() {		super.onLowMemory();		mMapView.onLowMemory();			}	@Override	public void onDestroy() {		super.onDestroy();		mMapView.onDestroy();		ServiceRestClientHelper.cancelRequests(ownerActivity);		if (socket != null) {			socket.setupSubs("_");		}	}		private class AddBusMarker implements Runnable {	    private MarkerOptions markerOptions;	    private BusMarker busMarker;	    public void setBusMarker(BusMarker busMarker) {	    	this.busMarker = busMarker;	    	this.markerOptions = getNewMarkerOptions(busMarker);	        busMarkersMap.put(busMarker.getIdVeiculo(), busMarker);	    }		private MarkerOptions getNewMarkerOptions(BusMarker busMarker) {			MarkerOptions mo = new MarkerOptions();			mo.title(busMarker.getTitle());			mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_onibus));			mo.position(new LatLng(busMarker.getLatitude(), busMarker.getLongitude()));			mo.snippet(busMarker.getIdVeiculo());			mo.draggable(true);			return mo;		}	    @Override	    public void run() {	        Marker m = googleMap.addMarker(markerOptions);	        busMarker.setMarker(m);	    }	}	private class RemoveBusMarker implements Runnable {		private BusMarker busMarker;		public void setBusMarker(BusMarker busMarker) {			busMarkersMap.remove(busMarker.getIdVeiculo());	    	this.busMarker = busMarker;	    }		@Override		public void run() {			if (busMarker.getMarker() != null) {				busMarker.getMarker().remove();			}		}	}}