package br.com.m2m.meuonibus.fragments;import java.util.ArrayList;import java.util.List;import android.annotation.SuppressLint;import android.app.Activity;import android.content.Context;import android.os.Bundle;import android.support.v4.app.Fragment;import android.view.LayoutInflater;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.widget.ArrayAdapter;import android.widget.ImageView;import android.widget.ListView;import android.widget.TextView;import br.com.m2m.meuonibus.R;import br.com.m2m.meuonibuscommons.models.AjustesOpcaoRaioBusca;public class AjustesFragment extends Fragment {	private Activity ownerActivity;	private ListView listView;	private List<AjustesOpcaoRaioBusca> opcoes = new ArrayList<AjustesOpcaoRaioBusca>();	private List<AdapterViewHolder> listViewHolder = new ArrayList<AdapterViewHolder>();	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {		setHasOptionsMenu(true);		View contentView = inflater.inflate(R.layout.fragment_ajustes,				container, false);		setLayout(contentView);		createOption();		showOptions();				return contentView;	}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		ownerActivity.onBackPressed();		return super.onOptionsItemSelected(item);	}		@Override	public void onPause() {		super.onPause();	}	@Override	public void onResume() {		super.onResume();	}		@Override	public void onDestroy() {		super.onDestroy();	}	@Override	public void onAttach(Activity activity) {		super.onAttach(activity);		ownerActivity = activity;	}		private void setLayout(View contentView) {		listView = (ListView) contentView				.findViewById(R.id.ajustes_listview);	}		private void showOptions() {		ArrayAdapter<AjustesOpcaoRaioBusca> adapter = new AjustesAdapter(ownerActivity,				opcoes);		listView.setAdapter(adapter);	}		private void createOption() {		opcoes.clear();		opcoes.add(new AjustesOpcaoRaioBusca(getString(R.string.opcao_300m), "300"));		opcoes.add(new AjustesOpcaoRaioBusca(getString(R.string.opcao_500m), "500"));		opcoes.add(new AjustesOpcaoRaioBusca(getString(R.string.opcao_1k), "1000"));		opcoes.add(new AjustesOpcaoRaioBusca(getString(R.string.opcao_2k), "2000"));		checkOptionDefault();	}		private void checkOptionDefault() {		for (AjustesOpcaoRaioBusca opcao : opcoes) {			if (opcao.valor.equals(AjustesOpcaoRaioBusca.getSavedRaio(ownerActivity))) {				opcao.isCheck = true;				break;			}		}	}		private void cleanOpcoes() {		for (AdapterViewHolder view : listViewHolder) {			view.unCheck();		}	}	public class AjustesAdapter extends ArrayAdapter<AjustesOpcaoRaioBusca> {		private List<AjustesOpcaoRaioBusca> opcoes;		public AjustesAdapter(Context context, List<AjustesOpcaoRaioBusca> objects) {			super(context, 0, objects);			this.opcoes = objects;		}		@Override		public int getCount() {			return opcoes.size();		}		@SuppressLint("InflateParams")		@Override		public View getView(int position, View convertView, ViewGroup parent) {			View v = convertView;			final AdapterViewHolder viewHolder;			AjustesOpcaoRaioBusca currentObj = opcoes.get(position);			if (convertView == null) {				LayoutInflater li = (LayoutInflater) getContext()						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);				v = li.inflate(R.layout.adapter_lista_ajustes, null);				viewHolder = new AdapterViewHolder(v, currentObj);				v.setTag(viewHolder);				listViewHolder.add(viewHolder);				v.setOnClickListener(new View.OnClickListener() {					@Override					public void onClick(View view) {						AdapterViewHolder adapterViewHolder = (AdapterViewHolder) view								.getTag();						cleanOpcoes();						adapterViewHolder.check();						AjustesOpcaoRaioBusca.saveRaio(ownerActivity, adapterViewHolder.opcao.valor);					}				});			}			return v;		}	}	class AdapterViewHolder {		public AjustesOpcaoRaioBusca opcao;		public boolean isCheck = false;		public TextView nome;		public ImageView check;				public void check() {			check.setVisibility(View.VISIBLE);			nome.setTextColor(getResources().getColor(R.color.green));		}				public void unCheck() {			check.setVisibility(View.INVISIBLE);			nome.setTextColor(getResources().getColor(R.color.textDefault));		}		public AdapterViewHolder(View base, AjustesOpcaoRaioBusca opcao) {			this.opcao = opcao;			nome = (TextView) base					.findViewById(R.id.nome_opcao_adapter_ajustes);			check = (ImageView) base					.findViewById(R.id.check_adapter_ajustes);			nome.setText(opcao.nome);			if (opcao.isCheck) {				check();			} else {				unCheck();			}		}	}	}