package br.com.m2m.meuonibuscommons.models;import java.io.Serializable;import com.google.gson.annotations.SerializedName;public class PontoOnibus implements Serializable {		/**	 * 	 */	private static final long serialVersionUID = -2114426639371020091L;	@SerializedName("id")	public int idPonto;	@SerializedName("name")	public String nome;	@SerializedName("endereco")	public String endereco;		@SerializedName("latLng")	public Coordenada latLong;}