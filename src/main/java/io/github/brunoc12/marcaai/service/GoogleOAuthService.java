
package io.github.brunoc12.marcaai.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class GoogleOAuthService {

    private static final String USUARIO = "marca-ai-local";

    private final GoogleAuthorizationCodeFlow fluxo;

    private final String redirectUri;

    public GoogleOAuthService(
            @Value("${google.calendar.client-id}") String clientId,
            @Value("${google.calendar.client-secret}") String clientSecret,
            @Value("${google.calendar.redirect-uri}") String redirectUri) {

        this.redirectUri = redirectUri;

        try {

            File pastaTokens = new File(
                    System.getProperty("user.home"),
                    ".marca-ai/google-calendar-tokens"
            );

            Files.createDirectories(pastaTokens.toPath());

            this.fluxo = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    clientId,
                    clientSecret,
                    List.of(CalendarScopes.CALENDAR_EVENTS_OWNED)
            )
                    .setDataStoreFactory(
                            new FileDataStoreFactory(pastaTokens)
                    )
                    .setAccessType("offline")
                    .build();

        } catch (GeneralSecurityException | IOException e) {

            throw new IllegalStateException(
                    "Erro ao configurar autenticação com Google.",
                    e
            );
        }
    }

    // Gera a URL de autorização
    public String gerarUrlAutorizacao(String state) {

        return fluxo.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setState(state)
                .set("prompt", "consent")
                .set("include_granted_scopes", "true")
                .build();
    }

    // Troca o código de autorização pelos tokens
    public void salvarCredenciais(String codigoAutorizacao)
            throws IOException {

        TokenResponse resposta = fluxo.newTokenRequest(codigoAutorizacao)
                .setRedirectUri(redirectUri)
                .execute();

        fluxo.createAndStoreCredential(resposta, USUARIO);
    }

    // Recupera as credenciais armazenadas
    public Credential carregarCredencial() throws IOException {

        Credential credencial = fluxo.loadCredential(USUARIO);

        if (credencial == null) {

            throw new IllegalStateException(
                    "Google Calendar ainda não foi autorizado."
            );
        }

        return credencial;
    }
}
