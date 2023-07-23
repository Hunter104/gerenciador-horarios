package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.Horario;
import com.hunter104.model.Turma;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Optional;

public abstract class AbstractTurmaTableModel extends AbstractTableModel {
    protected static final int COL_DISCIPLINA = 0;
    protected static final int COL_ID = 1;
    protected static final int COL_PROFESSOR = 2;
    protected static final int COL_HORARIO = 3;
    protected static final int COL_SALA = 4;
    protected static final int DISCIPLINA = 0;
    protected static final int TURMA = 1;
    private final String[] colunas = new String[]{"Disciplina", "id",
            "Professor", "Horário", "Sala"};
    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colunas[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == COL_ID) {
            return Integer.class;
        }
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            Disciplina disciplina =(Disciplina) getElemento(row, DISCIPLINA).orElseThrow();
            return disciplina.getNome();
        } else {
            Turma turma = (Turma) getElemento(row, TURMA).orElseThrow();
            return switch (column) {
                case COL_ID -> turma.getId();
                case COL_PROFESSOR -> turma.getProfessor();
                case COL_HORARIO -> turma.getHorario().horarioCodificado();
                case COL_SALA ->  turma.getSalas();
                default -> null;
            };
        }
    }

    public Turma getTurma(int row) {
        return (Turma) getElemento(row, TURMA).orElseThrow();
    }

    public Disciplina getDisciplina(int row) {
        return (Disciplina) getElemento(row, DISCIPLINA).orElseThrow();
    }

    abstract protected Optional<?> getElemento(int row, int tipoElemento);

    public void atualizarDados() {
        fireTableDataChanged();
    }

    public String[] getColunas() {
        return colunas;
    }
}
