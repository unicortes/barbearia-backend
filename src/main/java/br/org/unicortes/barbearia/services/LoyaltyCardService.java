package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.models.LoyaltyCard;
import br.org.unicortes.barbearia.models.Servico;
import br.org.unicortes.barbearia.repositories.ClientRepository;
import br.org.unicortes.barbearia.repositories.LoyaltyCardRepository;
import br.org.unicortes.barbearia.repositories.ServicoRepository;
import br.org.unicortes.barbearia.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoyaltyCardService {

    @Autowired
    private LoyaltyCardRepository loyaltyCardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    public List<LoyaltyCard> getAllLoyaltyCards() {
        return loyaltyCardRepository.findAll();
    }

    public LoyaltyCard getLoyaltyCardById(Long id) {
        return loyaltyCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public LoyaltyCard createLoyaltyCard(LoyaltyCard loyaltyCard) {
        Client client = clientRepository.findById(loyaltyCard.getClient().getId())
                .orElseThrow(() -> new ResourceNotFoundException(loyaltyCard.getClient().getId()));

        Servico service = servicoRepository.findById(loyaltyCard.getService().getId())
                .orElseThrow(() -> new ResourceNotFoundException(loyaltyCard.getService().getId()));

        loyaltyCard.setClient(client);
        loyaltyCard.setService(service);

        return loyaltyCardRepository.save(loyaltyCard);
    }

    public LoyaltyCard updateLoyaltyCard(Long id, LoyaltyCard loyaltyCardDetails) {
        LoyaltyCard existingLoyaltyCard = loyaltyCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        Client client = clientRepository.findById(loyaltyCardDetails.getClient().getId())
                .orElseThrow(() -> new ResourceNotFoundException(loyaltyCardDetails.getClient().getId()));

        Servico service = servicoRepository.findById(loyaltyCardDetails.getService().getId())
                .orElseThrow(() -> new ResourceNotFoundException(loyaltyCardDetails.getService().getId()));

        existingLoyaltyCard.setClient(client);
        existingLoyaltyCard.setDateAdmission(loyaltyCardDetails.getDateAdmission());
        existingLoyaltyCard.setService(service);
        existingLoyaltyCard.setPoints(loyaltyCardDetails.getPoints());

        return loyaltyCardRepository.save(existingLoyaltyCard);
    }

    public void deleteLoyaltyCard(Long id) {
        LoyaltyCard loyaltyCard = loyaltyCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        loyaltyCardRepository.delete(loyaltyCard);
    }
}
