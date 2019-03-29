package br.com.paulo.servicos;

import br.com.paulo.exception.NaoPodeDividirPorZeroException;

public class Calculadora {

	public double somar(double a, double b) {
		return a+b;
	}

	public double subtrair(double a, double b) {
		return a-b;
	}

	public double dividir(double a, double b) throws NaoPodeDividirPorZeroException {
		if(b == 0)
			throw new NaoPodeDividirPorZeroException();
		
		return a/b;
	}

	public double multiplicar(double a, double b) {
		return a*b;
	}

}
