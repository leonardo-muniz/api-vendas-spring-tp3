package com.ecommerce.auth_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.auth_service.dto.LoginRequest;
import com.ecommerce.auth_service.dto.UsuarioRequest;
import com.ecommerce.auth_service.model.Usuario;
import com.ecommerce.auth_service.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestClient restClient;

    @Value("${keycloak.admin-token-uri}")
    private String adminTokenUri;

    @Value("${keycloak.admin-users-uri}")
    private String adminUsersUri;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    public Usuario cadastrar(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe um usuário cadastrado com o email " + request.getEmail());
        }

                criarUsuarioNoKeycloak(request);

        Usuario usuario = new Usuario(
                request.getNome(),
                request.getEmail(),
                passwordEncoder.encode(request.getSenha()));

        return usuarioRepository.save(usuario);
    }

    private void criarUsuarioNoKeycloak(UsuarioRequest request) {
        MultiValueMap<String, String> tokenForm = new LinkedMultiValueMap<>();
        tokenForm.add("grant_type", "password");
        tokenForm.add("client_id", "admin-cli");
        tokenForm.add("username", adminUsername);
        tokenForm.add("password", adminPassword);

        try {
            String adminToken = restClient.post()
                    .uri(adminTokenUri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(tokenForm)
                    .retrieve()
                    .body(AdminTokenResponse.class)
                    .accessToken();

            restClient.post()
                    .uri(adminUsersUri)
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new KeycloakUserRequest(
                            request.getEmail(),
                            request.getNome(),
                            "Usuario",
                            request.getEmail(),
                            true,
                            true,
                            new Credential[] { new Credential("password", request.getSenha(), false) }))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().value() == 409) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Já existe um usuário cadastrado no Keycloak com o email " + request.getEmail(),
                        exception);
            }
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Keycloak rejeitou o cadastro: " + exception.getResponseBodyAsString(), exception);
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Não foi possível cadastrar o usuário no Keycloak", exception);
        }
    }

    public Usuario autenticar(LoginRequest request){

        Usuario usuario = this.usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"E-mail ou senha inválidos"));
    
        if(!passwordEncoder.matches(request.getSenha(),usuario.getSenha())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"E-mail ou senha inválidos");
        }

        return usuario;
    }

    private record AdminTokenResponse(String access_token) {
        String accessToken() {
            return access_token;
        }
    }

    private record KeycloakUserRequest(
            String username,
            String firstName,
            String lastName,
            String email,
            boolean enabled,
            boolean emailVerified,
            Credential[] credentials) {
    }

    private record Credential(String type, String value, boolean temporary) {}
}
