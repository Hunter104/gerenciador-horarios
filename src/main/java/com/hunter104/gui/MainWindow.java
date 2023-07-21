package com.hunter104.gui;

import com.hunter104.model.Disciplina;
import com.hunter104.model.PlanejadorGradeHoraria;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;

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
    private JButton removerTurmaButton1;
    private JButton exportarDadosButton;
    private JLabel chHorasLabel;
    private JLabel informacoesLabel;
    private final DisciplinasTableModel crudDisciplinasModel;
    private final TurmasTableModel turmasTableModel;
    private final PlanejadorGradeHoraria planejador;

    public MainWindow() {

        // Modelo
        planejador = criarPlanejadorComDados();
        planejador.addPropertyChangeListener(this);

        // Tabelas
        crudDisciplinasModel = new DisciplinasTableModel(planejador.getDisciplinasOrdemAlfabetica());
        disciplinasCrudTable.setModel(crudDisciplinasModel);

        turmasTableModel = new TurmasTableModel(planejador.getDisciplinasOrdemAlfabetica());
        turmasCrudTable.setModel(turmasTableModel);


        // Botões
        adicionarDisciplinaButton.addActionListener(e -> {
            AdicionarDisciplina dialog = new AdicionarDisciplina(planejador);
            dialog.pack();
            dialog.setVisible(true);
        });
        removerDisciplinaButton.addActionListener(e -> planejador.removerDisciplina(
                crudDisciplinasModel.getDisciplina(disciplinasCrudTable.getSelectedRow()).getNome()
        ));

        // Labels
        chHorasLabel.setText(String.valueOf(planejador.getCargaHorariaTotalHoras()));
        informacoesLabel.putClientProperty("FlatLaf.styleClass", "h1");
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
        if ("disciplinas".equals(evt.getPropertyName())) {
            crudDisciplinasModel.setLinhas(planejador.getDisciplinasOrdemAlfabetica());
            chHorasLabel.setText(String.valueOf(planejador.getCargaHorariaTotalHoras()));
        }
    }
}
