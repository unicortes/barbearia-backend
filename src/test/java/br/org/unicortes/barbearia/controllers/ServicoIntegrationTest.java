package br.org.unicortes.barbearia.controllers;


import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServicoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {servicoRepository.deleteAll();}

    @Test
    public void testCreateServico() throws Exception {

        Servico servico = new Servico();
        servico.setServicoId(1L);
        servico.setName("Corte de cabelo");
        servico.setDescription("Descrição do corte de cabelo");
        servico.setPrice(15.0);

        mockMvc.perform(post("/api/servicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servico)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value("15.0"));
    }

    @Test
    public void testGetAllServicos() throws Exception {

        Servico servico1 = new Servico();
        servico1.setServicoId(1L);
        servico1.setName("Corte de cabelo");
        servico1.setDescription("Descrição do corte de cabelo");
        servico1.setPrice(15.0);

        Servico servico2 = new Servico();
        servico2.setServicoId(2L);
        servico2.setName("Barba");
        servico2.setDescription("Descrição da barba");
        servico2.setPrice(10.0);

        servicoRepository.save(servico1);
        servicoRepository.save(servico2);

        mockMvc.perform(get("/api/servicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value("15.0"))
                .andExpect(jsonPath("$[1].price").value("10.0"));


    }

    @Test
    public void testGetServico() throws Exception {

        Servico servico1 = new Servico();
        servico1.setServicoId(1L);
        servico1.setName("Corte de cabelo");
        servico1.setDescription("Descrição do corte de cabelo");
        servico1.setPrice(15.0);

        Servico ret= servicoRepository.save(servico1);

        mockMvc.perform(get("/api/servicos/{id}",ret.getServicoId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value("15.0"));


    }

    @Test
    void testUpdateService() throws Exception {
        Servico servico1 = new Servico();
        servico1.setServicoId(1L);
        servico1.setName("Corte de cabelo");
        servico1.setDescription("Descrição do corte de cabelo");
        servico1.setPrice(15.0);

        Servico servico= servicoRepository.save(servico1);

        Servico servicoAtual=new Servico();
        servicoAtual.setDescription("corte de barba");
        servicoAtual.setName("barba");
        servicoAtual.setPrice(14.0);

        mockMvc.perform(put("/api/servicos/{id}", servico.getServicoId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicoAtual)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("barba"))
                .andExpect(jsonPath("$.description").value("corte de barba"))
                .andExpect(jsonPath("$.price").value("14.0"));
    }

    @Test
    void testDeleteServico() throws Exception {

        Servico servico1 = new Servico();
        servico1.setServicoId(1L);
        servico1.setName("Corte de cabelo");
        servico1.setDescription("Descrição do corte de cabelo");
        servico1.setPrice(15.0);

        Servico servico= servicoRepository.save(servico1);

        mockMvc.perform(delete("/api/servicos/{id}", servico.getServicoId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/servicos/{id}", servico.getServicoId()))
                .andExpect(status().isNotFound());
    }


}
