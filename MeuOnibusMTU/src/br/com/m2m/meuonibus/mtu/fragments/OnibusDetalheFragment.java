package br.com.m2m.meuonibus.mtu.fragments;import java.util.ArrayList;import java.util.List;import java.util.Timer;import java.util.TimerTask;import android.annotation.SuppressLint;import android.app.Activity;import android.graphics.Color;import android.os.Build;import android.os.Bundle;import android.os.Handler;import android.support.v4.app.Fragment;import android.view.LayoutInflater;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.view.animation.AlphaAnimation;import android.widget.RelativeLayout;import android.widget.TextView;import android.widget.Toast;import br.com.hhw.startapp.helpers.ServiceRestClientHelper;import br.com.m2m.meuonibus.mtu.R;import br.com.m2m.meuonibus.mtu.models.ws.MeuOnibusWS;import br.com.m2m.meuonibuscommons.activities.helpers.DateTimeHelper;import br.com.m2m.meuonibuscommons.handlers.LinhaOnibusHandler;import br.com.m2m.meuonibuscommons.handlers.TrajetoHandler;import br.com.m2m.meuonibuscommons.models.Coordenada;import br.com.m2m.meuonibuscommons.models.LinhaOnibus;import br.com.m2m.meuonibuscommons.models.Trajeto;import br.com.m2m.meuonibuscommons.models.db.LocalizacaoDAO;import com.google.android.gms.common.ConnectionResult;import com.google.android.gms.common.GooglePlayServicesUtil;import com.google.android.gms.maps.CameraUpdateFactory;import com.google.android.gms.maps.GoogleMap;import com.google.android.gms.maps.MapView;import com.google.android.gms.maps.MapsInitializer;import com.google.android.gms.maps.model.BitmapDescriptorFactory;import com.google.android.gms.maps.model.CameraPosition;import com.google.android.gms.maps.model.LatLng;import com.google.android.gms.maps.model.Marker;import com.google.android.gms.maps.model.MarkerOptions;import com.google.android.gms.maps.model.PolylineOptions;public class OnibusDetalheFragment extends Fragment {	LinhaOnibus linha;	private Activity ownerActivity;	private TextView nomeLinha;	private TextView veiculoLinha;	private TextView sentidoLinha;	private TextView tempoChegada;	private RelativeLayout caixaNomeLinha;	private static int SECONDS_CALL_BUS_INFORMATION = 30;	MapView mMapView;	private GoogleMap googleMap;	int statusGooglePlayServices;	public static final String BUNDLE_ONIBUS_DETALHE_LINHA = OnibusDetalheFragment.class			.getName() + "#bundle_LINHA";	private List<Trajeto> trajetos = new ArrayList<Trajeto>();	private List<Coordenada> coordenadas = new ArrayList<Coordenada>();	private Marker busMarker;	private TimerTask task;	private Handler handler;	public static OnibusDetalheFragment newInstance(LinhaOnibus linha) {		OnibusDetalheFragment fragment = new OnibusDetalheFragment();		Bundle bundle = new Bundle();		bundle.putSerializable(BUNDLE_ONIBUS_DETALHE_LINHA, linha);		fragment.setArguments(bundle);		return fragment;	}	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {		setHasOptionsMenu(true);		handler = new Handler();		statusGooglePlayServices = GooglePlayServicesUtil				.isGooglePlayServicesAvailable(getActivity());		View contentView = inflater.inflate(R.layout.fragment_onibus_detalhe,				container, false);		mMapView = (MapView) contentView				.findViewById(R.id.mapView_onibus_detalhe);		mMapView.onCreate(savedInstanceState);		setLayout(contentView);		try {			linha = (LinhaOnibus) getArguments().getSerializable(					BUNDLE_ONIBUS_DETALHE_LINHA);			nomeLinha.setText(linha.nome);			sentidoLinha.setText(linha.destino);			veiculoLinha.setText(linha.codVeiculo);//			startTask();			// loadLinhaInformacao();		} catch (Exception e) {			// TODO: handle exception		}		if (statusGooglePlayServices == ConnectionResult.SUCCESS) {			initMap();			loadTrajeto();		} else {		}		return contentView;	}	@SuppressLint("NewApi")	private void setLayout(View contentView) {		nomeLinha = (TextView) contentView				.findViewById(R.id.nome_linha_onibus_detalhe_fragment);		sentidoLinha = (TextView) contentView				.findViewById(R.id.sentido_linha_onibus_detalhe_fragment);		veiculoLinha = (TextView) contentView				.findViewById(R.id.veiculo_linha_onibus_detalhe_fragment);		tempoChegada = (TextView) contentView				.findViewById(R.id.tempo_chegada_linha_onibus_detalhe_fragment);		caixaNomeLinha = (RelativeLayout) contentView				.findViewById(R.id.container_nome_linha_onibus_detalhe_fragment);		float alpha = 0.85f;		if (Build.VERSION.SDK_INT < 11) {			final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);			animation.setDuration(0);			animation.setFillAfter(true);			caixaNomeLinha.startAnimation(animation);		} else {			caixaNomeLinha.setAlpha(alpha);		}	}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		ownerActivity.onBackPressed();		return super.onOptionsItemSelected(item);	}	private void initMap() {		try {			MapsInitializer.initialize(getActivity().getApplicationContext());			showBrasil();		} catch (Exception e) {			e.printStackTrace();		}	}	private void showBrasil() {		LocalizacaoDAO localDao = new LocalizacaoDAO(ownerActivity);		localDao.open();		Coordenada localDB = localDao.selectUltimoPonto();		if (mMapView != null) {			googleMap = mMapView.getMap();			if (localDB == null) {				Coordenada local = new Coordenada();				local.latitude = -14.2392976;				local.longitude = -53.1805017;				localDB = local;			}			CameraPosition cameraPosition = new CameraPosition.Builder()					.target(new LatLng(localDB.latitude, localDB.longitude))					.zoom(4).build();			googleMap.moveCamera(CameraUpdateFactory					.newCameraPosition(cameraPosition));			googleMap.setIndoorEnabled(true);		}	}	@Override	public void onAttach(Activity activity) {		super.onAttach(activity);		ownerActivity = activity;	}	private void loadTrajeto() {		if (linha == null)			return;		MeuOnibusWS.getTrajetos(linha.idLinha, new TrajetoHandler() {			@Override			public void setTrajetos(ArrayList<Trajeto> trajetosHandler) {				trajetos = trajetosHandler;				try {					if (trajetos.size() > 0) {						for (Trajeto trajeto: trajetos) {							if (trajeto.idPonto == linha.idLinhaSentido) {								coordenadas = trajeto.coordenadas;								showTrajeto();							}						}					}				} catch (Exception e) {					// TODO: handle exception				}						}			@Override			public void setErro(Throwable e) {				try {					Toast.makeText(ownerActivity.getApplicationContext(),							getString(R.string.nao_foi_possivel_obter_trajeto),							Toast.LENGTH_SHORT).show();				} catch (Exception e2) {					// TODO: handle exception				}			}		});	}	private void loadLinhaInformacao() {		if (linha == null)			return;		MeuOnibusWS.getLinhaInformacao(linha.codVeiculo, linha.idPonto, linha.idLinhaSentido, new LinhaOnibusHandler() {					@Override					public void setLinha(LinhaOnibus linhaHandler) {						if(linhaHandler != null) {							String tempo = DateTimeHelper									.formatHoursMinutes(linhaHandler.tempoChegada);							tempoChegada.setText(tempo);							if (linhaHandler.latLong != null) {								linha.latLong = linhaHandler.latLong;								if (tempo.equals("-")) {									if (busMarker != null) {										busMarker.remove();									}								} else {									setBusLocalization();								}								}						} else {							if (busMarker != null)								busMarker.remove();							tempoChegada.setText("-");						}											}					@Override					public void setErro(Throwable e) {						if (busMarker != null)							busMarker.remove();						tempoChegada.setText("--");						try {							Toast.makeText(									ownerActivity.getApplicationContext(),									getString(R.string.nao_foi_possivel_obter_previsao),									Toast.LENGTH_SHORT).show();						} catch (Exception e2) {							// TODO: handle exception						}					}				});	}	private void showTrajeto() {		zoomInPolyline();		setPolyline();		setMarkers();	}	private void setMarkers() {		setBusLocalization();		setBusStopLocalization();		setPolylineMarkerStartEnd();	}	private void setPolyline() {		PolylineOptions options = new PolylineOptions();		options.width(5);		options.color(Color.rgb(255, 165, 0));		options.geodesic(true);		for (int i = 0; i < coordenadas.size(); i++) {			Coordenada point = coordenadas.get(i);			options.add(new LatLng(point.latitude, point.longitude));			googleMap.addPolyline(options);		}	}	private void zoomInPolyline() {		int resultado = (int) Math.ceil(coordenadas.size() / 2);		Coordenada coordenada = coordenadas.get(resultado);		googleMap = mMapView.getMap();		CameraPosition cameraPosition = new CameraPosition.Builder()				.target(new LatLng(coordenada.latitude, coordenada.longitude))				.zoom(12.2f).build();		googleMap.moveCamera(CameraUpdateFactory				.newCameraPosition(cameraPosition));	}	private void setPolylineMarkerStartEnd() {		Coordenada point = coordenadas.get(0);		MarkerOptions start = new MarkerOptions().position(new LatLng(				point.latitude, point.longitude));		point = coordenadas.get(coordenadas.size() - 1);		MarkerOptions end = new MarkerOptions().position(new LatLng(				point.latitude, point.longitude));		start.anchor(0.5f, 0.5f);		start.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_bolinha));		end.anchor(0.5f, 0.5f);		end.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_bolinha));		googleMap.addMarker(start);		googleMap.addMarker(end);	}	private void setBusLocalization() {		if (busMarker != null) {			busMarker.remove();		}		MarkerOptions bus = new MarkerOptions().position(new LatLng(				linha.latLong.latitude, linha.latLong.longitude));		bus.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_onibus));		busMarker = googleMap.addMarker(bus);	}	private void setBusStopLocalization() {		if (linha.latLongPonto == null)			return;		MarkerOptions busStop = new MarkerOptions().position(new LatLng(				linha.latLongPonto.latitude, linha.latLongPonto.longitude));		busStop.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_ponto));		googleMap.addMarker(busStop);	}	@Override	public void onResume() {		super.onResume();		mMapView.onResume();		startTask();	}	@Override	public void onPause() {		super.onPause();		stopTask();		mMapView.onPause();	}	@Override	public void onLowMemory() {		super.onLowMemory();		mMapView.onLowMemory();	}	@Override	public void onDestroy() {		super.onDestroy();		mMapView.onDestroy();		ServiceRestClientHelper.cancelRequests(ownerActivity);	}	private void startTask() {		if (task != null && linha != null)			return;		task = new TimerTask() {			public void run() {				consultarLinhaInformacao();			}		};		new Timer().scheduleAtFixedRate(task, 0,				1000 * SECONDS_CALL_BUS_INFORMATION);	}	private void stopTask() {		if (task == null)			return;		task.cancel();		task = null;	}	public void consultarLinhaInformacao() {		Runnable runnable = new Runnable() {			@Override			public void run() {				handler.post(new Runnable() {					@Override					public void run() {						loadLinhaInformacao();					}				});			}		};		new Thread(runnable).start();	}}