package br.com.hhw.startapp.fragments;

import br.com.hhw.startapp.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint({ "SetJavaScriptEnabled", "ValidFragment" })
public class TutorialFragment extends Fragment {

	private ImageView tutorialImage;
	private TextView tutorialText;

	private Integer imageResource;
	private int textResource;

	Activity ownerActivity;

	public TutorialFragment(Integer imageResource, int textResource) {
		this.imageResource = imageResource;
		this.textResource = textResource;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View contentView = inflater.inflate(R.layout.fragment_tutorial_start_app,
				container, false);

		tutorialImage = (ImageView) contentView
				.findViewById(R.id.tutorial_imageview);
		tutorialImage.setImageResource(imageResource);

		tutorialText = (TextView) contentView.findViewById(R.id.tutorial_text);
		tutorialText.setText(textResource);

		return contentView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.ownerActivity = activity;
	}

	// TODO: Verificar qual deve ser o tutorial da v1, webview ou pager???
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	//
	// setHasOptionsMenu(true);
	//
	// View contentView = inflater.inflate(R.layout.fragment_tutorial,
	// container, false);
	//
	// WebView webview = (WebView) contentView
	// .findViewById(R.id.tutorial_webview);
	// webview.getSettings().setJavaScriptEnabled(true);
	// webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	// webview.loadUrl("http://162.243.239.6/ios.html");
	//
	// return contentView;
	// }
}
