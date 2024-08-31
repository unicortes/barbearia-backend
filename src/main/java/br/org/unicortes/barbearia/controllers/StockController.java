package br.org.unicortes.barbearia.controllers;

import br.org.unicortes.barbearia.dtos.StockDTO;
import br.org.unicortes.barbearia.models.Product;
import br.org.unicortes.barbearia.models.Stock;
import br.org.unicortes.barbearia.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<List<StockDTO>> getAllStocks() {
        List<StockDTO> stocks = stockService.getAllStocks().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<StockDTO> getStockById(@PathVariable Long id) {
        Stock stock = stockService.getStockById(id);
        return ResponseEntity.ok(convertToDTO(stock));
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<List<StockDTO>> getStocksByProductId(@PathVariable Long productId) {
        List<StockDTO> stocks = stockService.getStocksByProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/product/name/{name}")
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<List<StockDTO>> getStocksByProductName(@PathVariable String name) {
        List<StockDTO> stocks = stockService.getStocksByProductName(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(stocks);
    }

    @PostMapping
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<StockDTO> createStock(@RequestBody StockDTO stockDTO) {
        Stock stock = convertToEntity(stockDTO);
        Stock savedStock = stockService.createStock(stock);
        return ResponseEntity.ok(convertToDTO(savedStock));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<StockDTO> updateStock(@PathVariable Long id, @RequestBody StockDTO stockDTO) {
        Stock stock = convertToEntity(stockDTO);
        Stock updatedStock = stockService.updateStock(id, stock);
        return ResponseEntity.ok(convertToDTO(updatedStock));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('BARBER, ADMIN')")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    private StockDTO convertToDTO(Stock stock) {
        StockDTO stockDTO = new StockDTO();
        stockDTO.setId(stock.getId());
        stockDTO.setProductId(stock.getProduct().getId());
        stockDTO.setQuantity(stock.getQuantity());
        stockDTO.setStatus(stock.getStatus());
        return stockDTO;
    }

    private Stock convertToEntity(StockDTO stockDTO) {
        Stock stock = new Stock();
        Product product = new Product();
        product.setId(stockDTO.getProductId());
        stock.setProduct(product);
        stock.setQuantity(stockDTO.getQuantity());
        stock.setStatus(stockDTO.getStatus());
        return stock;
    }
}