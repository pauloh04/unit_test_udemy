package br.com.paulo.servicos;

import static br.com.paulo.builders.FilmeBuilder.umFilme;
import static br.com.paulo.builders.LocacaoBuilder.umLocacao;
import static br.com.paulo.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.mockito.Mockito;

import br.com.paulo.builders.UsuarioBuilder;
import br.com.paulo.dao.LocacaoDAO;
import br.com.paulo.entidades.Filme;
import br.com.paulo.entidades.Locacao;
import br.com.paulo.entidades.Usuario;
import br.com.paulo.matchers.MatcherProprios;
import br.com.paulo.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService service;
	private Usuario usuario;
	private SPCService spcService;
	private LocacaoDAO locacaoDAO;
	private EmailService emailService;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		service = new LocacaoService();
		usuario = UsuarioBuilder.umUsuario().agora();
//		LocacaoDAO locacaoDAO = new LocacaoDAOFake(); // Fake object
		locacaoDAO = Mockito.mock(LocacaoDAO.class); // Mockito
		service.setLocacaoDAO(locacaoDAO);
		spcService = Mockito.mock(SPCService.class);
		service.setSPCService(spcService);
		emailService = Mockito.mock(EmailService.class);
		service.setEmailService(emailService);
	}

	@BeforeClass
	public static void setupClass() {
		// TODO
	}

	@Test
	public void testeLocacao() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// cenario

		List<Filme> filmes = Arrays.asList(umFilme().preco5().agora());

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
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

		// acao
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void testeLocacao_SemEstoque_2() {
		// cenario
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

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
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

		// excecao esperada
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		// acao
		service.alugarFilme(usuario, filmes);
	}

//	@Test
	public void naoDeveDevolverFilmeNoDomingo() throws Exception {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		Locacao locacao = service.alugarFilme(usuario, filmes);
//		assertTrue(DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY));
//		assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(locacao.getDataRetorno(), MatcherProprios.caiEm(Calendar.TUESDAY));
		assertThat(locacao.getDataRetorno(), MatcherProprios.caiNumaSegunda());
	}

//	@Test
	public void deveAlugarFilme() throws Exception {
//		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		error.checkThat(locacao.getDataLocacao(), MatcherProprios.ehHoje());
//		error.checkThat(locacao.getDataLocacao(), MatcherProprios.ehHojeComDiferencaDias(1));
	}
	
//	@Test(expected = Exception.class)
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spcService.possuiNegativacao(usuario)).thenReturn(true);
		
		// excecao esperada
		exception.expect(Exception.class);
		exception.expectMessage("Usuario negativado");
				
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		List<Locacao> listLocacao = Arrays.asList(umLocacao().comUsuario(usuario).comDataRetorno(obterDataComDiferencaDias(-2)).agora());
		when(locacaoDAO.obterLocacoesPendentes()).thenReturn(listLocacao);
		
		service.notificarAtrasos();
		
		verify(emailService).notificarAtraso(usuario);
	}
}