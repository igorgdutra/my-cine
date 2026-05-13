package testes;

import model.Filme;
import model.PerfilCinefilo;
import model.Recomendacao;
import model.Usuario;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import service.*;
import util.GeradorAleatorio;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecomendadorServiceTest {

    @Mock
    private CatalogoFilmesAPI catalogo;

    @Mock
    private HistoricoUsuarioRepository historico;

    @Mock
    private NotificadorPush notificador;

    @Mock
    private GeradorAleatorio gerador;

    private RecomendadorService service;
    private Usuario usuario;
    private CalculadoraScore calculadoraSpy;

    @BeforeEach
    void setUp() {
        calculadoraSpy = spy(new CalculadoraScore());

        service = new RecomendadorService(
                catalogo,
                historico,
                notificador,
                gerador,
                calculadoraSpy,
                new FiltroFilmes()
        );

        PerfilCinefilo perfil = new PerfilCinefilo();
        perfil.setFaixaDuracao(90, 150);
        perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZESSEIS);
        perfil.adicionarIdioma(Idioma.INGLES);
        perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 0.9);
        perfil.setPesoGenero(Genero.DRAMA, 0.8);
        perfil.setPesoGenero(Genero.TERROR, 0.0);

        usuario = new Usuario("Maria", 28, perfil);
    }

    @Nested
    @DisplayName("Quando o catálogo possui filmes")
    class QuandoCatalogoPossuiFilmes {

        @Test
        @DisplayName("deve retornar top N recomendações")
        void deve_RetornarTopN_Quando_CatalogoTemFilmes() {
            when(catalogo.buscarTodos()).thenReturn(List.of(
                    criarFilme("F01", "A Chegada", 84.0),
                    criarFilme("F02", "Ela", 78.0),
                    criarFilme("F03", "Duna", 92.0)
            ));

            List<Recomendacao> resultado = service.recomendar(usuario, 2);

            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("deve registrar recomendação no histórico")
        void deve_RegistrarHistorico_Quando_Recomendar() {
            when(catalogo.buscarTodos()).thenReturn(List.of(criarFilme("F01", "A Chegada", 84.0)));

            service.recomendar(usuario, 1);

            verify(historico, times(1)).registrarRecomendacao(eq(usuario), anyList());
        }

        @Test
        @DisplayName("deve enviar notificação quando notificações estão ativas")
        void deve_EnviarNotificacao_Quando_NotificacoesAtivas() {
            when(catalogo.buscarTodos()).thenReturn(List.of(criarFilme("F01", "A Chegada", 84.0)));

            service.recomendar(usuario, 1);

            verify(notificador, times(1)).enviar(eq(usuario), anyString());
        }

        @Test
        @DisplayName("não deve enviar notificação quando notificações estão desativadas")
        void deve_NaoEnviarNotificacao_Quando_NotificacoesDesativadas() {
            usuario.setNotificacoesAtivas(false);
            when(catalogo.buscarTodos()).thenReturn(List.of(criarFilme("F01", "A Chegada", 84.0)));

            service.recomendar(usuario, 1);

            verify(notificador, never()).enviar(any(), anyString());
        }

        @Test
        @DisplayName("deve capturar recomendações registradas no histórico")
        void deve_CapturarRecomendacoes_Quando_RegistrarHistorico() {
            Filme filme = criarFilme("F01", "A Chegada", 84.0);

            when(catalogo.buscarTodos()).thenReturn(List.of(filme));

            service.recomendar(usuario, 1);

            @SuppressWarnings("unchecked")
            ArgumentCaptor<List<Recomendacao>> captor =
                    (ArgumentCaptor<List<Recomendacao>>) (ArgumentCaptor<?>)
                            ArgumentCaptor.forClass(List.class);

            verify(historico).registrarRecomendacao(eq(usuario), captor.capture());

            List<Recomendacao> recomendacoesCapturadas = captor.getValue();

            assertAll(
                    () -> assertEquals(1, recomendacoesCapturadas.size()),
                    () -> assertEquals("A Chegada", recomendacoesCapturadas.get(0).getFilme().getTitulo()),
                    () -> assertNotNull(recomendacoesCapturadas.get(0).getJustificativa())
            );
        }

        @Test
        @DisplayName("deve usar CalculadoraScore real com spy")
        void deve_UsarCalculadoraScoreReal_Quando_Recomendar() {
            when(catalogo.buscarTodos()).thenReturn(List.of(
                    criarFilme("F01", "A Chegada", 84.0),
                    criarFilme("F02", "Ela", 78.0)
            ));

            service.recomendar(usuario, 2);

            verify(calculadoraSpy, times(2)).calcularScore(eq(usuario.getPerfil()), any(Filme.class));
        }
    }

    @Nested
    @DisplayName("Quando o catálogo está vazio ou falha")
    class QuandoCatalogoEstaVazioOuFalha {

        @Test
        @DisplayName("deve retornar lista vazia quando catálogo vazio")
        void deve_RetornarListaVazia_Quando_CatalogoVazio() {
            when(catalogo.buscarTodos()).thenReturn(List.of());

            List<Recomendacao> resultado = service.recomendar(usuario, 5);

            assertTrue(resultado.isEmpty());
        }

        @Test
        @DisplayName("deve retornar lista vazia quando catálogo lança exceção")
        void deve_RetornarListaVazia_Quando_CatalogoLancaExcecao() {
            when(catalogo.buscarTodos()).thenThrow(new RuntimeException("API offline"));

            List<Recomendacao> resultado = service.recomendar(usuario, 5);

            assertTrue(resultado.isEmpty());
            verify(notificador, never()).enviar(any(), anyString());
        }
    }

    @Nested
    @DisplayName("Quando usa modo surpreenda-me")
    class QuandoUsaModoSurpreendaMe {

        @Test
        @DisplayName("deve recomendar filme aleatório")
        void deve_RecomendarAleatorio_Quando_ExistemFilmes() {
            Filme filme1 = criarFilme("F01", "A Chegada", 84.0);
            Filme filme2 = criarFilme("F02", "Ela", 78.0);

            when(catalogo.buscarTodos()).thenReturn(List.of(filme1, filme2));
            when(gerador.sortearInteiro(0, 1)).thenReturn(1);

            Recomendacao recomendacao = service.recomendarAleatorio(usuario);

            assertNotNull(recomendacao);
            assertEquals("Ela", recomendacao.getFilme().getTitulo());
        }
    }

    @Test
    @DisplayName("deve comparar ordem dos ids recomendados")
    void deve_CompararIds_Quando_RecomendacoesOrdenadas() {
        when(catalogo.buscarTodos()).thenReturn(List.of(
                criarFilme("F01", "Filme Baixo", 50.0),
                criarFilme("F02", "Filme Alto", 90.0)
        ));

        List<Recomendacao> resultado = service.recomendar(usuario, 2);

        String[] idsEsperados = {"F02", "F01"};

        String[] idsObtidos = resultado.stream()
                .map(recomendacao -> recomendacao.getFilme().getId())
                .toArray(String[]::new);

        assertArrayEquals(idsEsperados, idsObtidos);
    }

    private Filme criarFilme(String id, String titulo, double popularidade) {
        return new Filme(
                id,
                titulo,
                2020,
                120,
                List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
                ClassificacaoEtaria.DOZE,
                Idioma.INGLES,
                popularidade
        );
    }
}