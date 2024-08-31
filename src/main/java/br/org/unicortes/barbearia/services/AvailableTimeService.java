package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.AvailableTime;
import br.org.unicortes.barbearia.repositories.AvailableTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailableTimeService {
    @Autowired
    private AvailableTimeRepository availableTimeRepository;

    public List<AvailableTime> getAll() {
        return availableTimeRepository.findAll();
    }

    public AvailableTime create(AvailableTime availableTime) {
        return availableTimeRepository.save(availableTime);
    }

    public List<AvailableTime> findByServiceId(Long serviceId) {
        return availableTimeRepository.findByServiceId(serviceId);
    }

    public List<AvailableTime> findByIsScheduledFalse() {
        return availableTimeRepository.findByIsScheduledFalse();
    }

    public void deleteTime(Long id) {
        availableTimeRepository.deleteById(id);
    }
}
