package br.com.hhw.startapp.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.hhw.startapp.R;
import br.com.hhw.startapp.fragments.TutorialFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TutorialPagerAdapter extends FragmentPagerAdapter {

	public final static List<Integer> TUTORIAL_IMAGES = Arrays
			.asList(new Integer[] { R.drawable.tutorial_1,
					R.drawable.tutorial_2, R.drawable.tutorial_3 });

	public final static List<Integer> TUTORIAL_TEXTS = Arrays
			.asList(new Integer[] { R.string.tutorial_text_1,
					R.string.tutorial_text_2, R.string.tutorial_text_3 });

	private ArrayList<Fragment> datasource;

	public TutorialPagerAdapter(FragmentManager fm) {
		super(fm);
		datasource = new ArrayList<Fragment>();
		
		for (int i = 0; i < TUTORIAL_IMAGES.size(); i++) {
			int imageResource = TUTORIAL_IMAGES.get(i);
			int textResource = TUTORIAL_TEXTS.get(i);
			datasource.add(new TutorialFragment(imageResource, textResource));
		}
	}

	@Override
	public Fragment getItem(int position) {
		return this.datasource.get(position);
	}

	@Override
	public int getCount() {
		return this.datasource.size();
	}

}
