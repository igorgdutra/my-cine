package service;

import model.Filme;
import model.Recomendacao;
import model.Usuario;
import model.enums.Genero;
import util.GeradorAleatorio;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RecomendadorService {

	private final CatalogoFilmesAPI catalogo;
	private final HistoricoUsuarioRepository historicoRepo;
	private final NotificadorPush notificador;
	private final GeradorAleatorio gerador;
	private final CalculadoraScore calculadora;
	private final FiltroFilmes filtro;

	public RecomendadorService(CatalogoFilmesAPI catalogo, HistoricoUsuarioRepository historicoRepo,
			NotificadorPush notificador, GeradorAleatorio gerador, CalculadoraScore calculadora, FiltroFilmes filtro) {
		this.catalogo = catalogo;
		this.historicoRepo = historicoRepo;
		this.notificador = notificador;
		this.gerador = gerador;
		this.calculadora = calculadora;
		this.filtro = filtro;
	}

	public List<Recomendacao> recomendar(Usuario usuario, int topN) {
		List<Filme> todosFilmes = buscarCatalogoComSeguranca();
		if (todosFilmes.isEmpty()) {
			return Collections.emptyList();
		}

		List<Filme> filtrados = filtro.filtrar(usuario.getPerfil(), todosFilmes);
		if (filtrados.isEmpty()) {
			return Collections.emptyList();
		}
		List<Recomendacao> ranqueadas = pontuar(usuario.getPerfil(), filtrados);
		List<Recomendacao> ordenadas = ordenarPorScore(ranqueadas);
		List<Recomendacao> resultado = ordenadas.stream().limit(topN).collect(Collectors.toList());

		historicoRepo.registrarRecomendacao(usuario, resultado);

		if (usuario.isNotificacoesAtivas()) {
			notificador.enviar(usuario, "Sua recomendação do dia está pronta!");
		}

		return resultado;
	}

	public Recomendacao recomendarAleatorio(Usuario usuario) {
		List<Filme> todosFilmes = buscarCatalogoComSeguranca();
		List<Filme> filtrados = filtro.filtrar(usuario.getPerfil(), todosFilmes);

		if (filtrados.isEmpty()) {
			return null;
		}

		int indice = gerador.sortearInteiro(0, filtrados.size() - 1);
		Filme escolha = filtrados.get(indice);
		double score = calculadora.calcularScore(usuario.getPerfil(), escolha);

		return new Recomendacao(escolha, score, "Surpresa escolhida para você!");
	}

	private List<Filme> buscarCatalogoComSeguranca() {
		try {
			List<Filme> filmes = catalogo.buscarTodos();
			return filmes != null ? filmes : Collections.emptyList();
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	private List<Recomendacao> pontuar(model.PerfilCinefilo perfil, List<Filme> filmes) {
		return filmes.stream().map(filme -> new Recomendacao(filme, calculadora.calcularScore(perfil, filme),
				gerarJustificativa(perfil, filme))).collect(Collectors.toList());
	}

	private List<Recomendacao> ordenarPorScore(List<Recomendacao> lista) {
		return lista.stream().sorted(Comparator.comparingDouble(Recomendacao::getScore).reversed()
				.thenComparingDouble(r -> -r.getFilme().getPopularidade())).collect(Collectors.toList());
	}

	private String gerarJustificativa(model.PerfilCinefilo perfil, Filme filme) {
		String generoDestaque = filme.getGeneros().stream().max(Comparator.comparingDouble(perfil::getPesoGenero))
				.map(Genero::name).orElse("seus gêneros favoritos");

		return String.format(
				"Recomendado para '%s' porque combina com sua preferência por %s e tem %.0f%% de popularidade.",
				filme.getTitulo(), generoDestaque, filme.getPopularidade());
	}
}
