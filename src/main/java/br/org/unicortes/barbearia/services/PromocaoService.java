package br.org.unicortes.barbearia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import br.org.unicortes.barbearia.models.Promocao;
import br.org.unicortes.barbearia.repositories.PromocaoRepository;

@Service
public class PromocaoService{

    @Autowired
	private PromocaoRepository repository;

    @Transactional
    public List<Promocao> getAllPromocao(){
		return repository.findAll();
	}

    @Transactional
    public Optional<Promocao> getPromocaoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Promocao savePromocao(Promocao promocao){
        return repository.save(promocao);
    };

    @Transactional
    public Promocao updatePromocao(Long id, Promocao promocao){
        promocao.setId(id);

        return repository.save(promocao);
    };

    @Transactional
    public void deletePromocao(Long id){
        repository.deleteById(id);
    };

}
