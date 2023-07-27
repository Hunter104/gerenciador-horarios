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

    private boolean existeConflitoEmCelula(int row, int column) {
        return getConflito(row, column).isPresent();
    }


    public Optional<ConflitoHorario> getConflito(int row, int column) {
        DiadaSemana[] dias = DiadaSemana.values();
        Hora[] horas = Hora.values();

        // Aqui a coluna de dias tem um offset de 1, pois o primeiro dia na tablela
        // começa na segunda coluna(index 1), mas no array o primeiro dia tem index 0
        boolean diaInvalido = column < 1 || column - 1 > dias.length;
        boolean horaInvalida = row < 1 || row > horas.length;

        if (diaInvalido || horaInvalida) {
            return Optional.empty();
        }

        DiadaSemana diaAtual = dias[column - 1];
        Hora horaAtual = horas[row];
        return getConflito(diaAtual, horaAtual);

    }

    public Optional<ConflitoHorario> getConflito(DiadaSemana dia, Hora hora) {
        return conflitos
                .stream()
                .filter(conflitoHorario ->
                        conflitoHorario.dia() == dia && conflitoHorario.hora() == hora)
                .findFirst();
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
