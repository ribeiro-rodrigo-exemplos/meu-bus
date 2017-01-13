package br.com.m2m.meuonibus.assetur.fragments;import java.util.ArrayList;import java.util.List;import java.util.Timer;import java.util.TimerTask;import android.annotation.SuppressLint;import android.app.Activity;import android.content.Context;import android.content.Intent;import android.os.Bundle;import android.os.Handler;import android.support.v4.app.Fragment;import android.support.v4.widget.SwipeRefreshLayout;import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;import android.util.Log;import android.view.LayoutInflater;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.widget.ArrayAdapter;import android.widget.ListView;import android.widget.TextView;import android.widget.Toast;import br.com.hhw.startapp.helpers.ServiceRestClientHelper;import br.com.m2m.meuonibus.assetur.R;import br.com.m2m.meuonibus.assetur.activities.OnibusDetalheActivity;import br.com.m2m.meuonibus.assetur.models.ws.MeuOnibusWS;import br.com.m2m.meuonibuscommons.handlers.LinhaOnibusHandler;import br.com.m2m.meuonibuscommons.models.LinhaOnibus;public class ListaLinhasFragment extends Fragment implements OnRefreshListener {	private Activity ownerActivity;	private SwipeRefreshLayout swipeLayout;	private ListView listView;	private static int SECONDS_CALL_LINES_INFORMATION = 30;	private TimerTask task;	private Handler handler;	private boolean requestSuccess;	public static final String BUNDLE_LISTA_LINHAS_PONTO = ListaLinhasFragment.class.getName() + "#bundle_ponto";	private ArrayList<LinhaOnibus> linhas = new ArrayList<LinhaOnibus>();	public static ListaLinhasFragment newInstance() {		return new ListaLinhasFragment();	}	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		setHasOptionsMenu(true);		requestSuccess = false;		handler = new Handler();		View contentView = inflater.inflate(R.layout.fragment_lista_linhas, container, false);		setLayout(contentView);		setSwipeRefresh(contentView);		return contentView;	}	@Override	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {		super.onCreateOptionsMenu(menu, inflater);	}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		ownerActivity.onBackPressed();		return super.onOptionsItemSelected(item);	}	@Override	public void onDestroy() {		super.onDestroy();		ServiceRestClientHelper.cancelRequests(ownerActivity);	}	@Override	public void onAttach(Activity activity) {		super.onAttach(activity);		ownerActivity = activity;	}	@Override	public void onRefresh() {		loadLinhas();	}	@Override	public void onResume() {		super.onResume();		startTask();	}	@Override	public void onPause() {		super.onPause();		stopTask();	}	@SuppressLint("NewApi")	private void setLayout(View contentView) {		listView = (ListView) contentView.findViewById(R.id.linhas_listview);	}	private void setSwipeRefresh(View contentView) {		swipeLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_container_lista_linhas_fragment);		swipeLayout.setOnRefreshListener((OnRefreshListener) this);	}	private void loadLinhas() {		swipeLayout.post(new Runnable() {			@Override			public void run() {				swipeLayout.setRefreshing(true);			}		});		swipeLayout.setRefreshing(false);		swipeLayout.setEnabled(false);		MeuOnibusWS.getLinhas(ownerActivity, new LinhaOnibusHandler() {	      @Override	      public void setLinhasOnibus(ArrayList<LinhaOnibus> linhasHandler) {	        swipeLayout.setRefreshing(false);	        swipeLayout.setEnabled(true);	        linhas = linhasHandler;	        showLinhas();	      }	      @Override	      public void setErro(Throwable e) {	        swipeLayout.setRefreshing(false);	        swipeLayout.setEnabled(true);	        try {	          Toast.makeText(ownerActivity.getApplicationContext(),	              getString(R.string.nao_foi_possivel_localizar_linhas), Toast.LENGTH_SHORT).show();	          if (!requestSuccess) {	            showLinhas(null);	          }	          Log.d("request erro >>>>> ", "excecao");	        } catch (Exception e2) {	        }	      }	    });	  }	private void showLinhas() {		ArrayAdapter<LinhaOnibus> adapter = new LinhasAdapter(ownerActivity, linhas);		listView.setAdapter(adapter);	}	private void showLinhas(ArrayList<LinhaOnibus> lista) {		ArrayAdapter<LinhaOnibus> adapter = new LinhasAdapter(ownerActivity, lista);		listView.setAdapter(adapter);	}	private void startTask() {		if (task != null)			return;		task = new TimerTask() {			public void run() {				loadLinhasTask();			}		};		new Timer().scheduleAtFixedRate(task, 0, 1000 * SECONDS_CALL_LINES_INFORMATION);	}	private void stopTask() {		if (task == null)			return;		task.cancel();		task = null;	}	public void loadLinhasTask() {		Runnable runnable = new Runnable() {			@Override			public void run() {				handler.post(new Runnable() {					@Override					public void run() {						loadLinhas();					}				});			}		};		new Thread(runnable).start();	}	public class LinhasAdapter extends ArrayAdapter<LinhaOnibus> {		private List<LinhaOnibus> linhas;		public LinhasAdapter(Context context, List<LinhaOnibus> objects) {			super(context, 0, objects);			this.linhas = objects;		}		@Override		public int getCount() {			if (linhas == null) {				return 1;			}			if (linhas.size() == 0) {				return 1;			} else {				requestSuccess = true;			}			return linhas.size();		}		@SuppressLint("InflateParams")		@Override		public View getView(int position, View convertView, ViewGroup parent) {			View v = convertView;			ViewHolder viewHolder = null;			LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);						if (linhas == null || linhas.size() == 0) {				if (convertView == null) {					v = li.inflate(R.layout.adapter_mensagem, null);				}				return v;			}						final LinhaOnibus currentObj = linhas.get(position);			if (convertView == null) {				v = li.inflate(R.layout.adapter_lista_linhas, null);				viewHolder = new ViewHolder(v);				v.setTag(viewHolder);			} else {				viewHolder = (ViewHolder) v.getTag();			}						v.setOnClickListener(new View.OnClickListener() {				@Override				public void onClick(View view) {					Intent intent = new Intent(ownerActivity, OnibusDetalheActivity.class);					Bundle extra = new Bundle();					extra.putSerializable(OnibusDetalheActivity.BUNDLE_ONIBUS_DETALHE_LINHA, currentObj);					intent.putExtra(OnibusDetalheActivity.EXTRA_ONIBUS_DETALHE_LINHA, extra);					ownerActivity.startActivity(intent);				}			});			String nome = currentObj.name;			viewHolder.nome.setText(nome);			return v;		}		class ViewHolder {			public int idLinha;			public TextView nome;			public ViewHolder(View base) {				nome = (TextView) base.findViewById(R.id.nome_adapter_lista_linha);			}		}	}}