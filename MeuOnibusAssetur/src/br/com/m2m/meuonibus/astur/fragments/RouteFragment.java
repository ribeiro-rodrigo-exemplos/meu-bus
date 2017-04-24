package br.com.m2m.meuonibus.astur.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import br.com.m2m.meuonibus.astur.R;
import br.com.m2m.meuonibuscommons.models.Coordenada;

public class RouteFragment extends Fragment {
	
	private WebView mWebView;
	private Coordenada mOrigin;
	private Coordenada mDestiny;
	
	private Activity ownerActivity;
	
	public static final String BUNDLE_ROUTE_ORIGIN = RouteFragment.class
			.getName() + "#bundle_ORIGIN";
	
	public static final String BUNDLE_ROUTE_DESTINY = RouteFragment.class
			.getName() + "#bundle_DESTINY";
	
	public static RouteFragment newInstance(Coordenada origin, Coordenada Destiny) {
		RouteFragment fragment = new RouteFragment();
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(BUNDLE_ROUTE_ORIGIN, origin);
		bundle.putSerializable(BUNDLE_ROUTE_DESTINY, Destiny);
		fragment.setArguments(bundle);
		
		return fragment;	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_route, container, false);
		setHasOptionsMenu(true);

		mWebView = (WebView) view.findViewById(R.id.web_view);
		
		try {
			mOrigin = (Coordenada) getArguments().getSerializable(BUNDLE_ROUTE_ORIGIN);
			mDestiny = (Coordenada) getArguments().getSerializable(BUNDLE_ROUTE_DESTINY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onResume() {
		super.onResume();
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("http://maps.google.com/maps?" + 
				"saddr= " + mOrigin.latitude + "," + mOrigin.longitude + 
				"&daddr= " + mDestiny.latitude + "," + mDestiny.longitude);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		ownerActivity = activity;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ownerActivity.onBackPressed();
		return super.onOptionsItemSelected(item);
	}
}
