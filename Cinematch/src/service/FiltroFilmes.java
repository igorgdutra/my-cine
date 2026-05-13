package service;

import model.Filme;
import model.PerfilCinefilo;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroFilmes {

	public List<Filme> filtrar(PerfilCinefilo perfil, List<Filme> filmes) {
		if (filmes == null || filmes.isEmpty()) {
			return Collections.emptyList();
		}

		return filmes.stream().filter(filme -> naoFoiAssistido(perfil, filme))
				.filter(filme -> classificacaoAceitavel(perfil, filme)).filter(filme -> idiomaAceito(perfil, filme))
				.filter(filme -> semGeneroProibido(perfil, filme)).collect(Collectors.toList());
	}

	private boolean naoFoiAssistido(PerfilCinefilo perfil, Filme filme) {
		return !perfil.jaAssistiu(filme.getId());
	}

	private boolean classificacaoAceitavel(PerfilCinefilo perfil, Filme filme) {
		return filme.getClassificacao().getIdadeMinima() <= perfil.getClassificacaoMaxima().getIdadeMinima();
	}

	private boolean idiomaAceito(PerfilCinefilo perfil, Filme filme) {
		return perfil.getIdiomasAceitos().contains(filme.getIdioma());
	}

	private boolean semGeneroProibido(PerfilCinefilo perfil, Filme filme) {
		return filme.getGeneros().stream().noneMatch(genero -> perfil.getPesoGenero(genero) == 0.0);
	}

}
