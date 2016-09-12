package br.com.m2m.meuonibus.mandala.fragments;import java.util.ArrayList;import java.util.List;import java.util.Timer;import java.util.TimerTask;import android.annotation.SuppressLint;import android.app.Activity;import android.content.Context;import android.content.Intent;import android.os.Build;import android.os.Bundle;import android.os.Handler;import android.support.v4.app.Fragment;import android.support.v4.widget.SwipeRefreshLayout;import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;import android.util.Log;import android.view.LayoutInflater;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.view.animation.AlphaAnimation;import android.widget.ArrayAdapter;import android.widget.LinearLayout;import android.widget.ListView;import android.widget.RelativeLayout;import android.widget.TextView;import android.widget.Toast;import br.com.hhw.startapp.helpers.ServiceRestClientHelper;import br.com.m2m.meuonibus.mandala.R;import br.com.m2m.meuonibus.mandala.activities.ListaLinhasActivity;import br.com.m2m.meuonibus.mandala.activities.OnibusDetalheActivity;import br.com.m2m.meuonibus.mandala.models.ws.MeuOnibusWS;import br.com.m2m.meuonibuscommons.activities.helpers.AppCustomDialogHelper;import br.com.m2m.meuonibuscommons.activities.helpers.DateTimeHelper;import br.com.m2m.meuonibuscommons.handlers.AppCustomDialogHandler;import br.com.m2m.meuonibuscommons.handlers.LinhaOnibusHandler;import br.com.m2m.meuonibuscommons.models.LinhaOnibus;import br.com.m2m.meuonibuscommons.models.PontoOnibus;import br.com.m2m.meuonibuscommons.models.db.PontoDAO;public class ListaLinhasFragment extends Fragment implements OnRefreshListener {	PontoOnibus ponto;	private Activity ownerActivity;	private SwipeRefreshLayout swipeLayout;	private ListView listView;	private TextView enderecoPonto;	private RelativeLayout caixaEnderecoPonto;		private static int SECONDS_CALL_LINES_INFORMATION = 30;	private TimerTask task;	private Handler handler;		private boolean requestSuccess;		MenuItem favorito;	private PontoDAO pontoDao;	public static final String BUNDLE_LISTA_LINHAS_PONTO = ListaLinhasFragment.class			.getName() + "#bundle_ponto";	private ArrayList<LinhaOnibus> linhas = new ArrayList<LinhaOnibus>();	public static ListaLinhasFragment newInstance(PontoOnibus ponto) {		ListaLinhasFragment fragment = new ListaLinhasFragment();		Bundle bundle = new Bundle();		bundle.putSerializable(BUNDLE_LISTA_LINHAS_PONTO, ponto);		fragment.setArguments(bundle);		return fragment;	}	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {		setHasOptionsMenu(true);		requestSuccess = false;		handler = new Handler();		View contentView = inflater.inflate(R.layout.fragment_lista_linhas,				container, false);		setLayout(contentView);		setSwipeRefresh(contentView);		try {			ponto = (PontoOnibus) getArguments().getSerializable(					BUNDLE_LISTA_LINHAS_PONTO);			// Toast.makeText(ownerActivity.getApplicationContext(), ponto.nome,			// Toast.LENGTH_SHORT).show();			if (ponto.endereco != null) {				enderecoPonto.setText(ponto.endereco);			}			Log.d("id ponto >>>>> ", ponto.idPonto + "");//			loadLinhas();		} catch (Exception e) {			Log.d("Error", e.toString());			// Coordenada latLong = new Coordenada();			// latLong.latitude = -14.2392976;			// latLong.longitude = -53.1805017;			// ponto = new PontoOnibus();			// ponto.nome = "Ponto: ";			// ponto.endereco = "Rua. ";			// ponto.idPonto = 111222333;			// ponto.latLong = latLong;		}		return contentView;	}	@Override	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {		super.onCreateOptionsMenu(menu, inflater);		inflater.inflate(R.menu.menu_favorito, menu);		favorito = menu.findItem(R.id.action_favorito);		pontoDao = new PontoDAO(ownerActivity);		pontoDao.open();		updateMenuStatus();	}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		int id = item.getItemId();		if (id == R.id.action_favorito) {			if (pontoDao.selectPonto(ponto) == null) {				pontoDao.insertPonto(ponto);				//imagem invertida				favorito.setIcon(R.drawable.ac_favorito_off);			} else {				desfavoritar(ponto);			}			return true;		} else if (id == R.id.action_editar) {			showEditNameMessage();		} else {			ownerActivity.onBackPressed();		}		return super.onOptionsItemSelected(item);	}	@Override	public void onDestroy() {		super.onDestroy();		ServiceRestClientHelper.cancelRequests(ownerActivity);	}	@Override	public void onAttach(Activity activity) {		super.onAttach(activity);		ownerActivity = activity;	}	@Override	public void onRefresh() {		// TODO Auto-generated method stub		loadLinhas();	}		@Override	public void onResume() {		super.onResume();		startTask();	}	@Override	public void onPause() {		super.onPause();		stopTask();	}	@SuppressLint("NewApi")	private void setLayout(View contentView) {		enderecoPonto = (TextView) contentView				.findViewById(R.id.endereco_lista_linhas_fragment);		listView = (ListView) contentView.findViewById(R.id.linhas_listview);		caixaEnderecoPonto = (RelativeLayout) contentView				.findViewById(R.id.container_endereco_lista_linhas_fragment);		float alpha = 0.8f;		if (Build.VERSION.SDK_INT < 11) {			final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);			animation.setDuration(0);			animation.setFillAfter(true);			caixaEnderecoPonto.startAnimation(animation);		} else {			caixaEnderecoPonto.setAlpha(alpha);		}	}	private void desfavoritar(PontoOnibus ponto) {		showConfirmationMessage();	}	private void showConfirmationMessage() {		String text = getString(R.string.desfavoritar_confirmacao);		AppCustomDialogHelper.showGenericDialog(ownerActivity, null, text,				getString(R.string.cancelar), new AppCustomDialogHandler() {					@Override					public void setOk() {						pontoDao.deletePonto(ponto);						favorito.setIcon(R.drawable.ac_favorito_on);					}				});	}	private void showEditNameMessage() {		String text = getString(R.string.digite_novo_nome_ponto);		AppCustomDialogHelper.showDialogInput(ownerActivity, text,				new AppCustomDialogHandler() {					@Override					public void setText(String nome) {						if (nome != null && !nome.equals("")) {							((ListaLinhasActivity) ownerActivity)									.setCustomTitle(nome);							// PontoOnibus pontoDB =							// pontoDao.selectPonto(ponto);							pontoDao.deletePonto(ponto);							ponto.nome = nome;							pontoDao.insertPonto(ponto);							updateMenuStatus();						}					}				});	}	public void updateMenuStatus() {		//imagens ivertidas		if (pontoDao.selectPonto(ponto) == null) {			favorito.setIcon(R.drawable.ac_favorito_on);		} else {			favorito.setIcon(R.drawable.ac_favorito_off);		}	}	private void setSwipeRefresh(View contentView) {		swipeLayout = (SwipeRefreshLayout) contentView				.findViewById(R.id.swipe_container_lista_linhas_fragment);		swipeLayout.setOnRefreshListener((OnRefreshListener) this);	}	private void loadLinhas() {		swipeLayout.post(new Runnable() {			@Override			public void run() {				swipeLayout.setRefreshing(true);			}		});		swipeLayout.setRefreshing(false);		swipeLayout.setEnabled(false);		MeuOnibusWS.getLinhas(ownerActivity, String.valueOf(ponto.idPonto),				new LinhaOnibusHandler() {					@Override					public void setLinhasOnibus(							ArrayList<LinhaOnibus> linhasHandler) {						swipeLayout.setRefreshing(false);						swipeLayout.setEnabled(true);										linhas = linhasHandler;//						linhas = filtrarLinhas(linhasHandler);						showLinhas();					}					@Override					public void setErro(Throwable e) {						swipeLayout.setRefreshing(false);						swipeLayout.setEnabled(true);						try {							Toast.makeText(									ownerActivity.getApplicationContext(),									getString(R.string.nao_foi_possivel_localizar_linhas),									Toast.LENGTH_SHORT).show();							if (!requestSuccess) {								showLinhas(null);							}							Log.d("request erro >>>>> ", "excecao");						} catch (Exception e2) {							// TODO: handle exception						}					}				});	}		@SuppressWarnings("unused")	private ArrayList<LinhaOnibus> filtrarLinhas(ArrayList<LinhaOnibus> linhas) {		ArrayList<LinhaOnibus> novaLista = new ArrayList<LinhaOnibus>();		if(linhas != null && !linhas.isEmpty()) {			for (int i = 0; i < linhas.size(); i++) {				List<Integer> listaIndexRepetidos = new ArrayList<Integer>();				int linhasEncontradas = 0;				for (int x = 0; x < linhas.size(); x++) {					if(i != x) {						if (linhas.get(i).numero.equals(linhas.get(x).numero)) {							if (!listaIndexRepetidos.contains(i)) {								listaIndexRepetidos.add(i);							}							listaIndexRepetidos.add(x);							linhasEncontradas++;						}					}				}				if (linhasEncontradas <= 1) {					novaLista.add(linhas.get(i));				} else {					if ( listaIndexRepetidos.get(0) == i) {						novaLista.add(linhas.get(i));					} else if (listaIndexRepetidos.get(1) == i) {						novaLista.add(linhas.get(i));					} else {						Log.d(" >>>>>>>>>>>> nao add ", linhas.get(i).numero+" index >  "+i);					}					}			}				}		return novaLista;	}	private void showLinhas() {		ArrayAdapter<LinhaOnibus> adapter = new LinhasAdapter(ownerActivity,				linhas);		listView.setAdapter(adapter);	}	private void showLinhas(ArrayList<LinhaOnibus> lista) {		ArrayAdapter<LinhaOnibus> adapter = new LinhasAdapter(ownerActivity,				lista);		listView.setAdapter(adapter);	}		private void startTask() {		if (task != null)			return;		task = new TimerTask() {			public void run() {				loadLinhasTask();			}		};		new Timer().scheduleAtFixedRate(task, 0,				1000 * SECONDS_CALL_LINES_INFORMATION);	}	private void stopTask() {		if (task == null)			return;		task.cancel();		task = null;	}	public void loadLinhasTask() {		Runnable runnable = new Runnable() {			@Override			public void run() {				handler.post(new Runnable() {					@Override					public void run() {						loadLinhas();					}				});			}		};		new Thread(runnable).start();	}	public class LinhasAdapter extends ArrayAdapter<LinhaOnibus> {		private List<LinhaOnibus> linhas;		public LinhasAdapter(Context context, List<LinhaOnibus> objects) {			super(context, 0, objects);			this.linhas = objects;		}		@Override		public int getCount() {			if (linhas == null) {				return 1;			}			if (linhas.size() == 0) {				return 1;			} else {				requestSuccess = true;			}			return linhas.size();		}		@SuppressLint("InflateParams")		@Override		public View getView(int position, View convertView, ViewGroup parent) {			View v = convertView;			ViewHolder viewHolder = null;			LayoutInflater li = (LayoutInflater) getContext().getSystemService(					Context.LAYOUT_INFLATER_SERVICE);			// mostrando mensagem em vez de lista			if (linhas == null) {				if (convertView == null) {					v = li.inflate(R.layout.adapter_mensagem, null);				}				return v;			}			if (linhas.size() == 0) {				if (convertView == null) {					v = li.inflate(R.layout.adapter_mensagem, null);				}				return v;			}			final LinhaOnibus currentObj = linhas.get(position);			try {				currentObj.latLongPonto = ponto.latLong;				currentObj.idPonto = ponto.idPonto;			} catch (Exception e) {				// TODO: handle exception			}			if (convertView == null) {				v = li.inflate(R.layout.adapter_lista_linhas, null);				viewHolder = new ViewHolder(v);				v.setTag(viewHolder);							} else {				viewHolder = (ViewHolder) v.getTag();			}			v.setOnClickListener(new View.OnClickListener() {				@Override				public void onClick(View view) {					Intent intent = new Intent(ownerActivity,							OnibusDetalheActivity.class);					Bundle extra = new Bundle();					extra.putSerializable(							OnibusDetalheActivity.BUNDLE_ONIBUS_DETALHE_LINHA,							currentObj);					intent.putExtra(							OnibusDetalheActivity.EXTRA_ONIBUS_DETALHE_LINHA,							extra);					ownerActivity.startActivity(intent);				}			});			//			String nome = currentObj.nome != null?currentObj.nome: currentObj.patternName;			String nome = currentObj.nome;			viewHolder.linha.setText(currentObj.numero);			viewHolder.nome.setText(nome);			String tempo = DateTimeHelper					.formatHoursMinutes(currentObj.tempoChegada);			viewHolder.tempo.setText(tempo);			if (position == 0) {				viewHolder.header.setVisibility(View.VISIBLE);			} else {				viewHolder.header.setVisibility(View.GONE);			}			return v;		}		class ViewHolder {			public int idLinha;			public LinearLayout header;			public TextView linha;			public TextView nome;			public TextView tempo;			public ViewHolder(View base) {				header = (LinearLayout) base						.findViewById(R.id.header_adapter_lista_linha);				linha = (TextView) base						.findViewById(R.id.linha_adapter_lista_linha);				nome = (TextView) base						.findViewById(R.id.nome_adapter_lista_linha);				tempo = (TextView) base						.findViewById(R.id.tempo_adapter_lista_linha);			}		}	}}