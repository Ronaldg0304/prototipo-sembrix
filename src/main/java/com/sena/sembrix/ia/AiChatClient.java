package com.sena.sembrix.ia;

public interface AiChatClient {
    /**
     * Envía un mensaje a la IA y recibe una respuesta.
     * @param prompt El texto con instrucciones y datos.
     * @return La respuesta de la IA.
     */
    String generateResponse(String prompt);
}