package com.hunter104.view;

import com.hunter104.model.Disciplina;
import com.hunter104.model.PlanodeGrade;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.Objects;

public class AdicionarTurma extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField idField;
    private JTextField professorField;
    private JComboBox<Disciplina> disciplinaBox;
    private JTextField horarioField;
    private JTextField salaField;

    public AdicionarTurma(PlanodeGrade planejador) {

        disciplinaBox.setRenderer(new DisciplinasRenderer());
        List<Disciplina> disciplinas = planejador.getDisciplinasOrdemAlfabetica();
        disciplinas.forEach(disciplina -> disciplinaBox.addItem(disciplina));

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
        Disciplina disciplinaEscolhida = (Disciplina) disciplinaBox.getSelectedItem();
        Objects.requireNonNull(disciplinaEscolhida);
        disciplinaEscolhida.adicionarTurma(
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
