package com.ecommerce.vendas_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.vendas_service.models.Venda;

public interface VendasRepository extends JpaRepository<Venda, Long> {}
