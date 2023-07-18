package com.hunter104.model;

import java.util.*;

public class ConflitoHorario {
    private final Hora hora;
    private final DiadaSemana dia;
    private final Map<Disciplina, Integer> turmas;

    private ConflitoHorario(DiadaSemana dia, Hora hora, Map<Disciplina, Integer> turmas) {
        this.hora = hora;
        this.dia = dia;
        this.turmas = turmas;
    }

    public static ConflitoHorario checarPorConfltio(DiadaSemana dia, Hora hora, Set<Disciplina> disciplinaSet) {
        Map<Disciplina, Integer> turmasIntercedentes = new HashMap<>();
        for (Disciplina disciplina : disciplinaSet) {
            for (Turma turma : disciplina.getTurmas()) {
                Horario horario = turma.getHorario();
                if (horario.temInterseccao(dia, hora)) {
                    turmasIntercedentes.put(disciplina, turma.getId());
                }
            }
        }
        if (turmasIntercedentes.size() > 1) {
            return new ConflitoHorario(dia, hora, turmasIntercedentes);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "ConflitoHorario{" +
                "hora=" + hora +
                ", dia=" + dia +
                ", turmas=" + turmas +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConflitoHorario that = (ConflitoHorario) o;
        return hora == that.hora && dia == that.dia && Objects.equals(turmas, that.turmas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hora, dia, turmas);
    }

    public Hora getHora() {
        return hora;
    }

    public DiadaSemana getDia() {
        return dia;
    }

    public Map<Disciplina, Integer> getTurmas() {
        return turmas;
    }
}
