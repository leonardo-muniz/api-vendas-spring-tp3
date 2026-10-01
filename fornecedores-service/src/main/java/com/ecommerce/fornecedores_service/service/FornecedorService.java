package com.ecommerce.fornecedores_service.service;

import com.ecommerce.fornecedores_service.model.Fornecedor;
import com.ecommerce.fornecedores_service.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Regra de negócio de Fornecedor. O controller não fala direto com o repository,
 * fala com este service.
 */
@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    public Optional<Fornecedor> buscarPorId(Long id) {
        return fornecedorRepository.findById(id);
    }
}
