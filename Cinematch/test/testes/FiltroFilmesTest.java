package testes;

import model.Filme;
import model.PerfilCinefilo;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.FiltroFilmes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FiltroFilmesTest {

    private PerfilCinefilo perfil;
    private FiltroFilmes filtro;

    @BeforeEach
    void setUp() {
        filtro = new FiltroFilmes();

        perfil = new PerfilCinefilo();
        perfil.setFaixaDuracao(90, 150);
        perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZESSEIS);
        perfil.adicionarIdioma(Idioma.INGLES);
        perfil.adicionarIdioma(Idioma.PORTUGUES);
        perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 0.9);
        perfil.setPesoGenero(Genero.DRAMA, 0.7);
        perfil.setPesoGenero(Genero.TERROR, 0.0);
    }

    @Test
    @DisplayName("deve remover filme já assistido")
    void deve_RemoverFilme_Quando_JaAssistido() {
        Filme filme = criarFilme("F01", "Interestelar", ClassificacaoEtaria.DOZE, Idioma.INGLES, Genero.DRAMA);
        perfil.marcarAssistido("F01");

        List<Filme> resultado = filtro.filtrar(perfil, List.of(filme));

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("deve remover filme acima da classificação máxima")
    void deve_RemoverFilme_Quando_ClassificacaoMaior() {
        Filme filme = criarFilme("F02", "O Iluminado", ClassificacaoEtaria.DEZOITO, Idioma.INGLES, Genero.DRAMA);

        List<Filme> resultado = filtro.filtrar(perfil, List.of(filme));

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("deve remover filme com idioma não aceito")
    void deve_RemoverFilme_Quando_IdiomaNaoAceito() {
        Filme filme = criarFilme("F03", "Filme Francês", ClassificacaoEtaria.DOZE, Idioma.FRANCES, Genero.DRAMA);

        List<Filme> resultado = filtro.filtrar(perfil, List.of(filme));

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("deve remover filme com gênero de peso zero")
    void deve_RemoverFilme_Quando_GeneroPesoZero() {
        Filme filme = criarFilme("F04", "Terror", ClassificacaoEtaria.DOZE, Idioma.INGLES, Genero.TERROR);

        List<Filme> resultado = filtro.filtrar(perfil, List.of(filme));

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("deve manter filme válido")
    void deve_ManterFilme_Quando_Valido() {
        Filme filme = criarFilme("F05", "A Chegada", ClassificacaoEtaria.DOZE, Idioma.INGLES, Genero.FICCAO_CIENTIFICA);

        List<Filme> resultado = filtro.filtrar(perfil, List.of(filme));

        assertEquals(1, resultado.size());
        assertEquals(filme, resultado.get(0));
    }

    private Filme criarFilme(String id, String titulo, ClassificacaoEtaria classificacao, Idioma idioma, Genero genero) {
        return new Filme(id, titulo, 2020, 120, List.of(genero), classificacao, idioma, 80.0);
    }
}