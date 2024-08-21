package br.org.unicortes.barbearia.controllers;


import br.org.unicortes.barbearia.dtos.ServicoDTO;
import br.org.unicortes.barbearia.exceptions.ServicoNotFoundException;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.services.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/servicos")
public class ServicoController {
    @Autowired
    private ServicoService serviceServico;

    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO>getServicoById(@PathVariable Long id){
        ServicoDTO servicoDTO=new ServicoDTO();

        try{
            Servico servicoRet=serviceServico.getServico(id);
            servicoDTO=servicoGetTo(servicoRet);
            return ResponseEntity.ok(servicoDTO);
        }catch (ServicoNotFoundException e){
            e.getStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND) .body(null);
        }

    }
    @GetMapping
    public ResponseEntity<List<ServicoDTO>>getAllServicos(){

        List<ServicoDTO>servicoDTOS=new ArrayList<>();
        List<Servico>servicos=serviceServico.getAllServico();

        if(servicos!=null && !servicos.isEmpty()){
            servicoDTOS=servicos.stream()
                    .map(this::servicoGetTo)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(servicoDTOS);
    }

    @PostMapping
    public ResponseEntity<ServicoDTO>createServico(@RequestBody ServicoDTO servicoDTO){
        Servico servico=getEntity(servicoDTO);

        try{

            Servico ret = serviceServico.createServico(servico);
            ServicoDTO servicoSalvo = servicoGetTo(ret);

            return ResponseEntity.ok(servicoSalvo);

        }catch (Exception e){
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }
    @PutMapping("/{id}")
    private ResponseEntity<ServicoDTO>updateServico(@PathVariable Long id, @RequestBody ServicoDTO servicoDTO){

        try{
            Servico servico=getEntity(servicoDTO);
            Servico servicoRet=serviceServico.updateServico(id,servico);
            ServicoDTO ret=servicoGetTo(servicoRet);
            return ResponseEntity.ok(ret);
        }catch (ServicoNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        try{
            serviceServico.deleteServico(id);
            return ResponseEntity.noContent().build();
        }catch (ServicoNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }
    private ServicoDTO servicoGetTo(Servico servico){
        ServicoDTO servicoDTO=new ServicoDTO();
        servicoDTO.setId(servico.getServicoId());
        servicoDTO.setName(servico.getName());
        servicoDTO.setDescription(servico.getDescription());
        servicoDTO.setPrice(servico.getPrice());
        return servicoDTO;
    }

    private Servico getEntity(ServicoDTO servicoDTO){
        Servico servicoNew=new Servico();

        if(servicoDTO.getId()!=null){
            servicoNew.setServicoId(servicoDTO.getId());
        }
        servicoNew.setName(servicoDTO.getName());
        servicoNew.setDescription(servicoDTO.getDescription());
        servicoNew.setPrice(servicoDTO.getPrice());
        return servicoNew;
    }
}
