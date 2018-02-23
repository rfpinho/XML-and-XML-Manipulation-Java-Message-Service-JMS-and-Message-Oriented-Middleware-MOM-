package projeto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class Bronze {
	@XmlElement(name = "atleta")
	private List<Atleta> atletas;
	
	public Bronze() {
		this.atletas = new ArrayList<Atleta>();
	}
	
	public void adicionarAtleta(Atleta a) {
		atletas.add(a);
	}
	
	public String atletasMedalhadosModalidade(String modalidade, String categoria, String nomePais) {
		int i;
		String string = "";
		for (i = 0; i < atletas.size(); i = i + 1){
			if ((atletas.get(i).getModalidade().toLowerCase()).equals(modalidade.toLowerCase()) && (atletas.get(i).getCategoria().toLowerCase()).equals(categoria.toLowerCase())){
				return "Bronze: " + atletas.get(i).getNome() + ", " + nomePais + "\n";
			}
		}
		return string;
	}
	
	public String medalhasGanhasAtleta(String atleta) {
		int i;
		String string = "";
		String[] atletaPartido = atleta.split(" ");
		if (atletaPartido.length == 2){
			for (i = 0; i < atletas.size(); i = i + 1){
				if ((atletas.get(i).getNome().toLowerCase()).equals(atleta.toLowerCase()) || (atletas.get(i).getNome().toLowerCase()).equals((atletaPartido[1] + " " + atletaPartido[0]).toLowerCase())){
					string = string + "Bronze: " + atletas.get(i).toString().trim() + "\n";
				}
			}
		}
		return string;
	}
	
	public String toString() {
		int i;
		String string = "    Bronze:\n";
		for (i = 0; i < atletas.size(); i = i + 1){
			string = string + atletas.get(i).toString();
		}
		return string;
	}
}