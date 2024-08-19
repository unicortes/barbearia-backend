package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tb_sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_seq")
    @SequenceGenerator(name = "sale_seq", sequenceName = "sale_sequence", allocationSize = 1)
    @Column(name = "sale_id")
    private long saleId;

    @NotBlank(message = "O campo 'nome' é obrigatório")
    @Column(name = "sale_name")
    private String saleName;

    @NotBlank(message = "O campo 'descrição' é obrigatório")
    @Column(name = "sale_description")
    private String saleDescription;

    @NotBlank(message = "O campo 'código' é obrigatório")
    @Column(name = "sale_promo_code")
    private String salePromoCode;

    @NotBlank(message = "O campo 'desconto' é obrigatório")
    @Column(name = "sale_discount")
    private double saleDiscount;

    @NotBlank(message = "O campo 'data de validade' é obrigatório")
    @Column(name = "sale_expiration_date")
    private Date saleExpirationDate;

    @NotBlank(message = "O campo 'categoria' é obrigatório")
    @Column(name = "sale_category")
    private String saleCategory;

    @NotBlank(message = "O campo 'disponibilidade' é obrigatório")
    @Column(name = "sale_availability")
    private boolean saleAvailability;

}
