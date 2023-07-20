package com.hunter104.gui;

import javax.swing.*;

public class MainWindow {
    private JTabbedPane Main;
    private JTable table1;
    private JButton adicionarDisciplinaButton;
    private JButton removerDisciplinaButton;
    private JTable turmasCrudTable;
    private JButton adicionarTurmaButton;
    private JButton removerTurmaButton;
    private JButton otimizarButton;
    private JButton visualizarConflitoButton;
    private JTable horarioConflitoTable;
    private JTable table4;
    private JScrollPane turmasConflitoTable;
    private JScrollPane disciplinasCrudTable;
    private JTable table2;
    private JTable table3;
    private JButton escolherTurmaButton;
    private JButton removerTurmaButton1;
    private JButton exportarDadosButton;

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
