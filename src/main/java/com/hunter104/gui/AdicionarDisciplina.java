package com.hunter104.gui;

import com.hunter104.model.PlanejadorGradeHoraria;

import javax.swing.*;
import java.awt.event.*;

public class AdicionarDisciplina extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField codigoField;
    private JTextField nomeField;
    private JTextField abreviacaoField;
    private JTextField cargaHorariaField;
    private final PlanejadorGradeHoraria planejador;

    public AdicionarDisciplina(PlanejadorGradeHoraria planejador) {
        this.planejador = planejador;
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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        planejador.adicionarDisciplina(
                codigoField.getText(),
                nomeField.getText(),
                abreviacaoField.getText(),
                Integer.parseInt(cargaHorariaField.getText()));
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
