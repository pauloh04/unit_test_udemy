package br.com.paulo.servicos;

import static br.com.paulo.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import br.com.paulo.entidades.Filme;
import br.com.paulo.entidades.Locacao;
import br.com.paulo.entidades.Usuario;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {

		for (Filme filme : filmes) {
			if(filme.getEstoque() == 0) {
				throw new Exception("Filme sem estoque");
			}
		}

		
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		
		Double valor = 0d;
		for (Filme filme : filmes) {
			valor += filme.getPrecoLocacao();
		}
		locacao.setValor(valor);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}
}