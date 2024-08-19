package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByProductName(String productName);

    List<Stock> findByProductId(Long productId);
}
