package com.hunter104.model;

public enum Hora {
    M1("08:00 - 08:55", "1", Turno.MANHA),
    M2("08:55 - 09:50", "2", Turno.MANHA),
    M3("10:00 - 10:55", "3", Turno.MANHA),
    M4("10:55 - 11:50", "4", Turno.MANHA),
    M5("12:00 - 12:55", "5", Turno.MANHA),
    T1("12:55 - 13:50", "1", Turno.TARDE),
    T2("14:00 - 14:55", "2", Turno.TARDE),
    T3("14:55 - 15:50", "3", Turno.TARDE),
    T4("16:00 - 16:55", "4", Turno.TARDE),
    T5("16:55 - 17:50", "5", Turno.TARDE),
    T6("17:50 - 18:55", "6", Turno.TARDE),
    N1("19:00 - 19:50", "1", Turno.NOITE),
    N2("19:50 - 20:40", "2", Turno.NOITE),
    N3("20:50 - 21:40", "3", Turno.NOITE),
    N4("21:40 - 22:30", "4", Turno.NOITE);

    private final String nome;
    private final String codigo;
    private final Turno turno;

    Hora(String nome, String codigo, Turno turno) {
        this.nome = nome;
        this.codigo = codigo;
        this.turno = turno;
    }

    public String nome() {
        return nome;
    }

    public String codigo() {
        return codigo;
    }

    public Turno turno() {
        return turno;
    }
}
