package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.Turma;

import javax.swing.table.AbstractTableModel;
import java.util.Map;
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
            return getDisciplina(row).map(Disciplina::getNome).orElse(null);
        } else {
            return getTurma(row).map(turma -> getCampo(column, turma)).orElse(null);
        }
    }

    /**
     * Retorna o campo de uma turma presente em uma certa coluna
     *
     * @param turma  turma a ter o campo lido
     * @param column coluna onde o campo está presente
     * @return um objeto com a contedo a informação lida do campo
     */
    private Object getCampo(int column, Turma turma) {
        return switch (column) {
            case COL_ID -> turma.getId();
            case COL_PROFESSOR -> turma.getProfessor();
            case COL_HORARIO -> turma.getHorario().horarioCodificado();
            case COL_SALA -> turma.getSalas();
            default -> null;
        };
    }

    public Optional<Turma> getTurma(int row) {
        return getElemento(row).map(Map.Entry::getValue);
    }

    public Optional<Disciplina> getDisciplina(int row) {
        return getElemento(row).map(Map.Entry::getKey);
    }

    abstract public Optional<Map.Entry<Disciplina, Turma>> getElemento(int row);

    public void atualizarDados() {
        fireTableDataChanged();
    }

    public String[] getColunas() {
        return colunas;
    }
}
