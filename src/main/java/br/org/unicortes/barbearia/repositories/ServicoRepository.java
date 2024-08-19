package br.org.unicortes.barbearia.repositories;


import br.org.unicortes.barbearia.models.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {


    Optional<Servico>findByName(String name);

    Servico findByServicoId(Long id);

}
