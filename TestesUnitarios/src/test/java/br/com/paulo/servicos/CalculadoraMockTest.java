package br.com.paulo.servicos;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class CalculadoraMockTest {

	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Double> argumentCaptor = ArgumentCaptor.forClass(Double.class);
		
		when(calc.somar(argumentCaptor.capture(), anyDouble())).thenReturn(5.0);
		
	}
}
