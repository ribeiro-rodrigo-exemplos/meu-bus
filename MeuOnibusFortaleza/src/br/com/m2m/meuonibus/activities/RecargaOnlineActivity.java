package br.com.m2m.meuonibus.activities;

import br.com.m2m.meuonibus.R;
import br.com.m2m.meuonibus.fragments.RecargaOnlineFragment;
import br.com.m2m.meuonibuscommons.activities.base.BaseWithTitleActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

public class RecargaOnlineActivity extends BaseWithTitleActivity {
	
	public static String URL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recarga_online);
		
		Fragment fragment = RecargaOnlineFragment.newInstance();
		getSupportFragmentManager().beginTransaction()
			.add(R.id.container_recarga_online, fragment).commit();
		
		ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_custom_with_logo);
	}
}
