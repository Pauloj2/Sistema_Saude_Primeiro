package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class ChatBotApiController {

  // ✅ API LOCAL DO OLLAMA
  private final String URL = "http://localhost:11434/api/generate";

  @PostMapping("/chatbot")
  public Map<String, String> conversar(@RequestBody Map<String, String> body) {

    try {
      String pergunta = body.get("pergunta");

      RestTemplate restTemplate = new RestTemplate();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      String jsonBody = """
          {
            "model": "phi3",
            "prompt": "Responda de forma curta, direta e em português: %s",
            "num_predict": 80,
            "stream": false
          }
          """.formatted(pergunta);

      HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

      ResponseEntity<Map> response = restTemplate.postForEntity(URL, request, Map.class);

      String resposta = response.getBody().get("response").toString();

      return Map.of("resposta", resposta);

    } catch (Exception e) {
      e.printStackTrace();
      return Map.of("resposta", "❌ Erro ao conectar com a IA local.");
    }
  }
}
