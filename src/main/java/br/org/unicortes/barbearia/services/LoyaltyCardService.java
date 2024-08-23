package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.dtos.LoyaltyCardDTO;
import br.org.unicortes.barbearia.exceptions.ResourceNotFoundException;
import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.models.LoyaltyCard;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.ClientRepository;
import br.org.unicortes.barbearia.repositories.LoyaltyCardRepository;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoyaltyCardService {

    @Autowired
    private LoyaltyCardRepository loyaltyCardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    public List<LoyaltyCardDTO> findAll() {
        return loyaltyCardRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LoyaltyCardDTO findById(Long id) {
        LoyaltyCard loyaltyCard = loyaltyCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return convertToDTO(loyaltyCard);
    }

    public LoyaltyCardDTO create(LoyaltyCardDTO loyaltyCardDTO) {
        Client client = clientRepository.findById(loyaltyCardDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(loyaltyCardDTO.getClientId()));

        Servico service = servicoRepository.findById(loyaltyCardDTO.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException(loyaltyCardDTO.getServiceId()));

        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setClientId(client);
        loyaltyCard.setDateAdmission(loyaltyCardDTO.getDataAdmission());
        loyaltyCard.setService(service);
        loyaltyCard.setPoints(loyaltyCardDTO.getPoints());

        LoyaltyCard savedLoyaltyCard = loyaltyCardRepository.save(loyaltyCard);
        return convertToDTO(savedLoyaltyCard);
    }

    public LoyaltyCardDTO update(Long id, LoyaltyCardDTO loyaltyCardDTO) {
        LoyaltyCard existingLoyaltyCard = loyaltyCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        Client client = clientRepository.findById(loyaltyCardDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(loyaltyCardDTO.getClientId()));

        Servico service = servicoRepository.findById(loyaltyCardDTO.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException(loyaltyCardDTO.getServiceId()));

        existingLoyaltyCard.setClientId(client);
        existingLoyaltyCard.setDateAdmission(loyaltyCardDTO.getDataAdmission());
        existingLoyaltyCard.setService(service);
        existingLoyaltyCard.setPoints(loyaltyCardDTO.getPoints());

        LoyaltyCard updatedLoyaltyCard = loyaltyCardRepository.save(existingLoyaltyCard);
        return convertToDTO(updatedLoyaltyCard);
    }

    public void delete(Long id) {
        LoyaltyCard loyaltyCard = loyaltyCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        loyaltyCardRepository.delete(loyaltyCard);
    }

    private LoyaltyCardDTO convertToDTO(LoyaltyCard loyaltyCard) {
        return new LoyaltyCardDTO(
                loyaltyCard.getId(),
                loyaltyCard.getClientId().getId(),
                loyaltyCard.getDateAdmission(),
                loyaltyCard.getService().getServicoId(),
                loyaltyCard.getPoints()
        );
    }
}

