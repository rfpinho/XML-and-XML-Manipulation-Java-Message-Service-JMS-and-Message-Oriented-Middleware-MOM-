package projeto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Pais {
	@XmlElement
	private int ranking;
	@XmlElement
	private String abreviatura;
	@XmlAttribute
	private String nome;
	@XmlElement
	private int medalhasOuro;
	@XmlElement
	private int medalhasPrata;
	@XmlElement
	private int medalhasBronze;
	@XmlElement
	private int totalMedalhas;
	@XmlElement(name = "ouro")
	private Ouro objOuro;
	@XmlElement(name = "prata")
	private Prata objPrata;
	@XmlElement(name = "bronze")
	private Bronze objBronze;
	
	public Pais() {
		objOuro = new Ouro();
		objPrata = new Prata();
		objBronze = new Bronze();
	}
	
	public Pais(int ranking, String abreviatura, String nome, int medalhasOuro, int medalhasPrata, int medalhasBronze, int totalMedalhas) {
		this.ranking = ranking;
		this.abreviatura = abreviatura;
		this.nome = nome;
		this.medalhasOuro = medalhasOuro;
		this.medalhasPrata = medalhasPrata;
		this.medalhasBronze = medalhasBronze;
		this.totalMedalhas = totalMedalhas;
		objOuro = new Ouro();
		objPrata = new Prata();
		objBronze = new Bronze();
	}
	
	public void adicionarAtletaOuro(Atleta a) {
		this.objOuro.adicionarAtleta(a);
	}
	
	public void adicionarAtletaPrata(Atleta a) {
		this.objPrata.adicionarAtleta(a);
	}
	
	public void adicionarAtletaBronze(Atleta a) {
		this.objBronze.adicionarAtleta(a);
	}
	
	public String getAbreviatura() {
		return abreviatura;
	}
	
	public String getNome() {
		return nome;
	}
	
	public int getMedalhasOuro() {
		return medalhasOuro;
	}
	
	public int getMedalhasPrata() {
		return medalhasPrata;
	}
	
	public int getMedalhasBronze() {
		return medalhasBronze;
	}
	
	public int getTotalMedalhas() {
		return totalMedalhas;
	}
	
	public Ouro getObjOuro() {
		return objOuro;
	}
	
	public Prata getObjPrata() {
		return objPrata;
	}
	
	public Bronze getObjBronze() {
		return objBronze;
	}
	
	public String atletasMedalhadosPais() {
		String string = "";
		string = string + objOuro.toString();
		string = string + objPrata.toString();
		string = string + objBronze.toString();
		return string;
	}
	
	public String atletasMedalhadosModalidade(int medalha, String modalidade, String categoria) {
		String string = "";
		if (medalha == 1){
			string = objOuro.atletasMedalhadosModalidade(modalidade, categoria, nome);
		}
		else if (medalha == 2){
			string = objPrata.atletasMedalhadosModalidade(modalidade, categoria, nome);
		}
		else if (medalha == 3){
			string = objBronze.atletasMedalhadosModalidade(modalidade, categoria, nome);
		}
		return string;
	}
	
	public String medalhasGanhasAtleta(String atleta) {
		String string = "";
		string = string + objOuro.medalhasGanhasAtleta(atleta);
		string = string + objPrata.medalhasGanhasAtleta(atleta);
		string = string + objBronze.medalhasGanhasAtleta(atleta);
		return string;
	}
	
	public String toString() {
		String string = ranking + ". " + abreviatura + "\t" + nome + "\t" + medalhasOuro + "\t" + medalhasPrata + "\t" + medalhasBronze + "\t" + totalMedalhas + "\n";
		string = string + objOuro.toString();
		string = string + objPrata.toString();
		string = string + objBronze.toString();
		return string;
	}
}