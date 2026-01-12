package com.sena.sembrix.ia.service;

import com.sena.sembrix.ia.AiChatClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
//@Primary // Esto indica que esta es la implementación por defecto
public class GenericAiClient implements AiChatClient {

    private final WebClient webClient;

    public GenericAiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("URL_DE_LA_API_ELEGIDA").build();

    }

    @Override
    public String generateResponse(String prompt) {
        // Aquí se hace la llamada real a la API (Gemini, Groq, etc.)
        // Por ahora simulamos la llamada:
        return "Simulación: Analizando tus datos de " + prompt.substring(0, 20) + "...";
    }
}