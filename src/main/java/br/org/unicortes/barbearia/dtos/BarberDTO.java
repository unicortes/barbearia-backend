package br.org.unicortes.barbearia.dtos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class BarberDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String cpf;
    private Double salary;
    private String address;
    private LocalDate admissionDate;
    private String openingHours;
}
