Chatbot Culinário (Java + OpenAI)

Um assistente virtual a base de inteligência artificial (GPT-4) que gera receitas a partir de ingredientes inscritos pelo usuário
## Funcionalidades
- **Gerenciamento de Dispensa:** Adicionar e remover ingredientes da memória.
- **Geração de Receitas:** Cria receitas completas baseadas apenas nos ingredientes da memória e sugestões rápidas
- **Chat Livre:** Conversa amigável sobre culinária.
- **Interface:** Comandos simples via terminal.

## Configuração

### Pré-requisitos
- Java JDK 17 ou superior.
- Maven (para build).
- Uma chave de API da OpenAI.

### Instalação
1. Clone este repositório.
2. Crie um arquivo chamado `key.txt` na raiz do projeto.
3. Cole sua chave da OpenAI (iniciada em `sk-...`) dentro deste arquivo.

O arquivo `key.txt` não é versionado por motivos de segurança.

## Como Configurar e Executar

Como este repositório contém apenas o código-fonte (por segurança), você precisará compilar o projeto antes de rodar.

### Passo 1: Configurar a Chave de API
1. Na raiz do projeto, crie um arquivo chamado `key.txt`.
2. Cole sua chave da OpenAI (formato `sk-...`) dentro dele.
   > **Nota:** O arquivo `key.txt` não é versionado no Git por questões de segurança

### Passo 2: Compilar o Projeto
Abra o terminal na pasta raiz e execute:

mvn clean package
Ainda no terminal, execute o comando 
java -jar target/recipe-bot-1.0-SNAPSHOT.jar.

Com isso, o projeto será iniciado

### Diagrama de classes
```bash
classDiagram
    class App {
        +main(args: String[])
        -isRecipeRequest(input: String): boolean
    }

    class IngredientManager {
        -ingredients: List<String>
        +addIngredient(ingredient: String)
        +removeIngredient(ingredient: String): boolean
        +getIngredients(): List<String>
        +isIngredientInput(input: String): boolean
    }

    class LLMClient {
        -apiKey: String
        -API_URL: String
        +generateResponse(input: String, ingredients: List): String
        +generateRecipe(ingredients: List): String
        -loadApiKey(): String
    }

    App --> IngredientManager : usa
    App --> LLMClient : usa
