package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.LoyaltyCardDTO;
import br.org.unicortes.barbearia.services.LoyaltyCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty-cards")
public class LoyaltyCardController {

    @Autowired
    private LoyaltyCardService loyaltyCardService;

    @GetMapping
    public ResponseEntity<List<LoyaltyCardDTO>> getAllLoyaltyCards() {
        List<LoyaltyCardDTO> loyaltyCards = loyaltyCardService.findAll();
        return new ResponseEntity<>(loyaltyCards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoyaltyCardDTO> getLoyaltyCardById(@PathVariable Long id) {
        LoyaltyCardDTO loyaltyCard = loyaltyCardService.findById(id);
        return new ResponseEntity<>(loyaltyCard, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LoyaltyCardDTO> createLoyaltyCard(@RequestBody LoyaltyCardDTO loyaltyCardDTO) {
        LoyaltyCardDTO newLoyaltyCard = loyaltyCardService.create(loyaltyCardDTO);
        return new ResponseEntity<>(newLoyaltyCard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoyaltyCardDTO> updateLoyaltyCard(@PathVariable Long id, @RequestBody LoyaltyCardDTO loyaltyCardDTO) {
        LoyaltyCardDTO updatedLoyaltyCard = loyaltyCardService.update(id, loyaltyCardDTO);
        return new ResponseEntity<>(updatedLoyaltyCard, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoyaltyCard(@PathVariable Long id) {
        loyaltyCardService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

