package com.hunter104.gui;

import com.hunter104.model.Disciplina;
import com.hunter104.model.PlanejadorGradeHoraria;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class AdicionarTurma extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField idField;
    private JTextField professorField;
    private JComboBox<String> disciplinaBox;
    private JTextField horarioField;
    private JTextField salaField;
    private final PlanejadorGradeHoraria planejador;

    public AdicionarTurma(PlanejadorGradeHoraria planejador) {
        this.planejador = planejador;

        disciplinaBox.setRenderer(new DisciplinasRenderer());
        List<Disciplina> disciplinas = planejador.getDisciplinasOrdemAlfabetica();
        disciplinas.forEach(disciplina -> disciplinaBox.addItem(disciplina.getNome()));

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        planejador.adicionarTurma(
                (String) disciplinaBox.getSelectedItem(),
                Integer.parseInt(idField.getText()),
                professorField.getText(),
                salaField.getText(),
                horarioField.getText());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
