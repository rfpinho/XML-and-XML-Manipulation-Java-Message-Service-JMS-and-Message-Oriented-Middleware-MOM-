package projeto;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

public class MedalKeeper {
	private ConnectionFactory cf = null;
	private Destination d = null;
	private final String login = "is";
	private final String password = "is";
	private JogosOlimpicos jo = new JogosOlimpicos();
	
	public MedalKeeper() {
		try {
			cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
			d = InitialContext.doLookup("jms/queue/Queue");
		} catch (NamingException e) {
			System.out.println("NamingException: " + e.getMessage());
		}
	}
	
	private void receive() {
		int option;
		String string = null;
		String[] arrayStrings = new String[3];
		try{
			JMSContext jcontex = cf.createContext(login, password);
			JMSConsumer mc = jcontex.createConsumer(d);
			while (true){
				TextMessage msg = (TextMessage) mc.receive();
				System.out.println("[MedalKeeper] Pedido recebido do MedalRequester");
                arrayStrings = msg.getText().split("#");
                option = Integer.parseInt(arrayStrings[0]);
                synchronized (jo){
	                if (option == 1){
	                	string = jo.numeroMedalhasGanhasPais(arrayStrings[1]);
	                }
	                else if (option == 2){
	                	string = jo.atletasMedalhadosPais(arrayStrings[1]);
	                }
	                else if (option == 3){
	                	string = jo.atletasMedalhadosModalidade(arrayStrings[1], arrayStrings[2]);
	                }
	                else if (option == 4){
	                	string = jo.medalhasGanhasAtleta(arrayStrings[1]);
	                }
                }
				JMSProducer mp = jcontex.createProducer();
				mp.send(msg.getJMSReplyTo(), string);
				System.out.println("[MedalKeeper] Resposta enviada para a fila temporária");
			}
		} catch (JMSRuntimeException re){
			System.out.println("JMSRuntimeException: " + re.getMessage());
		} catch (JMSException e){
			System.out.println("JMSException: " + e.getMessage());
        } catch (NullPointerException pe){
            System.out.println("NullPointerException: " + pe.getMessage());
        }
	}
	
	public boolean validateXMLSchema(String xsdPath, String strXml) {
        try{
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(strXml)));
        } catch (IOException e){
            System.out.println("Exception: " + e.getMessage());
            return false;
        } catch (SAXException e){
            System.out.println("SAXException: " + e.getMessage());
            return false;
        }
        return true;
    }
    
	public void unmarshall(String strXml) {
		try{
			JAXBContext jaxbContext = JAXBContext.newInstance(JogosOlimpicos.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			synchronized (jo){
				jo = (JogosOlimpicos) jaxbUnmarshaller.unmarshal(new StringReader(strXml));
			}
			System.out.println("[MedalKeeper] Unmarshall executado, string xml convertida num objeto");
		} catch (JAXBException e){
			System.out.println("JAXBException: " + e.getMessage());
		}
    }
    
	
	public static void main(String[] args) {
		MedalKeeper mk = new MedalKeeper();
		new TopicReader(mk);
		mk.receive();
	}
}

class TopicReader extends Thread implements MessageListener {
	private ConnectionFactory cf = null;
	private Topic t = null;
	private final String login = "is";
	private final String password = "is";
	private MedalKeeper mk = null;
	
	public TopicReader(MedalKeeper mk) {
		this.mk = mk;
		try{
			cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
			t = InitialContext.doLookup("jms/topic/Topic");
			this.start();
		} catch (NamingException e){
			System.out.println("NamingException: " + e.getMessage());
		}
	}
	
	public void run() {
		try{
			JMSContext jcontext = cf.createContext(login, password);
			jcontext.setClientID("2");
			JMSConsumer consumer = jcontext.createDurableConsumer(t, "MedalKeeper");
			consumer.setMessageListener(this);
			System.out.println("[MedalKeeper] Pressione enter para terminar...");
			System.in.read();
			jcontext.close();
		} catch (JMSRuntimeException re){
			System.out.println("JMSRuntimeException: " + re.getMessage());
		} catch (IOException e){
			System.out.println("IOException: " + e.getMessage());
		} catch (NullPointerException pe){
            System.out.println("NullPointerException: " + pe.getMessage());
        }
		System.exit(0);
	}
	
	@Override
	public void onMessage(Message msg) {
		try{
			TextMessage tmsg = (TextMessage) msg;
			String strXml = tmsg.getText();
			System.out.println("[MedalKeeper] Xml recebido do WebCrawler");
	    	boolean isValid = mk.validateXMLSchema(System.getProperty("user.dir") + "\\QuadroMedalhas.xsd", strXml);
	    	if (isValid){
	    		System.out.println("[MedalKeeper] Xml validado contra o esquema xsd");
	    		mk.unmarshall(strXml);
	    	}
	    	else{
	    		System.out.println("[MedalKeeper] Erro na validação do xml contra o esquema xsd");
	    	}
		} catch (JMSException e){
			System.out.println("JMSException: " + e.getMessage());
		}
	}
}