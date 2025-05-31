package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.Estoque;
import br.org.unicortes.barbearia.models.Produto;
import br.org.unicortes.barbearia.enums.EstoqueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Estoque e " +
            "WHERE e.produto = :produto " +
            "AND e.status = :status " +
            "AND (:idIgnorado < 0 OR e.id <> :idIgnorado)")
    boolean existsByProdutoAndStatusAndIdNot(@Param("produto") Produto produto, @Param("status") EstoqueStatus status, @Param("idIgnorado") Long idIgnorado);
}
