package projeto;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {
	private ConnectionFactory cf = null;
	private Topic t = null;
	private final String login = "is";
	private final String password = "is";
	
	public WebCrawler() {
		
	}
	
	private void htmlParser() {
		String tipoMedalha = null, strXml;
		JogosOlimpicos jo = new JogosOlimpicos();
		Pais p = null;
		Atleta a = null;
		try{
			Document doc = Jsoup.connect("https://www.rio2016.com/en/medal-count-country").get();
			Elements table = doc.select("div[class=table-count]");
			for (Element row : table.select("tr")){
				Elements columns = row.select("td");
				if (row.select("td").size() == 7){
					p = new Pais(Integer.parseInt(columns.get(0).text()), columns.get(1).text(), columns.get(2).text(),
							Integer.parseInt(columns.get(3).text()), Integer.parseInt(columns.get(4).text()),
							Integer.parseInt(columns.get(5).text()), Integer.parseInt(columns.get(6).text()));
					jo.adicionarPais(p);
				}
				else if (row.select("td").size() == 4){
					if (columns.get(0).text().isEmpty() == false){
						tipoMedalha = columns.get(0).text();
					}
					a = new Atleta(columns.get(1).text(), columns.get(2).text(), columns.get(3).text());
					if (tipoMedalha.equals("Gold")){
						p.adicionarAtletaOuro(a);
					}
					else if (tipoMedalha.equals("Silver")){
						p.adicionarAtletaPrata(a);
					}
					else if (tipoMedalha.equals("Bronze")){
						p.adicionarAtletaBronze(a);
					}
				}
			}
		} catch (IOException e){
			System.out.println("IOException: " + e.getMessage());
		}
		System.out.println("[WebCrawler] Informação sobre as medalhas recolhida do web site dos jogos olímpicos");
		strXml = marshall(jo);
		sendTopic(strXml);
	}
	
	private String marshall(JogosOlimpicos jo) {
		StringWriter sw = new StringWriter();
		try{
            File file = new File("QuadroMedalhas.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(JogosOlimpicos.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(jo, sw);
            jaxbMarshaller.marshal(jo, file);
            System.out.println("[WebCrawler] Marshall executado, objeto convertido numa string xml e num ficheiro .xml");
        } catch (JAXBException e){
        	System.out.println("JAXBException: " + e.getMessage());
        }
		return sw.toString();
	}
	
	private void sendTopic(String strXml) {
		int segundos = 10;
		while (true){
			try{
				cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
				t = InitialContext.doLookup("jms/topic/Topic");
				JMSContext jcontext = cf.createContext(login, password);
				JMSProducer mp = jcontext.createProducer();
				mp.send(t, strXml);
				jcontext.close();
				break;
			} catch (NamingException | JMSRuntimeException | NullPointerException e){
				System.out.println("NamingException | JMSRuntimeException | NullPointerException: " + e.getMessage());
				try{
					System.out.println("[WebCrawler] A tentar novamente enviar o XML para o tópico dentro de " + segundos + "s");
					Thread.sleep(segundos * 1000);
					segundos = segundos + 10;
				} catch (InterruptedException ie){
					System.out.println("InterruptedException: " + ie.getMessage());
				}
			}
		}
		System.out.println("[WebCrawler] Xml enviado para o tópico");
	}
	
	
	public static void main(String[] args) {
		WebCrawler wc = new WebCrawler();
		wc.htmlParser();
	}
}