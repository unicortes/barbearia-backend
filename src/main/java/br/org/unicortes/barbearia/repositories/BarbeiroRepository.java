package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {
    boolean existsByName(String name);
}
