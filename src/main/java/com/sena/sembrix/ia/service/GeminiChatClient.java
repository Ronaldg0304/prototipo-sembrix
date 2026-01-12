package com.sena.sembrix.ia.service;

import com.sena.sembrix.ia.AiChatClient;
import com.sena.sembrix.ia.dto.IARequest;
import com.sena.sembrix.ia.dto.IAResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class GeminiChatClient implements AiChatClient {

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Override
    public String generateResponse(String prompt) {

        String url = apiUrl + "?key=" + apiKey;

        IARequest request = new IARequest(
                List.of(
                        new IARequest.Content(
                                List.of(new IARequest.Part(prompt))
                        )
                )
        );

        try {
            ResponseEntity<IAResponse> response =
                    restTemplate.postForEntity(url, request, IAResponse.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Gemini error HTTP: {}", response.getStatusCode());
                return "El servicio de IA no está disponible en este momento.";
            }

            IAResponse body = response.getBody();

            if (body == null || body.getCandidates() == null || body.getCandidates().isEmpty()) {
                log.error("Respuesta inválida de Gemini: {}", body);
                return "La IA no devolvió una respuesta válida.";
            }

            IAResponse.Candidate candidate = body.getCandidates().getFirst();

            if (candidate.getContent() == null ||
                    candidate.getContent().getParts() == null ||
                    candidate.getContent().getParts().isEmpty()) {

                log.error("Contenido vacío de Gemini: {}", body);
                return "La IA devolvió una respuesta vacía.";
            }

            return candidate.getContent().getParts().getFirst().getText();

        } catch (HttpClientErrorException e) {
            log.error("Error HTTP Gemini: {}", e.getResponseBodyAsString(), e);
            return "Error al comunicarse con el servicio de IA.";
        } catch (Exception e) {
            log.error("Error inesperado Gemini", e);
            return "Ocurrió un error interno procesando la respuesta de IA.";
        }
    }
}
