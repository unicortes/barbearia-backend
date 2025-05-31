package br.org.unicortes.barbearia.services.interfaces;

import br.org.unicortes.barbearia.models.Usuario;
import java.util.List;

public interface IUsuarioService {

    List<Usuario> listarTodos();
    Usuario buscarPorId(Long id);
    Usuario criar(Usuario novoUsuario);
    Usuario atualizar(Long id, Usuario usuarioAtualizado);
    void remover(Long id);

    Usuario buscarPorEmail(String email);
    void alterarSenha(Long id, String novaSenha);

    boolean verificarDisponibilidadeEmail(String email);
    void ativarUsuario(Long id);
    void desativarUsuario(Long id);
}