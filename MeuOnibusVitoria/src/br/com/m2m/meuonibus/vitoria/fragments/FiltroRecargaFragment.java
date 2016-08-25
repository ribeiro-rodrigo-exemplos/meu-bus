package br.com.m2m.meuonibus.vitoria.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import br.com.m2m.meuonibus.vitoria.R;

public class FiltroRecargaFragment extends Fragment {

	private Activity ownerActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View contentView = inflater.inflate(R.layout.activity_filtro_recarga, container, false);
		return contentView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_search, menu);
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
