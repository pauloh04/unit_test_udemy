package br.com.paulo.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {

	private static int contador = 0;
	
	@Test
	public void inicio() {
		contador = 1;
	}

	@Test
	public void verifica() {
		assertThat(1, is(contador));
	}
}
