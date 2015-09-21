package br.com.m2m.meuonibus.transrequinte.model;import android.content.Context;import br.com.m2m.meuonibus.transrequinte.activities.helpers.AppSharedPreferencesHelper;public class AjustesOpcaoRaioBusca {		public static String KEY_RAIO = "raioPontoConsulta";	public static String VALOR_DEFAULT = "500";	public String nome;	public String valor;	public boolean isCheck;	public AjustesOpcaoRaioBusca(String nome, String valor) {		this.nome = nome;		this.valor = valor;		this.valor = valor;		this.isCheck = false;	}	public AjustesOpcaoRaioBusca(String nome, String valor, boolean isCheck) {		this.nome = nome;		this.valor = valor;		this.valor = valor;		this.isCheck = isCheck;	}		public static String getSavedRaio(Context context) {		return AppSharedPreferencesHelper.getInstance(context).getString(KEY_RAIO, VALOR_DEFAULT);	}		public static void saveRaio(Context context, String raio) {		AppSharedPreferencesHelper.getInstance(context).setString(KEY_RAIO, raio);	}}