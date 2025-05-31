package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.HorarioLivre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HorarioLivreRepository extends JpaRepository<HorarioLivre, Long> {
    @Query("SELECT h FROM HorarioLivre h WHERE h.barbeiro.id = :barbeiroId " +
            "AND h.id <> :idParaIgnorar " +
            "AND (" +
            "   (h.inicio < :fim AND h.fim > :inicio)" +
            ")")
    List<HorarioLivre> findConflictingSchedules(@Param("barbeiroId") Long barbeiroId,
                                                @Param("inicio") LocalDateTime inicio,
                                                @Param("fim") LocalDateTime fim,
                                                @Param("idParaIgnorar") Long idParaIgnorar);
}
