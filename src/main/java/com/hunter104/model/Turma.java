package com.hunter104.model;

import java.util.Collection;
import java.util.Objects;

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

    public boolean conflitaComTurma(Turma turma) {
        return turma.getHorario().temInterseccao(this.getHorario());
    }

    public boolean conflitaComTurmas(Collection<Turma> turmas) {
        return turmas.stream().anyMatch(this::conflitaComTurma);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turma turma = (Turma) o;
        return id == turma.id && Objects.equals(professor, turma.professor) && Objects.equals(horario, turma.horario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, professor, horario);
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
