package service;

import model.Filme;
import model.enums.ClassificacaoEtaria;
import model.enums.Genero;
import model.enums.Idioma;

import java.util.List;

public class CatalogoMock implements CatalogoFilmesAPI {

	@Override
	public List<Filme> buscarTodos() {
		return List.of(
				new Filme("F01", "Interestelar", 2014, 169, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
						ClassificacaoEtaria.DOZE, Idioma.INGLES, 95.0),
				new Filme("F02", "A Chegada", 2016, 116, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
						ClassificacaoEtaria.DOZE, Idioma.INGLES, 84.0),
				new Filme("F03", "Click", 2006, 107, List.of(Genero.COMEDIA, Genero.DRAMA), ClassificacaoEtaria.DOZE,
						Idioma.INGLES, 80.0),
				new Filme("F04", "Invocação do Mal", 2013, 112, List.of(Genero.TERROR), ClassificacaoEtaria.DEZESSEIS,
						Idioma.INGLES, 88.0),
				new Filme("F05", "Como Eu Era Antes de Você", 2016, 110, List.of(Genero.ROMANCE, Genero.DRAMA),
						ClassificacaoEtaria.DOZE, Idioma.INGLES, 82.0),
				new Filme("F06", "Minha Mãe é uma Peça", 2013, 140, List.of(Genero.COMEDIA), ClassificacaoEtaria.DOZE,
						Idioma.PORTUGUES, 90.0),
				new Filme("F07", "Auto da Compadecida", 2000, 157, List.of(Genero.COMEDIA, Genero.DRAMA),
						ClassificacaoEtaria.DEZ, Idioma.PORTUGUES, 96.0),
				new Filme("F08", "Duna: Parte Dois", 2024, 166, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
						ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 92.0),
				new Filme("F09", "Central do Brasil", 1998, 113, List.of(Genero.DRAMA), ClassificacaoEtaria.DOZE,
						Idioma.PORTUGUES, 89.0),
				new Filme("F10", "Questão de Tempo", 2013, 123, List.of(Genero.ROMANCE, Genero.COMEDIA, Genero.DRAMA),
						ClassificacaoEtaria.DOZE, Idioma.INGLES, 87.0),
				new Filme("F11", "Matrix", 1999, 136, List.of(Genero.FICCAO_CIENTIFICA, Genero.ACAO),
						ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 94.0),
				new Filme("F12", "O Show de Truman", 1998, 103, List.of(Genero.DRAMA, Genero.COMEDIA),
						ClassificacaoEtaria.LIVRE, Idioma.INGLES, 86.0),
				new Filme("F13", "Forrest Gump", 1994, 142, List.of(Genero.DRAMA, Genero.ROMANCE),
						ClassificacaoEtaria.DOZE, Idioma.INGLES, 91.0),
				new Filme("F14", "Divertida Mente", 2015, 95, List.of(Genero.COMEDIA, Genero.DRAMA),
						ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 85.0),
				new Filme("F15", "Wall-E", 2008, 98, List.of(Genero.FICCAO_CIENTIFICA, Genero.ROMANCE),
						ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 88.0),
				new Filme("F16", "O Iluminado", 1980, 146, List.of(Genero.TERROR, Genero.DRAMA),
						ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 89.0),
				new Filme("F17", "Hereditário", 2018, 127, List.of(Genero.TERROR, Genero.DRAMA),
						ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 83.0),
				new Filme("F18", "A Origem", 2010, 148, List.of(Genero.FICCAO_CIENTIFICA, Genero.ACAO),
						ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 93.0),
				new Filme("F19", "Blade Runner 2049", 2017, 164, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
						ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 90.0),
				new Filme("F20", "O Homem do Futuro", 2011, 106,
						List.of(Genero.FICCAO_CIENTIFICA, Genero.COMEDIA, Genero.ROMANCE), ClassificacaoEtaria.DOZE,
						Idioma.PORTUGUES, 78.0),
				new Filme("F21", "Cidade de Deus", 2002, 130, List.of(Genero.DRAMA), ClassificacaoEtaria.DEZOITO,
						Idioma.PORTUGUES, 95.0),
				new Filme("F22", "Tropa de Elite", 2007, 115, List.of(Genero.ACAO, Genero.DRAMA),
						ClassificacaoEtaria.DEZOITO, Idioma.PORTUGUES, 92.0),
				new Filme("F23", "Se Eu Fosse Você", 2006, 108, List.of(Genero.COMEDIA, Genero.ROMANCE),
						ClassificacaoEtaria.DEZ, Idioma.PORTUGUES, 77.0),
				new Filme("F24", "Dois Filhos de Francisco", 2005, 132, List.of(Genero.DRAMA),
						ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 76.0),
				new Filme("F25", "O Palhaço", 2011, 88, List.of(Genero.COMEDIA, Genero.DRAMA),
						ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 79.0),
				new Filme("F26", "Titanic", 1997, 195, List.of(Genero.ROMANCE, Genero.DRAMA), ClassificacaoEtaria.DOZE,
						Idioma.INGLES, 94.0),
				new Filme("F27", "La La Land", 2016, 128, List.of(Genero.ROMANCE, Genero.DRAMA),
						ClassificacaoEtaria.DOZE, Idioma.INGLES, 85.0),
				new Filme("F28", "O Grande Hotel Budapeste", 2014, 99, List.of(Genero.COMEDIA, Genero.DRAMA),
						ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 82.0),
				new Filme("F29", "Mad Max: Estrada da Fúria", 2015, 120, List.of(Genero.ACAO, Genero.FICCAO_CIENTIFICA),
						ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 91.0),
				new Filme("F30", "O Dilema das Redes", 2020, 94, List.of(Genero.DOCUMENTARIO), ClassificacaoEtaria.DOZE,
						Idioma.PORTUGUES, 81.0));
	}
}