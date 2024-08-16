package br.org.unicortes.barbearia.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Table(name = "tbSales")
public class SaleModel {

    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    @Column(name = "saleId")
    private long saleId;

    @Column(name = "saleName")
    private String saleName;

    @Column(name = "saleDescription")
    private String saleDescription;

    @Column(name = "salePromoCode")
    private String salePromoCode;

    @Column(name = "saleDiscount")
    private double saleDiscount;

    @Column(name = "saleExpirationDate")
    private Date saleExpirationDate;

    @Column(name = "saleCategory")
    private String saleCategory;

    @Column(name = "saleAvailability")
    private boolean saleAvailability;

}
