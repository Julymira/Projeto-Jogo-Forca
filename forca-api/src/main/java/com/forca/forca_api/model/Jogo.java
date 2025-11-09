package com.forca.forca_api.model;

import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class Jogo {
    
    private static final String[] LISTA_PALAVRAS = {
            "COMPUTADOR", "PROGRAMACAO", "JAVA", "PHP", "INTEGRACAO", 
            "FRONTEND", "BACKEND", "REST", "API", "DESENVOLVIMENTO"
    };

    private String id; // ID único para o jogo (para a API saber qual jogo o PHP está pedindo)
    private String palavraSecreta;
    private StringBuilder palavraMascarada; // Ex: "_A_B_"
    private int erros;
    private HashSet<Character> letrasTentadas; // String de letras tentadas: "AEIOU"
    private String status; // Ex: "JOGANDO", "VENCEU", "PERDEU"
    private final int MAX_ERROS = 6;
    
    // Construtor
    public Jogo() {
        this.id = UUID.randomUUID().toString();

        Random rand = new Random();
        this.palavraSecreta = LISTA_PALAVRAS[rand.nextInt(LISTA_PALAVRAS.length)];
        
        this.palavraMascarada = new StringBuilder("_".repeat(this.palavraSecreta.length()));
        
        // 4. Inicializa os contadores
        this.erros = 0;
        this.letrasTentadas = new HashSet<Character>();
        this.status = "JOGANDO";

    }

    public String getId() {
        return id;
    }

    // Retornamos a palavra mascarada como String para o JSON
    public String getPalavraMascarada() {
        return palavraMascarada.toString();
    }

    public int getErros() {
        return erros;
    }
    
    // Converte o Set de caracteres em uma String de letras separadas por vírgula para o JSON
    public String getLetrasTentadas() {
    // Converte o Set de caracteres em uma String de letras separadas por vírgula
    return letrasTentadas.stream() // Mantenha esta linha, mas ela depende da correção do Maven.
        .map(String::valueOf)
        .collect(Collectors.joining(", "));
}

    public String getStatus() {
        return status;
    }

    // (Opcional) Retornamos a palavra secreta SOMENTE se o jogo tiver terminado
    public String getPalavraSecreta() {
        if (this.status.equals("VENCEU") || this.status.equals("PERDEU")) {
             return palavraSecreta;
        }
        return null;
    }

    // Usado pelo Service para processar um chute
    public boolean tentarLetra(char letra) {
        char chute = Character.toUpperCase(letra);
        
        // 1. Já tentou?
        if (letrasTentadas.contains(chute)) {
            return true; // Consideramos um "acerto" para não contar um erro, mas não muda o estado
        }
        letrasTentadas.add(chute);

        // 2. A letra está na palavra?
        if (palavraSecreta.contains(String.valueOf(chute))) {
            // Atualiza a palavra mascarada
            for (int i = 0; i < palavraSecreta.length(); i++) {
                if (palavraSecreta.charAt(i) == chute) {
                    palavraMascarada.setCharAt(i, chute);
                }
            }
            return true; // Acertou
        } else {
            this.erros++;
            return false; // Errou
        }
    }

    // Usado pelo Service para verificar se o jogo terminou
    public void verificarFimDeJogo() {
        if (erros >= this.MAX_ERROS) {
            this.status = "PERDEU";
        } else if (!palavraMascarada.toString().contains("_")) {
            this.status = "VENCEU";
        } else {
            this.status = "JOGANDO";
        }
    }

    // Adicione os Setters que forem estritamente necessários (ID e Palavra Secreta não precisam de Setters externos)
    public void setStatus(String status) {
        this.status = status;
    }
    
    

}
