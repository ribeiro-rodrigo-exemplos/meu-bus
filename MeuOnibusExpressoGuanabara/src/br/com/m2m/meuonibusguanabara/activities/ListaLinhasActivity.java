package br.com.m2m.meuonibusguanabara.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import br.com.m2m.meuonibusguanabara.R;
import br.com.m2m.meuonibuscommons.activities.base.BaseWithTitleActivity;
import br.com.m2m.meuonibuscommons.models.PontoOnibus;
import br.com.m2m.meuonibusguanabara.fragments.ListaLinhasFragment;

public class ListaLinhasActivity extends BaseWithTitleActivity {

	public static final String EXTRA_LISTA_LINHAS_PONTO = ListaLinhasActivity.class
			.getName() + "#ponto";
	public static final String BUNDLE_LISTA_LINHAS_PONTO = ListaLinhasActivity.class
			.getName() + "#bundle_ponto";

	public PontoOnibus ponto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_lista_linhas);

		try {
			Bundle extra = getIntent().getBundleExtra(EXTRA_LISTA_LINHAS_PONTO);
			ponto = (PontoOnibus) extra
					.getSerializable(BUNDLE_LISTA_LINHAS_PONTO);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (ponto != null) {
			setActionBarTitle(ponto.nome);
		} else {
			setActionBarTitle("Ponto:");
		}

		if (savedInstanceState == null) {
			Fragment fragment = ListaLinhasFragment.newInstance(ponto);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container_lista_linha, fragment).commit();
		}
	}

	public void setCustomTitle(String nome) {
		setActionBarTitle(nome);
	}
}

