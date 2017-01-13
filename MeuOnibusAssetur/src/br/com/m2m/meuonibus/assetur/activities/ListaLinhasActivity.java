package br.com.m2m.meuonibus.assetur.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import br.com.m2m.meuonibus.assetur.R;
import br.com.m2m.meuonibus.assetur.fragments.ListaLinhasFragment;
import br.com.m2m.meuonibuscommons.activities.base.BaseWithTitleActivity;
import br.com.m2m.meuonibuscommons.models.PontoOnibus;

public class ListaLinhasActivity extends BaseWithTitleActivity {

	public PontoOnibus ponto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_lista_linhas);

		if (savedInstanceState == null) {
			Fragment fragment = ListaLinhasFragment.newInstance(ponto);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container_lista_linha, fragment).commit();
		}
	}

}
