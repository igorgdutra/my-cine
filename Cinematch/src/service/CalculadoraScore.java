package service;

import model.Filme;
import model.PerfilCinefilo;
import java.util.Map;

public class CalculadoraScore {

	static final double PESO_GENERO = 0.50;
	static final double PESO_DURACAO = 0.20;
	static final double PESO_POPULARIDADE = 0.15;
	static final double PESO_AFINIDADE = 0.15;

	private static final int TOLERANCIA_DURACAO_MINUTOS = 30;

	public double calcularScore(PerfilCinefilo perfil, Filme filme) {
		double scoreGenero = calcularScoreGenero(perfil, filme);
		double scoreDuracao = calcularScoreDuracao(perfil, filme);
		double scorePopular = filme.getPopularidade();
		double scoreAfinidade = calcularAfinidade(perfil);
		double total = (scoreGenero * PESO_GENERO) + (scoreDuracao * PESO_DURACAO) + (scorePopular * PESO_POPULARIDADE)
				+ (scoreAfinidade * PESO_AFINIDADE);
		return Math.min(100.0, Math.max(0.0, total));
	}

	private double calcularScoreGenero(PerfilCinefilo perfil, Filme filme) {
		if (filme.getGeneros().isEmpty())
			return 0.0;
		double somaPesos = filme.getGeneros().stream().mapToDouble(perfil::getPesoGenero).sum();
		return (somaPesos / filme.getGeneros().size()) * 100.0;
	}

	private double calcularScoreDuracao(PerfilCinefilo perfil, Filme filme) {
		int duracao = filme.getDuracaoMinutos();
		int min = perfil.getDuracaoMinimaMin();
		int max = perfil.getDuracaoMaximaMin();
		if (duracao >= min && duracao <= max) {
			return 100.0;
		}

		int desvio = duracao < min ? (min - duracao) : (duracao - max);

		double reducao = ((double) desvio / TOLERANCIA_DURACAO_MINUTOS) * 100.0;
		return Math.max(0.0, 100.0 - reducao);
	}

	private double calcularAfinidade(PerfilCinefilo perfil) {
		Map<String, Integer> notas = perfil.getNotas();

		if (notas.isEmpty()) {
			return 50.0;
		}

		double mediaNotas = notas.values().stream().mapToInt(Integer::intValue).average().orElse(3.0);

		return (mediaNotas / 5.0) * 100.0;
	}
}
