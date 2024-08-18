package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.models.SaleModel;
import br.org.unicortes.barbearia.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping(path = "/sale/{id}")
    public SaleModel getSaleById(@PathVariable int id) throws Exception{
        var sale = this.saleService.getSaleById(id);
        return sale;
    }

    @PostMapping(path = "/newSale")
    public SaleModel createSale(@RequestBody SaleModel sale) throws Exception {
        SaleModel createdSale = this.saleService.createSale(sale);
        return createdSale;
    }

    @PutMapping(path = "/sale/edit/{id}")
    public SaleModel updateSale(@RequestBody SaleModel sale) throws Exception {
        SaleModel updatedSale = this.saleService.updateSale(sale);
        return updatedSale;
    }

    @DeleteMapping(path = "/sale/delete/{id}")
    public void deleteSale(Long saleId){
        this.saleService.deleteSale(saleId);
    }

    @GetMapping(path = "/")
    public List<SaleModel> findAllSales(){
        List<SaleModel> allSales = this.saleService.getAllSales();
        return allSales;
    }

}
