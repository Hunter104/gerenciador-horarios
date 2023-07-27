package com.hunter104.view;

import com.hunter104.model.ConflitoHorario;
import com.hunter104.model.Disciplina;
import com.hunter104.model.PlanodeGrade;
import com.hunter104.model.Turma;

import javax.swing.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import static com.hunter104.Main.criarPlanejadorComDados;

public class MainWindow implements PropertyChangeListener {
    private final DisciplinasTableModel crudDisciplinasModel;
    private final TurmasTableModel crudTurmasModel;
    private final PlanodeGrade planejador;
    private final ConflitosTableModel conflitosTableModel;
    private final TurmasEspecificasTableModel turmasConflitoTableModel;
    private final TurmasEspecificasTableModel turmasPossiveisTableModel;
    private final TurmasEspecificasTableModel turmasEscolhidasTableModel;
    private JTabbedPane Main;
    private JButton adicionarDisciplinaButton;
    private JButton removerDisciplinaButton;
    private JTable turmasCrudTable;
    private JButton adicionarTurmaButton;
    private JButton removerTurmaButton;
    private JButton otimizarButton;
    private JButton visualizarConflitoButton;
    private JTable horarioConflitoTable;
    private JTable turmasConflitoTable;
    private JTable disciplinasCrudTable;
    private JTable turmasPossiveisTable;
    private JTable turmasEscolhidasTable;
    private JButton escolherTurmaButton;
    private JButton removerTurmaEscolhida;
    private JButton exportarDadosButton;
    private JLabel chHorasLabel;
    private JLabel disciplinasTituloLabel;
    private JLabel turmasTituloLabel;
    private JLabel conflitoEscolhidoLabel;
    private JPanel visualizarHorarioConflitoPanel;
    private JPanel VisualizarTurmaConflitoPanel;
    private JPanel controlPanel;
    private JLabel otimizavelLabel;

    public MainWindow() {

        // Modelo
        planejador = criarPlanejadorComDados();
        planejador.addPropertyChangeListener(this);
        planejador.getDisciplinas().forEach(disciplina -> disciplina.addPropertyChangeListener(this));

        // Tabelas
        crudDisciplinasModel = new DisciplinasTableModel(planejador.getDisciplinasOrdemAlfabetica());
        disciplinasCrudTable.setModel(crudDisciplinasModel);

        crudTurmasModel = new TurmasTableModel(planejador.getDisciplinasOrdemAlfabetica());
        turmasCrudTable.setModel(crudTurmasModel);

        conflitosTableModel = new ConflitosTableModel(planejador.getConflitos());
        horarioConflitoTable.setModel(conflitosTableModel);

        turmasConflitoTableModel = new TurmasEspecificasTableModel(new HashMap<>());
        turmasConflitoTable.setModel(turmasConflitoTableModel);

        turmasPossiveisTableModel = new TurmasEspecificasTableModel(planejador.getTurmasEscolhiveis());
        turmasPossiveisTable.setModel(turmasPossiveisTableModel);

        turmasEscolhidasTableModel = new TurmasEspecificasTableModel(planejador.getTurmasEscolhidasSet());
        turmasEscolhidasTable.setModel(turmasEscolhidasTableModel);

        // Botões
        adicionarDisciplinaButton.addActionListener(e ->
                mostrarDialogo(new AdicionarDisciplina(planejador), "Cadastrar nova disciplina")
        );
        removerDisciplinaButton.addActionListener(e -> planejador.removerDisciplina(
                crudDisciplinasModel.getDisciplina(disciplinasCrudTable.getSelectedRow())
        ));

        adicionarTurmaButton.addActionListener(e ->
                mostrarDialogo(new AdicionarTurma(planejador), "Cadastrar nova turma")
        );
        removerTurmaButton.addActionListener(e -> {
            int row = turmasCrudTable.getSelectedRow();
            Optional<Turma> turmaEscolhida = crudTurmasModel.getTurma(row);
            Optional<Disciplina> disciplinaEscolhida = crudTurmasModel.getDisciplina(row);
            turmaEscolhida.ifPresent(turma ->
                    disciplinaEscolhida.ifPresent(disciplina -> disciplina.removerTurma(turma))
            );
        });

        // TODO: trocar null checks por optionals
        visualizarConflitoButton.addActionListener(e -> {
            int row = horarioConflitoTable.getSelectedRow();
            int column = horarioConflitoTable.getSelectedColumn();
            conflitosTableModel.getConflito(row, column).ifPresent(conflito -> {
                turmasConflitoTableModel.setTurmas(conflito.turmas());
                conflitoEscolhidoLabel.setText(formatarConflito(conflito));
                otimizavelLabel.setText(conflito.otimizavel() ? "Sim" : "Não");
            });

        });
        otimizarButton.addActionListener(e -> planejador.removerTurmasInalcancaveis());

        // Labels
        chHorasLabel.setText(String.valueOf(planejador.getCargaHorariaTotalHoras()));
        disciplinasTituloLabel.putClientProperty("FlatLaf.styleClass", "h1");
        turmasTituloLabel.putClientProperty("FlatLaf.styleClass", "h1");

        escolherTurmaButton.addActionListener(e -> {
            int row = turmasPossiveisTable.getSelectedRow();
            Optional<Map.Entry<Disciplina, Turma>> turmaEscolhida = turmasPossiveisTableModel.getElemento(row);
            turmaEscolhida.ifPresent(planejador::escolherUmaTurma);
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Planejador de disciplinas");
        frame.setContentPane(new MainWindow().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private String formatarConflito(ConflitoHorario conflito) {
        return String.format("%s - %s", conflito.hora().nome(), conflito.dia().nome());
    }

    private void mostrarDialogo(JDialog dialog, String titulo) {
        dialog.setTitle(titulo);
        dialog.pack();
        dialog.setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "disciplinas" -> {
                planejador.getDisciplinas().forEach(disciplina -> disciplina.addPropertyChangeListener(this));

                List<Disciplina> novasDisciplinas = planejador.getDisciplinasOrdemAlfabetica();

                crudDisciplinasModel.setLinhas(novasDisciplinas);
                crudTurmasModel.setDisciplinas(novasDisciplinas);

                chHorasLabel.setText(String.valueOf(planejador.getCargaHorariaTotalHoras()));

                turmasPossiveisTableModel.setTurmas(planejador.getTurmasEscolhiveis());
                turmasEscolhidasTableModel.setTurmas(planejador.getTurmasEscolhidasSet());
            }
            case "turmas" -> {
                crudTurmasModel.atualizarDados();
                turmasPossiveisTableModel.setTurmas(planejador.getTurmasEscolhiveis());
                turmasEscolhidasTableModel.setTurmas(planejador.getTurmasEscolhidasSet());
            }
            case "conflitos" -> {
                conflitosTableModel.setConflitos(planejador.getConflitos());
                turmasConflitoTableModel.setTurmas(new HashMap<>());
                conflitoEscolhidoLabel.setText("Nenhum");
            }
            case "turmasEscolhidas" -> {
                turmasPossiveisTableModel.setTurmas(planejador.getTurmasEscolhiveis());
                turmasEscolhidasTableModel.setTurmas(planejador.getTurmasEscolhidasSet());
            }
        }
    }
}
