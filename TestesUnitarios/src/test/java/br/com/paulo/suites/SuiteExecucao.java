package br.com.paulo.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.paulo.servicos.CalculadoraTest;
import br.com.paulo.servicos.CalculoValorLocacaoTest;
import br.com.paulo.servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ CalculadoraTest.class, CalculoValorLocacaoTest.class, LocacaoServiceTest.class })
public class SuiteExecucao {

}