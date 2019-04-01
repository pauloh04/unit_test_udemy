package br.com.paulo.dao;

import java.util.List;

import br.com.paulo.entidades.Locacao;

public interface LocacaoDAO {

	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendentes();
}
