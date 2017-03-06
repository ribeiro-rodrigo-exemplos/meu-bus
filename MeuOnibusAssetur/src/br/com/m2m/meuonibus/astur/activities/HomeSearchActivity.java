package br.com.m2m.meuonibus.astur.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import br.com.m2m.meuonibus.astur.R;
import br.com.m2m.meuonibus.astur.fragments.HomeSearchFragment;
import br.com.m2m.meuonibuscommons.activities.base.BaseWithTitleActivity;

public class HomeSearchActivity extends BaseWithTitleActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_linhas);
		
		Boolean isNewFilter = getIntent().getBooleanExtra("isNewFilter", false);
		if (isNewFilter) {
			actionBar.setCustomView(R.layout.actionbar_custom_with_logo);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
		}
	    if (savedInstanceState == null) {
			Fragment fragment = HomeSearchFragment.newInstance();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container_lista_linha, fragment).commit();
		}
	}

	public void setCustomTitle(String nome) {
		setActionBarTitle(nome);
	}
}
