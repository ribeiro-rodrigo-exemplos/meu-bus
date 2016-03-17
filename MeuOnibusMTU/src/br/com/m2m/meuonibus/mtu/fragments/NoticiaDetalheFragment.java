package br.com.m2m.meuonibus.mtu.fragments;import android.annotation.SuppressLint;import android.app.Activity;import android.os.Bundle;import android.support.v4.app.Fragment;import android.util.Log;import android.view.LayoutInflater;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.webkit.WebView;import br.com.hhw.startapp.helpers.ServiceRestClientHelper;import br.com.m2m.meuonibus.mtu.R;import br.com.m2m.meuonibuscommons.activities.helpers.DateTimeHelper;import br.com.m2m.meuonibuscommons.activities.helpers.RawFilesHelper;import br.com.m2m.meuonibuscommons.models.Noticia;public class NoticiaDetalheFragment extends Fragment {	Noticia noticia;	private Activity ownerActivity;	private WebView webview;	public static final String BUNDLE_NOTICIA_DETALHES = NoticiaDetalheFragment.class			.getName() + "#bundle_noticia";	public static NoticiaDetalheFragment newInstance(Noticia noticia) {		NoticiaDetalheFragment fragment = new NoticiaDetalheFragment();		Bundle bundle = new Bundle();		bundle.putSerializable(BUNDLE_NOTICIA_DETALHES, noticia);		fragment.setArguments(bundle);		return fragment;	}	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {				setHasOptionsMenu(true);		View contentView = inflater.inflate(R.layout.fragment_noticia_detalhe,				container, false);		setLayout(contentView);		try {			noticia = (Noticia) getArguments().getSerializable(					BUNDLE_NOTICIA_DETALHES);//			Toast.makeText(ownerActivity.getApplicationContext(),//					noticia.titulo, Toast.LENGTH_SHORT).show();			showNoticias();		} catch (Exception e) {			Log.d("Error", e.toString());		}		return contentView;	}	private void setLayout(View contentView) {		webview = (WebView) contentView.findViewById(R.id.noticia_webview);	}	@Override	public boolean onOptionsItemSelected(MenuItem item) {		ownerActivity.onBackPressed();		return super.onOptionsItemSelected(item);	}		@Override	public void onDestroy() {		super.onDestroy();		ServiceRestClientHelper.cancelRequests(ownerActivity);	}	@Override	public void onAttach(Activity activity) {		super.onAttach(activity);		ownerActivity = activity;	}	private void showNoticias() {		buildNewsWebView();	}		@SuppressLint("SetJavaScriptEnabled") 	public void buildNewsWebView() {		String htmlContent = RawFilesHelper.loadRawFileWith(ownerActivity, R.raw.news_template);				htmlContent = htmlContent.replace("%{title}", noticia.titulo);		htmlContent = htmlContent.replace("%{datetime_with_format}", DateTimeHelper.formatDateTime(noticia.data));		htmlContent = htmlContent.replace("%{text}", noticia.conteudo);		if (noticia.imagens != null) {			String divImagem = "<div id=\"imagem\"><img src=\""+noticia.imagens.full.url+"\" /></div>";			htmlContent = htmlContent.replace("%{img}", divImagem);		} else {			htmlContent = htmlContent.replace("%{img}", "");		}				Log.d("html >>> ", htmlContent);		webview.getSettings().setJavaScriptEnabled(true);		webview.setBackgroundColor(0x00000000);		webview.loadDataWithBaseURL(				"file:///android_assets/", htmlContent, "text/html",				"utf-8", null);	}}