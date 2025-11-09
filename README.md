# 🕹️ Jogo da Forca Integrado (Java API + PHP Frontend)

Este projeto demonstra uma arquitetura moderna de aplicações web, onde a **Lógica de Negócio (Back-end)** é separada da **Interface do Usuário (Front-end)**. A comunicação entre os dois sistemas é feita exclusivamente por uma API REST.

## ✨ Tecnologias Utilizadas

| Componente | Linguagem/Framework | Objetivo |
| :--- | :--- | :--- |
| **Back-end/API** | **Java 21**, **Spring Boot 3.x**, **Maven** | Gerenciar o estado do jogo (palavra secreta, erros, status) e expor *endpoints* REST (JSON) na porta **8080**. |
| **Front-end/Cliente** | **PHP 8.x**, **Apache (XAMPP)**, **cURL** | Servir a interface HTML/CSS, capturar chutes do usuário e atuar como cliente HTTP da API Java. |
| **Arquitetura** | **RESTful API** e **JSON** | Padrão de comunicação entre os dois servidores. |

## 🚀 Como Rodar o Projeto

Para rodar o Jogo da Forca, você precisará iniciar dois servidores separados: o Back-end Java (API) e o Front-end PHP (Apache).

### Pré-Requisitos
* [**Java Development Kit (JDK) 21**](https://www.oracle.com/java/technologies/downloads/) (LTS recomendado).
* [**Git**](https://git-scm.com/)
* [**XAMPP**](https://www.apachefriends.org/index.html) (ou WAMP/MAMP) para o servidor Apache e PHP.
* **VS Code** com o [**Extension Pack for Java**](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) instalado.

---

### 1. Preparação da Estrutura

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/Julymira/Projeto-Jogo-Forca.git
    cd Projeto-Jogo-Forca
    ```

2.  **Organize o Front-end (Symlink):**
    * **Finalidade:** Criar um atalho na pasta do Apache (`htdocs`) que aponte para a pasta `forca-frontend` (que está agora no seu repositório Git).
    * **Ação:** Execute o comando abaixo (será necessário rodar o **Prompt de Comando como Administrador**). Substitua `[SEU CAMINHO AQUI]` pelo caminho absoluto da sua pasta Git.
        ```bash
        # Exemplo (se XAMPP estiver em D:):
        mklink /D "D:\Xampp\htdocs\forca-frontend" "C:\...[SEU CAMINHO AQUI]\Projeto-Jogo-Forca\forca-frontend"
        ```

3.  **Configure o Acesso Simplificado:**
    * Crie um arquivo `index.php` na raiz do `htdocs` (`D:\Xampp\htdocs\index.php`) com o seguinte conteúdo para redirecionar para o jogo:
        ```php
        <?php
        header("Location: /forca-frontend/");
        exit;
        ?>
        ```

### 2. Inicialização do Back-end (Java API)

1.  **Abra o Projeto Java:**
    * No VS Code, abra a sub