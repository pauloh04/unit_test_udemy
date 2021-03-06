package br.com.paulo.servicos;

import static br.com.paulo.builders.FilmeBuilder.umFilme;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.paulo.dao.LocacaoDAO;
import br.com.paulo.entidades.Filme;
import br.com.paulo.entidades.Locacao;
import br.com.paulo.entidades.Usuario;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

	@InjectMocks
	private LocacaoService service;
	private Usuario usuario;
	
	@Mock
	private SPCService spcService;
	@Mock
	private LocacaoDAO locacaoDAO;

	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value = 1)
	public double valorLocacao;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
//		service = new LocacaoService();
//		usuario = umUsuario().agora();
////		LocacaoDAO locacaoDAO = new LocacaoDAOFake(); // Fake object 
//		locacaoDAO = Mockito.mock(LocacaoDAO.class); // Mockito
//		service.setLocacaoDAO(locacaoDAO);
//		spcService = Mockito.mock(SPCService.class);
//		service.setSPCService(spcService);
		System.out.println("iniciando 3");
		CalculadoraTest.ordem.append("3");
	}
	
	@After
	public void teatDown() {
		System.out.println("finalizando 3...");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println(CalculadoraTest.ordem.toString());
	}
	
	private static Filme filme1 = umFilme().agora();
	private static Filme filme2 = umFilme().agora();
	private static Filme filme3 = umFilme().agora();
	private static Filme filme4 = umFilme().agora();
	private static Filme filme5 = umFilme().agora();
	private static Filme filme6 = umFilme().agora();
	
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