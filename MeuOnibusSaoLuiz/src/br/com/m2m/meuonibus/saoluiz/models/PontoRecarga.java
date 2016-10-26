package br.com.m2m.meuonibus.saoluiz.models;

import java.io.Serializable;
import br.com.m2m.meuonibuscommons.models.Coordenada;
import com.google.gson.annotations.SerializedName;

public class PontoRecarga implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1403523499899114582L;

	@SerializedName("id")
	public int idPonto;

	@SerializedName("name")
	public String nome;
	
	@SerializedName("loja")
	public String loja;

	@SerializedName("endereco")
	public String endereco;
	
	@SerializedName("latLng")
	public Coordenada latLong;
}
