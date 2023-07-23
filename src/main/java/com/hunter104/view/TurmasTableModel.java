package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.Horario;
import com.hunter104.model.Turma;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TurmasTableModel extends AbstractTurmaTableModel {
    List<Disciplina> disciplinas;

    public TurmasTableModel(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    @Override
    public int getRowCount() {
        return disciplinas.stream().flatMap(disciplina -> disciplina.getTurmas().stream()).toList().size();
    }

    @Override
    protected Optional<?> getElemento(int row, int tipoElemento) {
        int linhaAtual = 0;
        for (Disciplina disciplina : disciplinas) {
            for (Turma turma : disciplina.getTurmasPorId()) {
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

    @Override
    public boolean isCellEditable(int rowIndex, int
            columnIndex) {
        return columnIndex != COL_DISCIPLINA;
    }

    @Override
    public void setValueAt(Object aValue, int row,
                             int column) {
        Turma turma = (Turma) getElemento(row, TURMA).orElseThrow();
        switch (column) {
            case COL_ID -> turma.setId((Integer) aValue);
            case COL_PROFESSOR -> turma.setProfessor((String) aValue);
            case COL_HORARIO -> turma.setHorario(Horario.criarFromCodigo((String) aValue));
            case COL_SALA -> turma.setSalas((String) aValue);
        }
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
        fireTableDataChanged();
    }

}
