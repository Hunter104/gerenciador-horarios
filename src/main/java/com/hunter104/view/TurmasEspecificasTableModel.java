package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.Horario;
import com.hunter104.model.Turma;

import javax.swing.table.AbstractTableModel;
import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

public class TurmasEspecificasTableModel extends AbstractTurmaTableModel {
    Map<Disciplina, Set<Turma>> turmas;

    public TurmasEspecificasTableModel(Map<Disciplina, Set<Turma>> turmas) {
        this.turmas = turmas;
    }

    @Override
    public int getRowCount() {
        return turmas.values().stream().mapToInt(Set::size).sum();
    }


    protected Optional<?> getElemento(int row, int tipoElemento) {
        int linhaAtual = 0;
        List<Disciplina> disciplinasAlfabeticas = turmas.keySet().stream().sorted(Comparator.comparing(Disciplina::getNome)).toList();
        for (Disciplina disciplina : disciplinasAlfabeticas) {
            for (Turma turma : getTurmasOrdemId(disciplina)) {
                if (linhaAtual == row) {
                    return switch (tipoElemento){
                        case DISCIPLINA -> Optional.of(disciplina);
                        case TURMA -> Optional.of(turma);
                        default -> Optional.empty();
                    };
                }
                linhaAtual++;
            }
        }
        return Optional.empty();
    }

    private List<Turma> getTurmasOrdemId(Disciplina disciplina) {
        return turmas.get(disciplina).stream().sorted(Comparator.comparing(Turma::getId)).toList();
    }

    public void setTurmas(Map<Disciplina, Set<Turma>> turmas) {
        this.turmas = turmas;
        fireTableDataChanged();
    }
}
