package com.ecommerce.produtos_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.produtos_service.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {}
