package br.org.unicortes.barbearia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.unicortes.barbearia.models.Promocao;


@Repository
public interface PromocaoRepository extends JpaRepository<Promocao, Long>{

    
} 