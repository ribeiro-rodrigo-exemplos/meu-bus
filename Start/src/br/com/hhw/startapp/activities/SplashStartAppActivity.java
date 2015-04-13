package br.com.hhw.startapp.activities;

import br.com.hhw.startapp.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class SplashStartAppActivity extends ActionBarActivity implements Runnable {

	private static final int SHOW_SPLASH_MILLIS = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_start_app);
		Handler handler = new Handler();
		handler.postDelayed(this, SHOW_SPLASH_MILLIS);
	}

	@Override
	public void run() {
		startActivity(new Intent(SplashStartAppActivity.this,
				MainStartAppActivity.class));
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		finish();
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				startActivity(new Intent(SplashActivity.this,
//						MainActivity.class));
//				finish();
//			}
//		}, 500);
	}
}
