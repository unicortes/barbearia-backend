package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.PromotionAlreadyExistsException;
import br.org.unicortes.barbearia.exceptions.PromotionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import br.org.unicortes.barbearia.models.Promotion;
import br.org.unicortes.barbearia.repositories.PromotionRepository;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;

    @Transactional
    public Promotion createPromotion(Promotion promotion) {
        if (promotionRepository.existsByName(promotion.getName())) {
            throw new PromotionAlreadyExistsException(promotion.getName());
        }
        return promotionRepository.save(promotion);
    };

    public Promotion updatePromotion(Long id, Promotion promotionDetails) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new PromotionNotFoundException(id));
        promotion.setId(promotionDetails.getId());
        promotion.setName(promotionDetails.getName());
        promotion.setDescription(promotionDetails.getDescription());
        promotion.setPromotionCode(promotionDetails.getPromotionCode());
        promotion.setCategory(promotionDetails.getCategory());
        promotion.setDiscount(promotionDetails.getDiscount());
        promotion.setAvailability(promotionDetails.getAvailability());
        promotion.setStartDate(promotionDetails.getStartDate());
        promotion.setEndDate(promotionDetails.getEndDate());
        return promotionRepository.save(promotion);
    };

    public void deletePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new PromotionNotFoundException(id));
        promotionRepository.delete(promotion);
    };

    public List<Promotion> getAllPromotions(){
        return promotionRepository.findAll();
    }

    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new PromotionNotFoundException(id));
    }
}