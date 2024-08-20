package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.SaleDTO;
import br.org.unicortes.barbearia.dtos.SaleForLoyaltyCardDTO;
import br.org.unicortes.barbearia.models.Sale;
import br.org.unicortes.barbearia.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping(path = "/sale/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) throws Exception {
        Sale sale = this.saleService.getSaleById(id);
        return ResponseEntity.ok(convertToDto(sale));
    }

    @PostMapping(path = "/newSale")
    public ResponseEntity<SaleDTO> createSale(@RequestBody SaleDTO saleDto) throws Exception {
        Sale sale = convertToSale(saleDto);
        Sale createdSale = this.saleService.createSale(sale);
        return ResponseEntity.ok(convertToDto(createdSale));
    }

    @PutMapping(path = "/sale/edit/{id}")
    public ResponseEntity<SaleDTO> updateSale(@PathVariable Long id, @RequestBody SaleDTO saleDto) throws Exception {
        Sale sale = convertToSale(saleDto);
        sale.setSaleId(id); // Ensure the correct ID is set for the update
        Sale updatedSale = this.saleService.updateSale(sale);
        return ResponseEntity.ok(convertToDto(updatedSale));
    }

    @DeleteMapping(path = "/sale/delete/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        this.saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping(path = "/")
    public ResponseEntity<List<SaleDTO>> findAllSales() {
        List<SaleDTO> allSales = this.saleService.getAllSales().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(allSales);
    }

    private SaleDTO convertToDto(Sale sale) {
        SaleDTO saleDto = new SaleDTO();
        saleDto.setSaleId(sale.getSaleId());
        saleDto.setSaleName(sale.getSaleName());
        saleDto.setSaleDescription(sale.getSaleDescription());
        saleDto.setSalePromoCode(sale.getSalePromoCode());
        saleDto.setSaleDiscount(sale.getSaleDiscount());
        saleDto.setSaleExpirationDate(sale.getSaleExpirationDate());
        saleDto.setSaleCategory(sale.getSaleCategory());
        saleDto.setSaleAvailability(sale.isSaleAvailability());
        return saleDto;
    }

    private Sale convertToSale(SaleDTO saleDto) {
        Sale sale = new Sale();
        sale.setSaleName(saleDto.getSaleName());
        sale.setSaleDescription(saleDto.getSaleDescription());
        sale.setSalePromoCode(saleDto.getSalePromoCode());
        sale.setSaleDiscount(saleDto.getSaleDiscount());
        sale.setSaleExpirationDate(saleDto.getSaleExpirationDate());
        sale.setSaleCategory(saleDto.getSaleCategory());
        sale.setSaleAvailability(saleDto.isSaleAvailability());
        return sale;
    }
}
