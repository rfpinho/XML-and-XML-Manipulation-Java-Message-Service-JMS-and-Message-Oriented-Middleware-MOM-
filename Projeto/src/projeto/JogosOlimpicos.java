package projeto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JogosOlimpicos {
	@XmlElement(name = "pais")
	private List<Pais> paises;
	
	public JogosOlimpicos() {
		paises = new ArrayList<Pais>();
	}
	
	public void adicionarPais(Pais p) {
		paises.add(p);
    }
	
	public String numeroMedalhasGanhasPais(String strPais) {
		int i;
		String string = "";
		for (i = 0; i < paises.size(); i = i + 1){
			if ((paises.get(i).getAbreviatura().toLowerCase()).equals(strPais.toLowerCase()) || (paises.get(i).getNome().toLowerCase()).equals(strPais.toLowerCase())){
				string = "Ouro: " + paises.get(i).getMedalhasOuro() + ", Prata: " + paises.get(i).getMedalhasPrata() + ", Bronze: " + paises.get(i).getMedalhasBronze() + ", Total: " + paises.get(i).getTotalMedalhas() + "\n";
				break;
			}
		}
		if (string.equals("")){
			string = "País não encontrado\n";
		}
		return string;
	}
	
	public String atletasMedalhadosPais(String strPais) {
		int i;
		String string = "";
		for (i = 0; i < paises.size(); i = i + 1){
			if ((paises.get(i).getAbreviatura().toLowerCase()).equals(strPais.toLowerCase()) || (paises.get(i).getNome().toLowerCase()).equals(strPais.toLowerCase())){
				string = paises.get(i).atletasMedalhadosPais();
				break;
			}
		}
		if (string.equals("")){
			string = "País não encontrado\n";
		}
		return string;
	}
	
	public String atletasMedalhadosModalidade(String modalidade, String categoria) {
		int i;
		String string = "", stringAux;
		for (i = 0; i < paises.size(); i = i + 1){
			stringAux = paises.get(i).atletasMedalhadosModalidade(1, modalidade, categoria);
			if (stringAux.equals("") == false){
				string = string + stringAux;
				break;
			}
		}
		for (i = 0; i < paises.size(); i = i + 1){
			stringAux = paises.get(i).atletasMedalhadosModalidade(2, modalidade, categoria);
			if (stringAux.equals("") == false){
				string = string + stringAux;
				break;
			}
		}
		for (i = 0; i < paises.size(); i = i + 1){
			stringAux = paises.get(i).atletasMedalhadosModalidade(3, modalidade, categoria);
			if (stringAux.equals("") == false){
				string = string + stringAux;
				break;
			}
		}
		if (string.equals("")){
			string = "Modalidade não encontrada\n";
		}
		return string;
	}
	
	public String medalhasGanhasAtleta(String atleta) {
		int i;
		String string = "";
		for (i = 0; i < paises.size(); i = i + 1){
			string = paises.get(i).medalhasGanhasAtleta(atleta);
			if (string.equals("") == false){
				break;
			}
		}
		if (string.equals("")){
			string = "Atleta não encontrado\n";
		}
		return string;
	}
	
	public String toString() {
		int i;
		String string = "";
		for (i = 0; i < paises.size(); i = i + 1){
			string = string + paises.get(i).toString();
		}
		return string;
	}
}