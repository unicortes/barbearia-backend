package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.models.SaleModel;
import br.org.unicortes.barbearia.repositories.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Transactional
    public SaleModel getSaleById(int id){
        return saleRepository.findBySaleId(id);
    }

    //inserir (rollbackFor = Throwable.class) quando criar exceção
    @Transactional
    public SaleModel createSale(SaleModel sale){
        return saleRepository.save(sale);
    }
    //inserir (rollbackFor = Throwable.class) quando criar exceção
    @Transactional
    public SaleModel updateSale(SaleModel sale){
        if (!saleRepository.existsById(sale.getSaleId())){
            return null;
        }else{
            return saleRepository.save(sale);
        }

    }

    @Transactional
    public void deleteSale(Long saleId){
        saleRepository.deleteById(saleId);
    }

    public List<SaleModel> getAllSales() {
        return saleRepository.findAll();
    }

}
