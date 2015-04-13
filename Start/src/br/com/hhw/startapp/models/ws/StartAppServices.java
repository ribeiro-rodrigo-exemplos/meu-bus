package br.com.hhw.startapp.models.ws;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.hhw.startapp.handlers.DestaqueModelHandler;
import br.com.hhw.startapp.helpers.ServiceRestClientHelper;
import br.com.hhw.startapp.models.DestaqueModel;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.loopj.android.http.JsonHttpResponseHandler;

public class StartAppServices {

	public static void getDestaques(final DestaqueModelHandler destaqueHandler) {
		final ArrayList<DestaqueModel> destaques = new ArrayList<DestaqueModel>();
		String url = "http://private-6e81e-projetoviajabessa.apiary-mock.com/pacotes";
		ServiceRestClientHelper.get(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray pacotesJsonArray) {
				for (int i = 0; i < pacotesJsonArray.length(); i++) {
					try {
						JSONObject item = (JSONObject) pacotesJsonArray
								.getJSONObject(i);
						JsonParser parser = new JsonParser();
						JsonElement jsonElement = parser.parse(item.toString());
						Gson gson = new Gson();
						DestaqueModel destaque = gson
								.fromJson(jsonElement, DestaqueModel.class);
						destaques.add(destaque);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				destaqueHandler.setDestaques(destaques);

			}
			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					java.lang.String responseString,
					java.lang.Throwable throwable) {
				destaqueHandler.setErro(throwable);
			}
			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					java.lang.Throwable throwable,
					org.json.JSONObject errorResponse) {
				destaqueHandler.setErro(throwable);
			}
			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					java.lang.Throwable throwable,
					org.json.JSONArray errorResponse) {
				destaqueHandler.setErro(throwable);
			}

		});
	}

	public static void getDestaque(int idDestaque, final DestaqueModelHandler destaqueHandler) {
		String url = "http://private-6e81e-projetoviajabessa.apiary-mock.com/pacotes/%s";
		url = String.format(url, idDestaque);
		ServiceRestClientHelper.get(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject pacotesJson) {
				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(pacotesJson.toString());
				Gson gson = new Gson();
				DestaqueModel destaque = gson.fromJson(jsonElement, DestaqueModel.class);
				destaqueHandler.setDestaque(destaque);
			}
			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					java.lang.String responseString,
					java.lang.Throwable throwable) {
				destaqueHandler.setErro(throwable);
			}
			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					java.lang.Throwable throwable,
					org.json.JSONObject errorResponse) {
				destaqueHandler.setErro(throwable);
			}
			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					java.lang.Throwable throwable,
					org.json.JSONArray errorResponse) {
				destaqueHandler.setErro(throwable);
			}
		});
	}
}
