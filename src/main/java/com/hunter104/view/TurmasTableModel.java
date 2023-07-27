package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.Horario;
import com.hunter104.model.Turma;

import javax.swing.plaf.OptionPaneUI;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class TurmasTableModel extends AbstractTurmaTableModel {
    List<Disciplina> disciplinas;

    public TurmasTableModel(Collection<Disciplina> disciplinas) {
        this.disciplinas = ordenarDisciplinasAlfabeticamente(disciplinas);
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
        return columnIndex != Coluna.DISCIPLINA.num();
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        getTurma(row).ifPresent(turma -> setCampo(column, turma, aValue));
    }

    /**
     * Modifica o campo naquela coluna de uma turma
     *
     * @param turma  turma a ter o campo lido
     * @param column número coluna onde o campo está presente
     * @param valor  valor a modificar o campo
     */
    private void setCampo(int column, Turma turma, Object valor) {
        getColumnEnum(column).ifPresent(colunaEnum -> setCampo(colunaEnum, turma, valor));
    }

    /**
     * Modifica o campo naquela coluna de uma turma
     *
     * @param turma  turma a ter o campo lido
     * @param column constante da coluna onde o campo está presente
     * @param valor  valor a modificar o campo
     */
    private void setCampo(Coluna column, Turma turma, Object valor) {
        switch (column) {
            case ID -> turma.setId((Integer) valor);
            case PROFESSOR -> turma.setProfessor((String) valor);
            case HORARIO -> turma.setHorario(Horario.criarFromCodigo((String) valor));
            case SALA -> turma.setSalas((String) valor);
        }
    }

    public void setDisciplinas(Collection<Disciplina> disciplinas) {
        this.disciplinas = ordenarDisciplinasAlfabeticamente(disciplinas);
        fireTableDataChanged();
    }

}
