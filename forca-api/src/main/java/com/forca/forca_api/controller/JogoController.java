package com.forca.forca_api.controller;

import com.forca.forca_api.model.*;
import com.forca.forca_api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Indica que esta classe é um Controller REST
@RequestMapping("/api/jogo") // Prefixo para todos os endpoints desta classe
public class JogoController {
    
    @Autowired
    private JogoService jogoService; // Injeta a lógica de negócio (Service)
    
    // Endpoint 1: Iniciar um novo jogo
    @PostMapping("/iniciar")
    public Jogo iniciarJogo() {
        // Chama o método do Service e retorna o JSON do novo jogo
        return jogoService.iniciarNovoJogo();
    }
    
    //  Endpoint 2: BUSCA o estado de um jogo (usado pelo PHP para recarregar a tela)
    @GetMapping("/{jogoId}")
    public ResponseEntity<Jogo> getJogo(@PathVariable String jogoId) {
        Jogo jogo = jogoService.getEstadoJogo(jogoId);
        if (jogo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jogo);
    }
    
    // Endpoint 3: Chutar uma letra
    // O PHP chamará esta URL passando o ID do jogo e a letra
    @PostMapping("/{jogoId}/chutar")
    public ResponseEntity<Jogo> chutarLetra(@PathVariable String jogoId, @RequestBody ChuteRequest request) {
        // A lógica do Service espera um único char MAIÚSCULO
        char letra = Character.toUpperCase(request.getLetra().charAt(0)); 
        
        Jogo jogoAtualizado = jogoService.tentarLetra(jogoId, letra);
        
        if (jogoAtualizado == null) {
            return ResponseEntity.badRequest().build(); // Retorna erro 400 se o jogo não existir/tiver acabado
        }
        return ResponseEntity.ok(jogoAtualizado);
    }

    @GetMapping("/status") // Novo endpoint de teste
    public String checkStatus() {
        return "API da Forca está ONLINE!";
    }
}
// Note: Você precisará criar a classe ChuteRequest para receber o JSON do PHP.
