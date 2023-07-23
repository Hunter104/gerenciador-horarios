package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.Horario;
import com.hunter104.model.Turma;

import javax.swing.plaf.OptionPaneUI;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Map;
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
    public Optional<Map.Entry<Disciplina, Turma>> getElemento(int row) {
        int linhaAtual = 0;
        for (Disciplina disciplina : disciplinas) {
            for (Turma turma : disciplina.getTurmasPorId()) {
                if (linhaAtual == row) {
                    return Optional.of(Map.entry(disciplina, turma));
                }
                linhaAtual++;
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != COL_DISCIPLINA;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        getTurma(row).ifPresent(turma -> setCampo(column, turma, aValue));
    }

    /**
     * Modifica o campo de uma turma presente em uma certa coluna
     *
     * @param turma  turma a ter o campo lido
     * @param column coluna onde o campo está presente
     */
    private void setCampo(int column, Turma turma, Object valor) {
        switch (column) {
            case COL_ID -> turma.setId((Integer) valor);
            case COL_PROFESSOR -> turma.setProfessor((String) valor);
            case COL_HORARIO -> turma.setHorario(Horario.criarFromCodigo((String) valor));
            case COL_SALA -> turma.setSalas((String) valor);
        }
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
        fireTableDataChanged();
    }

}
