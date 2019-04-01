package br.com.paulo.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.com.paulo.entidades.Filme;
import br.com.paulo.entidades.Locacao;
import br.com.paulo.entidades.Usuario;
import br.com.paulo.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService service;
	private Usuario usuario;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		service = new LocacaoService();
		usuario = new Usuario("Usuario 1");
	}

	@BeforeClass
	public static void setupClass() {
		// TODO
	}

	@Test
	public void testeLocacao() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// cenario

		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao
		assertEquals(5.0, locacao.getValor(), 0.01);
		assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

		// CoreMatcher
		assertThat(locacao.getValor(), is(5.0));
		assertThat(locacao.getValor(), is(equalTo(5.0)));
		assertThat(locacao.getValor(), not(6.0));
		assertThat(locacao.getValor(), not(equalTo(6.0)));
		assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));

		// ErrorColletor
		error.checkThat(locacao.getValor(), is(5.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
	}

	@Test(expected = Exception.class)
	public void testeLocacao_SemEstoque() throws Exception {
		// cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

		// acao
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void testeLocacao_SemEstoque_2() {
		// cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

		// acao
		try {
			service.alugarFilme(usuario, filmes);
			fail("Deveria lancar uma excecao");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}
	}

	@Test
	public void testeLocacao_SemEstoque_3() throws Exception {
		// cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

		// excecao esperada
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		// acao
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void devePagar75PctoNoFilme3() throws Exception {
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 1, 4.0), 
				new Filme("Filme 2", 1, 4.0),
				new Filme("Filme 3", 1, 4.0));
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		// 4 + 4 + 3 = 11d
		assertThat(locacao.getValor(), is(11.0));
	}
	
	@Test
	public void devePagar50PctoNoFilme4() throws Exception {
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 1, 4.0), 
				new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 1, 4.0),
				new Filme("Filme 4", 2, 4.0));
		
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// 4 + 4 + 3 + 2 = d
		assertThat(locacao.getValor(), is(13d));
	}
	
	@Test
	public void devePagar25PctoNoFilme5() throws Exception {
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 1, 4.0), 
				new Filme("Filme 2", 1, 4.0),
				new Filme("Filme 3", 1, 4.0),
				new Filme("Filme 4", 1, 4.0),
				new Filme("Filme 5", 1, 4.0));
	
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		// 4 + 4 + 3 + 2 + 1 = d
		assertThat(locacao.getValor(), is(14d));
	}
	
	@Test
	public void devePagar100PctoNoFilme6() throws Exception {
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 1, 4.0), 
				new Filme("Filme 2", 1, 4.0),
				new Filme("Filme 3", 1, 4.0),
				new Filme("Filme 4", 1, 4.0),
				new Filme("Filme 5", 1, 4.0),
				new Filme("Filme 6", 1, 4.0));
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		// 4 + 4 + 3 + 2 + 1 + 0 = d
		assertThat(locacao.getValor(), is(14d));
	}

	@Test
	public void naoDeveDevolverFilmeNoDomingo() throws Exception {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0));
		Locacao locacao = service.alugarFilme(usuario, filmes);
		assertTrue(DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY));
	}
}