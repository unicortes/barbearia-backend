package br.org.unicortes.barbearia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.unicortes.barbearia.model.Promocao;

@Repository
public interface PromocaoRepository extends JpaRepository<Promocao, Long>{

    
} 