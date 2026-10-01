package com.ecommerce.fornecedores_service.config;

import com.ecommerce.fornecedores_service.model.Fornecedor;
import com.ecommerce.fornecedores_service.repository.FornecedorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Popula o banco H2 em memória com clientes de teste assim que a aplicação sobe.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final FornecedorRepository fornecedorRepository;

    public DataInitializer(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public void initializeFornecedores() {
        /* Nota: 
        *  Estes dados são inteiramente simulados e estruturados matematicamente por algoritmos de validação. 
        *  Eles não correspondem a empresas reais ativas na Receita Federal.
        */
        fornecedorRepository.save(new Fornecedor("TechNova Soluções Digitais Ltda", "31.905.734/0001-00"));
        fornecedorRepository.save(new Fornecedor("Aliança Logística e Transportes S.A.", "84.267.150/0001-09"));
        fornecedorRepository.save(new Fornecedor("Bella Vita Cosméticos EIRELI", "40.221.130/0001-90"));
        fornecedorRepository.save(new Fornecedor("Horizonte Engenharia e Construções", "53.821.045/0001-87"));
        fornecedorRepository.save(new Fornecedor("Vanguarda Alimentos e Bebidas", "84.324.049/0001-54"));
    }

    @Override
    public void run(String... args) {
        initializeFornecedores();
    }
}
