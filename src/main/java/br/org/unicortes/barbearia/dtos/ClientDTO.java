package br.org.unicortes.barbearia.dtos;

import java.util.Date;
import lombok.Data;

@Data
public class ClientDTO {
    private Long id;
    private String name;
    private String phone;
    private Date birthday;
    private String email;
}
