package com.hunter104.view;

import com.hunter104.model.Disciplina;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class DisciplinasTableModel extends AbstractTableModel {
    List<Disciplina> linhas;

    public DisciplinasTableModel(Collection<Disciplina> disciplinas) {
        this.linhas = ordenarDisciplinasAlfabeticamente(disciplinas);
    }

    private List<Disciplina> ordenarDisciplinasAlfabeticamente(Collection<Disciplina> disciplinas) {
        return disciplinas.stream().sorted(Comparator.comparing(Disciplina::getNome)).toList();
    }

    @Override
    public int getRowCount() {
        return linhas.size();
    }

    @Override
    public int getColumnCount() {
        return Coluna.values().length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return Arrays.stream(Coluna.values())
                .filter(coluna -> coluna.num == columnIndex)
                .map(coluna -> coluna.nome)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == Coluna.CARGA_HORARIA.num) {
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
        Coluna colunaEnum = getColunaEnum(column).orElseThrow();
        return switch (colunaEnum) {
            case CODIGO -> m.getCodigo();
            case NOME -> m.getNome();
            case CARGA_HORARIA -> m.getCargaHoraria();
            case ABREVIACAO -> m.getAbreviacao();
        };
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        Disciplina d = linhas.get(row);
        Coluna colunaEnum = getColunaEnum(column).orElseThrow();
        switch (colunaEnum) {
            case CODIGO -> d.setCodigo(aValue.toString());
            case NOME -> d.setNome(aValue.toString());
            case CARGA_HORARIA -> d.setCargaHoraria((Integer) aValue);
            case ABREVIACAO -> d.setAbreviacao(aValue.toString());
        }
    }

    private Optional<Coluna> getColunaEnum(int column) {
        return Arrays.stream(Coluna.values()).filter(coluna -> coluna.num == column).findFirst();
    }

    public Disciplina getDisciplina(int indiceLinha) {
        return linhas.get(indiceLinha);
    }

    public void setLinhas(Collection<Disciplina> linhas) {
        this.linhas = ordenarDisciplinasAlfabeticamente(linhas);
        fireTableDataChanged();
    }

    private enum Coluna {
        CODIGO("Código", 0),
        NOME("Nome", 1),
        CARGA_HORARIA("Carga horária", 2),
        ABREVIACAO("Abreviação", 3);
        private final int num;
        private final String nome;

        Coluna(String nome, int num) {
            this.nome = nome;
            this.num = num;
        }

    }
}
