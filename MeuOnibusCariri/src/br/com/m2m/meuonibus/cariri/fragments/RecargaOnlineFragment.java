package br.com.m2m.meuonibus.cariri.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import br.com.m2m.meuonibus.cariri.MeuOnibusApplication;
import br.com.m2m.meuonibus.cariri.R;

public class RecargaOnlineFragment extends Fragment {

	private Activity ownerActivity;
	private ProgressDialog progDailog;
	private static String URL;
	
	public static RecargaOnlineFragment newInstance() {
		RecargaOnlineFragment fragment = new RecargaOnlineFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);

		return fragment;
	}

	@SuppressLint("SetJavaScriptEnabled") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View contentView = inflater.inflate(R.layout.fragment_recarga_online, container, false);

		progDailog = ProgressDialog.show(ownerActivity, "Carregando","Aguarde...", true);
        progDailog.setCancelable(false);
		
		WebView webView = (WebView) contentView.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);     
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);        
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	progDailog.show();
                view.loadUrl(url);

                return true;                
            }
            
            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }
        });
        
        InputStream rawResourse = MeuOnibusApplication.getContext().getResources()
				.openRawResource(R.raw.webservicesconfig);
		
		Properties properties = new Properties();
		
        try {
			properties.load(rawResourse);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        URL = properties.getProperty("recarga_online");
        webView.loadUrl(URL);
		
		return contentView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ownerActivity.onBackPressed();
		getFragmentManager().beginTransaction()
        // Add this transaction to the back stack
        .addToBackStack("fragment")
        .commit();
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		ownerActivity = activity;
	}
}
