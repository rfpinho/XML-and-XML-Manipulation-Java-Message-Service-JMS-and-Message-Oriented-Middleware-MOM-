package projeto;

import java.io.IOException;
import java.util.Scanner;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MedalRequester {
	private ConnectionFactory cf = null;
	private Destination d = null;
	private final String login = "is";
	private final String password = "is";
	
	public MedalRequester() {
		try{
			this.cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
			this.d = InitialContext.doLookup("jms/queue/Queue");
		} catch (NamingException e){
			System.out.println("NamingException: " + e.getMessage());
		}
	}
	
	private String send(String text) {
		String response = null;
		try{
			JMSContext jcontext = cf.createContext(login, password);
			JMSProducer mp = jcontext.createProducer();
            TemporaryQueue tempQueue = jcontext.createTemporaryQueue();
            TextMessage request = jcontext.createTextMessage();
			request.setText(text);
			request.setJMSReplyTo(tempQueue);
			mp.send(d, request);
			System.out.println("[MedalRequester] Pedido enviado para o MedalKeeper");
			JMSConsumer mc = jcontext.createConsumer(tempQueue);
			response = mc.receiveBody(String.class);
			System.out.println("[MedalRequester] Resposta recebida do MedalKeeper");
        } catch (JMSRuntimeException re){
        	System.out.println("JMSRuntimeException: " + re.getMessage());
        } catch (JMSException e){
        	System.out.println("JMSException: " + e.getMessage());
        } catch (NullPointerException pe){
            System.out.println("NullPointerException: " + pe.getMessage());
        }
		return response;
	}
	
	private void menu() {
    	int opcao;
    	String opcaoStr, pais, modalidade, categoria, atleta;
        Scanner sc = new Scanner(System.in);
        while (true){
        	System.out.println("--------------------------------------------------");
        	System.out.println("1 - Listar o número de medalhas ganhas por um país");
        	System.out.println("2 - Listar todos os atletas medalhados de um país");
        	System.out.println("3 - Listar os atletas medalhados de uma modalidade");
        	System.out.println("4 - Listar as medalhas ganhas por um atleta");
        	System.out.println("0 - Para sair");
        	System.out.println("--------------------------------------------------");
        	do{
        		System.out.print("Opção: ");
        		opcaoStr = sc.nextLine();
        	} while (validarInteiro(opcaoStr) != true);
        	opcao = Integer.parseInt(opcaoStr); System.out.println();
        	if (opcao == 1){
                do{
                    System.out.print("País: ");
                    pais = sc.nextLine();
                } while (validarString(pais) != true);
                System.out.println("\n" + send("1#" + pais));
        	}
        	else if (opcao == 2){
                do{
                    System.out.print("País: ");
                    pais = sc.nextLine();
                } while (validarString(pais) != true);
                System.out.println("\n" + send("2#" + pais));
        	}
        	else if (opcao == 3){
                do{
                    System.out.print("Modalidade: ");
                    modalidade = sc.nextLine();
                    System.out.print("Categoria: ");
                    categoria = sc.nextLine();
                } while (validarString(modalidade) != true && validarString(categoria) != true);
                System.out.println("\n" + send("3#" + modalidade + "#" + categoria));
        	}
        	else if (opcao == 4){
                do{
                    System.out.print("Atleta: ");
                    atleta = sc.nextLine();
                } while (validarString(atleta) != true);
                System.out.println("\n" + send("4#" + atleta));
        	}
        	else if (opcao == 0){
        		sc.close();
        		System.exit(0);
        	}
        	else{
        		System.err.println("Número inválido\n");
        	}
			try{
				System.out.println("\n_________________________________________\nPressione enter para continuar...");
				System.in.read();
				sc.nextLine();
			} catch (IOException e){
				System.out.println("IOException: " + e.getMessage());
			}
        }
	}
	
    private boolean validarInteiro(String string) {
        try{
            Integer.parseInt(string);
            return true;
        } catch (Exception e){
            System.err.println("Número inválido");
            return false;
        }
    }
    
    private boolean validarString(String string) {
        if (string.trim().equals("")){
            System.err.println("String inválida");
            return false;
        }
        return true;
    }
	
	
    public static void main(String[] args) {
    	MedalRequester mr = new MedalRequester();
    	mr.menu();
    }
}