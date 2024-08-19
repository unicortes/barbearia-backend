package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.SaleForLoyaltyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleForLoyaltyCardRepository extends JpaRepository<SaleForLoyaltyCard, Long> {
    SaleForLoyaltyCardRepository findById(long id);
}
