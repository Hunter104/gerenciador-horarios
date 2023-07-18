package com.hunter104.model;

public enum Hora {
    M12("08h às 09h50", "12", Turno.MANHA),
    M34("10h às 11h50", "34", Turno.MANHA),
    M5("12h às 13h00", "5", Turno.MANHA),
    T1("13h às 13h50", "1", Turno.TARDE),
    T23("14h às 15h50", "23", Turno.TARDE),
    T45("16h às 17h50", "45", Turno.TARDE),
    N12("19h às 20h40", "12", Turno.NOITE),
    N34("20h50 às 22h30", "34", Turno.NOITE);

    private final String nome;
    private final String codigo;
    private final Turno turno;
    Hora(String nome, String codigo, Turno turno) {
        this.nome = nome;
        this.codigo = codigo;
        this.turno = turno;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public Turno getTurno() {
        return turno;
    }
}
