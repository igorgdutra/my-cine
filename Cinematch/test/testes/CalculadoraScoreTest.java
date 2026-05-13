package testes;

import model.Filme;
import model.PerfilCinefilo;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.CalculadoraScore;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraScoreTest {

	private PerfilCinefilo perfil;
	private CalculadoraScore calculadora;

	@BeforeEach
	void setUp() {
		calculadora = new CalculadoraScore();

		perfil = new PerfilCinefilo();
		perfil.setFaixaDuracao(90, 150);
		perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZESSEIS);
		perfil.adicionarIdioma(Idioma.INGLES);
		perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);
		perfil.setPesoGenero(Genero.DRAMA, 1.0);
	}

	@Test
	@DisplayName("deve gerar score alto para filme compatível")
	void deve_GerarScoreAlto_Quando_FilmeCompativel() {
		Filme filme = criarFilme("F01", 120, 90.0, Genero.FICCAO_CIENTIFICA);

		double score = calculadora.calcularScore(perfil, filme);

		assertTrue(score >= 80);
	}

	@Test
	@DisplayName("deve reduzir score quando duração está fora da faixa")
	void deve_ReduzirScore_Quando_DuracaoForaDaFaixa() {
		Filme filmeDentro = criarFilme("F01", 120, 80.0, Genero.DRAMA);
		Filme filmeLongo = criarFilme("F02", 200, 80.0, Genero.DRAMA);

		double scoreDentro = calculadora.calcularScore(perfil, filmeDentro);
		double scoreLongo = calculadora.calcularScore(perfil, filmeLongo);

		assertTrue(scoreDentro > scoreLongo);
	}

	@Test
	@DisplayName("score deve ficar entre 0 e 100")
	void deve_ManterScore_EntreZeroECem() {
		Filme filme = criarFilme("F03", 120, 100.0, Genero.FICCAO_CIENTIFICA);

		double score = calculadora.calcularScore(perfil, filme);

		assertTrue(score >= 0);
		assertTrue(score <= 100);
	}

	private Filme criarFilme(String id, int duracao, double popularidade, Genero genero) {
		return new Filme(id, "Filme Teste", 2020, duracao, List.of(genero), ClassificacaoEtaria.DOZE, Idioma.INGLES,
				popularidade);
	}
}