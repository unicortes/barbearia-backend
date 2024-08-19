package br.org.unicortes.barbearia.dtos;


import lombok.Data;

@Data
public class ServicoDTO {

    private Long id;
    private String name;
    private String description;
    private double price;
}
