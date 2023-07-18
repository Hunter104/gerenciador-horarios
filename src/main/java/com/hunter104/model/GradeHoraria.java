package com.hunter104.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GradeHoraria {
    private final Set<Disciplina> disciplinas;
    private final Set<ConflitoHorario> conflitos;

    public GradeHoraria() {
        disciplinas = new HashSet<>();
        conflitos = new HashSet<>();
    }

    public void adicionarDisciplina(String nome, int cargaHoraria) {
        Disciplina disciplina = new Disciplina(nome, cargaHoraria);
        disciplinas.add(disciplina);
        atualizarConflitos();
    }

    public void removerDisciplina(String nome) {
        disciplinas.removeIf(disciplina -> disciplina.getNome().equals(nome));
        atualizarConflitos();
    }

    public Disciplina getDisciplina(String nome) {
        return disciplinas.stream().filter(disciplina -> disciplina.getNome().equals(nome)).findFirst().orElseThrow();
    }

    public void adicionarTurma(String nomeDisciplina, int id, String professor, String horarioCodificado) {
        Turma turma = new Turma(id, professor, horarioCodificado);
        getDisciplina(nomeDisciplina).adicionarTurma(turma);
        atualizarConflitos();
    }

    public void removerTurma(String nomeDisciplina, int id) {
        getDisciplina(nomeDisciplina).removerTurma(id);
        atualizarConflitos();
    }

    public Turma getTurma(String nomeDisciplina, int id) {
        return getDisciplina(nomeDisciplina).getTurma(id);
    }

    public void atualizarConflitos() {
        for (DiadaSemana dia : DiadaSemana.values()) {
            for (Hora hora : Hora.values()) {
                ConflitoHorario conflito = ConflitoHorario.checarPorConfltio(dia, hora, disciplinas);
                if (conflito != null) {
                    conflitos.add(conflito);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "GradeHoraria{" +
                "disciplinas=" + disciplinas +
                ", conflitos=" + conflitos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeHoraria that = (GradeHoraria) o;
        return Objects.equals(disciplinas, that.disciplinas) && Objects.equals(conflitos, that.conflitos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(disciplinas, conflitos);
    }

    public Set<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public Set<ConflitoHorario> getConflitos() {
        return conflitos;
    }
}
