package br.org.unicortes.barbearia.repositories;

import br.org.unicortes.barbearia.models.AvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailableTimeRepository extends JpaRepository<AvailableTime, Long> {
    List<AvailableTime> findByServiceId(Long serviceId);

    List<AvailableTime> findByIsScheduledFalse();
}
