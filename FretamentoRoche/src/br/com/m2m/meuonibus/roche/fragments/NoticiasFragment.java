package br.com.m2m.meuonibus.roche.fragments;import java.util.ArrayList;import java.util.List;import com.nostra13.universalimageloader.core.ImageLoader;import android.annotation.SuppressLint;import android.app.Activity;import android.content.Context;import android.content.Intent;import android.os.Bundle;import android.support.v4.app.Fragment;import android.support.v4.widget.SwipeRefreshLayout;import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;import android.util.Log;import android.view.LayoutInflater;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.widget.ArrayAdapter;import android.widget.ImageView;import android.widget.ListView;import android.widget.RelativeLayout;import android.widget.TextView;import android.widget.Toast;import br.com.m2m.meuonibuscommons.R;import br.com.m2m.meuonibus.roche.activities.NoticiaDetalheActivity;import br.com.m2m.meuonibus.roche.activities.helpers.DateTimeHelper;import br.com.m2m.meuonibus.roche.models.ws.MeuOnibusWS;import br.com.m2m.meuonibuscommons.handlers.NoticiaHandler;import br.com.m2m.meuonibuscommons.models.Noticia;import br.com.m2m.meuonibuscommons.models.NoticiaJsonObj;public class NoticiasFragment extends Fragment implements OnRefreshListener{	private Activity ownerActivity;	private ListView listView;	private SwipeRefreshLayout swipeLayout;	private RelativeLayout noContent;	private List<Noticia> noticias = new ArrayList<Noticia>();//	private List<AdapterViewHolder> listViewHolder = new ArrayList<AdapterViewHolder>();	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {		View contentView = inflater.inflate(R.layout.fragment_noticias,				container, false);		setLayout(contentView);		setSwipeRefresh(contentView);				loadNoticias();		return contentView;	}	private void setLayout(View contentView) {		noContent = (RelativeLayout) contentView				.findViewById(R.id.container_no_news);		listView = (ListView) contentView				.findViewById(R.id.noticias_listview);	}		private void setSwipeRefresh(View contentView) {		swipeLayout = (SwipeRefreshLayout) contentView				.findViewById(R.id.swipe_container_noticias_fragment);		swipeLayout.setOnRefreshListener((OnRefreshListener) this);	}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		ownerActivity.onBackPressed();		return super.onOptionsItemSelected(item);	}	@Override	public void onDestroy() {		super.onDestroy();	}	@Override	public void onAttach(Activity activity) {		super.onAttach(activity);		ownerActivity = activity;	}	private void loadNoticias() {				swipeLayout.post(new Runnable() {			@Override			public void run() {				swipeLayout.setRefreshing(true);			}		});		swipeLayout.setRefreshing(false);		swipeLayout.setEnabled(false);				MeuOnibusWS.getNoticias( new NoticiaHandler() {						@Override			public void setNoticiaObj(NoticiaJsonObj noticiasHandler) {				swipeLayout.setRefreshing(false);				swipeLayout.setEnabled(true);				noticias = noticiasHandler.noticias;				showNoticia();			}			@Override			public void setErro(Throwable e) {				swipeLayout.setRefreshing(false);				swipeLayout.setEnabled(true);				noNewsContent();				Toast.makeText(ownerActivity.getApplicationContext(), getString(R.string.mensagem_sem_noticia),						Toast.LENGTH_SHORT).show();			}		});	}	private void noNewsContent() {		noContent.setVisibility(View.VISIBLE);		listView.setVisibility(View.GONE);	}		private void showNoticia() {//		Toast.makeText(ownerActivity.getApplicationContext(),//				noticias.get(0).titulo,//				Toast.LENGTH_SHORT).show();		if (noticias.size() > 0) {			noContent.setVisibility(View.GONE);			listView.setVisibility(View.VISIBLE);			ArrayAdapter<Noticia> adapter = new NoticiasAdapter(ownerActivity,					noticias);			listView.setAdapter(adapter);		} else {			noNewsContent();		}	}	public class NoticiasAdapter extends ArrayAdapter<Noticia> {		private List<Noticia> noticias;		public NoticiasAdapter(Context context, List<Noticia> objects) {			super(context, 0, objects);			this.noticias = objects;		}		@Override		public int getCount() {			return noticias.size();		}		@SuppressLint("InflateParams")		@Override		public View getView(int position, View convertView, ViewGroup parent) {			View v = convertView;			final AdapterViewHolder viewHolder;			Noticia currentObj = noticias.get(position);			if (convertView == null) {				LayoutInflater li = (LayoutInflater) getContext()						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);				v = li.inflate(R.layout.adapter_lista_noticias, null);				viewHolder = new AdapterViewHolder(v, currentObj);				v.setTag(viewHolder);				v.setOnClickListener(new View.OnClickListener() {					@Override					public void onClick(View view) {						AdapterViewHolder adapterViewHolder = (AdapterViewHolder) view								.getTag();						Intent intent = new Intent(ownerActivity,								NoticiaDetalheActivity.class);						Bundle extra = new Bundle();						extra.putSerializable(								NoticiaDetalheActivity.BUNDLE_NOTICIA_DETALHE,								adapterViewHolder.noticia);						intent.putExtra(								NoticiaDetalheActivity.EXTRA_NOTICIA_DETALHE,								extra);						ownerActivity.startActivity(intent);					}				});			}			return v;		}	}	class AdapterViewHolder {		public Noticia noticia;		public TextView titulo;		public TextView data;		public ImageView img;		public AdapterViewHolder(View base, Noticia noticia) {			this.noticia = noticia;			titulo = (TextView) base					.findViewById(R.id.titulo_adapter_noticias);			data = (TextView) base					.findViewById(R.id.data_adapter_noticias);			img = (ImageView) base					.findViewById(R.id.image_adapter_noticias);						titulo.setText(noticia.titulo);			data.setText(DateTimeHelper.formatDateTime(noticia.data));						if (noticia.imagens!= null) {				Log.d(">>>>>> ", noticia.imagens.thumb.url);				ImageLoader.getInstance().displayImage(noticia.imagens.thumb.url, img);			}					}	}	@Override	public void onPause() {		super.onPause();	}	@Override	public void onResume() {		super.onResume();	}	@Override	public void onRefresh() {		loadNoticias();		}}