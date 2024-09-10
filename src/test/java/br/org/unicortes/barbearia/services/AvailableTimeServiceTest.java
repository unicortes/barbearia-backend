package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.dtos.AvailableTimeDTO;
import br.org.unicortes.barbearia.models.AvailableTime;
import br.org.unicortes.barbearia.repositories.AvailableTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailableTimeServiceTest {

    @Mock
    private AvailableTimeRepository availableTimeRepository;

    @InjectMocks
    private AvailableTimeService availableTimeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to convert entity to DTO
    private AvailableTimeDTO toDTO(AvailableTime availableTime) {
        // Implement this based on your DTO conversion logic
        return new AvailableTimeDTO(
                availableTime.getId(),
                availableTime.getBarber().getId(),
                availableTime.getService().getId(),
                availableTime.getTimeStart(),
                availableTime.getTimeEnd(),
                availableTime.isScheduled()
        );
    }

    // Helper method to convert DTO to entity
    private AvailableTime toEntity(AvailableTimeDTO dto) {
        // Implement this based on your DTO conversion logic
        AvailableTime availableTime = new AvailableTime();
        availableTime.setId(dto.getId());
        // Set Barber and Service based on dto values if necessary
        availableTime.setTimeStart(dto.getTimeStart());
        availableTime.setTimeEnd(dto.getTimeEnd());
        availableTime.setScheduled(dto.isScheduled());
        return availableTime;
    }

    @Test
    void getAllAvailableTimes() {
        AvailableTime time1 = new AvailableTime();
        AvailableTime time2 = new AvailableTime();
        when(availableTimeRepository.findAll()).thenReturn(Arrays.asList(time1, time2));

        List<AvailableTimeDTO> result = availableTimeService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(availableTimeRepository, times(1)).findAll();
    }

    @Test
    void create() throws Exception {
        AvailableTimeDTO dto = new AvailableTimeDTO();
        AvailableTime entity = toEntity(dto);
        when(availableTimeRepository.save(entity)).thenReturn(entity);

        AvailableTimeDTO result = availableTimeService.create(dto);

        assertNotNull(result);
        assertEquals(dto, result);
        verify(availableTimeRepository, times(1)).save(entity);
    }

    @Test
    void findByServiceId() {
        Long serviceId = 1L;
        AvailableTime time1 = new AvailableTime();
        AvailableTime time2 = new AvailableTime();
        when(availableTimeRepository.findByServiceId(serviceId)).thenReturn(Arrays.asList(time1, time2));

        List<AvailableTimeDTO> result = availableTimeService.findByServiceId(serviceId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(availableTimeRepository, times(1)).findByServiceId(serviceId);
    }

    @Test
    void findByIsScheduledFalse() {
        AvailableTime time1 = new AvailableTime();
        AvailableTime time2 = new AvailableTime();
        when(availableTimeRepository.findByIsScheduledFalse()).thenReturn(Arrays.asList(time1, time2));

        List<AvailableTimeDTO> result = availableTimeService.findByIsScheduledFalse();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(availableTimeRepository, times(1)).findByIsScheduledFalse();
    }

    @Test
    void deleteTime() {
        Long id = 1L;
        doNothing().when(availableTimeRepository).deleteById(id);

        availableTimeService.deleteTime(id);

        verify(availableTimeRepository, times(1)).deleteById(id);
    }
}
