package com.hunter104.model;

import java.util.Objects;

public class Turma {
    private final int id;
    private final String professor;
    private final Horario horario;

    public Turma(int id, String professor, Horario horario) {
        this.id = id;
        this.professor = professor;
        this.horario = horario;
    }

    public Turma(int id, String professor, String horarioCodificado) {
        this.id = id;
        this.professor = professor;
        this.horario = new Horario(horarioCodificado);
    }

    @Override
    public String toString() {
        return "Turma{" +
                "id=" + id +
                ", professor='" + professor + '\'' +
                ", horario=" + horario +
                '}';
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

    public String getProfessor() {
        return professor;
    }

    public Horario getHorario() {
        return horario;
    }
}
