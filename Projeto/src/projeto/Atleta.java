package projeto;

import javax.xml.bind.annotation.XmlElement;

public class Atleta {
	@XmlElement
	private String modalidade;
	@XmlElement
	private String categoria;
	@XmlElement
	private String nome;
	
	public Atleta() {
		
	}
	
	public Atleta(String modalidade, String categoria, String nome) {
		this.modalidade = modalidade;
		this.categoria = categoria;
		this.nome = nome;
	}
	
	public String getModalidade() {
		return modalidade;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String toString() {
		return "\t" + modalidade + "\t" + categoria + "\t" + nome + "\n";
	}
}