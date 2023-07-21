package com.hunter104.gui;

import com.hunter104.model.Disciplina;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.List;

public class DisciplinasRenderer extends DefaultListCellRenderer  {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Disciplina d) {
            value = d.getNome();
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
