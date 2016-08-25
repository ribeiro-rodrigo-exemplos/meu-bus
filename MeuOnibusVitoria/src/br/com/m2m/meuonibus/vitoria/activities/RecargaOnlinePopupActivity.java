package br.com.m2m.meuonibus.vitoria.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import br.com.m2m.meuonibus.vitoria.MeuOnibusApplication;
import br.com.m2m.meuonibus.vitoria.R;

public class RecargaOnlinePopupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recarga_online_popup);
		
		Button cancelBtn = (Button) findViewById(R.id.button1);
		Button okBtn = (Button) findViewById(R.id.button2);
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent action = new Intent(MeuOnibusApplication.getContext(), RecargaOnlineActivity.class);
				RecargaOnlinePopupActivity.this.startActivity(action);
				finish();
			}
		}); 
	}
}
