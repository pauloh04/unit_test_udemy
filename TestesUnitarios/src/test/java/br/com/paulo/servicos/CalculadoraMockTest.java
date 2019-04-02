package br.com.paulo.servicos;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalculadoraMockTest {
	
	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void devoMostrarDiferencaEntreMockSpy() {
		when(calcMock.somar(1, 2)).thenReturn(8.0);
//		System.out.println("Mock " + calcMock.somar(1, 5));
		
		when(calcSpy.somar(1, 2)).thenReturn(8.0);
//		System.out.println("Spy " + calcSpy.somar(1, 5));
	}
	
	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Double> argumentCaptor = ArgumentCaptor.forClass(Double.class);
		
		when(calc.somar(argumentCaptor.capture(), anyDouble())).thenReturn(5.0);
		
	}
}
