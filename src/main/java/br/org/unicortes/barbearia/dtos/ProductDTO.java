package br.org.unicortes.barbearia.dtos;

import lombok.Data;
import java.util.Date;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Date expirationDate;
    private double cost;
    private String type;
}
