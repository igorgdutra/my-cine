package testes;

import exception.DuracaoInvalidaException;
import exception.PesoInvalidoException;
import model.PerfilCinefilo;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;


@Tag("unitario")
class PerfilCinefiloTest {

    private PerfilCinefilo perfil;

    @BeforeEach
    void setUp() {
        perfil = new PerfilCinefilo();
        perfil.setFaixaDuracao(90, 150);
        perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZESSEIS);
        perfil.adicionarIdioma(Idioma.PORTUGUES);
        perfil.adicionarIdioma(Idioma.INGLES);
    }

    @Test
    @DisplayName("deve criar perfil com pesos de gênero válidos")
    void deve_CriarPerfil_Quando_PesosGenerosSaoValidos() {

        assertDoesNotThrow(() -> {
            perfil.setPesoGenero(Genero.DRAMA,            0.8);
            perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA,1.0);
            perfil.setPesoGenero(Genero.TERROR,           0.0);
        });

        assertEquals(0.8, perfil.getPesoGenero(Genero.DRAMA));
        assertEquals(1.0, perfil.getPesoGenero(Genero.FICCAO_CIENTIFICA));
        assertEquals(0.0, perfil.getPesoGenero(Genero.TERROR));
    }

    @Test
    @DisplayName("deve lançar PesoInvalidoException quando peso é menor que zero")
    void deve_LancarExcecao_Quando_PesoMenorQueZero() {

        assertThrows(PesoInvalidoException.class,
            () -> perfil.setPesoGenero(Genero.ACAO, -0.1));
    }

    @Test
    @DisplayName("deve lançar PesoInvalidoException quando peso é maior que um")
    void deve_LancarExcecao_Quando_PesoMaiorQueUm() {
        assertThrows(PesoInvalidoException.class,
            () -> perfil.setPesoGenero(Genero.ACAO, 1.5));
    }

    @ParameterizedTest
    @CsvSource({"0.0", "0.5", "1.0"})
    @DisplayName("deve aceitar pesos nos limites do intervalo válido")
    void deve_AceitarPeso_Quando_NosLimitesDoIntervalo(double peso) {
        assertDoesNotThrow(() -> perfil.setPesoGenero(Genero.ROMANCE, peso));
    }

    @Test
    @DisplayName("deve retornar 0.5 como peso padrão para gênero não configurado")
    void deve_RetornarPesoPadrao_Quando_GeneroNaoConfigurado() {
        assertEquals(0.5, perfil.getPesoGenero(Genero.DOCUMENTARIO));
    }


    @Test
    @DisplayName("deve lançar DuracaoInvalidaException quando mínimo é maior que máximo")
    void deve_LancarExcecao_Quando_DuracaoMinimaEhMaiorQueMaxima() {
        assertThrows(DuracaoInvalidaException.class,
            () -> perfil.setFaixaDuracao(150, 90));
    }

    @Test
    @DisplayName("deve aceitar faixa de duração quando mínimo igual ao máximo")
    void deve_AceitarFaixaDuracao_Quando_MinEMaxSaoIguais() {
        assertDoesNotThrow(() -> perfil.setFaixaDuracao(120, 120));
    }

    @Test
    @DisplayName("deve lançar exceção quando nota está fora do intervalo 1 a 5")
    void deve_LancarExcecao_Quando_NotaForaDoIntervalo() {
        assertThrows(IllegalArgumentException.class,
            () -> perfil.adicionarNota("F01", 6));
        assertThrows(IllegalArgumentException.class,
            () -> perfil.adicionarNota("F02", 0));
    }

    @Test
    @DisplayName("deve aceitar notas nos extremos do intervalo válido (1 e 5)")
    void deve_AceitarNotas_NosExtremosDoIntervalo() {
        assertDoesNotThrow(() -> perfil.adicionarNota("F01", 1));
        assertDoesNotThrow(() -> perfil.adicionarNota("F02", 5));
    }

    @Test
    @DisplayName("deve retornar null para filme que nunca foi avaliado")
    void deve_RetornarNull_Quando_FilmeNuncaFoiAvaliado() {
        assertNull(perfil.getNotaPara("filme-que-nao-existe"));
    }

    @Test
    @DisplayName("deve retornar a nota correta para filme avaliado")
    void deve_RetornarNota_Quando_FilmeFoiAvaliado() {
        perfil.adicionarNota("F07", 4);
        assertNotNull(perfil.getNotaPara("F07"));
        assertEquals(4, perfil.getNotaPara("F07"));
    }

    @Test
    @DisplayName("deve registrar filme como assistido e aparecer no histórico")
    void deve_MarcarFilmeComoAssistido_Quando_Solicitado() {
        perfil.marcarAssistido("F04"); 

        assertTrue(perfil.jaAssistiu("F04"));
        assertFalse(perfil.jaAssistiu("F99"));
    }

    @Test
    @DisplayName("não deve duplicar filme no histórico se marcado duas vezes")
    void deve_NaoDuplicar_Quando_FilmeMarcadoDuasVezes() {
        perfil.marcarAssistido("F04");
        perfil.marcarAssistido("F04");

        assertEquals(1, perfil.getHistorico().size());
    }

    @ParameterizedTest
    @CsvSource({"PORTUGUES", "INGLES"})
    @DisplayName("deve conter os idiomas adicionados")
    void deve_ConterIdiomas_Quando_Adicionados(String nomeIdioma) {
        Idioma idioma = Idioma.valueOf(nomeIdioma);
        assertTrue(perfil.getIdiomasAceitos().contains(idioma));
    }
}