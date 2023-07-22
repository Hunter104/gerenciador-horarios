package com.hunter104.view;

import com.hunter104.model.*;

import javax.swing.table.AbstractTableModel;
import java.util.Optional;
import java.util.Set;

public class ConflitosTableModel extends AbstractTableModel {
    private static final int COL_HORARIO = 0;
    private static final int COL_SEGUNDA = 1;
    private static final int COL_TERCA = 2;
    private static final int COL_QUARTA = 4;
    private static final int COL_QUINTA = 5;
    private static final int COL_SEXTA = 6;
    private static final int COL_SABADO = 7;
    Set<ConflitoHorario> conflitos;
    private final String[] colunas = new String[]{"Horário", "Segunda",
            "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};

    public ConflitosTableModel(Set<ConflitoHorario> conflitos) {
        this.conflitos = conflitos;
    }

    @Override
    public int getRowCount() {
        return Hora.values().length;
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
        if (columnIndex == COL_HORARIO) {
            return String.class;
        }
        return Boolean.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == COL_HORARIO) {
            return Hora.values()[row].getNome();
        } else {
            return checarExisteConflitoNessaCelula(row, column);
        }
    }

    private boolean checarExisteConflitoNessaCelula(int row, int column) {
        DiadaSemana[] dias = DiadaSemana.values();
        Hora[] horas = Hora.values();
        for (int colunaAtual = 1; colunaAtual-1 < dias.length; colunaAtual++) {
            for (int linhaAtual = 1; linhaAtual < horas.length; linhaAtual++) {
                if (column == colunaAtual && row == linhaAtual) {
                    return getConflito(row, column).isPresent();
                }
            }
        }
        return false;
    }

    private boolean checarExisteConflitoNesseDia(DiadaSemana dia, Hora hora) {
        return conflitos
                .stream().anyMatch(conflitoHorario -> conflitoHorario.dia() == dia && conflitoHorario.hora() == hora);
    }

    public Optional<ConflitoHorario> getConflito(int row, int column) {
        DiadaSemana[] dias = DiadaSemana.values();
        Hora[] horas = Hora.values();
        for (int colunaAtual = 1; colunaAtual-1 < dias.length; colunaAtual++) {
            for (int linhaAtual = 1; linhaAtual < horas.length; linhaAtual++) {
                if (column == colunaAtual && row == linhaAtual) {
                    DiadaSemana diaAtual = dias[colunaAtual - 1];
                    Hora horaAtual = horas[linhaAtual];
                    return conflitos
                            .stream()
                            .filter(conflitoHorario -> conflitoHorario.dia() == diaAtual && conflitoHorario.hora() == horaAtual)
                            .findFirst();
                }
            }
        }
        return Optional.empty();
    }

    public void setConflitos(Set<ConflitoHorario> conflitos) {
        this.conflitos = conflitos;
        fireTableDataChanged();
    }
}
