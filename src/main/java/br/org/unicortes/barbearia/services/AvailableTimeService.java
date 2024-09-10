package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.dtos.AvailableTimeDTO;
import br.org.unicortes.barbearia.exceptions.ServicoNotFoundException;
import br.org.unicortes.barbearia.models.AvailableTime;
import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.AvailableTimeRepository;
import br.org.unicortes.barbearia.repositories.BarberRepository;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailableTimeService {
    @Autowired
    private AvailableTimeRepository availableTimeRepository;

    @Autowired
    private BarberRepository barberRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    public List<AvailableTimeDTO> getAll() {
        return availableTimeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AvailableTimeDTO create(AvailableTimeDTO availableTimeDTO) throws ServicoNotFoundException {
        AvailableTime availableTime = convertToEntity(availableTimeDTO);
        AvailableTime savedAvailableTime = availableTimeRepository.save(availableTime);
        return convertToDTO(savedAvailableTime);
    }


    public List<AvailableTimeDTO> findByServiceId(Long serviceId) {
        return availableTimeRepository.findByServiceId(serviceId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AvailableTimeDTO> findByIsScheduledFalse() {
        return availableTimeRepository.findByIsScheduledFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteTime(Long id) {
        availableTimeRepository.deleteById(id);
    }

    private AvailableTime convertToEntity(AvailableTimeDTO dto) throws ServicoNotFoundException {
        AvailableTime availableTime = new AvailableTime();
        availableTime.setId(dto.getId());

        Barber barber = barberRepository.findById(dto.getBarber())
                .orElseThrow(() -> new ServicoNotFoundException("Barber not found"));
        Servico service = servicoRepository.findById(dto.getService())
                .orElseThrow(() -> new ServicoNotFoundException("Service not found"));

        availableTime.setBarber(barber);
        availableTime.setService(service);
        availableTime.setTimeStart(dto.getTimeStart());
        availableTime.setTimeEnd(dto.getTimeEnd());
        availableTime.setScheduled(dto.isScheduled());

        return availableTime;
    }

    private AvailableTimeDTO convertToDTO(AvailableTime availableTime) {
        return AvailableTimeDTO.builder()
                .id(availableTime.getId())
                .barber(availableTime.getBarber().getId())
                .service(availableTime.getService().getId())
                .timeStart(availableTime.getTimeStart())
                .timeEnd(availableTime.getTimeEnd())
                .isScheduled(availableTime.isScheduled())
                .build();
    }
}
