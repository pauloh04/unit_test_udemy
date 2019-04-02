package br.com.paulo.servicos;

import static br.com.paulo.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.paulo.dao.LocacaoDAO;
import br.com.paulo.entidades.Filme;
import br.com.paulo.entidades.Locacao;
import br.com.paulo.entidades.Usuario;
import br.com.paulo.utils.DataUtils;

public class LocacaoService {

	private LocacaoDAO locacaoDAO;
	private SPCService spcService;
	private EmailService emailService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {

		for (Filme filme : filmes) {
			if (filme.getEstoque() == 0) {
				throw new Exception("Filme sem estoque");
			}
		}

		if(spcService.possuiNegativacao(usuario))
			throw new Exception("Usuario negativado");
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
//		locacao.setDataLocacao(new Date());
		locacao.setDataLocacao(Calendar.getInstance().getTime());
		locacao.setValor(calcularValorLocacao(filmes));

		// Entrega no dia seguinte
//		Date dataEntrega = new Date();
		Date dataEntrega = Calendar.getInstance().getTime();
		
		dataEntrega = adicionarDias(dataEntrega, 1);
		
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY))
			dataEntrega = adicionarDias(dataEntrega, 1);
		
		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		locacaoDAO.salvar(locacao);

		return locacao;
	}

	private Double calcularValorLocacao(List<Filme> filmes) {
		System.out.println("Calculando...");
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
		return valorLocacao;
	}

	public void notificarAtrasos() {
		List<Locacao> listLocacoes = locacaoDAO.obterLocacoesPendentes();
		
		for (Locacao locacao : listLocacoes) {
			if(locacao.getDataRetorno().before(new Date()))
				emailService.notificarAtraso(locacao.getUsuario());
		}
	}

	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setDataLocacao(new Date());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor() * dias);
		locacaoDAO.salvar(novaLocacao);
	}
	
//	public void setLocacaoDAO(LocacaoDAO locacaoDAO) {
//		this.locacaoDAO = locacaoDAO;
//	}
//	
//	public void setSPCService(SPCService spcService) {
//		this.spcService = spcService;
//	}
//	
//	public void setEmailService(EmailService emailService) {
//		this.emailService = emailService;
//	}
}