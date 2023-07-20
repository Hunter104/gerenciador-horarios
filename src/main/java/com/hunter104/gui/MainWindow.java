package com.hunter104.gui;

import com.hunter104.model.Disciplina;
import com.hunter104.model.PlanejadorGradeHoraria;

import javax.swing.*;

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
    private final DisciplinasTableModel crudDisciplinasModel;

    private final PlanejadorGradeHoraria planejador;

    public MainWindow() {
        planejador = criarPlanejadorComDados();
        planejador.addPropertyChangeListener(this);
        crudDisciplinasModel = new DisciplinasTableModel(planejador.getDisciplinas().stream().toList());
        disciplinasCrudTable.setModel(crudDisciplinasModel);
        adicionarDisciplinaButton.addActionListener(e -> {
            AdicionarDisciplina dialog = new AdicionarDisciplina(planejador);
            dialog.pack();
            dialog.setVisible(true);
        });
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
            List<Disciplina> disciplinasOrdenadas = planejador
                            .getDisciplinas()
                            .stream()
                            .sorted(Comparator.comparing(Disciplina::getNome)).toList();
            crudDisciplinasModel.setLinhas(disciplinasOrdenadas);
        }
    }
}
