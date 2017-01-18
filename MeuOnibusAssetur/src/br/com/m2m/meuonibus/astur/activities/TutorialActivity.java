package br.com.m2m.meuonibus.astur.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import br.com.m2m.meuonibus.astur.R;
import br.com.m2m.meuonibus.astur.adapters.TutorialPagerAdapter;

public class TutorialActivity extends ActionBarActivity {

	private ArrayList<ImageView> imagesScrollPager;

	private ImageView imageScroll0;
	private ImageView imageScroll1;
	private ImageView imageScroll2;

	private ImageView imageScroll3;
	private ImageView imageScroll4;
	private ImageView imageScroll5;
	
	private ImageView imageScroll6;
	private ImageView imageScroll7;

	private int pagerCurrentItem = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tutorial);

		loadTutorialPagerIndicator();
		setPagerScrollImageSelected(pagerCurrentItem);

		final ViewPager vp = (ViewPager) findViewById(R.id.tutorial_view_pager_content);
		vp.setAdapter(new TutorialPagerAdapter(getSupportFragmentManager()));

		vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				setPagerScrollImageSelected(position);
			}

		});

		vp.setCurrentItem(0);
	}

	private void loadTutorialPagerIndicator() {
		imageScroll0 = (ImageView) findViewById(R.id.fragment_seja_treinador_scroll_indicator_0);
		imageScroll1 = (ImageView) findViewById(R.id.fragment_seja_treinador_scroll_indicator_1);
		imageScroll2 = (ImageView) findViewById(R.id.fragment_seja_treinador_scroll_indicator_2);
		imageScroll3 = (ImageView) findViewById(R.id.fragment_seja_treinador_scroll_indicator_3);
		imageScroll4 = (ImageView) findViewById(R.id.fragment_seja_treinador_scroll_indicator_4);
		imageScroll5 = (ImageView) findViewById(R.id.fragment_seja_treinador_scroll_indicator_5);
		imageScroll6 = (ImageView) findViewById(R.id.fragment_seja_treinador_scroll_indicator_6);
		imageScroll7 = (ImageView) findViewById(R.id.fragment_seja_treinador_scroll_indicator_7);

		imagesScrollPager = new ArrayList<ImageView>();
		imagesScrollPager.add(imageScroll0);
		imagesScrollPager.add(imageScroll1);
		imagesScrollPager.add(imageScroll2);
		imagesScrollPager.add(imageScroll3);
		imagesScrollPager.add(imageScroll4);
		imagesScrollPager.add(imageScroll5);
		imagesScrollPager.add(imageScroll6);
		imagesScrollPager.add(imageScroll7);
	}

	private void setPagerScrollImageSelected(int position) {
		ImageView imageScrollLastIndex = imagesScrollPager
				.get(pagerCurrentItem);
		imageScrollLastIndex.setImageDrawable(getResources().getDrawable(
				R.drawable.scroll_inativo));

		ImageView imageScrollNextIndex = imagesScrollPager.get(position);
		imageScrollNextIndex.setImageDrawable(getResources().getDrawable(
				R.drawable.scroll_ativo));

		pagerCurrentItem = position;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
