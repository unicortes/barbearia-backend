package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarberRepository extends JpaRepository<Barber, Long> {
    boolean existsByName(String name);
}
