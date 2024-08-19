package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tbSales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_seq")
    @SequenceGenerator(name = "sale_seq", sequenceName = "sale_sequence", allocationSize = 1)
    @Column(name = "sale_id")
    private long saleId;

    @Column(name = "sale_name")
    private String saleName;

    @Column(name = "sale_description")
    private String saleDescription;

    @Column(name = "sale_promo_code")
    private String salePromoCode;

    @Column(name = "sale_discount")
    private double saleDiscount;

    @Column(name = "sale_expiration_date")
    private Date saleExpirationDate;

    @Column(name = "sale_category")
    private String saleCategory;

    @Column(name = "sale_availability")
    private boolean saleAvailability;

}
