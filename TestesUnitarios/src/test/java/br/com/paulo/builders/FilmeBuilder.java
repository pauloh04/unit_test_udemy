package br.com.paulo.builders;

import br.com.paulo.entidades.Filme;

public class FilmeBuilder {

	private Filme filme;

	private FilmeBuilder() {
	}

	public static FilmeBuilder umFilme() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setNome("Filme tal");
		builder.filme.setEstoque(2);
		builder.filme.setPrecoLocacao(4.0);
		return builder;
	}
	
	public FilmeBuilder preco5() {
		filme.setPrecoLocacao(5.0);
		return this;
	}
	
	public FilmeBuilder semEstoque() {
		filme.setEstoque(0);
		return this;
	}
	
	public Filme agora() {
		return filme;
	}
}
