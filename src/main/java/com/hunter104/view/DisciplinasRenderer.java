package com.hunter104.view;

import com.hunter104.model.Disciplina;

import javax.swing.*;
import java.awt.*;

public class DisciplinasRenderer extends DefaultListCellRenderer  {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Disciplina d) {
            value = d.getNome();
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
