package com.hunter104.model;

import java.util.Collection;

public class Turma {
    private int id;
    private String professor;
    private Horario horario;
    private String salas;

    public Turma(int id, String professor, String salas, Horario horario) {
        this.id = id;
        this.professor = professor;
        this.horario = horario;
        this.salas = salas;
    }

    public Turma(int id, String professor, String salas, String horarioCodificado) {
        this(id, professor, salas, Horario.criarFromCodigo(horarioCodificado));
    }

    @Override
    public String toString() {
        return "Turma{" +
                "id=" + id +
                ", professor='" + professor + '\'' +
                ", horario=" + horario +
                '}';
    }

    public boolean temInterseccao(DiadaSemana dia, Hora hora) {
        return horario.temInterseccao(dia, hora);
    }

    public boolean conflitaComTurma(Turma turma) {
        return turma.getHorario().temInterseccao(this.getHorario());
    }

    public boolean conflitaComTurmas(Collection<Turma> turmas) {
        return turmas.stream().anyMatch(this::conflitaComTurma);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public String getSalas() {
        return salas;
    }

    public void setSalas(String salas) {
        this.salas = salas;
    }
}
