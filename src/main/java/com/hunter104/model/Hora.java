package com.hunter104.model;

public enum Hora {
    M12("08:00 - 09:00", "12", Turno.MANHA),
    M34("10:00 - 11:00", "34", Turno.MANHA),
    M5("12:00 - 13:00", "5", Turno.MANHA),
    T1("13:00 - 13:00", "1", Turno.TARDE),
    T23("14:00 - 15:00", "23", Turno.TARDE),
    T45("16:00 - 17:00", "45", Turno.TARDE),
    N12("19:00 - 20:00", "12", Turno.NOITE),
    N34("20:00 - 22:30", "34", Turno.NOITE);

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
