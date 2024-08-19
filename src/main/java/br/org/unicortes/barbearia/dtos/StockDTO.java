package br.org.unicortes.barbearia.dtos;

import br.org.unicortes.barbearia.enums.StockStatus;
import lombok.Data;

@Data
public class StockDTO {
    private Long id;
    private Long productId;
    private int quantity;
    private StockStatus status;
}
