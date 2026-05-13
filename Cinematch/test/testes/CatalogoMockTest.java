package testes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.Filme;
import service.CatalogoMock;

class CatalogoMockTest {

    @Test
    @DisplayName("deve retornar lista de filmes")
    void deve_RetornarListaDeFilmes_Quando_BuscarTodos() {

        CatalogoMock catalogo = new CatalogoMock();

        List<Filme> filmes = catalogo.buscarTodos();

        assertNotNull(filmes);
        assertFalse(filmes.isEmpty());
    }

    @Test
    @DisplayName("deve retornar pelo menos 30 filmes")
    void deve_RetornarQuantidadeMinima_Quando_BuscarTodos() {

        CatalogoMock catalogo = new CatalogoMock();

        List<Filme> filmes = catalogo.buscarTodos();

        assertTrue(filmes.size() >= 30);
    }
}