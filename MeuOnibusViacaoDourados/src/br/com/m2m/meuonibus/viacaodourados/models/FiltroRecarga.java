package br.com.m2m.meuonibus.viacaodourados.models;

import br.com.m2m.meuonibus.viacaodourados.MeuOnibusApplication;
import br.com.m2m.meuonibuscommons.activities.helpers.AppSharedPreferencesHelper;

public class FiltroRecarga {

	public static String KEY_FILTRO_RECARGA = "showRecarga";
	public static String KEY_FILTRO_CREDITO = "showCredito";
	
	public static boolean getFiltroRecarga() {
		return AppSharedPreferencesHelper.getInstance(MeuOnibusApplication.getContext()).getBoolean(KEY_FILTRO_RECARGA);
	}
	
	public static boolean getFiltroCredito() {
		return AppSharedPreferencesHelper.getInstance(MeuOnibusApplication.getContext()).getBoolean(KEY_FILTRO_CREDITO);
	}

	public static void setFiltroRecarga(boolean showRecarga) {
		AppSharedPreferencesHelper.getInstance(MeuOnibusApplication.getContext()).setBoolean(KEY_FILTRO_RECARGA, showRecarga);
	}

	public static void setFiltroCredito(boolean showCredito) {
		AppSharedPreferencesHelper.getInstance(MeuOnibusApplication.getContext()).setBoolean(KEY_FILTRO_CREDITO, showCredito);
	}
}