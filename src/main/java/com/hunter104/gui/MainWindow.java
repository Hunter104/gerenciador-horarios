package com.hunter104.gui;

import com.hunter104.model.PlanejadorGradeHoraria;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.*;

import static com.hunter104.Main.criarPlanejadorComDados;

public class MainWindow {
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

    public MainWindow() {
        PlanejadorGradeHoraria planejador = criarPlanejadorComDados();
        crudDisciplinasModel = new DisciplinasTableModel(planejador.getDisciplinas().stream().toList());
        disciplinasCrudTable.setModel(crudDisciplinasModel);
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
}
