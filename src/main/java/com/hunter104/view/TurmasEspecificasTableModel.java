package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.Horario;
import com.hunter104.model.Turma;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class TurmasEspecificasTableModel extends AbstractTableModel {
    private static final int COL_DISCIPLINA = 0;
    private static final int COL_ID = 1;
    private static final int COL_PROFESSOR = 2;
    private static final int COL_HORARIO = 3;
    private static final int COL_SALA = 4;
    private static final int DISCIPLINA = 0;
    private static final int TURMA = 1;
    Map<Disciplina, Set<Turma>> turmas;
    private final String[] colunas = new String[]{"Disciplina", "id",
            "Professor", "Horário", "Sala"};

    public TurmasEspecificasTableModel(Map<Disciplina, Set<Turma>> turmas) {
        this.turmas = turmas;
    }

    @Override
    public int getRowCount() {
        return turmas.size();
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

    public Turma getTurma(int row) {
        return (Turma) getElemento(row, TURMA);
    }

    public Disciplina getDisciplina(int row) {
        return (Disciplina) getElemento(row, DISCIPLINA);
    }

    private Object getElemento(int row, int tipoElemento) {
        int linhaAtual = 0;
        List<Disciplina> disciplinasAlfabeticas = turmas.keySet().stream().sorted(Comparator.comparing(Disciplina::getNome)).toList();
        for (Disciplina disciplina : disciplinasAlfabeticas) {
            for (Turma turma : getTurmasOrdemId(disciplina)) {
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

    private List<Turma> getTurmasOrdemId(Disciplina disciplina) {
        return turmas.get(disciplina).stream().sorted(Comparator.comparing(Turma::getId)).toList();
    }

    public Map<Disciplina, Set<Turma>> getTurmas() {
        return turmas;
    }

    public void setTurmas(Map<Disciplina, Set<Turma>> turmas) {
        this.turmas = turmas;
        fireTableDataChanged();
    }
}
