package com.hunter104.model;

import java.util.HashSet;
import java.util.Set;

public class GradeHoraria {
    private final Set<Disciplina> disciplinas;

    public GradeHoraria() {
        disciplinas = new HashSet<>();
    }

    public void adicionarDisciplina(String nome, int cargaHoraria) {
        Disciplina disciplina = new Disciplina(nome, cargaHoraria);
        disciplinas.add(disciplina);
    }

    public void removerDisciplina(String nome) {
        disciplinas.removeIf(disciplina -> disciplina.getNome().equals(nome));
    }

    public Disciplina getDisciplina(String nome) {
        return disciplinas.stream().filter(disciplina -> disciplina.getNome().equals(nome)).findFirst().orElseThrow();
    }

    public void adicionarTurma(String nomeDisciplina, int id, String professor, String horarioCodificado) {
        Turma turma = new Turma(id, professor, horarioCodificado);
        getDisciplina(nomeDisciplina).adicionarTurma(turma);
    }

    public void removerTurma(String nomeDisciplina, int id) {
        getDisciplina(nomeDisciplina).removerTurma(id);
    }

    public Turma getTurma(String nomeDisciplina, int id) {
        return getDisciplina(nomeDisciplina).getTurma(id);
    }
}
