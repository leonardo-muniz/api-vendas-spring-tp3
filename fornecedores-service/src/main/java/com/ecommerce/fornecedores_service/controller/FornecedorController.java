package com.ecommerce.fornecedores_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.fornecedores_service.client.ProdutoClient;
import com.ecommerce.fornecedores_service.dto.ProdutoDTO;
import com.ecommerce.fornecedores_service.model.Fornecedor;
import com.ecommerce.fornecedores_service.service.FornecedorService;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorService service;
    private final ProdutoClient produtoClient;

    public FornecedorController(FornecedorService service, ProdutoClient produtoClient) {
        this.service = service;
        this.produtoClient = produtoClient;
    }

    @GetMapping
    public List<Fornecedor> listarTodos() {
        return service.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Fornecedor> salvar(@RequestBody Fornecedor fornecedor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(fornecedor));
    }

    @GetMapping("/{id}")
    public Optional<Fornecedor> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/produtos")
    public List<ProdutoDTO> listarProdutos() {
        return produtoClient.listarTodos();
    }
}
