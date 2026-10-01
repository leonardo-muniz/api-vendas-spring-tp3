package com.ecommerce.auth_service.service;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.auth_service.dto.LoginRequest;
import com.ecommerce.auth_service.dto.LoginResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeycloakTokenService {

    private final RestClient restClient;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret:}")
    private String clientSecret;

    public LoginResponse login(LoginRequest request) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("username", request.getEmail());
        form.add("password", request.getSenha());
        return requestToken(form);
    }

    public LoginResponse refresh(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);
        return requestToken(form);
    }

    private LoginResponse requestToken(MultiValueMap<String, String> form) {
        form.add("client_id", clientId);
        if (!clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }

        try {
            KeycloakTokenResponse response = restClient.post()
                    .uri(tokenUri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(KeycloakTokenResponse.class);

            return new LoginResponse(response.accessToken(), response.refreshToken(),
                    response.tokenType(), response.expiresIn());
        } catch (RestClientResponseException exception) {
            throw new ResponseStatusException(UNAUTHORIZED,
                    "Keycloak rejeitou a solicitação: " + exception.getResponseBodyAsString(), exception);
        } catch (RestClientException | NullPointerException exception) {
            throw new ResponseStatusException(UNAUTHORIZED, "Credenciais inválidas ou token expirado");
        }
    }

    private record KeycloakTokenResponse(
            String access_token,
            String refresh_token,
            String token_type,
            Long expires_in) {
        String accessToken() { return access_token; }
        String refreshToken() { return refresh_token; }
        String tokenType() { return token_type; }
        Long expiresIn() { return expires_in; }
    }
}