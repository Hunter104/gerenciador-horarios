package com.hunter104.model;

public enum DiadaSemana {
    SEGUNDA("segunda-feira", "2"),
    TERCA("terça-feira", "3"),
    QUARTA("quarta-feira", "4"),
    QUINTA("quinta-feira", "5"),
    SEXTA("sexta-feira", "6"),
    SABADO("sábado", "7");

    private final String nome;
    private final String codigo;

    DiadaSemana(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

    public String nome() {
        return nome;
    }

    public String codigo() {
        return codigo;
    }
}
