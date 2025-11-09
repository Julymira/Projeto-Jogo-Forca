<?php
// Arquivo: C:\xampp\htdocs\forca-frontend\index.php

// 1. Configuração e Funções de Comunicação
session_start();
define('API_URL', 'http://localhost:8080/api/jogo');

function callApi($endpoint, $method = 'GET', $data = []) {
    $ch = curl_init(API_URL . $endpoint);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, $method);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
    
    if (!empty($data)) {
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
    }
    
    $response = curl_exec($ch);
    $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);
    
    if ($http_code != 200 && $http_code != 201) {
        // Se a API Java não responder ou der erro 400/500
        error_log("Erro na chamada à API. Código HTTP: " . $http_code);
        return null; 
    }
    return json_decode($response);
}

// 2. Lógica do Jogo
// Tenta inicializar ou buscar o estado do jogo
$jogo = null;
$error_message = '';

if (!isset($_SESSION['jogo_id']) || isset($_POST['novo_jogo'])) {
    $novoJogo = callApi('/iniciar', 'POST');
    if ($novoJogo) {
        $_SESSION['jogo_id'] = $novoJogo->id;
        $_SESSION['jogo_status'] = $novoJogo;
        $jogo = $novoJogo;
    } else {
        $error_message = "ERRO: Não foi possível iniciar o jogo. API Java indisponível.";
    }
} elseif ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['letra'])) {
    // Processar chute
    $letra = trim($_POST['letra']);
    $jogoId = $_SESSION['jogo_id'];
    
    if (strlen($letra) === 1) {
        $data = ['letra' => strtoupper($letra)]; // Garantindo que seja maiúscula
        
        $jogoAtualizado = callApi("/{$jogoId}/chutar", 'POST', $data);
        
        if ($jogoAtualizado) {
            $_SESSION['jogo_status'] = $jogoAtualizado;
            $jogo = $jogoAtualizado;
        } else {
            $error_message = "ERRO: O jogo não foi atualizado (Letra inválida ou jogo encerrado).";
        }
    }
}

// Se não houve inicialização ou erro, busca o estado da sessão
if ($jogo === null && isset($_SESSION['jogo_status'])) {
    $jogo = $_SESSION['jogo_status'];
}

// Variáveis para renderização
$palavra_display = $jogo ? $jogo->palavraMascarada : 'Iniciando...';
$erros = $jogo ? $jogo->erros : 0;
$status = $jogo ? $jogo->status : 'INICIANDO';
$letras_tentadas = $jogo ? $jogo->letrasTentadas : '';
$max_erros = 6;
?>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Jogo da Forca - PHP + Java API</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; }
        .palavra { font-size: 2.5em; letter-spacing: 10px; margin-bottom: 30px; }
        .erros-info { color: red; font-weight: bold; }
        .venceu { color: green; font-size: 1.5em; }
        .perdeu { color: red; font-size: 1.5em; }
        .error { color: #800000; padding: 10px; border: 1px solid #800000; background-color: #fdd; margin: 20px auto; width: 80%; }
    </style>
</head>
<body>

    <h1>Jogo da Forca Integrado (PHP e Java)</h1>
    <?php if ($error_message): ?>
        <div class="error"><?php echo htmlspecialchars($error_message); ?></div>
    <?php endif; ?>

    <?php if ($jogo && $status != 'INICIANDO'): ?>
        <p>ID do Jogo: <strong><?php echo htmlspecialchars($jogo->id); ?></strong></p>
        
        <div class="game-status">
            <p>Erros: <span class="erros-info"><?php echo $erros; ?> / <?php echo $max_erros; ?></span></p>
            <p>Letras Tentadas: <strong><?php echo htmlspecialchars($letras_tentadas); ?></strong></p>
        </div>

        <div class="palavra">
            <?php echo htmlspecialchars(str_replace('_', '_ ', $palavra_display)); ?>
        </div>

        <?php if ($status === 'JOGANDO'): ?>
            <form method="POST">
                <label for="letra">Chute uma letra:</label>
                <input type="text" id="letra" name="letra" maxlength="1" required style="text-transform:uppercase;">
                <button type="submit">Chutar</button>
            </form>
        <?php elseif ($status === 'VENCEU'): ?>
            <p class="venceu">🎉 PARABÉNS! VOCÊ VENCEU! 🎉</p>
            <p>A palavra era: <strong><?php echo htmlspecialchars($jogo->palavraSecreta); ?></strong></p>
        <?php elseif ($status === 'PERDEU'): ?>
            <p class="perdeu">❌ VOCÊ PERDEU! 😢</p>
            <p>A palavra secreta era: <strong><?php echo htmlspecialchars($jogo->palavraSecreta); ?></strong></p>
        <?php endif; ?>
    <?php else: ?>
        <p>Aguardando inicialização do jogo...</p>
    <?php endif; ?>

    <form method="POST" style="margin-top: 40px;">
        <button type="submit" name="novo_jogo">Iniciar Novo Jogo</button>
    </form>

</body>
</html>