package com.view;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import com.model.ProcessListModel;

public class ProcessList extends JList<Integer> implements ChangeListener
{

    private MainWindow mainWindow;

    public ProcessList(MainWindow mainWindow)
    {
        if (mainWindow == null)
            {
                throw new NullPointerException();
            }
        this.mainWindow = mainWindow;

        mainWindow.getModel().addChangeListener(this);

        setCellRenderer(new ProcessListRenderer(mainWindow.getModel()));
        setModel(new ProcessListModel(mainWindow.getModel()));

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setSelectedValue(mainWindow.getModel().getSelectedProcess(), false);

        addListSelectionListener(mainWindow.getActionContainer().getSelectProcessAction());
    }

    public void stateChanged(ChangeEvent e)
    {
        if (!getValueIsAdjusting())
            {
                Integer selectedProcess = mainWindow.getModel().getSelectedProcess();
                if (getSelectedValue() != selectedProcess)
                    {
                        setSelectedValue(selectedProcess, false);
                    }
            }
    }

}
