package br.com.paulo.builders;

import static br.com.paulo.builders.FilmeBuilder.umFilme;
import static br.com.paulo.builders.UsuarioBuilder.umUsuario;

import java.util.Arrays;
import java.util.Date;

import br.com.paulo.entidades.Filme;
import br.com.paulo.entidades.Locacao;
import br.com.paulo.entidades.Usuario;
import br.com.paulo.utils.DataUtils;


public class LocacaoBuilder {
	private Locacao elemento;
	private LocacaoBuilder(){}

	public static LocacaoBuilder umLocacao() {
		LocacaoBuilder builder = new LocacaoBuilder();
		inicializarDadosPadroes(builder);
		return builder;
	}

	public static void inicializarDadosPadroes(LocacaoBuilder builder) {
		builder.elemento = new Locacao();
		Locacao elemento = builder.elemento;

		
		elemento.setUsuario(umUsuario().agora());
		elemento.setFilmes(Arrays.asList(umFilme().agora()));
		elemento.setDataLocacao(new Date());
		elemento.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
		elemento.setValor(4.0);
	}

	public LocacaoBuilder comUsuario(Usuario param) {
		elemento.setUsuario(param);
		return this;
	}

	public LocacaoBuilder comListaFilmes(Filme... params) {
		elemento.setFilmes(Arrays.asList(params));
		return this;
	}

	public LocacaoBuilder comDataLocacao(Date param) {
		elemento.setDataLocacao(param);
		return this;
	}

	public LocacaoBuilder comDataRetorno(Date param) {
		elemento.setDataRetorno(param);
		return this;
	}

	public LocacaoBuilder comValor(Double param) {
		elemento.setValor(param);
		return this;
	}

	public Locacao agora() {
		return elemento;
	}
}
