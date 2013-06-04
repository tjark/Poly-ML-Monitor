package com.view;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.model.Model;

public class ProcessListRenderer extends DefaultListCellRenderer
{

    private Model model;

    public ProcessListRenderer(Model model)
    {
        if (model == null)
            {
                throw new NullPointerException();
            }
        this.model = model;
    }

    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (!model.getCurrentProcessIDs().contains(value))
            {
                setForeground(Color.gray);
            }

        return this;
    }

}
