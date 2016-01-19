package br.com.m2m.meuonibusviacaoatual.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import br.com.m2m.meuonibusviacaoatual.R;

@SuppressLint({ "SetJavaScriptEnabled", "ValidFragment" })
public class TutorialFragment extends Fragment {

	private ImageView tutorialImage;
	private LinearLayout viewTutorialButtom;
	private Button tutorialButtom;
	
	private Integer imageResource;

	Activity ownerActivity;

	public TutorialFragment(Integer imageResource, int textResource) {
		this.imageResource = imageResource;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View contentView = inflater.inflate(R.layout.fragment_tutorial,
				container, false);

		tutorialImage = (ImageView) contentView
				.findViewById(R.id.tutorial_imageview);
		tutorialImage.setImageResource(imageResource);

		viewTutorialButtom =  (LinearLayout) contentView.findViewById(R.id.tutorial_ok_entendi);
		
		tutorialButtom =  (Button) contentView.findViewById(R.id.buttom_tutorial_ok_entendi);
		
		if(imageResource == R.drawable.tutorial_100) {
			viewTutorialButtom.setVisibility(View.VISIBLE);
			tutorialButtom.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ownerActivity.onBackPressed();
				}
			});
		} 
		return contentView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.ownerActivity = activity;
	}
	
//	// metodo para saber que fragment está visivel no ViewPager
//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		if (isVisibleToUser) {
//			if(imageResource == R.drawable.tutorial_6) {
//				viewTutorialButtom.setVisibility(View.VISIBLE);
//			} else {
////				tutorialButtom.setVisibility(View.GONE);
//			}
//		} 
//	}

}
