package br.com.paulo.servicos;

import static br.com.paulo.builders.FilmeBuilder.umFilme;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.com.paulo.dao.LocacaoDAO;
import br.com.paulo.entidades.Filme;
import br.com.paulo.entidades.Locacao;
import br.com.paulo.entidades.Usuario;
import br.com.paulo.matchers.MatcherProprios;
import br.com.paulo.utils.DataUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LocacaoService.class)
public class LocacaoServicePowermockTest {

	@InjectMocks
	private LocacaoService service;
	private Usuario usuario;
	
	@Mock
	private SPCService spcService;
	@Mock
	private LocacaoDAO locacaoDAO;
	@Mock
	private EmailService emailService;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		service = PowerMockito.spy(service);
		
//		service = new LocacaoService();
//		usuario = UsuarioBuilder.umUsuario().agora();
////		LocacaoDAO locacaoDAO = new LocacaoDAOFake(); // Fake object
//		locacaoDAO = Mockito.mock(LocacaoDAO.class); // Mockito
//		service.setLocacaoDAO(locacaoDAO);
//		spcService = Mockito.mock(SPCService.class);
//		service.setSPCService(spcService);
//		emailService = Mockito.mock(EmailService.class);
//		service.setEmailService(emailService);
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

	//@Test
	public void naoDeveDevolverFilmeNoDomingo() throws Exception {
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
//		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 30);
		cal.set(Calendar.MONTH, Calendar.MARCH);
		cal.set(Calendar.YEAR, 2019);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(cal);
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
//		assertTrue(DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY));
//		assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(locacao.getDataRetorno(), MatcherProprios.caiEm(Calendar.TUESDAY));
		assertThat(locacao.getDataRetorno(), MatcherProprios.caiNumaSegunda());
	}

	@Test
	public void deveAlugarFilme() throws Exception {
//		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		error.checkThat(locacao.getDataLocacao(), MatcherProprios.ehHoje());
//		error.checkThat(locacao.getDataLocacao(), MatcherProprios.ehHojeComDiferencaDias(1));
	}
	
	@Test
	public void deveAlugarFilmeSemCalcularValor() throws Exception {
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		assertThat(locacao.getValor(), is(1.0));
	}
	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);
		
		assertThat(valor, is(4.0));
	}	
}