package br.com.paulo.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.paulo.entidades.Filme;
import br.com.paulo.entidades.Locacao;
import br.com.paulo.entidades.Usuario;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

	private LocacaoService service;

	private Usuario usuario;

	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value = 1)
	public double valorLocacao;

	@Before
	public void setup() {
		service = new LocacaoService();
		usuario = new Usuario("Usuario 1");
	}
	
	private static Filme filme1 = new Filme("Filme 1", 1, 4.0);
	private static Filme filme2 = new Filme("Filme 2", 1, 4.0);
	private static Filme filme3 = new Filme("Filme 3", 1, 4.0);
	private static Filme filme4 = new Filme("Filme 4", 1, 4.0);
	private static Filme filme5 = new Filme("Filme 5", 1, 4.0);
	private static Filme filme6 = new Filme("Filme 6", 1, 4.0);
	
	@Parameters
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1, filme2, filme3), 11.0},
			{Arrays.asList(filme1, filme2, filme3, filme4), 13.0},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0}
		});
	}

	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws Exception {
		Locacao locacao = service.alugarFilme(usuario, filmes);
		assertThat(locacao.getValor(), is(valorLocacao));
	}
}