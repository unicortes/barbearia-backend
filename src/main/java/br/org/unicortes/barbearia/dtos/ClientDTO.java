package br.org.unicortes.barbearia.dtos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ClientDTO {
    private Long id;
    private String name;
    private String phone;
    private LocalDate birthday;
    private String email;
}
