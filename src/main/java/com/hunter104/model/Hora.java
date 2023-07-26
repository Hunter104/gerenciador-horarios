package com.hunter104.model;

public enum Hora {
    M12("08:00 - 09:50", "12", Turno.MANHA),
    M34("10:00 - 11:50", "34", Turno.MANHA),
    M5("12:00 - 12:55", "5", Turno.MANHA),
    T1("12:55 - 13:50", "1", Turno.TARDE),
    T23("14:00 - 15:50", "23", Turno.TARDE),
    T45("16:00 - 17:50", "45", Turno.TARDE),
    N12("19:00 - 20:40", "12", Turno.NOITE),
    N34("20:50 - 22:30", "34", Turno.NOITE);

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
