package br.org.unicortes.barbearia.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class SaleDTO {
    private long saleId;

    private String saleName;

    private String saleDescription;

    private String salePromoCode;

    private double saleDiscount;

    private Date saleExpirationDate;

    private String saleCategory;

    private boolean saleAvailability;
}
