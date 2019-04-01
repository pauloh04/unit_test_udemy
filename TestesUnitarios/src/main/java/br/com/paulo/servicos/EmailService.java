package br.com.paulo.servicos;

import br.com.paulo.entidades.Usuario;

public interface EmailService {

	public void notificarAtraso(Usuario usuario);
}
