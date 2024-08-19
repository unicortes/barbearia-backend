package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.exceptions.ResourceNotFoundException;
import br.org.unicortes.barbearia.repositories.BarbeiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BarbeiroService {
    @Autowired
    private BarbeiroRepository barbeiroRepository;

    public Barbeiro createBarbeiro(Barbeiro barbeiro) {
        return barbeiroRepository.save(barbeiro);
    }

    public Barbeiro updateBarbeiro(Long id, Barbeiro barbeiroAtualizado) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado com ID: " + id));
        barbeiro.setBarbeiroByNome(barbeiroAtualizado.getBarbeiroByNome());
        barbeiro.setBarbeiroByEmail(barbeiroAtualizado.getBarbeiroByEmail());
        barbeiro.setBarbeiroByTelefone(barbeiroAtualizado.getBarbeiroByTelefone());
        barbeiro.setBarbeiroByCpf(barbeiroAtualizado.getBarbeiroByCpf());
        barbeiro.setBarbeiroBySalario(barbeiroAtualizado.getBarbeiroBySalario());
        barbeiro.setBarbeiroByEndereco(barbeiroAtualizado.getBarbeiroByEndereco());
        barbeiro.setBarbeiroByDataAdmissao(barbeiroAtualizado.getBarbeiroByDataAdmissao());
        barbeiro.setBarbeiroByHorariosAtendimento(barbeiroAtualizado.getBarbeiroByHorariosAtendimento());
        return barbeiroRepository.save(barbeiro);
    }

    public void deleteBarbeiro(Long id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado com ID: " + id));
        barbeiroRepository.delete(barbeiro);
    }

    public List<Barbeiro> listarTodosBarbeiros() {
        return barbeiroRepository.findAll();
    }
}
