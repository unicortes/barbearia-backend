package br.org.unicortes.barbearia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.org.unicortes.barbearia.models.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    boolean existsByName(String name);
    Promotion findByName(String nome);
} 