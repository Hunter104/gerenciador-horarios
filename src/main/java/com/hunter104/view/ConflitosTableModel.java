package com.hunter104.view;

import com.hunter104.model.*;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public class ConflitosTableModel extends AbstractTableModel {
    Set<ConflitoHorario> conflitos;

    public ConflitosTableModel(Set<ConflitoHorario> conflitos) {
        this.conflitos = conflitos;
    }

    @Override
    public int getRowCount() {
        return Hora.values().length;
    }

    @Override
    public int getColumnCount() {
        return Coluna.values().length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return getColuna(columnIndex).map(Coluna::nome).orElseThrow();
    }

    private Optional<Coluna> getColuna(int column) {
        return Arrays.stream(Coluna.values()).filter(coluna -> coluna.num == column).findFirst();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Coluna coluna = getColuna(columnIndex).orElseThrow();
        if (coluna == Coluna.HORA) {
            return String.class;
        }
        return Boolean.class;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Coluna coluna = getColuna(column).orElseThrow();
        if (coluna == Coluna.HORA) {
            return Hora.values()[row].nome();
        } else {
            return existeConflitoEmCelula(row, column);
        }
    }

    private boolean checarExisteConflitoNessaCelula(int row, int column) {
        DiadaSemana[] dias = DiadaSemana.values();
        Hora[] horas = Hora.values();
        for (int colunaAtual = 1; colunaAtual - 1 < dias.length; colunaAtual++) {
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
        for (int colunaAtual = 1; colunaAtual - 1 < dias.length; colunaAtual++) {
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

    private enum Coluna {
        HORA("Hora", 0),
        SEGUNDA("Segunda", 1),
        TERCA("Terça", 2),
        QUARTA("Quarta", 3),
        QUINTA("Quinta", 4),
        SEXTA("Sexta", 5);
        private final int num;
        private final String nome;

        Coluna(String nome, int num) {
            this.num = num;
            this.nome = nome;
        }

        public String nome() {
            return nome;
        }

        public int num() {
            return num;
        }
    }
}
