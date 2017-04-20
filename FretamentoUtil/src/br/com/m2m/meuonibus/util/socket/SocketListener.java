package br.com.m2m.meuonibus.util.socket;

import org.json.JSONArray;

import android.support.annotation.Nullable;

public interface SocketListener {
	
	/**
	 * @return idTrajeto:idLinha:idVeiculo
	 */
	JSONArray getSubscription();
	void onSync(@Nullable JSONArray response);
	void onConnectError(Object error);
	void onEventError(Object error);
}