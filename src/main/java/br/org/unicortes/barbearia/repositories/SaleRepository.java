package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.SaleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<SaleModel, Long> {

    SaleModel findBySaleId(int id);
}
