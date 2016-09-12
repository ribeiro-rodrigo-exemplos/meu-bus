package br.com.m2m.meuonibus.mandala.activities;

import android.os.Bundle;
import br.com.m2m.meuonibus.mandala.R;
import br.com.m2m.meuonibus.mandala.fragments.NoticiaDetalheFragment;
import br.com.m2m.meuonibuscommons.activities.base.BaseWithTitleActivity;
import br.com.m2m.meuonibuscommons.models.Noticia;

public class NoticiaDetalheActivity extends BaseWithTitleActivity {

	public static final String EXTRA_NOTICIA_DETALHE = NoticiaDetalheActivity.class
			.getName() + "#noticia";
	public static final String BUNDLE_NOTICIA_DETALHE = NoticiaDetalheActivity.class
			.getName() + "#bundle_noticia";

	public Noticia noticia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_lista_linhas);

		try {
			Bundle extra = getIntent().getBundleExtra(EXTRA_NOTICIA_DETALHE);
			noticia = (Noticia) extra
					.getSerializable(BUNDLE_NOTICIA_DETALHE);
			
//			Toast.makeText(this.getApplicationContext(),
//					noticia.titulo, Toast.LENGTH_SHORT).show();
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		setActionBarTitle(getString(R.string.noticias));

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container_lista_linha, NoticiaDetalheFragment.newInstance(noticia)).commit();
		}
	}
}
