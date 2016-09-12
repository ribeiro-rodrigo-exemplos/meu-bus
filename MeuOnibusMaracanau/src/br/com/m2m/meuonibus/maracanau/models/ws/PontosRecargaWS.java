package br.com.m2m.meuonibus.maracanau.models.ws;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.os.StrictMode;
import br.com.m2m.meuonibus.maracanau.MeuOnibusApplication;
import br.com.m2m.meuonibus.maracanau.R;
import br.com.m2m.meuonibus.maracanau.fragments.PontosRecargaFragment;
import br.com.m2m.meuonibus.maracanau.handlers.PontoRecargaHandler;
import br.com.m2m.meuonibus.maracanau.models.FiltroRecarga;
import br.com.m2m.meuonibus.maracanau.models.PontoRecarga;
import br.com.m2m.meuonibuscommons.models.Coordenada;

public class PontosRecargaWS {
	
	public static ArrayList<PontoRecarga> pontos = new ArrayList<PontoRecarga>();
	static PontoRecarga ponto = new PontoRecarga();
	
	public static void getPontos(PontosRecargaFragment fragment, String lat, String lng, String raio,
			final PontoRecargaHandler handler) throws IOException {
		
		pontos.clear();

		InputStream rawResourse = MeuOnibusApplication.getContext().getResources()
				.openRawResource(R.raw.webservicesconfig);
		
		Properties properties = new Properties();
        properties.load(rawResourse);
        
        String base_url = properties.getProperty("pontos_recarga");
        String usu = properties.getProperty("recarga_usuario");
        String pwd = properties.getProperty("recarga_senha");
		
        String url = 
        		String.format("%1$s?p_latitude=%2$s&p_longitude=%3$s&p_dist_metros=%4$s&usu=%5$s&pwd=%6$s", 
				base_url, lat, lng, raio, usu, pwd);
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler default_handler = new DefaultHandler() {

			boolean id_tipo   = false;
			boolean nm_loja   = false;
			boolean end_rua   = false;
			boolean end_num   = false;
			boolean latitude  = false;
			boolean longitude = false;
			
			private StringBuffer id_tipo_value   = new StringBuffer(1024);
			private StringBuffer nm_loja_value   = new StringBuffer(1024);
			private StringBuffer end_rua_value   = new StringBuffer(1024);
			private StringBuffer end_num_value   = new StringBuffer(1024);
			private StringBuffer latitude_value  = new StringBuffer(1024);
			private StringBuffer longitude_value = new StringBuffer(1024);

			public void startElement(String uri, String localName,String qName, 
		                Attributes atts) throws SAXException {

				if (qName.equalsIgnoreCase("registro")) {
					ponto = new PontoRecarga();
				}
				
				if (qName.equals("id_tipo")) {
					id_tipo = true;
					id_tipo_value.setLength(0);
				}
				
				if (qName.equals("nm_loja")) {
					nm_loja = true;
					nm_loja_value.setLength(0);
				}

				if (qName.equals("end_rua")) {
					end_rua = true;
					end_rua_value.setLength(0);
				}

				if (qName.equalsIgnoreCase("end_num")) {
					end_num = true;
					end_num_value.setLength(0);
				}

				if (qName.equalsIgnoreCase("latitude")) {
					latitude = true;
					latitude_value.setLength(0);
				}

				if (qName.equalsIgnoreCase("longitude")) {
					longitude = true;
					longitude_value.setLength(0);
				}

			}

			public void endElement(String uri, String localName,
				String qName) throws SAXException {

				if (qName.equalsIgnoreCase("registro")) {
					try {
						Coordenada coord = new Coordenada();
						coord.latitude = Double.parseDouble(latitude_value.toString());
						coord.longitude = Double.parseDouble(longitude_value.toString());
						
						ponto.latLong = coord;
						if(
							(id_tipo_value.toString().equals("001") && FiltroRecarga.getFiltroCredito()) ||
							(id_tipo_value.toString().equals("002") && FiltroRecarga.getFiltroRecarga())
						) {
							pontos.add(ponto);
						}
					} catch (NumberFormatException e) {
						
					}
				}
				
				if (id_tipo) {
					id_tipo = false;
					String nome_value = new String();

					if(id_tipo_value.toString().equals("001")) {
						nome_value = MeuOnibusApplication.getContext().getString(R.string.venda_credito);
					} else if (id_tipo_value.toString().equals("002")) {
						nome_value = MeuOnibusApplication.getContext().getString(R.string.recarga);
					}

					ponto.nome = nome_value;
				}
				
				if (nm_loja) {
					nm_loja = false;
					ponto.loja = nm_loja_value.toString();
				}
			
				if (end_rua) {
					end_rua = false;
					ponto.endereco = end_rua_value.toString();
				}
				
				if (end_num) {
					end_num = false;
					ponto.endereco = ponto.endereco.concat(", ").concat(end_num_value.toString());
				}
			}

			public void characters(char ch[], int start, int length) throws SAXException {
				if (id_tipo) {
					id_tipo_value.append(ch, start, length);
				}
				
				if (nm_loja) {
					nm_loja_value.append(ch, start, length);
				}
				
				if (end_rua) {
					end_rua_value.append(ch, start, length);
				}

				if (end_num) {
					end_num_value.append(ch, start, length);
				}

				if (latitude) {
					if(latitude_value.length() == 0) {
						latitude_value.append(ch, start, length);
					}
				}

				if (longitude) {
					if(longitude_value.length() == 0) {
						longitude_value.append(ch, start, length);
					}
				}

			}

		    };
		    
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);

		    saxParser.parse(new InputSource(new URL(url).openStream()), default_handler);
		    
		    handler.setPontosRecarga(pontos);
	     } catch (Exception e) {
	       e.printStackTrace();
	     }
	}
}
