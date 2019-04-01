package br.com.paulo.servicos;

import static br.com.paulo.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.paulo.entidades.Filme;
import br.com.paulo.entidades.Locacao;
import br.com.paulo.entidades.Usuario;
import br.com.paulo.utils.DataUtils;
import buildermaster.BuilderMaster;

public class LocacaoService {

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {

		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new Exception("Filme sem estoque");
			}
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());

		Double valorLocacao = 0d;

		int count = 0;
		for (Filme filme : filmes) {
			
			double valorFilme = filme.getPrecoLocacao();
			
			if(count == 2)
				valorFilme = filme.getPrecoLocacao() * 0.75;
			if(count == 3)
				valorFilme = filme.getPrecoLocacao() * 0.50;
			if(count == 4)
				valorFilme = filme.getPrecoLocacao() * 0.25;
			if(count == 5)
				valorFilme = 0;
			
			valorLocacao += valorFilme;
			
			count++;
		}

		locacao.setValor(valorLocacao);

		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY))
			dataEntrega = adicionarDias(dataEntrega, 1);
		
		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		// TODO adicionar método para salvar

		return locacao;
	}
	
	public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
	}
}