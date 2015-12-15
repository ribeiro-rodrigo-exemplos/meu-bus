package br.com.m2m.meuonibusguanabara.fragments;import java.util.ArrayList;import java.util.List;import android.annotation.SuppressLint;import android.app.Activity;import android.content.Context;import android.content.Intent;import android.os.Bundle;import android.support.v4.app.Fragment;import android.view.LayoutInflater;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.widget.ArrayAdapter;import android.widget.FrameLayout;import android.widget.ImageView;import android.widget.ListView;import android.widget.TextView;import br.com.m2m.meuonibuscommons.activities.helpers.AppCustomDialogHelper;import br.com.m2m.meuonibuscommons.handlers.AppCustomDialogHandler;import br.com.m2m.meuonibuscommons.models.PontoOnibus;import br.com.m2m.meuonibuscommons.models.db.PontoDAO;import br.com.m2m.meuonibusguanabara.R;import br.com.m2m.meuonibusguanabara.activities.ListaLinhasActivity;public class MeusPontosFragment extends Fragment {	PontoOnibus ponto;	private Activity ownerActivity;	private ListView listView;	private FrameLayout frameNoContent;	MenuItem favorito;	private PontoDAO pontoDao;	private List<PontoOnibus> pontos = new ArrayList<PontoOnibus>();	private List<PontoOnibus> pontosToDelete = new ArrayList<PontoOnibus>();	private List<AdapterViewHolder> listViewHolder = new ArrayList<AdapterViewHolder>();	private boolean isDeleteMode;			@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {		isDeleteMode = false;		setHasOptionsMenu(true);		View contentView = inflater.inflate(R.layout.fragment_meus_pontos,				container, false);		setLayout(contentView);		pontoDao = new PontoDAO(ownerActivity);		pontoDao.open();//		Coordenada latLong = new Coordenada();//		latLong.latitude = -14.2392976;//		latLong.longitude = -53.1805017;////		ponto = new PontoOnibus();//		ponto.nome = "Ponto aaa lalal alala";//		ponto.endereco = "Rua llalala laala";//		ponto.idPonto = 111222333;//		ponto.latLong = latLong;////		pontoDao = new PontoDAO(ownerActivity);//		pontoDao.open();////		pontoDao.insertPonto(ponto);////		ponto = new PontoOnibus();//		ponto.nome = "Ponto teste";//		ponto.endereco = "Rua teste";//		ponto.idPonto = 111222334;//		ponto.latLong = latLong;////		pontoDao.insertPonto(ponto);//		ponto = new PontoOnibus();//		ponto.nome = "Ponto teste 2";//		ponto.endereco = "Rua teste 2";//		ponto.idPonto = 111222335;//		ponto.latLong = latLong;//		pontoDao.insertPonto(ponto);				//colocado chamada no onresume//		showPontos();		return contentView;	}	private void setLayout(View contentView) {		frameNoContent = (FrameLayout) contentView.findViewById(R.id.frame_meus_pontos);		listView = (ListView) contentView				.findViewById(R.id.meus_pontos_listview);	}	@Override	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {		super.onCreateOptionsMenu(menu, inflater);		inflater.inflate(R.menu.menu_apagar, menu);		favorito = menu.findItem(R.id.action_apagar);		// exibe menu se existir favoritos		showMenu();	}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		int id = item.getItemId();		if (id == R.id.action_apagar) {			actionMenu();			return true;		} else {			ownerActivity.onBackPressed();		}		return super.onOptionsItemSelected(item);	}	private void showCheckOptions(boolean isToShow) {		for (AdapterViewHolder view : listViewHolder) {			view.check.setVisibility(isToShow == true ? View.VISIBLE					: View.INVISIBLE);		}	}	public void showMenu() {		if (pontoDao.getAllPontos().size() < 1) {			favorito.setVisible(false);		} else {			favorito.setVisible(true);		}	}	public void actionMenu() {		if (isDeleteMode) {			if (pontosToDelete.size() > 0) {				showConfirmationDelete();			} else {				showNoSelect() ;			}		} else {			isDeleteMode = true;			//imagem invertida			favorito.setIcon(R.drawable.ac_lixeira_off);			showCheckOptions(true);		}	}	private void showConfirmationDelete() {		String text = getString(R.string.voce_realmente_deseja_apagar_itens);		AppCustomDialogHelper.showGenericDialog(ownerActivity, null, text, getString(R.string.cancelar),				new AppCustomDialogHandler() {					@Override					public void setOk() {						deleteFavorito();					}					@Override					public void setNo() {						disableDeleteMode();					}				});	}		private void showNoSelect() {		String text = getString(R.string.voce_nao_selecionou);		AppCustomDialogHelper.showGenericDialog(ownerActivity, null, text, getString(R.string.cancelar),				new AppCustomDialogHandler() {					@Override					public void setOk() {						deleteFavorito();					}					@Override					public void setNo() {						disableDeleteMode();					}				});	}	private void disableDeleteMode() {		isDeleteMode = false;		pontosToDelete.clear();		//imagem invertida		favorito.setIcon(R.drawable.ac_lixeira_on);		showCheckOptions(false);		showPontos();		showMenu();	}	private void deleteFavorito() {		for (PontoOnibus ponto : pontosToDelete) {			pontoDao.deletePonto(ponto);		}		disableDeleteMode();	}	@Override	public void onDestroy() {		super.onDestroy();		pontoDao.close();	}	@Override	public void onAttach(Activity activity) {		super.onAttach(activity);		ownerActivity = activity;	}	private void showPontos() {		pontos.clear();		pontos = pontoDao.getAllPontos();				if (pontos.size() > 0) {			frameNoContent.setVisibility(View.GONE);		} else {			frameNoContent.setVisibility(View.VISIBLE);			if (favorito != null) favorito.setVisible(false);			}				ArrayAdapter<PontoOnibus> adapter = new LinhasAdapter(ownerActivity,				pontos);		listView.setAdapter(adapter);	}	public class LinhasAdapter extends ArrayAdapter<PontoOnibus> {		private List<PontoOnibus> pontos;		public LinhasAdapter(Context context, List<PontoOnibus> objects) {			super(context, 0, objects);			this.pontos = objects;			listViewHolder.clear();		}		@Override		public int getCount() {			return pontos.size();		}		@SuppressLint("InflateParams")		@Override		public View getView(int position, View convertView, ViewGroup parent) {			View v = convertView;			final AdapterViewHolder viewHolder;			PontoOnibus currentObj = pontos.get(position);			if (convertView == null) {				LayoutInflater li = (LayoutInflater) getContext()						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);				v = li.inflate(R.layout.adapter_meus_pontos, null);				viewHolder = new AdapterViewHolder(v);				v.setTag(viewHolder);				listViewHolder.add(viewHolder);				v.setOnClickListener(new View.OnClickListener() {					@Override					public void onClick(View view) {						AdapterViewHolder adapterViewHolder = (AdapterViewHolder) view								.getTag();						if (isDeleteMode) {							if (adapterViewHolder.isCheck) {								adapterViewHolder.isCheck = false;								adapterViewHolder.check										.setImageResource(R.drawable.ic_checkbox_off);								pontosToDelete.remove(adapterViewHolder.ponto);							} else {								adapterViewHolder.isCheck = true;								adapterViewHolder.check										.setImageResource(R.drawable.ic_checkbox_on);								pontosToDelete.add(adapterViewHolder.ponto);							}						} else {							Intent intent = new Intent(ownerActivity,									ListaLinhasActivity.class);							Bundle extra = new Bundle();							extra.putSerializable(									ListaLinhasActivity.BUNDLE_LISTA_LINHAS_PONTO,									adapterViewHolder.ponto);							intent.putExtra(									ListaLinhasActivity.EXTRA_LISTA_LINHAS_PONTO,									extra);							ownerActivity.startActivity(intent);						}					}				});			} else {				viewHolder = (AdapterViewHolder) v.getTag();				viewHolder.isCheck = false;			}			viewHolder.ponto = currentObj;			viewHolder.nome.setText(currentObj.nome);			return v;		}	}	class AdapterViewHolder {		public PontoOnibus ponto;		public boolean isCheck = false;		public TextView nome;		public ImageView check;		public AdapterViewHolder(View base) {			nome = (TextView) base					.findViewById(R.id.nome_ponto_adapter_meus_pontos);			check = (ImageView) base					.findViewById(R.id.check_adapter_meus_pontos);		}	}		@Override	public void onPause() {		super.onPause();		}		@Override	public void onResume() {		super.onResume();			showPontos();	}}