package com.rohlik.data.commons.services;

import java.util.List;

import com.rohlik.data.entities.Sale;

public interface SalesService {
List<Sale> findAll();
Sale findBySalesId(Integer salesId);
Sale save(Sale sale);
void remove(Sale sale);
}
