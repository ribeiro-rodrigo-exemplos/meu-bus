package br.com.m2m.meuonibus.roche.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import br.com.m2m.meuonibuscommons.R;
import br.com.m2m.meuonibus.roche.fragments.RouteFragment;
import br.com.m2m.meuonibuscommons.activities.base.BaseWithTitleActivity;
import br.com.m2m.meuonibuscommons.models.Coordenada;

public class RouteActivity extends BaseWithTitleActivity {
	
	public static final String EXTRA_ROUTE_ORIGEM = RouteActivity.class.getName()
			+ "#origem";
	public static final String EXTRA_ROUTE_DESTINO = RouteActivity.class.getName()
			+ "#destino";
	
	public Coordenada mOrigin;
	public Coordenada mDestiny;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		actionBar.setCustomView(R.layout.actionbar_custom_with_logo);
 
		try {
			mOrigin = (Coordenada) getIntent().getSerializableExtra(EXTRA_ROUTE_ORIGEM);
			mDestiny = (Coordenada) getIntent().getSerializableExtra(EXTRA_ROUTE_DESTINO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (savedInstanceState == null) {
			Fragment fragment = RouteFragment.newInstance(mOrigin, mDestiny);
			getSupportFragmentManager().beginTransaction().add(R.id.container_route, fragment).commit();
		}
	}
}
