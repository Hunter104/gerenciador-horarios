package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.Turma;

import java.util.*;

public class TurmasEspecificasTableModel extends AbstractTurmaTableModel {
    Map<Disciplina, Set<Turma>> turmas;

    public TurmasEspecificasTableModel(Map<Disciplina, Set<Turma>> turmas) {
        this.turmas = turmas;
    }

    @Override
    public int getRowCount() {
        return turmas.values().stream().mapToInt(Set::size).sum();
    }

    @Override
    public Optional<Map.Entry<Disciplina, Turma>> getElemento(int row) {
        int linhaAtual = 0;
        List<Disciplina> disciplinasAlfabeticas = ordenarDisciplinasAlfabeticamente(turmas.keySet());
        for (Disciplina disciplina : disciplinasAlfabeticas) {
            for (Turma turma : ordenarTurmasPorId(disciplina)) {
                if (linhaAtual == row) {
                    return Optional.of(Map.entry(disciplina, turma));
                }
                linhaAtual++;
            }
        }
        return Optional.empty();
    }


    private List<Turma> ordenarTurmasPorId(Disciplina disciplina) {
        return turmas.get(disciplina).stream().sorted(Comparator.comparing(Turma::getId)).toList();
    }

    public void setTurmas(Map<Disciplina, Set<Turma>> turmas) {
        this.turmas = turmas;
        fireTableDataChanged();
    }
}
