package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.Horario;
import com.hunter104.model.Turma;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Objects;

public class TurmasEspecificasTableModel extends AbstractTableModel {
    private static final int COL_DISCIPLINA = 0;
    private static final int COL_ID = 1;
    private static final int COL_PROFESSOR = 2;
    private static final int COL_HORARIO = 3;
    private static final int COL_SALA = 4;
    private static final int DISCIPLINA = 0;
    private static final int TURMA = 1;
    List<Disciplina> disciplinas;
    private final String[] colunas = new String[]{"Disciplina", "id",
            "Professor", "Horário", "Sala"};

/*    public TurmasTableModel(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }*/

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
        if (column == 0) {
            Disciplina disciplina = Objects.requireNonNull((Disciplina) getElemento(row, DISCIPLINA));
            return disciplina.getNome();
        } else {
            Turma turma = Objects.requireNonNull((Turma) getElemento(row, TURMA));
            return switch (column) {
                case COL_ID -> turma.getId();
                case COL_PROFESSOR -> turma.getProfessor();
                case COL_HORARIO -> turma.getHorario().horarioCodificado();
                case COL_SALA ->  turma.getSalas();
                default -> null;
            };
        }
    }

    @Override
    public void setValueAt(Object aValue, int row,
                           int column) {
        Turma turma = Objects.requireNonNull((Turma) getElemento(row, TURMA));
        switch (column) {
            case COL_ID -> turma.setId((Integer) aValue);
            case COL_PROFESSOR -> turma.setProfessor((String) aValue);
            case COL_HORARIO -> turma.setHorario(Horario.criarFromCodigo((String) aValue));
            case COL_SALA -> turma.setSalas((String) aValue);
        }
    }

    public Turma getTurma(int row) {
        return (Turma) getElemento(row, TURMA);
    }

    public Disciplina getDisciplina(int row) {
        return (Disciplina) getElemento(row, DISCIPLINA);
    }

    private Object getElemento(int row, int tipoElemento) {
        int linhaAtual = 0;
        for (Disciplina disciplina : disciplinas) {
            for (Turma turma : disciplina.getTurmasPorId()) {
                if (linhaAtual == row) {
                    return switch (tipoElemento){
                        case DISCIPLINA -> disciplina;
                        case TURMA -> turma;
                        default -> null;
                    };
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
