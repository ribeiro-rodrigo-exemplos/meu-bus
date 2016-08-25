package br.com.m2m.meuonibus.maracanau.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;
import br.com.m2m.meuonibus.maracanau.R;
import br.com.m2m.meuonibus.maracanau.models.FiltroRecarga;
import br.com.m2m.meuonibuscommons.activities.base.BaseActivity;

public class FiltroRecargaActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_filtro_recarga);
		setCustomActionBar();
		
		final ToggleButton toggleCreditoBtn = (ToggleButton) findViewById(R.id.toggle_credito);
		final ToggleButton toggleRecargaBtn = (ToggleButton) findViewById(R.id.toggle_recarga);
		final ToggleButton toggleAllBtn = (ToggleButton) findViewById(R.id.toggle_all);
		
		toggleCreditoBtn.setChecked(FiltroRecarga.getFiltroCredito());
		toggleRecargaBtn.setChecked(FiltroRecarga.getFiltroRecarga());
		toggleAllBtn.setChecked(FiltroRecarga.getFiltroCredito() && FiltroRecarga.getFiltroRecarga());

		toggleCreditoBtn.setOnClickListener(new OnClickListener()
		{
		    @Override
		    public void onClick(View v)
		    {
		    	if(toggleCreditoBtn.isChecked())
		    		FiltroRecarga.setFiltroCredito(true);
	            else 
	            	FiltroRecarga.setFiltroCredito(false);
		    	
		    	toggleAllBtn.setChecked(FiltroRecarga.getFiltroCredito() && FiltroRecarga.getFiltroRecarga());
		    }
		});
		
		toggleRecargaBtn.setOnClickListener(new OnClickListener()
		{
		    @Override
		    public void onClick(View v)
		    {
		    	if(toggleRecargaBtn.isChecked())
		    		FiltroRecarga.setFiltroRecarga(true);
	            else 
	            	FiltroRecarga.setFiltroRecarga(false);
		    	
		    	toggleAllBtn.setChecked(FiltroRecarga.getFiltroCredito() && FiltroRecarga.getFiltroRecarga());
		    }
		});
		
		toggleAllBtn.setOnClickListener(new OnClickListener()
		{
		    @Override
		    public void onClick(View v)
		    {
		    	boolean isOn = toggleAllBtn.isChecked();
		    	
		    	FiltroRecarga.setFiltroCredito(isOn);
		    	FiltroRecarga.setFiltroRecarga(isOn);
		    	
		    	toggleCreditoBtn.setChecked(isOn);
				toggleRecargaBtn.setChecked(isOn);
		    }
		});
		
		Button cancelBtn = (Button) findViewById(R.id.button1);
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void setCustomActionBar() {
		ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_custom_with_logo);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5c9b91")));
        actionBar.setDisplayHomeAsUpEnabled(false);
	}
}
