package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.LoyaltyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyCardRepository extends JpaRepository<LoyaltyCard, Long> {
    public LoyaltyCard findById(int id);
    public LoyaltyCard findByClient(long clientId);
}
