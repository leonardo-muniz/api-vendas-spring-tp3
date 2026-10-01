package com.ecommerce.fornecedores_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.fornecedores_service.model.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {}
