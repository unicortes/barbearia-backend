package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.SaleDTO;
import br.org.unicortes.barbearia.models.Sale;
import br.org.unicortes.barbearia.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping(path = "/sale/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable int id) throws Exception{
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
    public ResponseEntity<SaleDTO> updateSale(@RequestBody SaleDTO saleDto) throws Exception {
        Sale sale = convertToSale(saleDto);
        Sale updatedSale = this.saleService.updateSale(sale);
        return ResponseEntity.ok(convertToDto(updatedSale));
    }

    @DeleteMapping(path = "/sale/delete/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long saleId){
        this.saleService.deleteSale(saleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<SaleDTO>> findAllSales(){
        List<SaleDTO> allSales = this.saleService.getAllSales().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(allSales);
    }

    private SaleDTO convertToDto(Sale sale){
        SaleDTO saleDto = new SaleDTO();
        saleDto.setSaleId(sale.getSaleId());
        saleDto.setSaleName(sale.getSaleName());
        saleDto.setSaleDescription(sale.getSaleDescription());
        saleDto.setSalePromoCode(sale.getSalePromoCode());
        saleDto.setSaleDiscount(sale.getSaleDiscount());
        saleDto.setSaleExpirationDate(sale.getSaleExpirationDate());
        saleDto.setSaleCategory(sale.getSaleCategory());
        saleDto.setSaleAvailability(sale.isSaleAvailability());
    }

    private Sale convertToSale(SaleDTO saleDto){
        Sale sale = new Sale();
        sale.setSaleId(sale.getSaleId());
        sale.setSaleName(sale.getSaleName());
        sale.setSaleDescription(sale.getSaleDescription());
        sale.setSalePromoCode(sale.getSalePromoCode());
        sale.setSaleDiscount(sale.getSaleDiscount());
        sale.setSaleExpirationDate(sale.getSaleExpirationDate());
        sale.setSaleCategory(sale.getSaleCategory());
        sale.setSaleAvailability(sale.isSaleAvailability());
    }


}
