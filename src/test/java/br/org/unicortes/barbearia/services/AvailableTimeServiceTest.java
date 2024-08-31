package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.AvailableTime;
import br.org.unicortes.barbearia.repositories.AvailableTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

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

    @Test
    void getAllAvailableTimes() {
        AvailableTime time1 = new AvailableTime();
        AvailableTime time2 = new AvailableTime();
        when(availableTimeRepository.findAll()).thenReturn(Arrays.asList(time1, time2));

        List<AvailableTime> result = availableTimeService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(availableTimeRepository, times(1)).findAll();
    }

    @Test
    void create() {
        AvailableTime time = new AvailableTime();
        when(availableTimeRepository.save(time)).thenReturn(time);

        AvailableTime result = availableTimeService.create(time);

        assertNotNull(result);
        assertEquals(time, result);
        verify(availableTimeRepository, times(1)).save(time);
    }

    @Test
    void findByServiceId() {
        Long serviceId = 1L;
        AvailableTime time1 = new AvailableTime();
        AvailableTime time2 = new AvailableTime();
        when(availableTimeRepository.findByServiceId(serviceId)).thenReturn(Arrays.asList(time1, time2));

        List<AvailableTime> result = availableTimeService.findByServiceId(serviceId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(availableTimeRepository, times(1)).findByServiceId(serviceId);
    }

    @Test
    void findByIsScheduledFalse() {
        AvailableTime time1 = new AvailableTime();
        AvailableTime time2 = new AvailableTime();
        when(availableTimeRepository.findByIsScheduledFalse()).thenReturn(Arrays.asList(time1, time2));

        List<AvailableTime> result = availableTimeService.findByIsScheduledFalse();

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
