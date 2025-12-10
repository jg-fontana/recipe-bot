package com.chatbot.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class LLMClient {

    private String apiKey;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public LLMClient() {
        this.apiKey = loadApiKey();
    }

    private String loadApiKey() {
        try {
            return Files.readString(Paths.get("key.txt")).trim();
        } catch (IOException e) {
            throw new RuntimeException("Erro: não foi possível ler o arquivo key.txt", e);
        }
    }

    /** Gera uma resposta **/ 
    public String generateResponse(String userInput, List<String> ingredients) {
    try {
        HttpClient client = HttpClient.newHttpClient();

        JSONObject json = new JSONObject();
        json.put("model", "gpt-4o-mini");

        String systemPrompt =
            "Você é um assistente culinário.\n"
          + "Regras do comportamento:\n"
          + "- Só sugira receitas se o usuário pedir diretamente.\n"
          + "- Se o usuário apenas disser algo como 'oi', 'tudo bem', etc, responda normalmente.\n"
          + "- Use ingredientes cadastrados SOMENTE quando forem úteis.\n"
          + "- Não force sugestões.\n"
          + "- Fale como um humano simpático.";

        JSONArray messages = new JSONArray();

        messages.put(new JSONObject()
            .put("role", "system")
            .put("content", systemPrompt));

        messages.put(new JSONObject()
            .put("role", "system")
            .put("content", "Ingredientes cadastrados até agora: " + ingredients));

        messages.put(new JSONObject()
            .put("role", "user")
            .put("content", userInput));

        json.put("messages", messages);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject resp = new JSONObject(response.body());
        return resp.getJSONArray("choices")
                   .getJSONObject(0)
                   .getJSONObject("message")
                   .getString("content");

    } catch (Exception e) {
        return "Desculpe, tive um problema ao gerar a resposta.";
    }


    }
    public String generateRecipe(List<String> ingredients) {
    try {
        HttpClient client = HttpClient.newHttpClient();

        JSONObject json = new JSONObject();
        json.put("model", "gpt-4o-mini");

        // Formatação da string do prompt
        String prompt = """
            Com base nestes ingredientes: %s
            Gere uma receita simples, objetiva e fácil de fazer.
            Inclua:
            - Nome da receita
            - Ingredientes necessários
            - Modo de preparo em passos
            """.formatted(ingredients.toString()); // Garante conversão para String

        JSONArray messages = new JSONArray();

        // MENSAGEM DO SISTEMA (Simplificada, igual ao generateResponse)
        messages.put(new JSONObject()
            .put("role", "system")
            .put("content", "Você é um chef criativo. Crie receitas apenas com os ingredientes fornecidos, se possível."));

        // MENSAGEM DO USUÁRIO (Simplificada: content é apenas uma String)
        messages.put(new JSONObject()
            .put("role", "user")
            .put("content", prompt));

        json.put("messages", messages);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Se o status não for 200, imprime o corpo do erro
        if (response.statusCode() != 200) {
            System.out.println("ERRO API: " + response.statusCode());
            System.out.println("BODY: " + response.body());
            return "Erro na comunicação com a OpenAI.";
        }

        JSONObject resp = new JSONObject(response.body());
        return resp.getJSONArray("choices")
                   .getJSONObject(0)
                   .getJSONObject("message")
                   .getString("content");

    } catch (Exception e) {
        e.printStackTrace(); 
        return "Desculpe, tive um problema técnico ao gerar a receita: " + e.getMessage();
    }
}
}
