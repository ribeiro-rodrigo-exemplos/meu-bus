package br.com.hhw.startapp.activities;

import java.util.ArrayList;

import br.com.hhw.startapp.R;
import br.com.hhw.startapp.adapters.TutorialPagerAdapter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TutorialStartAppActivity extends ActionBarActivity {

	private Button closeButton;

	private ArrayList<ImageView> imagesScrollPager;

	private ImageView imageScroll0;
	private ImageView imageScroll1;
	private ImageView imageScroll2;

	private int pagerCurrentItem = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tutorial_start_app);

		closeButton = (Button) findViewById(R.id.tutorial_close);

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

		this.closeButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

	}

	private void loadTutorialPagerIndicator() {
		imageScroll0 = (ImageView) findViewById(R.id.fragment_seja_treinador_scroll_indicator_0);
		imageScroll1 = (ImageView) findViewById(R.id.fragment_seja_treinador_scroll_indicator_1);
		imageScroll2 = (ImageView) findViewById(R.id.fragment_seja_treinador_scroll_indicator_2);

		imagesScrollPager = new ArrayList<ImageView>();
		imagesScrollPager.add(imageScroll0);
		imagesScrollPager.add(imageScroll1);
		imagesScrollPager.add(imageScroll2);

	}

	private void setPagerScrollImageSelected(int position) {
		ImageView imageScrollLastIndex = imagesScrollPager
				.get(pagerCurrentItem);
		imageScrollLastIndex.setImageDrawable(getResources().getDrawable(
				R.drawable.scroll_inativo_bgesc));

		ImageView imageScrollNextIndex = imagesScrollPager.get(position);
		imageScrollNextIndex.setImageDrawable(getResources().getDrawable(
				R.drawable.scroll_ativo_bgesc));

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
