package com.forca.forca_api.service;

import com.forca.forca_api.model.*;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class JogoService {

    private final Map<String, Jogo> jogosAtivos = new HashMap<>(); // Armazena todos os jogos ativos

    /**
     * Inicia um novo jogo da forca.
     * @return O objeto Jogo recém-criado.
     */
    public Jogo iniciarNovoJogo() {
        // O construtor de Jogo já faz toda a inicialização (escolhe palavra, gera ID).
        Jogo novoJogo = new Jogo(); 
        
        // Adiciona o novo jogo ao mapa para ser rastreado.
        jogosAtivos.put(novoJogo.getId(), novoJogo); 
        
        System.out.println("Novo jogo iniciado. ID: " + novoJogo.getId() + ", Palavra: " + novoJogo.getPalavraSecreta());
        
        return novoJogo;
    }

    /**
     * Processa o chute de uma letra em um jogo existente.
     * @param jogoId O ID do jogo a ser atualizado.
     * @param letra O caractere chutado.
     * @return O objeto Jogo atualizado, ou null se o jogo não existir.
     */
    public Jogo tentarLetra(String jogoId, char letra) {
        // 1. Busca o jogo ativo.
        Jogo jogo = jogosAtivos.get(jogoId); 
        
        if (jogo == null || !jogo.getStatus().equals("JOGANDO")) {
            // Se o jogo não existir ou já tiver terminado.
            return null; 
        }
        
        // 2. Processa o chute através do método que criamos no Model.
        jogo.tentarLetra(letra); 
        
        // 3. Verifica se o estado do jogo mudou (ganhou/perdeu).
        jogo.verificarFimDeJogo();
        
        // Se perdeu, removemos o jogo para liberar a memória (opcional, mas bom).
        if (jogo.getStatus().equals("PERDEU") || jogo.getStatus().equals("VENCEU")) {
             // jogosAtivos.remove(jogoId); // Você pode remover se quiser.
        }

        return jogo;
    }

    /**
     * (Opcional) Retorna o estado atual de um jogo pelo ID.
     */
    public Jogo getEstadoJogo(String jogoId) {
        return jogosAtivos.get(jogoId);
    }
}
