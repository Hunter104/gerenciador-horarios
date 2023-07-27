package com.hunter104.model;

public enum Turno {
    MANHA("M", "manhã"),
    TARDE("T", "tarde"),
    NOITE("N", "noite");

    private final String codigo;
    private final String nome;

    Turno(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String codigo() {
        return codigo;
    }

    public String nome() {
        return nome;
    }
}
