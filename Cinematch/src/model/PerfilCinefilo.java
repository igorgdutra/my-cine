package model;

import exception.DuracaoInvalidaException;
import exception.PesoInvalidoException;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import java.util.*;

public class PerfilCinefilo {

	private final Map<Genero, Double> pesosGenero = new HashMap<>();
	private int duracaoMinimaMin = 60;
	private int duracaoMaximaMin = 180;
	private ClassificacaoEtaria classificacaoMaxima = ClassificacaoEtaria.DEZOITO;
	private final List<Idioma> idiomasAceitos = new ArrayList<>();
	private final List<String> historico = new ArrayList<>();
	private final Map<String, Integer> notas = new HashMap<>();

	public void setPesoGenero(Genero genero, double peso) {
		if (peso < 0.0 || peso > 1.0) {
			throw new PesoInvalidoException("Peso deve estar entre 0.0 e 1.0, mas foi: " + peso);
		}
		pesosGenero.put(genero, peso);
	}

	public double getPesoGenero(Genero genero) {
		return pesosGenero.getOrDefault(genero, 0.5);
	}

	public Map<Genero, Double> getPesosGenero() {
		return Collections.unmodifiableMap(pesosGenero);
	}

	public void setFaixaDuracao(int min, int max) {
		if (min > max) {
			throw new DuracaoInvalidaException(
					"Duração mínima (" + min + "min) não pode ser maior que a máxima (" + max + "min)");
		}
		this.duracaoMinimaMin = min;
		this.duracaoMaximaMin = max;
	}

	public int getDuracaoMinimaMin() {
		return duracaoMinimaMin;
	}

	public int getDuracaoMaximaMin() {
		return duracaoMaximaMin;
	}

	public void setClassificacaoMaxima(ClassificacaoEtaria classificacao) {
		this.classificacaoMaxima = classificacao;
	}

	public ClassificacaoEtaria getClassificacaoMaxima() {
		return classificacaoMaxima;
	}

	public void adicionarIdioma(Idioma idioma) {
		if (!idiomasAceitos.contains(idioma)) {
			idiomasAceitos.add(idioma);
		}
	}

	public List<Idioma> getIdiomasAceitos() {
		return Collections.unmodifiableList(idiomasAceitos);
	}

	public void marcarAssistido(String idFilme) {
		if (!historico.contains(idFilme)) {
			historico.add(idFilme);
		}
	}

	public boolean jaAssistiu(String idFilme) {
		return historico.contains(idFilme);
	}

	public List<String> getHistorico() {
		return Collections.unmodifiableList(historico);
	}

	public void adicionarNota(String idFilme, int nota) {
		if (nota < 1 || nota > 5) {
			throw new IllegalArgumentException("Nota deve estar entre 1 e 5, mas foi: " + nota);
		}
		notas.put(idFilme, nota);
	}

	public Integer getNotaPara(String idFilme) {
		return notas.get(idFilme);
	}

	public Map<String, Integer> getNotas() {
		return Collections.unmodifiableMap(notas);
	}
}
