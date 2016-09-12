package br.com.m2m.meuonibus.cariri.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import br.com.m2m.meuonibus.cariri.R;
import br.com.m2m.meuonibuscommons.models.ConfiguracaoWS;

public class Util {
	
	
	
	
	public static ConfiguracaoWS carregarConfiguracaoWS(Context context){
		
		ConfiguracaoWS configuracaoWS = new ConfiguracaoWS();
		
		Properties properties = null;
		try{
			InputStream rawResourse = context.getResources().openRawResource(R.raw.webservicesconfig);
            properties = new Properties();
            
            properties.load(rawResourse);
            
            configuracaoWS.setUsuario(properties.getProperty("user"));
            configuracaoWS.setPassword(properties.getProperty("password"));
            configuracaoWS.setUrl(properties.getProperty("base.url"));
            configuracaoWS.setPontoOnibus(properties.getProperty("ponto_de_onibus"));
            configuracaoWS.setLinhasDoPonto(properties.getProperty("linhas_do_ponto"));
            configuracaoWS.setTrajetoLinha(properties.getProperty("trajeto_linha"));
            configuracaoWS.setInforLinha(properties.getProperty("linha_informacao"));
            configuracaoWS.setListaNoticias(properties.getProperty("lista_noticias"));
            configuracaoWS.setEnvioMsg(properties.getProperty("envio_mensagem"));
       
		} catch(IOException e) {
        	e.printStackTrace();
        }
		return configuracaoWS;
	}
	
	
	public static ConfiguracaoWS carregaConfCliente(Context context){
		
		ConfiguracaoWS configuracaoWS = new ConfiguracaoWS();
		
		Properties properties = null;
		try{
			InputStream rawResourse = context.getResources().openRawResource(R.raw.webservicesconfig);
            properties = new Properties();
            
            properties.load(rawResourse);
            
            configuracaoWS.setIdCliente(Integer.valueOf(properties.getProperty("idCliente")));
            
            
		} catch(IOException e) {
        	e.printStackTrace();
        }
		return configuracaoWS;   
		
	}

}
