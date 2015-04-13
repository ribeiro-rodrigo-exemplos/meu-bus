package br.com.hhw.startapp.helpers;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ServiceRestClientHelper {
	public static AsyncHttpClient client = new AsyncHttpClient();

	public static void getAuth(String url, RequestParams params, String login,
			String senha, AsyncHttpResponseHandler responseHandler) {
		client.setBasicAuth(login, senha);
		client.get(url, params, responseHandler);
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}

	public static void postAuth(String url, RequestParams params, String login,
			String senha, AsyncHttpResponseHandler responseHandler) {
		client.setBasicAuth(login, senha);
		client.post(url, params, responseHandler);
	}
	
	public static void postAuth(Context context, String url, HttpEntity entity, String contentType, String login,
			String senha, AsyncHttpResponseHandler responseHandler) {
		client.setBasicAuth(login, senha);
		client.post(context, url, entity, contentType, responseHandler);
	}

	public static void cancelRequests(Context context) {
		client.cancelRequests(context, true);
	}
}
