package br.com.m2m.meuonibus.astur.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import br.com.m2m.meuonibus.astur.R;
import br.com.m2m.meuonibus.astur.fragments.PrevisaoOnibusFragment;
import br.com.m2m.meuonibuscommons.activities.base.BaseWithTitleActivity;
import br.com.m2m.meuonibuscommons.models.LinhaOnibus;
import br.com.m2m.meuonibuscommons.models.PontoOnibus;
import br.com.m2m.meuonibuscommons.models.Trajeto;

public class PrevisaoOnibusActivity extends BaseWithTitleActivity {

	public static final String EXTRA_LISTA_LINHAS_PONTO = PrevisaoOnibusActivity.class.getName() + "#ponto";
	public static final String EXTRA_LISTA_LINHAS_TRAJETO = PrevisaoOnibusActivity.class.getName() + "#trajeto";
	public static final String EXTRA_LISTA_LINHAS = PrevisaoOnibusActivity.class.getName() + "#linha";

	public PontoOnibus mPonto;
	public Trajeto mTrajeto;
	public LinhaOnibus mLinhaOnibus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_lista_linhas);

		try {
			mPonto = (PontoOnibus) getIntent().getSerializableExtra(EXTRA_LISTA_LINHAS_PONTO);
			mTrajeto = (Trajeto) getIntent().getSerializableExtra(EXTRA_LISTA_LINHAS_TRAJETO);
			mLinhaOnibus = (LinhaOnibus) getIntent().getSerializableExtra(EXTRA_LISTA_LINHAS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (mPonto != null) {
			setActionBarTitle(mPonto.nome);
		} else {
			setActionBarTitle("Ponto:");
		}

		if (savedInstanceState == null) {
			Fragment fragment = PrevisaoOnibusFragment.newInstance(mPonto, mTrajeto, mLinhaOnibus);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container_lista_linha, fragment).commit();
		}
	}

	public void setCustomTitle(String nome) {
		setActionBarTitle(nome);
	}
}

