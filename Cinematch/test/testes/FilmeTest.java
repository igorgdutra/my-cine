package testes;

import model.Filme;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
class FilmeTest {

	private Filme filmePrincipal;
	private Filme filmeComMesmoId;
	private Filme filmeComIdDiferente;

	@BeforeEach
	void setUp() {
		filmePrincipal = new Filme("F01", "A Chegada", 2016, 116, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
				ClassificacaoEtaria.DOZE, Idioma.INGLES, 84.0);

		filmeComMesmoId = new Filme("F01", "Título Diferente", 2020, 90, List.of(Genero.COMEDIA),
				ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 50.0);

		filmeComIdDiferente = new Filme("F99", "Outro Filme", 2021, 100, List.of(Genero.DRAMA),
				ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 70.0);
	}

	@Test
	@DisplayName("deve criar filme com todos os atributos preenchidos")
	void deve_CriarFilme_ComTodosOsAtributosPreenchidos() {
		assertAll("Todos os atributos do filme", () -> assertEquals("F01", filmePrincipal.getId()),
				() -> assertEquals("A Chegada", filmePrincipal.getTitulo()),
				() -> assertEquals(2016, filmePrincipal.getAno()),
				() -> assertEquals(116, filmePrincipal.getDuracaoMinutos()),
				() -> assertEquals(ClassificacaoEtaria.DOZE, filmePrincipal.getClassificacao()),
				() -> assertEquals(Idioma.INGLES, filmePrincipal.getIdioma()),
				() -> assertEquals(84.0, filmePrincipal.getPopularidade()));
	}

	@Test
	@DisplayName("deve conter os gêneros informados na criação")
	void deve_ConterGenerosInformados_AoCriarFilme() {
		assertTrue(filmePrincipal.getGeneros().contains(Genero.FICCAO_CIENTIFICA));
		assertTrue(filmePrincipal.getGeneros().contains(Genero.DRAMA));
		assertEquals(2, filmePrincipal.getGeneros().size());
	}

	@Test
	@DisplayName("dois filmes com mesmo id devem ser iguais")
	void deve_SerIgual_Quando_MesmoId() {
		assertEquals(filmePrincipal, filmeComMesmoId);
	}

	@Test
	@DisplayName("dois filmes com ids diferentes devem ser diferentes")
	void deve_SerDiferente_Quando_IdsDistintos() {
		assertNotEquals(filmePrincipal, filmeComIdDiferente);
	}

	@Test
	@DisplayName("hashCode deve ser igual para filmes com mesmo id")
	void deve_TerMesmoHashCode_Quando_MesmoId() {
		assertEquals(filmePrincipal.hashCode(), filmeComMesmoId.hashCode());
	}

	@Test
	@DisplayName("lista de gêneros deve ser imutável")
	void deve_SerImutavel_ListaDeGeneros() {
		assertThrows(UnsupportedOperationException.class, () -> filmePrincipal.getGeneros().add(Genero.TERROR));
	}
}