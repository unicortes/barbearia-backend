package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
    Usuario findByName(String name);


}
