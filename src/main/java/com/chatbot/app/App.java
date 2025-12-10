package com.chatbot.app;

import java.util.Scanner;

import com.chatbot.api.LLMClient;
import com.chatbot.service.IngredientManager;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IngredientManager ingredientManager = new IngredientManager();
        LLMClient llmClient = new LLMClient();

        System.out.println("Olá! Sou seu assistente de receitas.");
        System.out.println("Diga ingredientes, peça sugestões ou converse comigo!");
        System.out.println("Para adicionar ingredientes, digite '+' antes do ingrediente desejado (Ex: +ovo)");
        System.out.println("Para pedir uma sugestão de receita use uma palavra de comando (sugira, gere, etc...)");
        System.out.println("Para sair, digite: sair\n");

        while (true) { //Mantem programa ativo até o usuário digitar sair
            System.out.print("Você: ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) continue;

            if (input.equalsIgnoreCase("sair")) {
                System.out.println("Chatbot: Até mais!");
                break;
            }
            if (input.startsWith("+")) {
                // Remove o "+" e espaços
                String ingredient = input.replace("+", "").trim(); 
                ingredientManager.addIngredient(ingredient);
                System.out.println("Chatbot: [Adicionado] " + ingredient);
                continue;
            }
            
            // Se o usuário pedir receita
            if (isRecipeRequest(input)) {
                String recipe = llmClient.generateRecipe(ingredientManager.getIngredients());
                System.out.println("Chatbot:\n" + recipe);
                continue;
            }
             // Gera resposta usando LLM + ingredientes armazenados
            String response = llmClient.generateResponse(input, ingredientManager.getIngredients());
            System.out.println("Chatbot: " + response);
        }

        scanner.close();
    }
        private static boolean isRecipeRequest(String input) {
        String text = input.toLowerCase();
        return text.contains("receita")
            || text.contains("sugere")
            || text.contains("sugestão")
            || text.contains("ideia")
            || text.contains("me ajuda a cozinhar")
            || text.contains("o que posso fazer")
            || text.contains("o que fazer")
            || text.contains("prato");
    }
}


