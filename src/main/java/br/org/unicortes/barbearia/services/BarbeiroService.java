package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.exceptions.BarbeiroAlreadyExistsException;
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
        if (barbeiroRepository.existsByName(barbeiro.getBarbeiroNome())) {
            throw new BarbeiroAlreadyExistsException(barbeiro.getBarbeiroNome());
        }
        return barbeiroRepository.save(barbeiro);
    }

    public Barbeiro updateBarbeiro(Long id, Barbeiro barbeiroAtualizado) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado com ID: " + id));
        barbeiro.setBarbeiroNome(barbeiroAtualizado.getBarbeiroNome());
        barbeiro.setBarbeiroEmail(barbeiroAtualizado.getBarbeiroEmail());
        barbeiro.setBarbeiroTelefone(barbeiroAtualizado.getBarbeiroTelefone());
        barbeiro.setBarbeiroCpf(barbeiroAtualizado.getBarbeiroCpf());
        barbeiro.setBarbeiroSalario(barbeiroAtualizado.getBarbeiroSalario());
        barbeiro.setBarbeiroEndereco(barbeiroAtualizado.getBarbeiroEndereco());
        barbeiro.setBarbeiroDataDeAdimissao(barbeiroAtualizado.getBarbeiroDataDeAdimissao());
        barbeiro.setBarbeiroHorariosAtendimento(barbeiroAtualizado.getBarbeiroHorariosAtendimento());
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
