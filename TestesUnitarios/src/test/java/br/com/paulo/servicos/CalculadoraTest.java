package br.com.paulo.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import br.com.paulo.exception.NaoPodeDividirPorZeroException;

public class CalculadoraTest {

	public static StringBuffer ordem = new StringBuffer();
	
	private Calculadora calc;

	@Before
	public void setup() {
		calc = new Calculadora();
		System.out.println("iniciando...");
		ordem.append("1");
	}

	@After
	public void teatDown() {
		System.out.println("finalizando...");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println(ordem.toString());
	}
	
	@Test
	public void deveSomarDoisValores() {
		double a = 5d;
		double b = 6d;
		double resultado = calc.somar(a, b);
		assertThat(resultado, is(11d));
	}

	@Test
	public void deveSubtrairDoisValores() {
		double a = 7d;
		double b = 5d;
		double resultado = calc.subtrair(a, b);
		assertThat(resultado, is(2d));
	}

	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		double a = 10d;
		double b = 2d;
		double resultado = calc.dividir(a, b);
		assertThat(resultado, is(5d));
	}

	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		double a = 2d;
		double b = 0d;
		double resultado = calc.dividir(a, b);
		assertThat(resultado, is(5d));
	}

	@Test
	public void deveMultiplicarDoisValores() {
		double a = 2d;
		double b = 5d;
		double resultado = calc.multiplicar(a, b);
		assertThat(resultado, is(10d));
	}
}