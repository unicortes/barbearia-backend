package br.org.unicortes.barbearia.services;


import br.org.unicortes.barbearia.exceptions.ServicoNotFoundException;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ServicoServiceTest {

    @Mock
    private ServicoRepository servicoRepository;

    @InjectMocks
    private ServicoService servicoService;

    private Servico servico;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        servico=new Servico();
        servico.setServicoId(3L);
        servico.setName("Sobrancelha");
        servico.setDescription("serviço de fazer sobrancelha");
        servico.setPrice(10.0);
    }

    @Test
    void testCreateServico() throws Exception {

        when(servicoRepository.save(servico)).thenReturn(servico);

        Servico servicoSalvo=servicoService.createServico(servico);
        assertNotNull(servicoSalvo);
        assertEquals("Sobrancelha",servicoSalvo.getName());
        assertEquals(3L,servicoSalvo.getServicoId());
    }

    @Test
    void testUpdateServico() throws Exception {
        Servico servico = new Servico();
        servico.setServicoId(4L);
        servico.setName("Sobrancelha");
        servico.setDescription("sobrancelha masculina");
        servico.setPrice(10.0);

        Servico servicoAtualizado = new Servico();
        servicoAtualizado.setPrice(15.0);
        servicoAtualizado.setName("Corte de cabelo");
        servicoAtualizado.setDescription(servico.getDescription());

        when(servicoRepository.findById(4L)).thenReturn(Optional.of(servico));
        when(servicoRepository.save(servico)).thenReturn(servico);

        Long id = servico.getServicoId();


        Servico servicoAtual = servicoService.updateServico(id, servicoAtualizado);

        assertNotNull(servicoAtual);
        assertEquals("Corte de cabelo", servicoAtual.getName());
        assertEquals(4L, servicoAtual.getServicoId());
        assertEquals(15.0, servicoAtual.getPrice());

    }

    @Test
    void testGetServico() throws ServicoNotFoundException {
        when(servicoRepository.findById(3L)).thenReturn(Optional.of(servico));

        Servico findId=servicoService.getServico(3L);
        assertNotNull(findId);
        assertEquals(3L, findId.getServicoId());
    }

    @Test
    void testGetAll() throws Exception {
        Servico servico2 = new Servico();
        servico2.setServicoId(4L);
        servico2.setName("Sobrancelha");
        servico2.setDescription("sobrancelha masculina");
        servico2.setPrice(10.0);

        List<Servico> servicoList=new ArrayList<>();
        servicoList.add(servico2);
        servicoList.add(servico);

        when(servicoRepository.findAll()).thenReturn(servicoList);

        List<Servico>servicos=servicoService.getAllServico();
        assertEquals(2, servicos.size());
        assertEquals("Sobrancelha", servicos.get(0).getName());

    }

    @Test
    void findByIdNotFound(){
        when(servicoRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(ServicoNotFoundException.class, () -> servicoService.getServico(1L));
    }
}
