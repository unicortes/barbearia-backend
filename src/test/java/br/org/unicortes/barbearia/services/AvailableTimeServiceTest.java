package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.dtos.AvailableTimeDTO;
import br.org.unicortes.barbearia.exceptions.ResourceNotFoundException;
import br.org.unicortes.barbearia.models.AvailableTime;
import br.org.unicortes.barbearia.models.Barber;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.AvailableTimeRepository;
import br.org.unicortes.barbearia.repositories.BarberRepository;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailableTimeServiceTest {

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private AvailableTimeRepository availableTimeRepository;

    @InjectMocks
    private AvailableTimeService availableTimeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        Barber barber = new Barber();
        barber.setId(1L);

        Servico servico = new Servico();
        servico.setId(1L);

        AvailableTime availableTime = new AvailableTime();
        availableTime.setId(1L);
        availableTime.setBarber(barber);
        availableTime.setService(servico);

        AvailableTimeDTO dto = AvailableTimeDTO.builder()
                .id(1L)
                .barber(1L)
                .service(1L)
                .build();

        when(availableTimeRepository.findAll()).thenReturn(Arrays.asList(availableTime));

        List<AvailableTimeDTO> result = availableTimeService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(1L, result.get(0).getBarber());
        assertEquals(1L, result.get(0).getService());
    }

    @Test
    void testGetAllEmptyList() {
        when(availableTimeRepository.findAll()).thenReturn(Collections.emptyList());

        List<AvailableTimeDTO> result = availableTimeService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "A lista deve estar vazia");
    }


    @Test
    void testFindByServiceId() {
        Barber barber = new Barber();
        barber.setId(1L);

        Servico service = new Servico();
        service.setId(1L);

        AvailableTime availableTime = AvailableTime.builder()
                .id(1L)
                .barber(barber)
                .service(service)
                .timeStart(new Date())
                .timeEnd(new Date())
                .isScheduled(false)
                .build();

        AvailableTimeDTO dto = AvailableTimeDTO.builder()
                .id(1L)
                .barber(1L)
                .service(1L)
                .timeStart(availableTime.getTimeStart())
                .timeEnd(availableTime.getTimeEnd())
                .isScheduled(availableTime.isScheduled())
                .build();

        when(availableTimeRepository.findByServiceId(1L)).thenReturn(Arrays.asList(availableTime));

        AvailableTimeService spyService = spy(availableTimeService);
        when(spyService.convertToDTO(availableTime)).thenReturn(dto);

        List<AvailableTimeDTO> result = spyService.findByServiceId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        AvailableTimeDTO resultDto = result.get(0);
        assertEquals(1L, resultDto.getId());
        assertEquals(1L, resultDto.getBarber());
        assertEquals(1L, resultDto.getService());
        assertEquals(availableTime.getTimeStart(), resultDto.getTimeStart());
        assertEquals(availableTime.getTimeEnd(), resultDto.getTimeEnd());
        assertEquals(availableTime.isScheduled(), resultDto.isScheduled());

        verify(availableTimeRepository).findByServiceId(1L);
    }

    @Test
    void testDeleteTime() {
        doNothing().when(availableTimeRepository).deleteById(1L);

        availableTimeService.deleteTime(1L);
        verify(availableTimeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTimeNotFound() {
        when(availableTimeRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException thrownException = assertThrows(ResourceNotFoundException.class, () -> {
            availableTimeService.deleteTime(1L);
        });

        assertEquals("Barbeiro com o id '1' não encontrado.", thrownException.getMessage());

        verify(availableTimeRepository, times(1)).existsById(1L);

        verify(availableTimeRepository, never()).deleteById(1L);
    }

}
