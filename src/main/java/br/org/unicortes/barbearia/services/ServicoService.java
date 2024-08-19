package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.ServicoNotFoundException;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;


    public Servico createServico(Servico servico) throws Exception {
        Servico servicoNew=new Servico();
        try {
            servicoNew = servicoRepository.save(servico);
        }catch (Exception  e){
            throw new Exception();
        }
        return servicoNew;
    }

    public List<Servico> getAllServico(){
        List<Servico> servicos=servicoRepository.findAll();
        return servicos;
    }

    public Servico getServico(Long id) throws ServicoNotFoundException {

        Servico servico=servicoRepository.findById(id)
                .orElseThrow(() -> new ServicoNotFoundException("Serviço não encontrado para o ID: " + id));

        return servico;
    }
    public Servico updateServico(Long id ,Servico servicoAtual) throws Exception {
        Servico servicoAtualizado=new Servico();
        Servico servicoAntigo=new Servico();

        try {
            servicoAntigo = getServico(id);

            if(!servicoAtual.getName().trim().equalsIgnoreCase(servicoAntigo.getName().trim())){
                servicoAntigo.setName(servicoAtual.getName());
            }
            if(!servicoAtual.getDescription().trim().equalsIgnoreCase(servicoAntigo.getDescription().trim())){
                servicoAntigo.setDescription(servicoAtual.getDescription());
            }
            if(servicoAtual.getPrice()!=servicoAntigo.getPrice()){
                servicoAntigo.setPrice(servicoAtual.getPrice());
            }

            servicoAtualizado=servicoRepository.save(servicoAntigo);

        }catch (ServicoNotFoundException e){
            throw  new ServicoNotFoundException();
        }catch (Exception e){
            throw new Exception();
        }



        return servicoAtualizado;
    }

    public void deleteServico(Long id) throws ServicoNotFoundException, Exception{

        try{
            Servico servico = getServico(id);
            servicoRepository.delete(servico);
        }catch (ServicoNotFoundException e){
            throw new ServicoNotFoundException();
        }catch (Exception e){
            throw new Exception();
        }
    }
}
