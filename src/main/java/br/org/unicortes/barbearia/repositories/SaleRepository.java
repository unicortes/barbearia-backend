package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Sale findBySaleId(int id);
}
