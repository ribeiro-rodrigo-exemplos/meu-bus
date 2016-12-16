package br.com.m2m.meuonibus.viacaomodelo.activities;import android.os.Bundle;import android.support.v4.app.Fragment;import br.com.m2m.meuonibus.viacaomodelo.R;import br.com.m2m.meuonibus.viacaomodelo.fragments.OnibusDetalheFragment;import br.com.m2m.meuonibuscommons.activities.base.BaseWithImageAndTitleActivity;import br.com.m2m.meuonibuscommons.models.LinhaOnibus;public class OnibusDetalheActivity extends BaseWithImageAndTitleActivity {		public static final String EXTRA_ONIBUS_DETALHE_LINHA = OnibusDetalheActivity.class.getName()			+ "#linha";	public static final String BUNDLE_ONIBUS_DETALHE_LINHA = OnibusDetalheActivity.class.getName()			+ "#bundle_linha";		public LinhaOnibus linha;		@Override	protected void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);				setContentView(R.layout.activity_onibus_detalhe);				try {			Bundle extra = getIntent().getBundleExtra(EXTRA_ONIBUS_DETALHE_LINHA);			linha = (LinhaOnibus) extra.getSerializable(BUNDLE_ONIBUS_DETALHE_LINHA);		} catch (Exception e) {			// TODO: handle exception		}				if(linha != null) {			setActionBarImageAndTitle(linha.numero);		} else {			setActionBarImageAndTitle("Linha:");		}				if (savedInstanceState == null) {			Fragment fragment = OnibusDetalheFragment.newInstance(linha);			getSupportFragmentManager().beginTransaction()					.add(R.id.container_onibus_detalhe, fragment).commit();		}	}}