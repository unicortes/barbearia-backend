package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.ConflitoException;
import br.org.unicortes.barbearia.exceptions.EntidadeNaoEncontradaException;
import br.org.unicortes.barbearia.models.Barbeiro;
import br.org.unicortes.barbearia.repositories.BarbeiroRepository;
import br.org.unicortes.barbearia.services.interfaces.IBarbeiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarbeiroService implements IBarbeiroService {

    private final BarbeiroRepository barbeiroRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Barbeiro> listarTodos() {
        return barbeiroRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Barbeiro buscarPorId(Long id) {
        return barbeiroRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Barbeiro não encontrado com id: " + id));
    }

    @Override
    @Transactional
    public Barbeiro criar(Barbeiro novoBarbeiro) {
        validarConflitos(novoBarbeiro, null);
        return barbeiroRepository.save(novoBarbeiro);
    }

    @Override
    @Transactional
    public Barbeiro atualizar(Long id, Barbeiro barbeiroAtualizado) {
        Barbeiro barbeiroExistente = buscarPorId(id);
        validarConflitos(barbeiroAtualizado, id);
        atualizarDadosBarbeiro(barbeiroExistente, barbeiroAtualizado);
        return barbeiroRepository.save(barbeiroExistente);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        if (!barbeiroRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Barbeiro não encontrado com id: " + id);
        }
        barbeiroRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void ativarBarbeiro(Long id) {
        Barbeiro barbeiro = buscarPorId(id);
        barbeiro.setAtivo(true);
        barbeiroRepository.save(barbeiro);
    }

    @Override
    @Transactional
    public void desativarBarbeiro(Long id) {
        Barbeiro barbeiro = buscarPorId(id);
        barbeiro.setAtivo(false);
        barbeiroRepository.save(barbeiro);
    }

    private void validarConflitos(Barbeiro barbeiro, Long idIgnorado) {
        if (this.emailEmUso(barbeiro.getEmail(), idIgnorado)) {
            throw new ConflitoException("Email já está em uso por outro barbeiro");
        }

        if (this.telefoneEmUso(barbeiro.getTelefone(), idIgnorado)) {
            throw new ConflitoException("Telefone já está em uso por outro barbeiro");
        }

        if (this.cpfEmUso(barbeiro.getCpf(), idIgnorado)) {
            throw new ConflitoException("CPF já está em uso por outro barbeiro");
        }
    }

    private boolean telefoneEmUso(String telefone, Long idIgnorado) {
        if (idIgnorado == null) {
            return barbeiroRepository.existsByTelefone(telefone);
        }
        return barbeiroRepository.existsByTelefoneAndIdNot(telefone, idIgnorado);
    }

    private boolean emailEmUso(String email, Long idIgnorado) {
        if (idIgnorado == null) {
            return barbeiroRepository.existsByEmail(email);
        }
        return barbeiroRepository.existsByEmailAndIdNot(email, idIgnorado);
    }

    private boolean cpfEmUso(String cpf, Long idIgnorado) {
        if (idIgnorado == null) {
            return barbeiroRepository.existsByCpf(cpf);
        }
        return barbeiroRepository.existsByCpfAndIdNot(cpf, idIgnorado);
    }

    private void atualizarDadosBarbeiro(Barbeiro existente, Barbeiro atualizado) {
        if (atualizado.getName() != null) {
            existente.setName(atualizado.getName());
        }
        if (atualizado.getEmail() != null) {
            existente.setEmail(atualizado.getEmail());
        }
        if (atualizado.getTelefone() != null) {
            existente.setTelefone(atualizado.getTelefone());
        }
        if (atualizado.getCpf() != null) {
            existente.setCpf(atualizado.getCpf());
        }
        if (atualizado.getSalario() != null) {
            existente.setSalario(atualizado.getSalario());
        }
        if (atualizado.getEndereco() != null) {
            existente.setEndereco(atualizado.getEndereco());
        }
        if (atualizado.getDataAdmissao() != null) {
            existente.setDataAdmissao(atualizado.getDataAdmissao());
        }
        if (atualizado.getDataDemissao() != null) {
            existente.setDataDemissao(atualizado.getDataDemissao());
        }
        if (atualizado.getEspecialidades() != null) {
            existente.setEspecialidades(atualizado.getEspecialidades());
        }
        if (atualizado.isAtivo() != existente.isAtivo()) {
            existente.setAtivo(atualizado.isAtivo());
        }
    }
}
