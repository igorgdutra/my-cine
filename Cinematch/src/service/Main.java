package service;

import model.PerfilCinefilo;
import model.Recomendacao;
import model.Usuario;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;
import util.GeradorAleatorio;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.println("=================================");
		System.out.println("         CINE MATCH");
		System.out.println(" Sistema Inteligente de Filmes");
		System.out.println("=================================\n");

		System.out.print("Digite seu nome: ");
		String nome = scanner.nextLine();

		System.out.print("Digite sua idade: ");
		int idade = scanner.nextInt();

		PerfilCinefilo perfil = new PerfilCinefilo();

		if (idade >= 18) {
			perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZOITO);
		} else {
			perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZESSEIS);
		}

		System.out.println("\nQual idioma você prefere?");
		System.out.println("1 - Português");
		System.out.println("2 - Inglês");
		int idiomaEscolhido = scanner.nextInt();

		if (idiomaEscolhido == 1) {
			perfil.adicionarIdioma(Idioma.PORTUGUES);
			perfil.adicionarIdioma(Idioma.INGLES);
		} else {
			perfil.adicionarIdioma(Idioma.INGLES);
			perfil.adicionarIdioma(Idioma.PORTUGUES);
		}

		System.out.println("\nQual duração de filme você prefere?");
		System.out.println("1 - Curto (até 100 min)");
		System.out.println("2 - Médio (100 a 140 min)");
		System.out.println("3 - Longo (140+ min)");
		int duracaoEscolhida = scanner.nextInt();

		switch (duracaoEscolhida) {
		case 1:
			perfil.setFaixaDuracao(60, 100);
			break;
		case 2:
			perfil.setFaixaDuracao(100, 140);
			break;
		case 3:
			perfil.setFaixaDuracao(140, 220);
			break;
		default:
			perfil.setFaixaDuracao(90, 150);
			break;
		}

		System.out.println("\nEscolha seu gênero favorito:");
		System.out.println("1 - Ficção Científica");
		System.out.println("2 - Drama");
		System.out.println("3 - Comédia");
		System.out.println("4 - Terror");
		System.out.println("5 - Romance");
		int generoEscolhido = scanner.nextInt();

		switch (generoEscolhido) {
		case 1:
			perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);
			perfil.setPesoGenero(Genero.DRAMA, 0.7);
			perfil.setPesoGenero(Genero.COMEDIA, 0.4);
			perfil.setPesoGenero(Genero.TERROR, 0.0);
			break;
		case 2:
			perfil.setPesoGenero(Genero.DRAMA, 1.0);
			perfil.setPesoGenero(Genero.ROMANCE, 0.7);
			perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 0.5);
			perfil.setPesoGenero(Genero.TERROR, 0.0);
			break;
		case 3:
			perfil.setPesoGenero(Genero.COMEDIA, 1.0);
			perfil.setPesoGenero(Genero.DRAMA, 0.5);
			perfil.setPesoGenero(Genero.ROMANCE, 0.4);
			perfil.setPesoGenero(Genero.TERROR, 0.0);
			break;
		case 4:
			perfil.setPesoGenero(Genero.TERROR, 1.0);
			perfil.setPesoGenero(Genero.DRAMA, 0.5);
			break;
		case 5:
			perfil.setPesoGenero(Genero.ROMANCE, 1.0);
			perfil.setPesoGenero(Genero.DRAMA, 0.8);
			perfil.setPesoGenero(Genero.COMEDIA, 0.5);
			perfil.setPesoGenero(Genero.TERROR, 0.0);
			break;
		default:
			perfil.setPesoGenero(Genero.DRAMA, 0.5);
			perfil.setPesoGenero(Genero.COMEDIA, 0.5);
			break;
		}

		scanner.nextLine();

		System.out.print("\nDeseja receber notificações? (s/n): ");
		String notificacao = scanner.nextLine();

		Usuario usuario = new Usuario(nome, idade, perfil);
		usuario.setNotificacoesAtivas(notificacao.equalsIgnoreCase("s"));

		CatalogoFilmesAPI catalogo = new CatalogoMock();

		HistoricoUsuarioRepository historico = (u, r) -> System.out.println("\nHistórico salvo com sucesso.");

		NotificadorPush notificador = (u, m) -> System.out.println("Notificação enviada: " + m);

		GeradorAleatorio gerador = (min, max) -> min;

		RecomendadorService recomendador = new RecomendadorService(catalogo, historico, notificador, gerador,
				new CalculadoraScore(), new FiltroFilmes());

		List<Recomendacao> recomendacoes = recomendador.recomendar(usuario, 3);

		if (recomendacoes.isEmpty()) {
			System.out.println("\nNenhum filme encontrado com esse perfil.");
			System.out.println("Tente escolher outro idioma, gênero ou duração.");
			scanner.close();
			return;
		}

		System.out.println("\n=================================");
		System.out.println("     FILMES RECOMENDADOS");
		System.out.println("=================================");

		for (Recomendacao r : recomendacoes) {
			System.out.println("\nFilme: " + r.getFilme().getTitulo());
			System.out.println("Score: " + String.format("%.2f", r.getScore()));
			System.out.println("Justificativa:");
			System.out.println(r.getJustificativa());
			System.out.println("---------------------------------");
		}

		scanner.close();
	}
}