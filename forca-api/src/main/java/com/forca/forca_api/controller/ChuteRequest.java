package com.forca.forca_api.controller;

// Esta classe é um DTO (Data Transfer Object) simples
// para receber o JSON do Front-end PHP.
public class ChuteRequest {

    private String letra;

    // Construtor vazio (necessário para o Spring/Jackson)
    public ChuteRequest() {
    }

    // Getters e Setters
    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }
}
