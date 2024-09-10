package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarberRepository extends JpaRepository<Barber, Long> {
    boolean existsByName(String name);

    Barber findByName(String name);
    Barber findByUsuarioId(Long id);
    void deleteByUsuarioId(Long id);
}
