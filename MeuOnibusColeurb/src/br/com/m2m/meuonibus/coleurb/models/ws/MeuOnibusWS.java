package br.com.m2m.meuonibus.coleurb.models.ws;import java.util.ArrayList;import org.apache.http.Header;import org.apache.http.HttpEntity;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import android.content.Context;import android.util.Log;import br.com.hhw.startapp.helpers.ServiceRestClientHelper;import br.com.m2m.meuonibus.coleurb.MeuOnibusApplication;import br.com.m2m.meuonibus.coleurb.util.Util;import br.com.m2m.meuonibuscommons.handlers.CustomJsonHttpResponseHandler;import br.com.m2m.meuonibuscommons.handlers.FaleConoscoHandler;import br.com.m2m.meuonibuscommons.handlers.LinhaOnibusHandler;import br.com.m2m.meuonibuscommons.handlers.NoticiaHandler;import br.com.m2m.meuonibuscommons.handlers.PontoOnibusHandler;import br.com.m2m.meuonibuscommons.handlers.TrajetoHandler;import br.com.m2m.meuonibuscommons.models.ConfiguracaoWS;import br.com.m2m.meuonibuscommons.models.LinhaOnibus;import br.com.m2m.meuonibuscommons.models.NoticiaJsonObj;import br.com.m2m.meuonibuscommons.models.PontoOnibus;import br.com.m2m.meuonibuscommons.models.Trajeto;import com.google.gson.Gson;import com.google.gson.JsonElement;import com.google.gson.JsonParser;import com.loopj.android.http.AsyncHttpResponseHandler;public class MeuOnibusWS {		// Carrega configuracao do WS 	public static ConfiguracaoWS configuracaoWS = Util.carregarConfiguracaoWS(MeuOnibusApplication.getContext());			public final static String AUTH_LOGIN = configuracaoWS.getUsuario();	public final static String AUTH_SENHA = configuracaoWS.getPassword();	public final static String BASE_URL = configuracaoWS.getUrl();	public final static String PONTOS_DE_ONIBUS = configuracaoWS.getPontoOnibus();	public final static String LINHAS_DO_PONTO = configuracaoWS.getLinhasDoPonto();	public final static String TRAJETO_LINHA = configuracaoWS.getTrajetoLinha();	public final static String LINHA_INFORMACAO = configuracaoWS.getInforLinha();	public final static String LISTA_NOTICIAS = configuracaoWS.getListaNoticias();	public final static String ENVIO_MENSAGEM = configuracaoWS.getEnvioMsg();	public static void getPontos(String lat, String lng, String raio,			final PontoOnibusHandler handler) {		final ArrayList<PontoOnibus> pontos = new ArrayList<PontoOnibus>();		String url = String.format(PONTOS_DE_ONIBUS, lat, lng, raio);		url = getURLBase(url);		ServiceRestClientHelper.getAuth(url, null, AUTH_LOGIN, AUTH_SENHA,				new CustomJsonHttpResponseHandler() {					@Override					public void onSuccess(int statusCode, Header[] headers,							JSONArray jsonArray) {						for (int i = 0; i < jsonArray.length(); i++) {							try {								JSONObject item = (JSONObject) jsonArray										.getJSONObject(i);								JsonParser parser = new JsonParser();								JsonElement jsonElement = parser.parse(item										.toString());								Gson gson = new Gson();								PontoOnibus ponto = gson.fromJson(jsonElement,										PontoOnibus.class);								pontos.add(ponto);							} catch (JSONException e) {								// TODO Auto-generated catch block								e.printStackTrace();							}						}						handler.setPontosOnibus(pontos);					}					@Override					public void setErro(Throwable throwable) {						handler.setErro(throwable);					}				});	}		public static void getLinhas(String idPonto,			final LinhaOnibusHandler handler) {		getLinhas(null,idPonto,handler);	}	public static void getLinhas(Context context, String idPonto,			final LinhaOnibusHandler handler) {		final ArrayList<LinhaOnibus> linhas = new ArrayList<LinhaOnibus>();		String url = String.format(LINHAS_DO_PONTO, idPonto);		url = getURLBase(url);//		url = "http://druidtarget1.virtuaserver.com.br:8890/M2MForecastWebMobile/rest/service/lines/load/forecast/lines/fromPoint/%s";//		url = String.format(url, idPonto);//		Log.d("URL nova >>> ", url);		//		url = "http://nosna.net/testes/linhas.json";		//		ServiceRestClientHelper.getAuth(url, null, AUTH_LOGIN, AUTH_SENHA,//				new CustomJsonHttpResponseHandler() {		ServiceRestClientHelper.getAuth(context, url, null, AUTH_LOGIN, AUTH_SENHA,				new CustomJsonHttpResponseHandler() {					@Override					public void onSuccess(int statusCode, Header[] headers,							JSONArray jsonArray) {						for (int i = 0; i < jsonArray.length(); i++) {							try {								JSONObject item = (JSONObject) jsonArray										.getJSONObject(i);								JsonParser parser = new JsonParser();								JsonElement jsonElement = parser.parse(item										.toString());								Gson gson = new Gson();								LinhaOnibus linha = gson.fromJson(jsonElement,										LinhaOnibus.class);								linhas.add(linha);							} catch (JSONException e) {								// TODO Auto-generated catch block								e.printStackTrace();							}						}						handler.setLinhasOnibus(linhas);					}					@Override					public void setErro(Throwable throwable) {						handler.setErro(throwable);					}				});	}	public static void getTrajetos(int idLinha, final TrajetoHandler handler) {		final ArrayList<Trajeto> trajetos = new ArrayList<Trajeto>();		String url = String.format(TRAJETO_LINHA, String.valueOf(idLinha));		url = getURLBase(url);		ServiceRestClientHelper.getAuth(url, null, AUTH_LOGIN, AUTH_SENHA,				new CustomJsonHttpResponseHandler() {					@Override					public void onSuccess(int statusCode, Header[] headers,							JSONArray jsonArray) {						for (int i = 0; i < jsonArray.length(); i++) {							try {								JSONObject item = (JSONObject) jsonArray										.getJSONObject(i);								JsonParser parser = new JsonParser();								JsonElement jsonElement = parser.parse(item										.toString());								Gson gson = new Gson();								Trajeto trajeto = gson.fromJson(jsonElement,										Trajeto.class);								trajetos.add(trajeto);							} catch (JSONException e) {								// TODO Auto-generated catch block								e.printStackTrace();							}						}						handler.setTrajetos(trajetos);					}					@Override					public void setErro(Throwable throwable) {						handler.setErro(throwable);					}				});	}		public static void getLinhaInformacao(final String codVeiculo, int idPonto, int idPatern, final LinhaOnibusHandler handler) {		String url = String.format(LINHA_INFORMACAO, idPonto, idPatern);		url = getURLBase(url);		ServiceRestClientHelper.getAuth(url, null, AUTH_LOGIN, AUTH_SENHA,				new CustomJsonHttpResponseHandler() {					@Override					public void onSuccess(int statusCode, Header[] headers,							JSONArray jsonArray) {						ArrayList<LinhaOnibus> linhas = new ArrayList<LinhaOnibus>();						for (int i = 0; i < jsonArray.length(); i++) {							try {								JSONObject item = (JSONObject) jsonArray										.getJSONObject(i);								JsonParser parser = new JsonParser();								JsonElement jsonElement = parser.parse(item										.toString());								Gson gson = new Gson();								LinhaOnibus linha = gson.fromJson(jsonElement,										LinhaOnibus.class);								if (linha.codVeiculo.equals(codVeiculo)) {									linhas.add(linha);								}									} catch (JSONException e) {								// TODO Auto-generated catch block								e.printStackTrace();							}						}						if (linhas.size() > 0) { 							handler.setLinha(linhas.get(0));						} else {							handler.setLinha(null);						}					}					@Override					public void setErro(Throwable throwable) {						handler.setErro(throwable);					}				});	}	public static void getLinhaInformacaoOld(int idPonto, int idPatern, final LinhaOnibusHandler handler) {//		if (busService == null) {//			busService = "";//		}//		String url = String.format(LINHA_INFORMACAO, idPonto, idPatern,//				busService);		String url = String.format(LINHA_INFORMACAO, idPonto, idPatern);		url = getURLBase(url);		ServiceRestClientHelper.getAuth(url, null, AUTH_LOGIN, AUTH_SENHA,				new CustomJsonHttpResponseHandler() {					@Override					public void onSuccess(int statusCode, Header[] headers,							JSONArray jsonArray) {						ArrayList<LinhaOnibus> linhas = new ArrayList<LinhaOnibus>();						for (int i = 0; i < jsonArray.length(); i++) {							try {								JSONObject item = (JSONObject) jsonArray										.getJSONObject(i);								JsonParser parser = new JsonParser();								JsonElement jsonElement = parser.parse(item										.toString());								Gson gson = new Gson();								LinhaOnibus linha = gson.fromJson(jsonElement,										LinhaOnibus.class);								linhas.add(linha);							} catch (JSONException e) {								// TODO Auto-generated catch block								e.printStackTrace();							}						}						if (linhas.size() > 0) {							Log.d("GET LINHA from array >>> ",									linhas.get(0).nome);							handler.setLinha(linhas.get(0));						}					}					@Override					public void onSuccess(int statusCode, Header[] headers,							JSONObject jsonObj) {						JsonParser parser = new JsonParser();						JsonElement jsonElement = parser.parse(jsonObj								.toString());						Gson gson = new Gson();						LinhaOnibus linha = gson.fromJson(jsonElement,								LinhaOnibus.class);						handler.setLinha(linha);						Log.d("GET LINHA >>> ", linha.nome);					}					@Override					public void setErro(Throwable throwable) {						handler.setErro(throwable);					}				});	}	public static void getNoticias(final NoticiaHandler handler) {		ServiceRestClientHelper.get(LISTA_NOTICIAS, null,				new CustomJsonHttpResponseHandler() {					@Override					public void onSuccess(int statusCode, Header[] headers,							JSONObject jsonObj) {						JsonParser parser = new JsonParser();						JsonElement jsonElement = parser.parse(jsonObj								.toString());						Gson gson = new Gson();						NoticiaJsonObj noticiaObj = gson.fromJson(jsonElement,								NoticiaJsonObj.class);						if (noticiaObj.noticias == null) {							handler.setErro(null);						} else {							handler.setNoticiaObj(noticiaObj);						}					}					@Override					public void setErro(Throwable throwable) {						handler.setErro(throwable);					}				});	}	public static void sendMessage(Context context, HttpEntity entity,			final FaleConoscoHandler handler) {				ServiceRestClientHelper.postAuth(context, getURLBase(ENVIO_MENSAGEM), entity,				"application/xml", AUTH_LOGIN, AUTH_SENHA,				new AsyncHttpResponseHandler() {					@Override					public void onStart() {						// called before request is started					}					@Override					public void onSuccess(int statusCode, Header[] headers,							byte[] response) {						handler.setRetorno(true);					}					@Override					public void onFailure(int statusCode, Header[] headers,							byte[] errorResponse, Throwable e) {						// called when response HTTP status is "4XX" (eg. 401,						// 403, 404)						handler.setErro(e);					}					@Override					public void onRetry(int retryNo) {						// called when request is retried					}				});	}			public static String getURLBase(String url) {		url = String.format("%s/%s", BASE_URL, url);		Log.d("URL REQUEST >>> ", url);		return url;	}}