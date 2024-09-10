package br.org.unicortes.barbearia.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.org.unicortes.barbearia.models.Promotion;
import br.org.unicortes.barbearia.dtos.PromotionDTO;
import br.org.unicortes.barbearia.services.PromotionService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/promotions")
public class PromotionController {
    @Autowired
    private PromotionService promotionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {
        List<PromotionDTO> promotions = promotionService.getAllPromotions().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable Long id) {
        Promotion promotion = promotionService.getPromotionById(id);
        return ResponseEntity.ok(convertToDTO(promotion));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromotionDTO> createPromotion(@RequestBody PromotionDTO promotionDTO) {
        Promotion promotion = convertToEntity(promotionDTO);
        Promotion newPromotion = promotionService.createPromotion(promotion);
        return ResponseEntity.ok(convertToDTO(newPromotion));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromotionDTO> updatePromotion(@PathVariable Long id, @RequestBody PromotionDTO promotionDTO) {
        Promotion promotion = convertToEntity(promotionDTO);
        Promotion updatedPromotion = promotionService.updatePromotion(id, promotion);
        return ResponseEntity.ok(convertToDTO(updatedPromotion));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    private PromotionDTO convertToDTO(Promotion promotion) {
        PromotionDTO promotionDTO = new PromotionDTO();
        promotionDTO.setId(promotion.getId());
        promotionDTO.setName(promotion.getName());
        promotionDTO.setDescription(promotion.getDescription());
        promotionDTO.setDiscount(promotion.getDiscount());
        promotionDTO.setCategory(promotion.getCategory());
        promotionDTO.setPromotionCode(promotion.getPromotionCode());
        promotionDTO.setAvailability(promotion.getAvailability());
        promotionDTO.setStartDate(promotion.getStartDate());
        promotionDTO.setEndDate(promotion.getEndDate());
        return promotionDTO;
    }

    private Promotion convertToEntity(PromotionDTO promotionDTO) {
        Promotion promotion = new Promotion();
        promotion.setId(promotionDTO.getId());
        promotion.setName(promotionDTO.getName());
        promotion.setDescription(promotionDTO.getDescription());
        promotion.setDiscount(promotionDTO.getDiscount());
        promotion.setCategory(promotionDTO.getCategory());
        promotion.setPromotionCode(promotionDTO.getPromotionCode());
        promotion.setAvailability(promotionDTO.getAvailability());
        promotion.setStartDate(promotionDTO.getStartDate());
        promotion.setEndDate(promotionDTO.getEndDate());
        return promotion;
    }
}