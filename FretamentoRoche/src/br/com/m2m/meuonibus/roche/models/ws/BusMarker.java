package br.com.m2m.meuonibus.roche.models.ws;

import com.google.android.gms.maps.model.Marker;

public class BusMarker {
	
	private String idVeiculo;
	private String title;
	private double latitude;
	private double longitude;
	private Marker marker;
	
	public BusMarker(double latitude, double longitude, String idVeiculo, String title) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.idVeiculo = idVeiculo;
		this.title = title;
	}
	
	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public String getIdVeiculo() {
		return idVeiculo;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public Marker getMarker() {
		return marker;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}