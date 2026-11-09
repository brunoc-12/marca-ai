
package io.github.brunoc12.marcaai.controller;

import io.github.brunoc12.marcaai.service.GoogleOAuthService;

import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@RestController
public class GoogleOAuthController {

    private static final Logger log =
            LoggerFactory.getLogger(GoogleOAuthController.class);

    private static final String SESSION_STATE =
            "GOOGLE_OAUTH_STATE";

    private static final SecureRandom RANDOM = new SecureRandom();

    private final GoogleOAuthService googleOAuthService;

    public GoogleOAuthController(
            GoogleOAuthService googleOAuthService) {

        this.googleOAuthService = googleOAuthService;
    }

    // Inicia a autorização com o Google
    @GetMapping("/google/authorize")
    public ResponseEntity<Void> autorizar(HttpSession session) {

        byte[] bytes = new byte[32];

        RANDOM.nextBytes(bytes);

        String state = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        session.setAttribute(SESSION_STATE, state);

        log.info(
                "OAuth iniciado: state salvo na sessão? {}",
                session.getAttribute(SESSION_STATE) != null
        );

        String url = googleOAuthService
                .gerarUrlAutorizacao(state);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    // Recebe a resposta do Google
    @GetMapping("/oauth2/callback")
    public ResponseEntity<String> callback(

            @RequestParam(name = "code", required = false)
            String code,

            @RequestParam(name = "state", required = false)
            String state,

            @RequestParam(name = "error", required = false)
            String error,

            HttpSession session
    ) throws IOException {

        String estadoEsperado =
                (String) session.getAttribute(SESSION_STATE);

        boolean stateValido =
                estadoEsperado != null &&
                        state != null &&
                        MessageDigest.isEqual(
                                estadoEsperado.getBytes(StandardCharsets.UTF_8),
                                state.getBytes(StandardCharsets.UTF_8)
                        );

        log.info(
                "OAuth callback: sessão nova? {}, state esperado? {}, state recebido? {}, state válido? {}",
                session.isNew(),
                estadoEsperado != null,
                state != null,
                stateValido
        );

        // O estado é consumido para impedir reutilização.
        session.removeAttribute(SESSION_STATE);

        if (!stateValido) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Estado OAuth inválido. Tente novamente.");
        }

        if (error != null) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Autorização não concluída: " + error);
        }

        if (code == null || code.isBlank()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Código de autorização não recebido.");
        }

        googleOAuthService.salvarCredenciais(code);

        return ResponseEntity.ok(
                "Google Calendar conectado com sucesso ao Marca AI!"
        );
    }
}
