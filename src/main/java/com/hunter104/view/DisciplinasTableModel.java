package com.hunter104.view;

import com.hunter104.model.Disciplina;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class DisciplinasTableModel extends AbstractTableModel {
    private static final int COL_CODIGO = 0;
    private static final int COL_NOME = 1;
    private static final int COL_ABREVIACAO = 2;
    private static final int COL_CARGA_HORARIA = 3;
    List<Disciplina> linhas;
    private final String[] colunas = new String[]{"Código", "Nome",
            "Abreviação", "Carga horária"};

    public DisciplinasTableModel(List<Disciplina> disciplinas) {
        this.linhas = new ArrayList<>(disciplinas);
    }

    @Override
    public int getRowCount() {
        return linhas.size();
    }

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
        if (columnIndex == COL_CARGA_HORARIA) {
            return Integer.class;
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int
            columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int row, int column) {

        Disciplina m = linhas.get(row);
        return switch (column) {
            case COL_CODIGO -> m.getCodigo();
            case COL_NOME -> m.getNome();
            case COL_CARGA_HORARIA -> m.getCargaHoraria();
            case COL_ABREVIACAO -> m.getAbreviacao();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object aValue, int row,
                           int column) {
        Disciplina d = linhas.get(row);
        switch (column) {
            case COL_CODIGO -> d.setCodigo(aValue.toString());
            case COL_NOME -> d.setNome(aValue.toString());
            case COL_CARGA_HORARIA -> d.setCargaHoraria((Integer) aValue);
            case COL_ABREVIACAO -> d.setAbreviacao(aValue.toString());
        }
    }

    public Disciplina getDisciplina(int indiceLinha) {
        return linhas.get(indiceLinha);
    }

    public void setLinhas(List<Disciplina> linhas) {
        this.linhas = linhas;
        fireTableDataChanged();
    }
}
