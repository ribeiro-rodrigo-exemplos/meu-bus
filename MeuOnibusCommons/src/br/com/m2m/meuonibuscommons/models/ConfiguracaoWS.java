package br.com.m2m.meuonibuscommons.models;

public class ConfiguracaoWS {

	private String usuario;
	private String password;
	private String url;
	private String pontoOnibus;
	private String linhasDoPonto;
	private String linhasDoCliente;
	private String trajetoLinha;
	private String inforLinha;
	private String listaNoticias;
	private String envioMsg;
	private int idCliente;
	private String linhasClienteAgrupamento;
	private String idConsorcio;
	private String pontosDoTrajeto;

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public String getPontoOnibus() {
		return pontoOnibus;
	}

	public void setPontoOnibus(String pontoOnibus) {
		this.pontoOnibus = pontoOnibus;
	}

	public String getLinhasDoPonto() {
		return linhasDoPonto;
	}

	public void setLinhasDoPonto(String linhasDoPonto) {
		this.linhasDoPonto = linhasDoPonto;
	}

	public String getLinhasDoCliente() {
		return linhasDoCliente;
	}

	public void setLinhasDoCliente(String linhasDoCliente) {
		this.linhasDoCliente = linhasDoCliente;
	}

	public String getTrajetoLinha() {
		return trajetoLinha;
	}

	public void setTrajetoLinha(String trajetoLinha) {
		this.trajetoLinha = trajetoLinha;
	}

	public String getInforLinha() {
		return inforLinha;
	}

	public void setInforLinha(String inforLinha) {
		this.inforLinha = inforLinha;
	}

	public String getListaNoticias() {
		return listaNoticias;
	}

	public void setListaNoticias(String listaNoticias) {
		this.listaNoticias = listaNoticias;
	}

	public String getEnvioMsg() {
		return envioMsg;
	}

	public void setEnvioMsg(String envioMsg) {
		this.envioMsg = envioMsg;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLinhasClienteAgrupamento() {
		return linhasClienteAgrupamento;
	}

	public void setLinhasClienteAgrupamento(String linhasClienteAgrupamento) {
		this.linhasClienteAgrupamento = linhasClienteAgrupamento;
	}

	public String getIdConsorcio() {
		return idConsorcio;
	}

	public void setIdConsorcio(String idConsorcio) {
		this.idConsorcio = idConsorcio;
	}

	public String getPontosDoTrajeto() {
		return pontosDoTrajeto;
	}

	public void setPontosDoTrajeto(String pontosDoTrajeto) {
		this.pontosDoTrajeto = pontosDoTrajeto;
	}
}