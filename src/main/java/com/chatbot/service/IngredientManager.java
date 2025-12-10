package com.chatbot.service;

import java.util.ArrayList;
import java.util.List;

public class IngredientManager {

    private List<String> ingredients = new ArrayList<>();

    /** Registra um ingrediente escrito pelo usuário */
    public void addIngredient(String ingredient) {
        ingredients.add(ingredient.toLowerCase().trim());
    }

    /** Retorna a lista atual de ingredientes */
    public List<String> getIngredients() {
        return ingredients;
    }

    // Regras simples para detectar ingrediente
    public boolean isIngredientInput(String input) {
        String text = input.trim().toLowerCase();
        if (text.isEmpty()) return false;

        // Saudações -> não serão tratadas como ingredientes
        if (text.equals("oi") || text.equals("olá") || text.equals("ola") || 
            text.equals("tudo bem") || text.startsWith("bom dia") || 
            text.startsWith("boa tarde") || text.equals("obrigado")) {
            return false;
        }
        return text.length() < 40;
    }
}
