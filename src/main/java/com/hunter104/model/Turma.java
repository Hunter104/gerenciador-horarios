package com.hunter104.model;

import java.util.Objects;

public class Turma {
    private int id;
    private String professor;
    private Horario horario;
    private String salas;

    @Deprecated
    public Turma(int id, String professor, Horario horario) {
        this.id = id;
        this.professor = professor;
        this.horario = horario;
    }

    public Turma(int id, String professor, String salas, Horario horario) {
        this.id = id;
        this.professor = professor;
        this.horario = horario;
        this.salas = salas;
    }

    @Deprecated
    public Turma(int id, String professor, String horarioCodificado) {
        this.id = id;
        this.professor = professor;
        this.horario = new Horario(horarioCodificado);
    }

    public Turma(int id, String professor, String salas, String horarioCodificado) {
        this.id = id;
        this.professor = professor;
        this.salas = salas;
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
