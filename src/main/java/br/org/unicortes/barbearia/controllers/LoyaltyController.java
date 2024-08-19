package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.LoyaltyCardDTO;
import br.org.unicortes.barbearia.dtos.SaleDTO;
import br.org.unicortes.barbearia.dtos.SaleForLoyaltyCardDTO;
import br.org.unicortes.barbearia.models.Client;
import br.org.unicortes.barbearia.models.LoyaltyCard;
import br.org.unicortes.barbearia.models.Sale;
import br.org.unicortes.barbearia.models.SaleForLoyaltyCard;
import br.org.unicortes.barbearia.services.LoyaltyCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/loyaltyCards")
public class LoyaltyController {

    @Autowired
    private LoyaltyCardService loyaltyCardService;


    @GetMapping("/{id}")
    public ResponseEntity<LoyaltyCardDTO> getSaleById(@PathVariable int id) throws Exception {
        LoyaltyCard loyaltyCard = this.loyaltyCardService.getLoyaltyCard(id);
        return ResponseEntity.ok(convertToDto(loyaltyCard));
    }

    @PostMapping(path = "/newLoyaltyCard")
    public ResponseEntity<LoyaltyCardDTO> createLoyaltyCard(@RequestBody LoyaltyCardDTO loyaltyCardDto) throws Exception {
        LoyaltyCard sale = convertToLoyaltyCard(loyaltyCardDto);
        LoyaltyCard createdLoyaltyCard = this.loyaltyCardService.createLoyaltyCard(sale);
        return ResponseEntity.ok(convertToDto(createdLoyaltyCard));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteLoyaltyCard(@PathVariable int id) throws Exception {
        this.loyaltyCardService.deleteLoyaltyCard((long) id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<LoyaltyCardDTO> updateLoyaltyCard(@PathVariable Long id, @RequestBody LoyaltyCardDTO loyaltyCardDto){
        LoyaltyCard loyaltyCard = convertToLoyaltyCard(loyaltyCardDto);
        loyaltyCard.setId(id); // Ensure the correct ID is set for the update
        LoyaltyCard updatedLoyaltyCard = this.loyaltyCardService.updateLoyaltyCard(loyaltyCard);
        return ResponseEntity.ok(convertToDto(updatedLoyaltyCard));
    }

    //Só possível terminar a implementação qundo houver cliente
    /*@PostMapping(path = "/sale/newSaleForLoyalty/birthday/{id}")
    public ResponseEntity<Void> createBirthdaySale(@RequestParam int cardId, @RequestBody SaleForLoyaltyCard saleForLoyaltyCard) {
        LoyaltyCard lc = this.loyaltyCardService.getLoyaltyCard((int) cardId);

        loyaltyCardService.createBirthdaySale(client, saleForLoyaltyCard);

        return ResponseEntity.noContent().build();
    }*/

    @PostMapping(path = "/sale/newSaleForLoyalty/{id}")
    public ResponseEntity<Void> createLoyaltySale(@RequestParam int loyaltyCardId, @RequestBody SaleForLoyaltyCard saleForLoyaltyCard) {
        LoyaltyCard lc = this.loyaltyCardService.getLoyaltyCard(loyaltyCardId);
        this.loyaltyCardService.createSaleForLoyaltyCard(saleForLoyaltyCard, lc);

        return ResponseEntity.noContent().build();
    }

    private LoyaltyCardDTO convertToDto(LoyaltyCard loyaltyCard) {
        LoyaltyCardDTO loyaltyCardDto = new LoyaltyCardDTO();
        loyaltyCardDto.setId(loyaltyCard.getId());
        loyaltyCardDto.setClient(loyaltyCard.getClient());
        loyaltyCardDto.setAdmissionDate(loyaltyCard.getAdmissionDate());
        loyaltyCardDto.setServicesAquired(loyaltyCard.getServicesAquired());
        return loyaltyCardDto;
    }

    private LoyaltyCard convertToLoyaltyCard(LoyaltyCardDTO loyaltyCardDto) {
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setId(loyaltyCard.getId());
        loyaltyCard.setClient(loyaltyCard.getClient());
        loyaltyCard.setAdmissionDate(loyaltyCard.getAdmissionDate());
        loyaltyCard.setServicesAquired(loyaltyCard.getServicesAquired());
        return loyaltyCard;
    }
}
