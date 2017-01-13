package br.com.m2m.meuonibus.assetur.models.ws;import java.util.ArrayList;import org.apache.http.Header;import org.apache.http.HttpEntity;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import android.content.Context;import android.util.Log;import br.com.hhw.startapp.helpers.ServiceRestClientHelper;import br.com.m2m.meuonibus.assetur.MeuOnibusApplication;import br.com.m2m.meuonibus.assetur.util.Util;import br.com.m2m.meuonibuscommons.handlers.CustomJsonHttpResponseHandler;import br.com.m2m.meuonibuscommons.handlers.FaleConoscoHandler;import br.com.m2m.meuonibuscommons.handlers.LinhaOnibusHandler;import br.com.m2m.meuonibuscommons.handlers.NoticiaHandler;import br.com.m2m.meuonibuscommons.handlers.TrajetoHandler;import br.com.m2m.meuonibuscommons.models.ConfiguracaoWS;import br.com.m2m.meuonibuscommons.models.LinhaOnibus;import br.com.m2m.meuonibuscommons.models.NoticiaJsonObj;import br.com.m2m.meuonibuscommons.models.Trajeto;import com.google.gson.Gson;import com.google.gson.JsonElement;import com.google.gson.JsonParser;import com.loopj.android.http.AsyncHttpResponseHandler;public class MeuOnibusWS {		// Carrega configuracoes do WS 	public static ConfiguracaoWS configuracaoWS = Util.carregarConfiguracaoWS(MeuOnibusApplication.getContext());			public final static int ID_CLIENTE = configuracaoWS.getIdCliente();	public final static String AUTH_LOGIN = configuracaoWS.getUsuario();	public final static String AUTH_SENHA = configuracaoWS.getPassword();	public final static String BASE_URL = configuracaoWS.getUrl();	public final static String LINHAS_DO_CLIENTE = configuracaoWS.getLinhasDoCliente();	public final static String TRAJETO_LINHA = configuracaoWS.getTrajetoLinha();	public final static String LINHA_INFORMACAO = configuracaoWS.getInforLinha();	public final static String LISTA_NOTICIAS = configuracaoWS.getListaNoticias();	public final static String ENVIO_MENSAGEM = configuracaoWS.getEnvioMsg();		public static void getLinhas(final LinhaOnibusHandler handler) {		getLinhas(null, handler);	}	public static void getLinhas(Context context, final LinhaOnibusHandler handler) {		final ArrayList<LinhaOnibus> linhas = new ArrayList<LinhaOnibus>();		String url = String.format(LINHAS_DO_CLIENTE, String.valueOf(ID_CLIENTE));		url = getURLBase(url);		ServiceRestClientHelper.get(url, null,				new CustomJsonHttpResponseHandler() {					@Override					public void onSuccess(int statusCode, Header[] headers,							JSONArray jsonArray) {						for (int i = 0; i < jsonArray.length(); i++) {							try {								JSONObject item = (JSONObject) jsonArray										.getJSONObject(i);								JsonParser parser = new JsonParser();								JsonElement jsonElement = parser.parse(item										.toString());								Gson gson = new Gson();								LinhaOnibus linha = gson.fromJson(jsonElement,										LinhaOnibus.class);								linhas.add(linha);							} catch (JSONException e) {								// TODO Auto-generated catch block								e.printStackTrace();							}						}						handler.setLinhasOnibus(linhas);					}					@Override					public void setErro(Throwable throwable) {						handler.setErro(throwable);					}				});	}	public static void getTrajetos(int idLinha, final TrajetoHandler handler) {		final ArrayList<Trajeto> trajetos = new ArrayList<Trajeto>();		String url = String.format(TRAJETO_LINHA, String.valueOf(idLinha), ID_CLIENTE);		url = getURLBase(url);		Log.d("ID LINHA >>>>", String.valueOf(idLinha));		Log.d("URL DA LINHA >>>>", url);		ServiceRestClientHelper.get(url, null,				new CustomJsonHttpResponseHandler() {					@Override					public void onSuccess(int statusCode, Header[] headers,							JSONArray jsonArray) {						for (int i = 0; i < jsonArray.length(); i++) {							try {								JSONObject item = (JSONObject) jsonArray										.getJSONObject(i);								JsonParser parser = new JsonParser();								JsonElement jsonElement = parser.parse(item										.toString());								Gson gson = new Gson();								Trajeto trajeto = gson.fromJson(jsonElement,										Trajeto.class);								trajetos.add(trajeto);							} catch (JSONException e) {								// TODO Auto-generated catch block								e.printStackTrace();							}						}						handler.setTrajetos(trajetos);					}					@Override					public void setErro(Throwable throwable) {						handler.setErro(throwable);					}				});	}		public static void getLinhaInformacao(final String codVeiculo, int idPonto, int idPatern, final LinhaOnibusHandler handler) {		String url = String.format(LINHA_INFORMACAO, idPonto, idPatern);		url = getURLBase(url);		ServiceRestClientHelper.getAuth(url, null, AUTH_LOGIN, AUTH_SENHA,				new CustomJsonHttpResponseHandler() {					@Override					public void onSuccess(int statusCode, Header[] headers,							JSONArray jsonArray) {						ArrayList<LinhaOnibus> linhas = new ArrayList<LinhaOnibus>();						for (int i = 0; i < jsonArray.length(); i++) {							try {								JSONObject item = (JSONObject) jsonArray										.getJSONObject(i);								JsonParser parser = new JsonParser();								JsonElement jsonElement = parser.parse(item										.toString());								Gson gson = new Gson();								LinhaOnibus linha = gson.fromJson(jsonElement,										LinhaOnibus.class);								if (linha.codVeiculo.equals(codVeiculo)) {									linhas.add(linha);								}									} catch (JSONException e) {								// TODO Auto-generated catch block								e.printStackTrace();							}						}						if (linhas.size() > 0) { 							handler.setLinha(linhas.get(0));						} else {							handler.setLinha(null);						}					}					@Override					public void setErro(Throwable throwable) {						handler.setErro(throwable);					}				});	}	public static void getNoticias(final NoticiaHandler handler) {		ServiceRestClientHelper.get(LISTA_NOTICIAS, null,				new CustomJsonHttpResponseHandler() {					@Override					public void onSuccess(int statusCode, Header[] headers,							JSONObject jsonObj) {						JsonParser parser = new JsonParser();						JsonElement jsonElement = parser.parse(jsonObj								.toString());						Gson gson = new Gson();						NoticiaJsonObj noticiaObj = gson.fromJson(jsonElement,								NoticiaJsonObj.class);						if (noticiaObj.noticias == null) {							handler.setErro(null);						} else {							handler.setNoticiaObj(noticiaObj);						}					}					@Override					public void setErro(Throwable throwable) {						handler.setErro(throwable);					}				});	}	public static void sendMessage(Context context, HttpEntity entity,			final FaleConoscoHandler handler) {				ServiceRestClientHelper.postAuth(context, getURLBase(ENVIO_MENSAGEM), entity,				"application/xml", AUTH_LOGIN, AUTH_SENHA,				new AsyncHttpResponseHandler() {					@Override					public void onStart() {						// called before request is started					}					@Override					public void onSuccess(int statusCode, Header[] headers,							byte[] response) {						handler.setRetorno(true);					}					@Override					public void onFailure(int statusCode, Header[] headers,							byte[] errorResponse, Throwable e) {						// called when response HTTP status is "4XX" (eg. 401,						// 403, 404)						handler.setErro(e);					}					@Override					public void onRetry(int retryNo) {						// called when request is retried					}				});	}			public static String getURLBase(String url) {		url = String.format("%s/%s", BASE_URL, url);		Log.d("URL REQUEST >>> ", url);		return url;	}}