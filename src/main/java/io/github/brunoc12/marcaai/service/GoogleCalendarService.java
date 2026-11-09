
package io.github.brunoc12.marcaai.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GoogleCalendarService {

    private static final String CALENDAR_ID = "primary";

    private static final ZoneId FUSO_HORARIO =
            ZoneId.of("America/Sao_Paulo");

    private final GoogleOAuthService googleOAuthService;

    public GoogleCalendarService(
            GoogleOAuthService googleOAuthService) {

        this.googleOAuthService = googleOAuthService;
    }

    // CRIAR EVENTO NO GOOGLE CALENDAR
    public Event criarEvento(
            String titulo,
            String descricao,
            LocalDateTime inicio,
            LocalDateTime fim
    ) throws IOException, GeneralSecurityException {

        validarDados(titulo, inicio, fim);

        Credential credencial =
                googleOAuthService.carregarCredencial();

        Calendar calendario = criarClienteCalendar(credencial);

        Event evento = new Event();

        evento.setSummary(titulo);
        evento.setDescription(descricao);
        evento.setStart(converterHorario(inicio));
        evento.setEnd(converterHorario(fim));

        return calendario.events()
                .insert(CALENDAR_ID, evento)
                .execute();
    }

    // ATUALIZAR EVENTO EXISTENTE NO GOOGLE CALENDAR
    public Event atualizarEvento(
            String googleEventId,
            String titulo,
            String descricao,
            LocalDateTime inicio,
            LocalDateTime fim
    ) throws IOException, GeneralSecurityException {

        validarDados(titulo, inicio, fim);

        if (googleEventId == null || googleEventId.isBlank()) {
            throw new IllegalArgumentException(
                    "O ID do evento Google é obrigatório."
            );
        }

        Credential credencial =
                googleOAuthService.carregarCredencial();

        Calendar calendario = criarClienteCalendar(credencial);

        Event eventoAtualizado = new Event();

        eventoAtualizado.setSummary(titulo);
        eventoAtualizado.setDescription(descricao);
        eventoAtualizado.setStart(converterHorario(inicio));
        eventoAtualizado.setEnd(converterHorario(fim));

        return calendario.events()
                .patch(CALENDAR_ID, googleEventId, eventoAtualizado)
                .execute();
    }

    // CONFIGURAR CLIENTE DO GOOGLE CALENDAR
    private Calendar criarClienteCalendar(Credential credencial)
            throws GeneralSecurityException, IOException {

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credencial
        )
                .setApplicationName("Marca AI")
                .build();
    }

    // VALIDAR DADOS DO EVENTO
    private void validarDados(
            String titulo,
            LocalDateTime inicio,
            LocalDateTime fim) {

        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException(
                    "O título do evento é obrigatório."
            );
        }

        if (inicio == null || fim == null || !fim.isAfter(inicio)) {
            throw new IllegalArgumentException(
                    "Os horários do evento são inválidos."
            );
        }
    }

    // CONVERTER HORÁRIO PARA O FORMATO DO GOOGLE
    private EventDateTime converterHorario(
            LocalDateTime horario) {

        ZonedDateTime horarioComFuso =
                horario.atZone(FUSO_HORARIO);

        String dataFormatada = horarioComFuso.format(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
        );

        return new EventDateTime()
                .setDateTime(new DateTime(dataFormatada))
                .setTimeZone(FUSO_HORARIO.getId());
    }



    // EXCLUIR EVENTO DO GOOGLE CALENDAR
    public void deletarEvento(String googleEventId)
            throws IOException, GeneralSecurityException {

        if (googleEventId == null || googleEventId.isBlank()) {
            throw new IllegalArgumentException(
                    "O ID do evento Google é obrigatório."
            );
        }

        Credential credencial =
                googleOAuthService.carregarCredencial();

        Calendar calendario = criarClienteCalendar(credencial);

        calendario.events()
                .delete(CALENDAR_ID, googleEventId)
                .execute();
    }

}
