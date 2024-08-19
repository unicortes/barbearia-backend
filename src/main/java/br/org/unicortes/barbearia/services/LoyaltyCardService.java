package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.ClientNotFoundException;
import br.org.unicortes.barbearia.exceptions.LoyaltyCardNotFoundException;
import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.models.LoyaltyCard;
import br.org.unicortes.barbearia.models.SaleForLoyaltyCard;
import br.org.unicortes.barbearia.repositories.LoyaltyCardRepository;
import br.org.unicortes.barbearia.repositories.SaleForLoyaltyCardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
public class LoyaltyCardService {

    @Autowired
    private LoyaltyCardRepository loyaltyCardRepository;

    @Autowired
    private SaleForLoyaltyCardRepository saleForLoyaltyCardRepository;

    @Transactional
    public LoyaltyCard createLoyaltyCard(LoyaltyCard loyaltyCard) {
        return this.loyaltyCardRepository.save(loyaltyCard);
    }

    @Transactional
    public LoyaltyCard updateLoyaltyCard(LoyaltyCard loyaltyCard){
        if (!loyaltyCardRepository.existsById(loyaltyCard.getId())){
            return null;
        }else{
            return loyaltyCardRepository.save(loyaltyCard);
        }
    }

    @Transactional
    public LoyaltyCard getLoyaltyCard(Long id) {
        return this.loyaltyCardRepository.findById(id)
                .orElseThrow(() -> new LoyaltyCardNotFoundException(id));
    }

    @Transactional
    public void deleteLoyaltyCard(Long saleId){
        this.loyaltyCardRepository.deleteById(saleId);
    }

    @Transactional
    public SaleForLoyaltyCard createSaleForLoyaltyCard(SaleForLoyaltyCard saleForLoyaltyCard, LoyaltyCard loyaltyCard) {
        return this.saleForLoyaltyCardRepository.save(saleForLoyaltyCard);
    }

    @Transactional
    public void createBirthdaySale(Client client, SaleForLoyaltyCard saleForLoyaltyCard) {
        try{
            LocalDate currentDate = LocalDate.now();
            LocalDate clientBirthday = client.getBirthday();
            LoyaltyCard loyaltyCard = this.loyaltyCardRepository.findByClientId(client.getId());
            if (clientBirthday.equals(currentDate)) {
                this.createSaleForLoyaltyCard(saleForLoyaltyCard, loyaltyCard);
            }
        }catch(ClientNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    public void createLoyaltySale(LoyaltyCard loyaltyCard, SaleForLoyaltyCard saleForLoyaltyCard){
        if (loyaltyCard.getServicesAquired().size() == 10){
            this.createSaleForLoyaltyCard(saleForLoyaltyCard, loyaltyCard);
        }
    }
}
