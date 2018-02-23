package projeto;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

public class HTMLSummaryCreator implements MessageListener {
	private ConnectionFactory cf = null;
	private Topic t = null;
	private final String login = "is";
	private final String password = "is";
	private AtomicInteger sequence = new AtomicInteger(1);
	
	public HTMLSummaryCreator() {
		try{
			cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
			t = InitialContext.doLookup("jms/topic/Topic");
		} catch (NamingException e){
			System.out.println("NamingException: " + e.getMessage());
		}
	}
	
	private void launchAndWait() {
		try{
			JMSContext jcontext = cf.createContext(login, password);
			jcontext.setClientID("1");
			JMSConsumer consumer = jcontext.createDurableConsumer(t, "HTMLSummaryCreator");
			consumer.setMessageListener(this);
			System.out.println("[HTMLSummaryCreator] Pressione enter para terminar...");
			System.in.read();
			jcontext.close();
		} catch (JMSRuntimeException re){
			System.out.println("JMSRuntimeException: " + re.getMessage());
		} catch (IOException e){
			System.out.println("IOException: " + e.getMessage());
		} catch (NullPointerException pe){
            System.out.println("NullPointerException: " + pe.getMessage());
        }
	}
	
	@Override
	public void onMessage(Message msg) {
		try{
			TextMessage tmsg = (TextMessage) msg;
			String strXml = tmsg.getText();
			System.out.println("[HTMLSummaryCreator] Xml recebido do WebCrawler");
	    	boolean isValid = validateXMLSchema(System.getProperty("user.dir") + "\\QuadroMedalhas.xsd", strXml);
	    	if (isValid){
	    		System.out.println("[HTMLSummaryCreator] Xml validado contra o esquema xsd");
	    		generateHTML(strXml);
	    	}
	    	else{
	    		System.out.println("[HTMLSummaryCreator] Erro na validação do xml contra o esquema xsd");
	    	}
		} catch (JMSException e){
			e.printStackTrace();
		}
	}
    
    private boolean validateXMLSchema(String xsdPath, String strXml) {
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
    
    private void generateHTML(String strXml) {
        StreamSource xslStreamSource = new StreamSource(Paths.get(System.getProperty("user.dir") + "\\QuadroMedalhas.xsl").toAbsolutePath().toFile());
        StreamSource xmlStreamSource = new StreamSource(new StringReader(strXml));
        TransformerFactory transformerFactory = TransformerFactory.newInstance("org.apache.xalan.processor.TransformerFactoryImpl", null);
        Path pathToHtmlFile = Paths.get("QuadroMedalhas_" + sequence.getAndIncrement() + ".html");
        StreamResult result = new StreamResult(pathToHtmlFile.toFile());
        try{
            Transformer transformer = transformerFactory.newTransformer(xslStreamSource);
            transformer.transform(xmlStreamSource, result);
            System.out.println("[HTMLSummaryCreator] Ficheiro HTML nº " + (sequence.get() - 1) + " gerado");
        } catch (TransformerConfigurationException e){
            System.out.println("TransformerConfigurationException: " + e.getMessage());
        } catch (TransformerException e){
            System.out.println("TransformerException: " + e.getMessage());
        }
    }
    
    
    public static void main(String[] args) {
    	HTMLSummaryCreator hsc = new HTMLSummaryCreator();
    	hsc.launchAndWait();
    }
}