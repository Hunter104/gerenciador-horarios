package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.PlanejadorGradeHoraria;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.hunter104.Main.criarPlanejadorComDados;

public class MainWindow implements PropertyChangeListener {
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
    private JButton removerTurmaButton2;
    private JButton exportarDadosButton;
    private JLabel chHorasLabel;
    private JLabel disciplinasTituloLabel;
    private JLabel turmasTituloLabel;
    private final DisciplinasTableModel crudDisciplinasModel;
    private final TurmasTableModel crudTurmasModel;
    private final PlanejadorGradeHoraria planejador;
    private final ConflitosTableModel conflitosTableModel;

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

        // Botões
        adicionarDisciplinaButton.addActionListener(e -> {
            AdicionarDisciplina dialog = new AdicionarDisciplina(planejador);
            dialog.setTitle("Cadastrar nova disciplina");
            dialog.pack();
            dialog.setVisible(true);
        });
        removerDisciplinaButton.addActionListener(e -> planejador.removerDisciplina(
                crudDisciplinasModel.getDisciplina(disciplinasCrudTable.getSelectedRow()).getNome()
        ));
        adicionarTurmaButton.addActionListener(e -> {
            AdicionarTurma dialog = new AdicionarTurma(planejador);
            dialog.setTitle("Cadastrar nova turma");
            dialog.pack();
            dialog.setVisible(true);
        });
        removerTurmaButton.addActionListener(e -> {
            int row = turmasCrudTable.getSelectedRow();
            Disciplina d = crudTurmasModel.getDisciplina(row);
            int id = crudTurmasModel.getTurma(row).getId();
            d.removerTurma(id);
        });
        // Labels
        chHorasLabel.setText(String.valueOf(planejador.getCargaHorariaTotalHoras()));
        disciplinasTituloLabel.putClientProperty("FlatLaf.styleClass", "h1");
        turmasTituloLabel.putClientProperty("FlatLaf.styleClass", "h1");

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Planejador de disciplinas");
        frame.setContentPane(new MainWindow().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "disciplinas")) {
            // Cadastrar observadores para as novas disciplinas
            planejador.getDisciplinas().forEach(disciplina -> disciplina.addPropertyChangeListener(this));

            List<Disciplina> novasDisciplinas = planejador.getDisciplinasOrdemAlfabetica();

            crudDisciplinasModel.setLinhas(novasDisciplinas);
            crudTurmasModel.setDisciplinas(novasDisciplinas);

            chHorasLabel.setText(String.valueOf(planejador.getCargaHorariaTotalHoras()));
        } else if (Objects.equals(evt.getPropertyName(), "turmas")) {
            crudTurmasModel.atualizarDados();
        }
    }
}
