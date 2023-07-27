package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.Turma;

import javax.swing.table.AbstractTableModel;
import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractTurmaTableModel extends AbstractTableModel {
    @Override
    public int getColumnCount() {
        return Coluna.values().length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return getDisciplina(row).map(Disciplina::getNome).orElse(null);
        } else {
            return getTurma(row).flatMap(turma -> getCampo(column, turma)).orElse(null);
        }
    }

    public Optional<Disciplina> getDisciplina(int row) {
        return getElemento(row).map(Map.Entry::getKey);
    }

    abstract public Optional<Map.Entry<Disciplina, Turma>> getElemento(int row);

    public Optional<Turma> getTurma(int row) {
        return getElemento(row).map(Map.Entry::getValue);
    }

    /**
     * Retorna o valor do campo de uma turma presente em uma certa coluna
     *
     * @param turma  turma a ter o campo lido
     * @param column número da coluna onde o campo está presente
     * @return um objeto com a contedo a informação lida do campo
     */
    private Optional<Object> getCampo(int column, Turma turma) {
        return getColumnEnum(column).flatMap(coluna -> getCampo(coluna, turma));
    }

    /**
     * Retorna a constante representando o número da coluna escolhido
     *
     * @param column número da coluna
     * @return constante representado a coluna referida
     */
    protected Optional<Coluna> getColumnEnum(int column) {
        return Arrays.stream(Coluna.values()).filter(coluna -> coluna.num == column).findFirst();
    }

    /**
     * Retorna o valor do campo de uma turma presente em uma certa coluna
     *
     * @param turma  turma a ter o campo lido
     * @param coluna constante relacionada a coluna onde o campo está presente
     * @return um objeto com a contedo a informação lida do campo
     */
    private Optional<Object> getCampo(Coluna coluna, Turma turma) {
        return switch (coluna) {
            case DISCIPLINA -> Optional.empty();
            case ID -> Optional.of(turma.getId());
            case PROFESSOR -> Optional.of(turma.getProfessor());
            case HORARIO -> Optional.of(turma.getHorario().horarioCodificado());
            case SALA -> Optional.of(turma.getSalas());
        };
    }

    @Override
    public String getColumnName(int columnIndex) {
        return Arrays.stream(Coluna.values())
                .filter(coluna -> coluna.num == columnIndex)
                .map(Coluna::nome)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == Coluna.ID.num()) {
            return Integer.class;
        }
        return String.class;
    }

    public void atualizarDados() {
        fireTableDataChanged();
    }

    protected enum Coluna {
        DISCIPLINA("Disciplina", 0),
        ID("Id", 1),
        PROFESSOR("Professor", 2),
        HORARIO("Horário", 3),
        SALA("Sala(s)", 4);
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
