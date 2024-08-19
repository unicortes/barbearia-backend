package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Product;
import br.org.unicortes.barbearia.exceptions.ProductAlreadyExistsException;
import br.org.unicortes.barbearia.exceptions.ProductNotFoundException;
import br.org.unicortes.barbearia.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BarbeiroService {
    @Autowired
    private BarbeiroRepository barbeiroRepository;

    public Barbeiro registrarBarbeiro(Barbeiro barbeiro) {
        return barbeiroRepository.save(barbeiro);
    }

    public Barbeiro editarBarbeiro(Long id, Barbeiro barbeiroAtualizado) {
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
        bar
    }

    public void removerBarbeiro(Long id) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Barbeiro não encontrado com ID: " + id));
        barbeiroRepository.delete(barbeiro);
    }

    public List<Barbeiro> listarTodosBarbeiros() {
        return barbeiroRepository.findAll();
    }
}
