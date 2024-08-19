package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.Promocao;
import br.org.unicortes.barbearia.services.PromocaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PromocaoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private PromocaoService promocaoService;

    @InjectMocks
    private PromocaoController promocaoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPromocao() throws Exception {
        Promocao promocao1 = new Promocao();
        promocao1.setId(1L);
        promocao1.setTitulo("Promoção 1");

        Promocao promocao2 = new Promocao();
        promocao2.setId(2L);
        promocao2.setTitulo("Promoção 2");

        when(promocaoService.getAllPromocao()).thenReturn(Arrays.asList(promocao1, promocao2));

        mockMvc.perform(get("/api/promocoes/index"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].titulo").value("Promoção 1"))
                .andExpect(jsonPath("$[1].titulo").value("Promoção 2"));
    }

    @Test
    public void testGetPromocaoById() throws Exception {
        Long id = 1L;
        Promocao promocao = new Promocao();
        promocao.setId(id);
        promocao.setTitulo("Promoção Encontrada");

        when(promocaoService.getPromocaoById(id)).thenReturn(Optional.of(promocao));

        mockMvc.perform(get("/api/promocoes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo").value("Promoção Encontrada"));
    }

    @Test
    public void testCriarPromocao() throws Exception {
        Promocao promocao = new Promocao();
        promocao.setId(1L);
        promocao.setTitulo("Nova Promoção");

        when(promocaoService.savePromocao(any(Promocao.class))).thenReturn(promocao);

        mockMvc.perform(post("/api/promocoes/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promocao)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo").value("Nova Promoção"));
    }

    @Test
    public void testEditarPromocao() throws Exception {
        Long id = 1L;
        Promocao promocao = new Promocao();
        promocao.setId(id);
        promocao.setTitulo("Promoção Atualizada");

        when(promocaoService.updatePromocao(anyLong(), any(Promocao.class))).thenReturn(promocao);

        mockMvc.perform(patch("/api/promocoes/editar/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(promocao)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo").value("Promoção Atualizada"));
    }

    @Test
    public void testDeletarPromocao() throws Exception {
        Long id = 1L;

        doNothing().when(promocaoService).deletePromocao(id);

        mockMvc.perform(delete("/api/promocoes/{id}", id))
                .andExpect(status().isNoContent());
    }
}
