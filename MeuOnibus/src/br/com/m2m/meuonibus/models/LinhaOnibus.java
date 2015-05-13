package br.com.m2m.meuonibus.models;import java.io.Serializable;import com.google.gson.annotations.SerializedName;public class LinhaOnibus implements Serializable {		/**	 * 	 */	private static final long serialVersionUID = 3771311093540072175L;	@SerializedName("patternId")	public int idLinhaSentido;	@SerializedName("patternName")	public String patternName;		@SerializedName("nameLine")	public String nome;		@SerializedName("codVehicle")	public String codVeiculo;		@SerializedName("busServiceNumber")	public String numero;		@SerializedName("busServiceClass")	public String serviceCass;		@SerializedName("pointLocationId")	public int idPontoLinha;		@SerializedName("busServiceId")	public int idLinha;		@SerializedName("latLng")	public Coordenada latLong;		@SerializedName("arrivalTime")	public String tempoChegada;		@SerializedName("destination")	public String destino;		public Coordenada latLongPonto;		public int idPonto;}