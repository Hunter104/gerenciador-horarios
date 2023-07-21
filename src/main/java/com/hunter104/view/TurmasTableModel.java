package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.Turma;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TurmasTableModel extends AbstractTableModel {
    private static final int COL_DISCIPLINA = 0;
    private static final int COL_ID = 1;
    private static final int COL_PROFESSOR = 2;
    private static final int COL_HORARIO = 3;
    private static final int COL_SALA = 4;
    List<Disciplina> disciplinas;
    private final String[] colunas = new String[]{"Disciplina", "id",
            "Professor", "Horário", "Sala"};

    public TurmasTableModel(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    @Override
    public int getRowCount() {
        return disciplinas.stream().flatMap(disciplina -> disciplina.getTurmas().stream()).toList().size();
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
        if (columnIndex == COL_ID) {
            return Integer.class;
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int
            columnIndex) {
        return columnIndex != COL_DISCIPLINA;
    }

    @Override
    public Object getValueAt(int row, int column) {
        int linhaAtual = 0;
        for (Disciplina disciplina : disciplinas) {
            for (Turma turma : disciplina.getTurmasOrdemId()) {
                if (linhaAtual == row) {
                    return switch (column) {
                        case COL_DISCIPLINA -> disciplina.getNome();
                        case COL_ID -> turma.getId();
                        case COL_PROFESSOR -> turma.getProfessor();
                        case COL_HORARIO -> turma.getHorario().getHorarioCodificado();
                        case COL_SALA ->  turma.getSalas();
                        default -> null;
                    };
                }
                linhaAtual++;
            }
        }
        return null;
    }

    public Turma getTurma(int row) {
        int linhaAtual = 0;
        for (Disciplina disciplina : disciplinas) {
            for (Turma turma : disciplina.getTurmasOrdemId()) {
                if (linhaAtual == row) {
                    return turma;
                }
                linhaAtual++;
            }
        }
        return null;
    }

    public Disciplina getDisciplina(int row) {
        int linhaAtual = 0;
        for (Disciplina disciplina : disciplinas) {
            for (Turma ignored : disciplina.getTurmasOrdemId()) {
                if (linhaAtual == row) {
                    return disciplina;
                }
                linhaAtual++;
            }
        }
        return null;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
        fireTableDataChanged();
    }

    public void atualizarDados() {
        fireTableDataChanged();
    }
}
