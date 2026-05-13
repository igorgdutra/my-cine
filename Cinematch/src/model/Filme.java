package model;

import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import java.util.List;
import java.util.Objects;
 

public final class Filme {

	private final String id;
	private final String titulo;
	private final int ano;
	private final int duracaoMinutos;
	private final List<Genero> generos;
	private final ClassificacaoEtaria classificacao;
	private final Idioma idioma;
	private final double popularidade;

	public Filme(String id, String titulo, int ano, int duracaoMinutos, List<Genero> generos,
			ClassificacaoEtaria classificacao, Idioma idioma, double popularidade) {
		this.id = id;
		this.titulo = titulo;
		this.ano = ano;
		this.duracaoMinutos = duracaoMinutos;
		this.generos = List.copyOf(generos);
		this.classificacao = classificacao;
		this.idioma = idioma;
		this.popularidade = popularidade;
	}

	public String getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getAno() {
		return ano;
	}

	public int getDuracaoMinutos() {
		return duracaoMinutos;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public ClassificacaoEtaria getClassificacao() {
		return classificacao;
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public double getPopularidade() {
		return popularidade;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Filme))
			return false;
		Filme filme = (Filme) o;
		return Objects.equals(id, filme.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Filme{id='" + id + "', titulo='" + titulo + "', ano=" + ano + "}";
	}
}
