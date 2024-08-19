package br.org.unicortes.barbearia.services;

import br.org.unicortes.barbearia.exceptions.StockNotFoundException;
import br.org.unicortes.barbearia.models.Stock;
import br.org.unicortes.barbearia.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new StockNotFoundException(id));
    }

    public List<Stock> getStocksByProductId(Long productId) {
        return stockRepository.findByProductId(productId);
    }

    public List<Stock> getStocksByProductName(String name) {
        return stockRepository.findByProductName(name);
    }

    public Stock createStock(Stock stock) {
        if (stock.getStatus() == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        return stockRepository.save(stock);
    }

    public Stock updateStock(Long id, Stock stockDetails) {
        Stock stock = getStockById(id);
        stock.setProduct(stockDetails.getProduct());
        stock.setStatus(stockDetails.getStatus());
        stock.setQuantity(stockDetails.getQuantity());
        return stockRepository.save(stock);
    }

    public void deleteStock(Long id) {
        Stock stock = getStockById(id);
        stockRepository.delete(stock);
    }
}
